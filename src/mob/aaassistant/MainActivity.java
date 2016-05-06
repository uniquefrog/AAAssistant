package mob.aaassistant;

import java.util.ArrayList;
import java.util.Date;

import mob.aaassistant.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import mob.aaassistant.base.ActivityFrame;
import mob.aaassistant.bussiness.BusinessDataBackup;
import mob.aaassistant.control.SlideMenuItem;
import mob.aaassistant.control.SlideMenuView;
import mob.aaassistant.control.SlideMenuView.OnSlideMenuListner;
import mob.aaassistant.service.ServiceDatabaseBackup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends ActivityFrame implements OnSlideMenuListner{

	GridView gridviewMainBody;
	//the data source of grid view
	String[] strItems;
	int[] images;
	//the data for sildmenu
	String[] menuDatas;
	//the list data for sildmenu
	ArrayList<SlideMenuItem> menuDataList;
	SlideMenuView slideMenuView;
	BusinessDataBackup mBusinessDataBackup;
	private AlertDialog mDatabaseBackupDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//initialize the view 
		initView();
		//initialize the data
		initData();
		//initialize the event
		initEvent();
		//bind data for the view
		BindData();
		//crrate the slide menu 
		CreateSlideMenu(menuDataList);
		 StartService();
	}
	private void StartService() {
		Intent _Intent = new Intent(this, ServiceDatabaseBackup.class);
		startService(_Intent);
	}
	private void initView() {
		gridviewMainBody=(GridView) this.findViewById(R.id.gridviewMainBody);
	}

	private void initData() {
		strItems=new String[]{"记录消费","查询消费","账本管理",
				"统计管理","类别管理","人员管理"};
		images=new int[]{R.drawable.grid_payout,R.drawable.grid_bill,R.drawable.grid_report,
				R.drawable.grid_account_book,R.drawable.grid_category,R.drawable.grid_user};
		menuDataList=new ArrayList<SlideMenuItem>();
		mBusinessDataBackup = new BusinessDataBackup(MainActivity.this);
		menuDatas=new String[]{"数据备份","软件帮助","关于软件","建议反馈","软件更新"};
		for (int i = 0; i < menuDatas.length; i++) {
			SlideMenuItem item=new SlideMenuItem();
			item.setId(i);
			item.setTitle(menuDatas[i]);
			menuDataList.add(item);
		}
	}	
	private void initEvent() {
		gridviewMainBody.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					OpenActivity(PayoutAddOrEditActivity.class);
					break;
				case 1:
					OpenActivity(PayoutActivity.class);
					break;
				case 2:
					OpenActivity(AccountBookActivity.class);
					break;
				case 3:
					OpenActivity(StatisticActivity.class);
					break;
				case 4:
					OpenActivity(CategoryActivity.class);
					break;
				case 5:
					OpenActivity(UserActivity.class);
					break;
				}
			}
		});
	}
	private void BindData() {
		GridMainBodyAdapter adapter=new GridMainBodyAdapter();
		gridviewMainBody.setAdapter(adapter);		
	}

	public class GridMainBodyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return strItems.length;
		}

		@Override
		public Object getItem(int position) {
			return strItems[position];
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
				convertView=LayoutInflater.from(MainActivity.this)
						.inflate(R.layout.main_body_item, null);
				holder.tvMainBodyItem=(TextView) convertView.findViewById(R.id.tvMainBodyItem);
				holder.imgMainBodyItem=(ImageView) convertView.findViewById(R.id.imgMainBodyItem);
				convertView.setTag(holder);
			}
			else{
				holder=(Holder) convertView.getTag();
			}
			holder.imgMainBodyItem.setImageResource(images[position]);
			holder.tvMainBodyItem.setText(strItems[position]);
			return convertView;
		}
		class Holder {
			ImageView imgMainBodyItem;
			TextView tvMainBodyItem;
		}
	}

	@Override
	public void onSlideMenuItemClick(View pView, SlideMenuItem pSlideMenuItem) {
		switch (pSlideMenuItem.getId()) {
		case 0:
			//data backup
			ShowDatabaseBackupDialog();
			break;

		default:
			break;
		}	
	}
	private void ShowDatabaseBackupDialog()
	{
		LayoutInflater _LayoutInflater = LayoutInflater.from(this);
		
		View _View = _LayoutInflater.inflate(R.layout.database_backup, null);
		
		Button _btnDatabaseBackup = (Button)_View.findViewById(R.id.btnDatabaseBackup);
		Button _btnDatabaseRestore = (Button)_View.findViewById(R.id.btnDatabaseRestore);
		
		_btnDatabaseBackup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mBusinessDataBackup.DatabaseBackup(new Date()))
				{
					ShowMsg("数据备份成功");
				}
				else {
					ShowMsg("数据备份失败");
				}
				
				mDatabaseBackupDialog.dismiss();				
			}
		});
		_btnDatabaseRestore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mBusinessDataBackup.DatabaseRestore())
				{
					ShowMsg("数据还原成功");
				}
				else {
					ShowMsg("数据还原失败");
				}
				
				mDatabaseBackupDialog.dismiss();
			}
		});
		
		String _Title = "数据备份";
		
		AlertDialog.Builder _Builder = new AlertDialog.Builder(this);
		_Builder.setTitle(_Title);
		_Builder.setView(_View);
		_Builder.setIcon(R.drawable.database_backup);
		_Builder.setNegativeButton("返回", null);
		mDatabaseBackupDialog = _Builder.show();
	}


}
