package mob.aaassistant;

import java.util.ArrayList;

import mob.aaassistant.R;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import mob.aaassistant.base.ActivityFrame;
import mob.aaassistant.base.AdapterAccountBookSelect;
import mob.aaassistant.bussiness.BusinessStatistics;
import mob.aaassistant.bussiness.BussinessAccountBook;
import mob.aaassistant.control.SlideMenuItem;
import mob.aaassistant.control.SlideMenuView.OnSlideMenuListner;
import mob.aaassistant.entity.AccountBook;

public class StatisticActivity extends ActivityFrame implements OnSlideMenuListner {

	private TextView tvTopTitle;
	private ImageView ivAppBack;
	private TextView tvStatisticsResult;
	//the data for sildmenu
	String[] menuDatas;
	//the list data for sildmenu
	ArrayList<SlideMenuItem> menuDataList;
	AccountBook mAccountBook;
	private BusinessStatistics mBusinessStatistics;
	private BussinessAccountBook mBusinessAccountBook;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistic);
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
		tvTopTitle=(TextView) findViewById(R.id.tvTopTitle);
		ivAppBack=(ImageView) findViewById(R.id.ivAppBack);
		tvStatisticsResult=(TextView) findViewById(R.id.tvStatisticsResult);
	}
	private void initData() {
		menuDatas=new String[]{"切换账本","导出表格"};
		menuDataList=new ArrayList<SlideMenuItem>();
		for (int i = 0; i < menuDatas.length; i++) {
			SlideMenuItem item=new SlideMenuItem();
			item.setId(i);
			item.setTitle(menuDatas[i]);
			menuDataList.add(item);
		}
		mBusinessStatistics=new BusinessStatistics(StatisticActivity.this);
		mBusinessAccountBook=new BussinessAccountBook(StatisticActivity.this);
		mAccountBook=mBusinessAccountBook.GetDefaultModelAccountBook();

	}

	private void initEvent() {
		ivAppBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
	}
	private void BindData() {
		ShowProgressDialog("统计", "正在统计中，请稍候...");
		new Thread(new BindDataThread()).start();
	}
	private class BindDataThread implements Runnable  {
		public void run() {
			String result = mBusinessStatistics.GetPayoutUserIDByAccountBookID(mAccountBook.getId());
			Message message = new Message();
			message.obj = result;
			message.what = 1;
			mHandler.sendMessage(message);
		}
	}

	private void SetTitle() {
		String titel = "统计管理-"+mAccountBook.getName();
		tvTopTitle.setText(titel);
	}
	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String result = (String) msg.obj;
				tvStatisticsResult.setText(result);
				DismissProgressDialog();
				break;
			default:
				break;
			}
		}
	};
	@Override
	public void onSlideMenuItemClick(View pView, SlideMenuItem pSlideMenuItem) {
		SlideToggle();
		if (pSlideMenuItem.getId() == 0) {
			ShowAccountBookSelectDialog();
		}
		if(pSlideMenuItem.getId() == 1) {
			//ExportData();
		}
	}
	private void ShowAccountBookSelectDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
		ListView listView = (ListView)view.findViewById(R.id.listViewSelect);
		AdapterAccountBookSelect adapterAccountBookSelect = new AdapterAccountBookSelect(this);
		listView.setAdapter(adapterAccountBookSelect);

		builder.setTitle("选择账本");
		builder.setNegativeButton("返回", null);
		builder.setView(view);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		listView.setOnItemClickListener(new OnAccountBookItemClickListener(alertDialog));
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
			BindData();
			m_AlertDialog.dismiss();
		}
	}
}
