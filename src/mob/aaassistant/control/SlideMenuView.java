package mob.aaassistant.control;

import java.util.ArrayList;

import org.w3c.dom.Text;

import mob.aaassistant.R;

import android.app.Activity;
import android.content.Context;
import android.opengl.Visibility;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import mob.aaassistant.MainActivity;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SlideMenuView {

	private Activity activity;
	private ListView listviewMenu;
	Button btnBottomMenu;
	private boolean mclose;
	ArrayList<SlideMenuItem> dataList;
	private TextView tvTitle;
	LinearLayout layBody;
	LinearLayout layMenuList;
	RelativeLayout topTitle;
	private OnSlideMenuListner mOnSlideMenuListner;
	public interface OnSlideMenuListner{
		public void onSlideMenuItemClick(View pView,SlideMenuItem pSlideMenuItem);
	}
	public SlideMenuView(Activity activity,ArrayList<SlideMenuItem> dataList){
		//initialize the data
		initData();
		this.activity=activity;
		this.dataList=dataList;
		if (activity instanceof OnSlideMenuListner) {
			mOnSlideMenuListner=(OnSlideMenuListner) activity;
		}
		//initialize the view 
		initView();
		
		//initialize the event
		initEvent();
		
	}
	private void initView() {
		btnBottomMenu=(Button) activity.findViewById(R.id.btnBottomMenu);
		listviewMenu=(ListView) activity.findViewById(R.id.listviewMenu);
		layBody=(LinearLayout) activity.findViewById(R.id.layBody);
		tvTitle=(TextView) activity.findViewById(R.id.tvTitle);
		layMenuList=(LinearLayout) activity.findViewById(R.id.layMenuList);
		topTitle=(RelativeLayout) activity.findViewById(R.id.topTitle);
	}
	private void initData() {
		mclose=true;
		dataList=new ArrayList<SlideMenuItem>();
		
	}
	private void initEvent() {
		btnBottomMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Toggle();
				
				//bind data for the view
				BindData();
			}
		});
		listviewMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SlideMenuItem item=(SlideMenuItem) parent.getItemAtPosition(position);
				mOnSlideMenuListner.onSlideMenuItemClick(view, item);
			}
		});
		btnBottomMenu.setFocusableInTouchMode(true);
		btnBottomMenu.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode==KeyEvent.KEYCODE_MENU && event.getAction()==KeyEvent.ACTION_UP) {
					Toggle();
				}
				return false;
			}
		});
		
	}
	private void BindData() {
		SlideMenuAdapter adapter=new SlideMenuAdapter();
		listviewMenu.setAdapter(adapter);
		
	}
	public void Open(){
		mclose=false;
		if (tvTitle!=null) {
			tvTitle.setVisibility(View.GONE);
		}
		else{
			topTitle.setVisibility(View.GONE);
		}
		
		layBody.setVisibility(View.GONE);
		layMenuList.setVisibility(View.VISIBLE);
	}
	public void Close(){
		mclose=true;
		if (tvTitle!=null) {
			tvTitle.setVisibility(View.VISIBLE);
		}
		else{
			topTitle.setVisibility(View.VISIBLE);
		}
		layBody.setVisibility(View.VISIBLE);
		layMenuList.setVisibility(View.GONE);
	}
	public void Toggle(){
		if (mclose) {
			//closed now
			Open();
		}
		else{
			//opened now
			Close();
		}
	}
	public void Add(SlideMenuItem item){
		dataList.add(item);
	}
//	public void RemoveBottomBox()
//	{
//		RelativeLayout _MainLayout = (RelativeLayout)activity.findViewById(R.id.layMainLayout);
//		_MainLayout.removeView(layBottomBox);
//		layBottomBox = null;
//	}
	public class SlideMenuAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			return dataList.get(position);
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
				convertView=LayoutInflater.from(activity)
						.inflate(R.layout.menu_item_layout, null);
				holder.tvMenuItem=(TextView) convertView.findViewById(R.id.tvMenuItem);
				convertView.setTag(holder);
			}
			else{
				holder=(Holder) convertView.getTag();
			}
			
			SlideMenuItem item=dataList.get(position);
			
			holder.tvMenuItem.setText(item.getTitle());
			
			return convertView;
		}
		class Holder{
			TextView tvMenuItem;
		}
		
		
		
	}


}
