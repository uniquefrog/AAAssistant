package mob.aaassistant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mob.aaassistant.R;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import mob.aaassistant.bussiness.BussinessAccountBook;
import mob.aaassistant.control.SlideMenuItem;
import mob.aaassistant.control.SlideMenuView.OnSlideMenuListner;
import mob.aaassistant.entity.AccountBook;
import mob.aaassistant.utils.RegexTools;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AccountBookActivity extends ActivityFrame implements OnSlideMenuListner{

	ListView listviewAccountBook;
	//the data for sildmenu
	String[] menuDatas;
	//the list data for sildmenu
	ArrayList<SlideMenuItem> menuDataList;
	//the person data
	List<AccountBook> AccountBookDataList;
	BussinessAccountBook bussinessAccountBook;
	final String TIPMSG="必须是中文英文和数字组成";
	final String REPEATEDMSG="账号名已存在，请换一个账户名称";
	AccountBookListViewAdapter adapter;
	AccountBook mAccountBook;
	AccountBook selectedAccountBook;
	private TextView tvTopTitle;
	private ImageView ivAppBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_book);
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
		listviewAccountBook=(ListView) this.findViewById(R.id.listviewAccountBookList);
		tvTopTitle=(TextView) findViewById(R.id.tvTopTitle);
		ivAppBack=(ImageView) findViewById(R.id.ivAppBack);
	}
	private void SetTitle() {
		
		String titel = "账本管理";
		tvTopTitle.setText(titel);
	}
	private void initData() {
		menuDatas=new String[]{"新增账本"};
		AccountBookDataList=new ArrayList<AccountBook>();
		menuDataList=new ArrayList<SlideMenuItem>();
		for (int i = 0; i < menuDatas.length; i++) {
			SlideMenuItem item=new SlideMenuItem();
			item.setId(i);
			item.setTitle(menuDatas[i]);
			menuDataList.add(item);
		}
		bussinessAccountBook=new BussinessAccountBook(AccountBookActivity.this);
		AccountBookDataList=bussinessAccountBook.getAccountBookHideList();
	}

	private void initEvent() {
		registerForContextMenu(listviewAccountBook);
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
		selectedAccountBook=(AccountBook) adapter.getItem(adapterContextMenuInfo.position);
		menu.setHeaderIcon(R.drawable.account_book_small_icon);
		menu.setHeaderTitle(selectedAccountBook.getName());

		CreatMenu(menu);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			//update
			ShowAccountBookUpdateOrAddDialog(selectedAccountBook);
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
		AlertDialog dialog=new AlertDialog.Builder(AccountBookActivity.this)
				.setTitle("提示")
				.setIcon(R.drawable.account_book_big_icon)
				.setMessage("确定要删除该账本吗？")
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectedAccountBook.setState(0);

						boolean result=bussinessAccountBook.HideAccountBookByAccountBookId(selectedAccountBook.getId());
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
		AccountBookDataList=bussinessAccountBook.getAccountBookHideList();
		adapter.notifyDataSetChanged();
	}

	private void BindData() {
		adapter=new AccountBookListViewAdapter();
		listviewAccountBook.setAdapter(adapter);

	}

	@Override
	public void onSlideMenuItemClick(View pView, SlideMenuItem pSlideMenuItem) {
		SlideToggle();
		ShowAccountBookUpdateOrAddDialog(null);
	}
	class AccountBookListViewAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return AccountBookDataList.size();
		}

		@Override
		public Object getItem(int position) {

			return AccountBookDataList.get(position);
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
				convertView=LayoutInflater.from(AccountBookActivity.this)
						.inflate(R.layout.account_book_list_item, null);
				holder.tvAccountBookItem=(TextView) convertView.findViewById(R.id.tvAccountBookName);
				holder.imgAccountBookItem=(ImageView) convertView.findViewById(R.id.imgAccountBookIcon);
				convertView.setTag(holder);
			}
			else{
				holder=(Holder) convertView.getTag();
			}
			AccountBook accountBook=AccountBookDataList.get(position);
			if (accountBook.getIsDefault()==0) {
				holder.imgAccountBookItem.setImageResource(R.drawable.account_book_big_icon);
				Log.i("uniquefrog", accountBook.getName()+"不是默认账本");
			}else{
				holder.imgAccountBookItem.setImageResource(R.drawable.account_book_default);
				Log.i("uniquefrog", accountBook.getName()+"是默认账本");
			}

			holder.tvAccountBookItem.setText(accountBook.getName());
			//			holder.tvMoney
			//			holder.tvTotal

			return convertView;
		}
		class Holder {
			ImageView imgAccountBookItem;
			TextView tvAccountBookItem;
			TextView tvTotal;
			TextView tvMoney;
		}
	}


	private void ShowAccountBookUpdateOrAddDialog(AccountBook AccountBook){

		View view=getLayoutInflater(AccountBookActivity.this).inflate(R.layout.account_book_add_or_edit, null);
		final EditText edtvAccountBookName=(EditText) view.findViewById(R.id.edtvAccountBookName);
		final CheckBox chkIsDefault=(CheckBox) view.findViewById(R.id.chkIsDefault);
		String title=null;

		mAccountBook=AccountBook;
		//AccountBook is null-add
		if (AccountBook==null) {
			mAccountBook=new AccountBook();
			title="新建账本";
		}
		//AccountBook is not null-update
		else{
			edtvAccountBookName.setText(AccountBook.getName());
			title="修改账本";
		}
		AlertDialog dialog=new AlertDialog.Builder(AccountBookActivity.this)
				.setIcon(R.drawable.account_book_big_icon)
				.setView(view)
				.setTitle(title)
				.setPositiveButton("保存", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						AccountBook newAccountBook=new AccountBook();
						boolean bCbIsDefault=chkIsDefault.isChecked();
						String AccountBookName=edtvAccountBookName.getText().toString().trim();
						boolean checkedResult=RegexTools.IsChineseEnglishNum(AccountBookName);

						if (!checkedResult) {
							ShowMsg(edtvAccountBookName.getHint()+TIPMSG);
							setAlertDialogClose(dialog, false);
							return;
						}
						else{
							setAlertDialogClose(dialog, true);
						}
						//check whether the AccountBookName is exist
						boolean isRepeated=bussinessAccountBook.IsExistAccountBookByAccountBookName(AccountBookName, mAccountBook.getId());
						if (!checkedResult) {
							ShowMsg(REPEATEDMSG);
							setAlertDialogClose(dialog, false);
							return;
						}
						else{
							setAlertDialogClose(dialog, true);
						}
						boolean reuslt=false;
						//AccountBook is null-add
						if (mAccountBook.getId()==0) {
							newAccountBook.setName(AccountBookName);
							Calendar calendar=Calendar.getInstance();
							calendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH,
									Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND);
							newAccountBook.setDate(calendar.getTime());
							newAccountBook.setState(1);
							if (bCbIsDefault) {
								newAccountBook.setIsDefault(1);
							}
							else {
								newAccountBook.setIsDefault(0);
							}
							boolean resultInsert= bussinessAccountBook.insertAccountBook(newAccountBook);
							if (resultInsert) {
								ShowMsg("新建成功");
							}
							else {
								ShowMsg("新建失败");
							}

						}
						//AccountBook is not null-update
						else{
							mAccountBook.setName(AccountBookName);
							if (bCbIsDefault) {
								mAccountBook.setIsDefault(1);
							}

							ShowMsg(bussinessAccountBook.updateAccountBook(mAccountBook));
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
