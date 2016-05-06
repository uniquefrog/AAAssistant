package mob.aaassistant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mob.aaassistant.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import mob.aaassistant.MainActivity.GridMainBodyAdapter.Holder;
import mob.aaassistant.base.ActivityFrame;
import mob.aaassistant.bussiness.BussinessCategory;
import mob.aaassistant.bussiness.BussinessUser;
import mob.aaassistant.control.SlideMenuItem;
import mob.aaassistant.control.SlideMenuView.OnSlideMenuListner;
import mob.aaassistant.entity.User;
import mob.aaassistant.utils.RegexTools;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UserActivity extends ActivityFrame implements OnSlideMenuListner{

	ListView listviewUser;
	//the data for sildmenu
	String[] menuDatas;
	//the list data for sildmenu
	ArrayList<SlideMenuItem> menuDataList;
	//the person data
	List<User> userDataList;
	BussinessUser bussinessUser;
	final String TIPMSG="必须是中文英文和数字组成";
	final String REPEATEDMSG="该用户名已存在，请换个用户名称";
	UserListViewAdapter adapter;
	User mUser;
	User selectedUser;
	private TextView tvTopTitle;
	private ImageView ivAppBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		//initialize the view 
		initView();
		//initialize the data
		initData();
		//initialize the event
		initEvent();
		//bind data for the view
		BindData();
		SetTitle();
		//crrate the slide menu 
		CreateSlideMenu(menuDataList);

	}

	private void initView() {
		listviewUser=(ListView) this.findViewById(R.id.listviewUser);
		tvTopTitle=(TextView) findViewById(R.id.tvTopTitle);
		ivAppBack=(ImageView) findViewById(R.id.ivAppBack);
	}
	private void SetTitle() {
		int count=userDataList.size();
		String titel = "人员管理"+"("+count+")";
		tvTopTitle.setText(titel);
	}
	private void initData() {
		menuDatas=new String[]{"新增人员"};
		userDataList=new ArrayList<User>();
		menuDataList=new ArrayList<SlideMenuItem>();
		for (int i = 0; i < menuDatas.length; i++) {
			SlideMenuItem item=new SlideMenuItem();
			item.setId(i);
			item.setTitle(menuDatas[i]);
			menuDataList.add(item);
		}
		bussinessUser=new BussinessUser(UserActivity.this);
		userDataList=bussinessUser.getUserHideList();
	}

	private void initEvent() {
		registerForContextMenu(listviewUser);
		ivAppBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();				
			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo adapterContextMenuInfo=(AdapterContextMenuInfo) menuInfo;
		selectedUser=(User) adapter.getItem(adapterContextMenuInfo.position);
		menu.setHeaderIcon(R.drawable.user_small_icon);
		menu.setHeaderTitle(selectedUser.getName());

		CreatMenu(menu);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			//update
			ShowUserUpdateOrAddDialog(selectedUser);
			break;

		case 2:
			//delete

			delete();
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	public void delete(){
		AlertDialog dialog=new AlertDialog.Builder(UserActivity.this)
				.setTitle("提示")
				.setIcon(R.drawable.user_big_icon)
				.setMessage("确定要删除该用户吗？")
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectedUser.setState(0);

						boolean result=bussinessUser.HideUserByUserId(selectedUser.getId());
						if (result) {
							notifyChangList();
							setAlertDialogClose(dialog, true);
							ShowMsg("删除成功");
						}
						else{
							setAlertDialogClose(dialog, true);
							ShowMsg("删除失败");
						}

					}
				})
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						setAlertDialogClose(dialog, true);
						dialog.dismiss();
					}
				})
				.create();
		dialog.show();
	}

	protected void notifyChangList() {
		userDataList=bussinessUser.getUserHideList();
		adapter.notifyDataSetChanged();
	}

	private void BindData() {
		adapter=new UserListViewAdapter(UserActivity.this);
		listviewUser.setAdapter(adapter);

	}

	@Override
	public void onSlideMenuItemClick(View pView, SlideMenuItem pSlideMenuItem) {
		SlideToggle();
		ShowUserUpdateOrAddDialog(null);
	}
	class UserListViewAdapter extends BaseAdapter{

		Context context;
		BussinessUser bussinessUser=new BussinessUser(context);
		public UserListViewAdapter(Context context){
			this.context=context;
			if (userDataList==null) {
				userDataList=bussinessUser.getUserHideList();
			}
		}
		@Override
		public int getCount() {
			return userDataList.size();
		}

		@Override
		public Object getItem(int position) {

			return userDataList.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder=null;
			if (convertView==null) {
				holder=new Holder();
				convertView=LayoutInflater.from(context)
						.inflate(R.layout.user_item, null);
				holder.tvUserItem=(TextView) convertView.findViewById(R.id.tvUserItem);
				holder.imgUserItem=(ImageView) convertView.findViewById(R.id.imgUserItem);
				convertView.setTag(holder);
			}
			else{
				holder=(Holder) convertView.getTag();
			}
			holder.imgUserItem.setImageResource(R.drawable.user_big_icon);
			holder.tvUserItem.setText(userDataList.get(position).getName());
			return convertView;
		}
		class Holder {
			ImageView imgUserItem;
			TextView tvUserItem;
		}
	}


	private void ShowUserUpdateOrAddDialog(User user){


		View view=getLayoutInflater(UserActivity.this).inflate(R.layout.activity_user_add_or_update, null);
		final EditText edtvEditName=(EditText) view.findViewById(R.id.edtvEditName);
		String title=null;
		mUser=user;
		//user is null-add
		if (user==null) {
			mUser=new User();
			title="新建人员";
		}
		//user is not null-update
		else{
			edtvEditName.setText(user.getName());
			title="修改人员";
		}
		AlertDialog dialog=new AlertDialog.Builder(UserActivity.this)
				.setIcon(R.drawable.user_big_icon)
				.setView(view)
				.setTitle(title)
				.setPositiveButton("保存", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						User newUser=new User();
						String userName=edtvEditName.getText().toString().trim();
						boolean checkedResult=RegexTools.IsChineseEnglishNum(userName);

						if (!checkedResult) {
							ShowMsg(edtvEditName.getHint()+TIPMSG);
							setAlertDialogClose(dialog, false);
							return;
						}
						else{
							setAlertDialogClose(dialog, true);
						}
						//check whether the userName is exist
						boolean isRepeated=bussinessUser.IsExistUserByUserName(userName, mUser.getId());
						if (!checkedResult) {
							ShowMsg(REPEATEDMSG);
							setAlertDialogClose(dialog, false);
							return;
						}
						else{
							setAlertDialogClose(dialog, true);
						}
						boolean reuslt=false;
						//user is null-add
						if (mUser.getId()==0) {
							newUser.setName(userName);
							Calendar calendar=Calendar.getInstance();
							calendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH,
									Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND);
							newUser.setDate(calendar.getTime());
							newUser.setState(1);
							ShowMsg(bussinessUser.insertUser(newUser));

						}
						//user is not null-update
						else{
							mUser.setName(userName);
							ShowMsg(bussinessUser.updateUser(mUser));
						}
						notifyChangList();


					}
				})
				.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						setAlertDialogClose(dialog, true);
					}
				} )
				.create();
		dialog.show();
	}

}
