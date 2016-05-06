package mob.aaassistant;

import java.util.ArrayList;
import java.util.List;

import mob.aaassistant.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import mob.aaassistant.base.ActivityFrame;
import mob.aaassistant.base.AdapterAccountBookSelect;
import mob.aaassistant.bussiness.BusinessPayout;
import mob.aaassistant.bussiness.BussinessAccountBook;
import mob.aaassistant.bussiness.BussinessUser;
import mob.aaassistant.control.SlideMenuItem;
import mob.aaassistant.control.SlideMenuView.OnSlideMenuListner;
import mob.aaassistant.entity.AccountBook;
import mob.aaassistant.entity.Payout;
import mob.aaassistant.utils.DateTools;

public class PayoutActivity extends ActivityFrame  implements OnSlideMenuListner{

	ListView listviewPayout;
	//the data for sildmenu
	String[] menuDatas;
	//the list data for sildmenu
	ArrayList<SlideMenuItem> menuDataList;
	//the person data
	List<Payout> payoutDataList;
	BusinessPayout bussinessPayout;
	final String TIPMSG="必须是中文英文和数字组成";
	final String REPEATEDMSG="该用户名已存在，请换个用户名称";
	PayoutListViewAdapter adapter;
	Payout mPayout;
	Payout selectedPayout;
	private AccountBook mAccountBook;
	private BussinessAccountBook mBusinessAccountBook;
	int accountBookId;
	private TextView tvTopTitle;
	private ImageView ivAppBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payout);
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

	private void SetTitle() {
		int count = listviewPayout.getCount();
		String titel = "查询消费-"+mAccountBook.getName()+"("+count+")";
		tvTopTitle.setText(titel);
	}
	private void initView() {
		listviewPayout=(ListView) this.findViewById(R.id.listviewPayout);
		tvTopTitle=(TextView) findViewById(R.id.tvTopTitle);
		ivAppBack=(ImageView) findViewById(R.id.ivAppBack);
	}

	private void initData() {
		menuDatas=new String[]{"切换账本"};
		payoutDataList=new ArrayList<Payout>();
		menuDataList=new ArrayList<SlideMenuItem>();
		for (int i = 0; i < menuDatas.length; i++) {
			SlideMenuItem item=new SlideMenuItem();
			item.setId(i);
			item.setTitle(menuDatas[i]);
			menuDataList.add(item);
		}
		bussinessPayout=new BusinessPayout(PayoutActivity.this);
		mBusinessAccountBook=new BussinessAccountBook(PayoutActivity.this);
		mAccountBook=mBusinessAccountBook.GetDefaultModelAccountBook();
		//Log.i("uniquefrog", "mAccountBook ID:"+mAccountBook.getId());
		payoutDataList=bussinessPayout.GetPayoutByAccountBookID(mAccountBook.getId());
		//Log.i("uniquefrog", "payoutList size:"+payoutDataList.size());
	}

	private void initEvent() {
		registerForContextMenu(listviewPayout);
		ivAppBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		notifyChangList();
		SetTitle();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo adapterContextMenuInfo=(AdapterContextMenuInfo) menuInfo;
		ListAdapter listAdapter=listviewPayout.getAdapter();
		selectedPayout=(Payout) listAdapter.getItem(adapterContextMenuInfo.position);
		menu.setHeaderIcon(R.drawable.payout_small_icon);
		menu.setHeaderTitle(selectedPayout.getCategoryName());
		CreatMenu(menu);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			Intent _Intent = new Intent(this, PayoutAddOrEditActivity.class);
			_Intent.putExtra("Payout", selectedPayout);
			this.startActivityForResult(_Intent, 1);
			notifyChangList();
			break;
		case 2:
			delete();
			notifyChangList();
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	public void delete(){
		AlertDialog dialog=new AlertDialog.Builder(PayoutActivity.this)
				.setTitle("提示")
				.setIcon(R.drawable.payout_small_icon)
				.setMessage("确定要删除该记录吗？")
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectedPayout.setState(0);
						boolean result=bussinessPayout.DeletePayoutByPayoutID(selectedPayout.getPayoutID());
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
	//payoutDataList=bussinessPayout.getPayoutHideList();
		payoutDataList=bussinessPayout.GetPayoutByAccountBookID(mAccountBook.getId());
		adapter.notifyDataSetChanged();
	}

	private void BindData() {
		adapter=new PayoutListViewAdapter();
		listviewPayout.setAdapter(adapter);

	}

	@Override
	public void onSlideMenuItemClick(View pView, SlideMenuItem pSlideMenuItem) {
		SlideToggle();
		if (pSlideMenuItem.getId() == 0) {
			ShowAccountBookSelectDialog();
		}
	}
	private void ShowAccountBookSelectDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
		ListView listView = (ListView)view.findViewById(R.id.listViewSelect);
		AdapterAccountBookSelect _AdapterAccountBookSelect = new AdapterAccountBookSelect(this);
		listView.setAdapter(_AdapterAccountBookSelect);
		
		builder.setTitle("选择账本");
		builder.setNegativeButton("返回", null);
		builder.setView(view);
		AlertDialog _AlertDialog = builder.create();
		_AlertDialog.show();
		listView.setOnItemClickListener(new OnAccountBookItemClickListener(_AlertDialog));
	}
	private class OnAccountBookItemClickListener implements AdapterView.OnItemClickListener
	{
		private AlertDialog m_AlertDialog;
		public OnAccountBookItemClickListener(AlertDialog p_AlertDialog)
		{
			m_AlertDialog = p_AlertDialog;
		}
		@Override
		public void onItemClick(AdapterView p_AdapterView, View arg1, int p_Position,
				long arg3) {
			mAccountBook = (AccountBook)((Adapter)p_AdapterView.getAdapter()).getItem(p_Position);
//			payoutDataList=bussinessPayout.GetPayoutByAccountBookID(mAccountBook.getId());
//			adapter.notifyDataSetChanged();
			notifyChangList();
			SetTitle();
			m_AlertDialog.dismiss();
		}
	}
	class PayoutListViewAdapter extends BaseAdapter{

		public PayoutListViewAdapter() {
			accountBookId=mAccountBook.getId();
		}
		@Override
		public int getCount() {
			return payoutDataList.size();
		}

		@Override
		public Object getItem(int position) {

			return payoutDataList.get(position);
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
				convertView=LayoutInflater.from(PayoutActivity.this)
						.inflate(R.layout.payout_list_item, null);
				holder.tvPayoutName=(TextView) convertView.findViewById(R.id.tvPayoutName);
				holder.Total=(TextView) convertView.findViewById(R.id.Total);
				holder.tvPayoutUserAndPayoutType=(TextView) convertView.findViewById(R.id.tvPayoutUserAndPayoutType);
				holder.imgPayoutIcon=(ImageView) convertView.findViewById(R.id.imgPayoutIcon);
				holder.relativeLayoutDate=(RelativeLayout) convertView.findViewById(R.id.relativeLayoutDate);
				convertView.setTag(holder);
			}
			else{
				holder=(Holder) convertView.getTag();
			}
			holder.relativeLayoutDate.setVisibility(View.GONE);
			Payout payout=(Payout) getItem(position);
			String payoutDate = DateTools.getFormatShortTime(payout.getPayoutDate());
			Boolean isShow = false;
			if(position > 0)
			{
				Payout payoutLast = (Payout)getItem(position - 1);
				String strPayoutDateLast = DateTools.getFormatShortTime(payoutLast.getPayoutDate());
				isShow = !payoutDate.equals(strPayoutDateLast);
			}
			if(isShow || position == 0)
			{
				holder.relativeLayoutDate.setVisibility(View.VISIBLE);
				String message = bussinessPayout.GetPayoutTotalMessage(payoutDate,accountBookId); 
				((TextView)holder.relativeLayoutDate.findViewById(R.id.tvPayoutDate)).setText(payoutDate);
				((TextView)holder.relativeLayoutDate.findViewById(R.id.tvTotal)).setText(message);
			}
			holder.imgPayoutIcon.setImageResource(R.drawable.payout_small_icon);
			holder.Total.setText(payout.getAmount().toString());
			holder.tvPayoutName.setText(payout.getCategoryName());

			BussinessUser businessUser = new BussinessUser(PayoutActivity.this);
			String userNameString = businessUser.GetUserNameByUserID(payout.getPayoutUserID());
			holder.tvPayoutUserAndPayoutType.setText(userNameString + " " + payout.getPayoutType());
			return convertView;
		}
		class Holder {
			ImageView imgPayoutIcon;
			TextView tvPayoutName;
			TextView Total;
			TextView tvPayoutUserAndPayoutType;
			RelativeLayout relativeLayoutDate;
		}
	}
	//	private void ShowPayoutUpdateOrAddDialog(Payout payout){
	//
	//		View view=getLayoutInflater(PayoutActivity.this).inflate(R.layout.activity_payout_add_or_update, null);
	//		final EditText edtvEditName=(EditText) view.findViewById(R.id.edtvEditName);
	//		String title=null;
	//		mPayout=payout;
	//		//payout is null-add
	//		if (payout==null) {
	//			mPayout=new Payout();
	//			title="新建人员";
	//		}
	//		//payout is not null-update
	//		else{
	//			edtvEditName.setText(payout.getName());
	//			title="修改人员";
	//		}
	//		AlertDialog dialog=new AlertDialog.Builder(PayoutActivity.this)
	//				.setIcon(R.drawable.payout_big_icon)
	//				.setView(view)
	//				.setTitle(title)
	//				.setPositiveButton("保存", new OnClickListener() {
	//					@Override
	//					public void onClick(DialogInterface dialog, int which) {
	//						Payout newPayout=new Payout();
	//						String payoutName=edtvEditName.getText().toString().trim();
	//						boolean checkedResult=RegexTools.IsChineseEnglishNum(payoutName);
	//
	//						if (!checkedResult) {
	//							ShowMsg(edtvEditName.getHint()+TIPMSG);
	//							setAlertDialogClose(dialog, false);
	//							return;
	//						}
	//						else{
	//							setAlertDialogClose(dialog, true);
	//						}
	//						//check whether the payoutName is exist
	//						boolean isRepeated=bussinessPayout.IsExistPayoutByPayoutName(payoutName, mPayout.getId());
	//						if (!checkedResult) {
	//							ShowMsg(REPEATEDMSG);
	//							setAlertDialogClose(dialog, false);
	//							return;
	//						}
	//						else{
	//							setAlertDialogClose(dialog, true);
	//						}
	//						boolean reuslt=false;
	//						//payout is null-add
	//						if (mPayout.getId()==0) {
	//							newPayout.setName(payoutName);
	//							Calendar calendar=Calendar.getInstance();
	//							calendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH,
	//									Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND);
	//							newPayout.setDate(calendar.getTime());
	//							newPayout.setState(1);
	//							ShowMsg(bussinessPayout.insertPayout(newPayout));
	//
	//						}
	//						//payout is not null-update
	//						else{
	//							mPayout.setName(payoutName);
	//							ShowMsg(bussinessPayout.updatePayout(mPayout));
	//						}
	//						notifyChangList();
	//						
	//
	//					}
	//				})
	//				.setNegativeButton("取消", new OnClickListener() {
	//
	//					@Override
	//					public void onClick(DialogInterface dialog, int which) {
	//
	//						setAlertDialogClose(dialog, true);
	//					}
	//				} )
	//				.create();
	//		dialog.show();
	//	}

}
