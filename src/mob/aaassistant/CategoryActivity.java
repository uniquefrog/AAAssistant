package mob.aaassistant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mob.aaassistant.R;

import android.R.plurals;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnChildClickListener;
import mob.aaassistant.base.ActivityFrame;
import mob.aaassistant.bussiness.BussinessCategory;
import mob.aaassistant.control.SlideMenuItem;
import mob.aaassistant.control.SlideMenuView.OnSlideMenuListner;
import mob.aaassistant.entity.Category;
import mob.aaassistant.entity.CategoryTotal;
import mob.aaassistant.utils.RegexTools;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class CategoryActivity extends ActivityFrame implements OnSlideMenuListner{

	ExpandableListView eplistviewCategoryList;
	//the data for sildmenu
	String[] menuDatas;
	//the list data for sildmenu
	ArrayList<SlideMenuItem> menuDataList;
	//the Category data
	List<Category> categoryDataList;
	//the chile Category data
	List<Object> categoryChildDataList;
	BussinessCategory businessCategory;
	final String TIPMSG="必须是中文英文和数字组成";
	final String REPEATEDMSG="该类别已存在，请换个类别名称";
	CategoryExpandListAdapter adapter;
	Category mCategory;
	Category selectedCategory;
	private int SELECTED_TYPE;
	private TextView tvTopTitle;
	private ImageView ivAppBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
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
		eplistviewCategoryList=(ExpandableListView) this.findViewById(R.id.eplistviewCategoryList);
		tvTopTitle=(TextView) findViewById(R.id.tvTopTitle);
		ivAppBack=(ImageView) findViewById(R.id.ivAppBack);
	}
	private void SetTitle() {
		int count=categoryDataList.size();
		String titel = "类别管理"+"("+count+")";
		tvTopTitle.setText(titel);
	}
	private void initData() {
		menuDatas=new String[]{"新增类别"};//,"统计类别"
		categoryDataList=new ArrayList<Category>();
		categoryChildDataList=new ArrayList<Object>();
		menuDataList=new ArrayList<SlideMenuItem>();
		for (int i = 0; i < menuDatas.length; i++) {
			SlideMenuItem item=new SlideMenuItem();
			item.setId(i);
			item.setTitle(menuDatas[i]);
			menuDataList.add(item);
		}
		businessCategory=new BussinessCategory(CategoryActivity.this);
		categoryDataList=businessCategory.GetNotHideRootCategory();
	}

	private void initEvent() {
		registerForContextMenu(eplistviewCategoryList);
		ivAppBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();				
			}
		});
		
		eplistviewCategoryList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				int type = ExpandableListView.getPackedPositionType(position);
				int groupPosition = ExpandableListView.getPackedPositionGroup(position);
				SELECTED_TYPE=type;
				switch (type) {
				case ExpandableListView.PACKED_POSITION_TYPE_GROUP:
					selectedCategory = (Category)adapter.getGroup(groupPosition);

					break;
				case ExpandableListView.PACKED_POSITION_TYPE_CHILD:
					int childPosition = ExpandableListView.getPackedPositionChild(position);
					selectedCategory = (Category)adapter.getChild(groupPosition, childPosition);
					//			Log.i("uniquefrog", "Group:"+groupPosition+"Child:"+childPosition);
					break;
				default:
					break;
				}
				
			}
		});

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		ExpandableListContextMenuInfo expandableListContextMenuInfo = (ExpandableListContextMenuInfo)menuInfo;
		long position = expandableListContextMenuInfo.packedPosition;
		int type = ExpandableListView.getPackedPositionType(position);
		int groupPosition = ExpandableListView.getPackedPositionGroup(position);
		SELECTED_TYPE=type;
		switch (type) {
		case ExpandableListView.PACKED_POSITION_TYPE_GROUP:
			selectedCategory = (Category)adapter.getGroup(groupPosition);

			break;
		case ExpandableListView.PACKED_POSITION_TYPE_CHILD:
			int childPosition = ExpandableListView.getPackedPositionChild(position);
			selectedCategory = (Category)adapter.getChild(groupPosition, childPosition);
			//			Log.i("uniquefrog", "Group:"+groupPosition+"Child:"+childPosition);
			break;
		default:
			break;
		}

		menu.setHeaderIcon(R.drawable.category_small_icon);
		if(selectedCategory != null)
		{
			menu.setHeaderTitle(selectedCategory.getName());
		}

		CreatMenu(menu);
		menu.add(0, 3, 0, "统计类别");
		//Log.i("uniquefrog", "ChildCountOfGroup:"+adapter.getChildCountOfGroup(groupPosition));

		if(adapter.getChildCountOfGroup(groupPosition) != 0 && selectedCategory.getParentID() == 0)
		{
			menu.findItem(2).setEnabled(false);
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			//update or add
			Intent intent=new Intent(CategoryActivity.this,CategoryAddOrEditActivity.class);
			intent.putExtra("Category", selectedCategory);
			intent.putExtra("SelectedType", SELECTED_TYPE);
			startActivityForResult(intent, 1);
			break;

		case 2:
			//delete

			delete();
			break;
		case 3:
			List<CategoryTotal> list = businessCategory.CategoryTotalByParentID(selectedCategory.getId());
			Intent intentChart = new Intent();
			intentChart.putExtra("Total", (Serializable)list);
			intentChart.setClass(CategoryActivity.this, CategoryChartActivity.class);
			startActivity(intentChart);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	public void delete(){
		AlertDialog dialog=new AlertDialog.Builder(CategoryActivity.this)
				.setTitle("提示")
				.setIcon(R.drawable.category_small_icon)
				.setMessage("确定要删除该类别吗？")
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectedCategory.setState(0);
						boolean result=businessCategory.HideCategoryByCategoryId(selectedCategory.getId());
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		notifyChangList();
		super.onActivityResult(requestCode, resultCode, data);
	}

	protected void notifyChangList() {
		categoryDataList=businessCategory.GetNotHideRootCategory();
		categoryChildDataList=new ArrayList<Object>();
		adapter.notifyDataSetChanged();
	}

	private void BindData() {
		adapter=new CategoryExpandListAdapter(CategoryActivity.this);
		eplistviewCategoryList.setAdapter(adapter);

	}

	@Override
	public void onSlideMenuItemClick(View pView, SlideMenuItem pSlideMenuItem) {
		SlideToggle();
		//		ShowCategoryUpdateOrAddDialog(null);
		if (pSlideMenuItem.getId() == 0) {
			Intent intent = new Intent(this, CategoryAddOrEditActivity.class);
			this.startActivityForResult(intent, 1);
			return;
		}
//
//		if (pSlideMenuItem.getId() == 1) {
//			if (selectedCategory!=null) {
//				List<CategoryTotal> list = businessCategory.CategoryTotalByParentID(selectedCategory.getId());
//				Intent intent = new Intent();
//				intent.putExtra("Total", (Serializable)list);
//				intent.setClass(this, CategoryChartActivity.class);
//				startActivity(intent);
//			}
//			else{
//				ShowMsg("请选择一项类别");
//			}
//
//		}
	}
	public class CategoryExpandListAdapter extends BaseExpandableListAdapter{
		Context context;
		public CategoryExpandListAdapter(Context context){
			this.context=context;
			if (categoryChildDataList==null) {
				categoryChildDataList=new ArrayList<Object>();
			}
			if (categoryDataList==null) {
				categoryDataList=new BussinessCategory(getBaseContext()).GetNotHideRootCategory();
			}
		}

		public Integer getChildCountOfGroup(int groupPosition)
		{
			return (Integer) categoryChildDataList.get(groupPosition);
		}
		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return categoryDataList.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			Category category = (Category) getGroup(groupPosition);
			List list = new BussinessCategory(context).GetNotHideCategoryListByParentID(category.getId());
			return list.size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return (Category)categoryDataList.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			Category category = (Category) getGroup(groupPosition);
			List list = new BussinessCategory(context).GetNotHideCategoryListByParentID(category.getId());
			return list.get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			//行是否具有唯一id
			//是否指定分组视图及其子视图的ID对应的后台数据改变也会保持该ID
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			GroupHolder groupHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.category_parent_list_item, null);
				groupHolder = new GroupHolder();
				groupHolder.tvCategoryName = (TextView)convertView.findViewById(R.id.tvCategoryName);
				groupHolder.tvChildNum = (TextView)convertView.findViewById(R.id.tvChildNum);
				convertView.setTag(groupHolder);
			}
			else {
				groupHolder = (GroupHolder)convertView.getTag();
			}
			Category category = (Category)getGroup(groupPosition);
			groupHolder.tvCategoryName.setText(category.getName());
			int count = new BussinessCategory(context).GetNotHideCountByParentID(category.getId());
			groupHolder.tvChildNum.setText(count+"个子类");
			//Log.i("uniquefrog", "count:"+count+" categoryChildDataList:"+categoryChildDataList.size());
			categoryChildDataList.add(count);
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			ChildHolder childHolder;
			if(convertView == null)
			{
				convertView = LayoutInflater.from(context).inflate(R.layout.category_child_list_item, null);
				childHolder = new ChildHolder();
				childHolder.tvCategoryChildName = (TextView)convertView.findViewById(R.id.tvCategoryChildName);
				convertView.setTag(childHolder);
			}
			else {
				childHolder = (ChildHolder)convertView.getTag();
			}
			Category category = (Category)getChild(groupPosition, childPosition);
			childHolder.tvCategoryChildName.setText(category.getName());
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		private class GroupHolder
		{
			TextView tvCategoryName;
			TextView tvChildNum;
		}

		private class ChildHolder
		{
			TextView tvCategoryChildName;
		}
	}

}
