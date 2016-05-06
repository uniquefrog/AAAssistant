package mob.aaassistant;

import mob.aaassistant.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;
import mob.aaassistant.base.ActivityFrame;
import mob.aaassistant.bussiness.BussinessCategory;
import mob.aaassistant.entity.Category;
import mob.aaassistant.utils.RegexTools;

public class CategoryAddOrEditActivity extends ActivityFrame {

	private Button btnSave;
	private Button btnCancel;
	private BussinessCategory mBusinessCategory;
	private Category mCategory;
	private Spinner spParentID;
	private EditText edtvCategoryName;
	final String TIPMSG="必须是中文英文和数字组成";
	final String REPEATEDMSG="该类别已存在，请换个类别名称";
	private int SELECTED_TYPE;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_add_or_edit);
		//initialize the view 
		initView();
		initVal();
		//initialize the event
		initEvent();
		//bind data for the view
		BindData();
		if (mCategory!=null) {
			//initialize the data
			initData(mCategory);
		}

	}

	private void initVal() {
		mBusinessCategory=new BussinessCategory(CategoryAddOrEditActivity.this);
		Intent intent=getIntent();
		mCategory = (Category) intent.getSerializableExtra("Category");
		SELECTED_TYPE=intent.getIntExtra("SelectedType", SELECTED_TYPE);
		
	}

	private void initView() {
		btnSave = (Button)findViewById(R.id.btnSave);
		btnCancel = (Button)findViewById(R.id.btnCancel);
		edtvCategoryName = (EditText)findViewById(R.id.edtvCategoryName);
		spParentID = (Spinner)findViewById(R.id.SpinnerParentID);
	}

	private void initData(Category pCategory) {

		edtvCategoryName.setText(pCategory.getName());
		ArrayAdapter arrayAdapter = (ArrayAdapter) spParentID.getAdapter();
		if(pCategory.getId() != 0)
		{
			int position = 0;
			for(int i=1;i<arrayAdapter.getCount();i++)
			{
				Category category = (Category)arrayAdapter.getItem(i);
				if(category.getId() == pCategory.getId())
				{
					position = arrayAdapter.getPosition(category);
				}
			}	
			spParentID.setSelection(position);
		}
		else {
			int count = mBusinessCategory.GetNotHideCountByParentID(pCategory.getId());
			if(count != 0)
			{
				spParentID.setEnabled(false);
			}
		}
	}

	private void initEvent() {
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AddOrEditCategory();
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}


	private void BindData() {
		ArrayAdapter<Category> arrayAdapter = mBusinessCategory.GetRootCategoryArrayAdapter();
		spParentID.setAdapter(arrayAdapter);
	}


	protected void AddOrEditCategory() {
		String _CategoryName = edtvCategoryName.getText().toString().trim();
		Boolean _CheckResult = RegexTools.IsChineseEnglishNum(_CategoryName);
		if(!_CheckResult)
		{
			ShowMsg(TIPMSG);
			return;
		}

		//新增
		if(mCategory == null)
		{
			mCategory = new Category();
			mCategory.setTypeFlag("消费");
			mCategory.setPath("");
			if(!spParentID.getSelectedItem().toString().equals("--请选择--"))
			{
				Category category = (Category)spParentID.getSelectedItem();
				if(category != null)
				{
					mCategory.setParentID(category.getId());

				}

			} else {
				mCategory.setParentID(0);
			}
		}
		else{
			//修改
			switch (SELECTED_TYPE) {
			case ExpandableListView.PACKED_POSITION_TYPE_GROUP:
				
				break;
			case ExpandableListView.PACKED_POSITION_TYPE_CHILD:
				if(!spParentID.getSelectedItem().toString().equals("--请选择--"))
				{
					Category category = (Category)spParentID.getSelectedItem();
					if(category != null)
					{
						mCategory.setParentID(category.getId());
					}

				} else {
					mCategory.setParentID(0);
				}
				break;
			}
			
		}
		mCategory.setName(_CategoryName);
		

		Boolean _Result = false;

		if(mCategory.getId() == 0)
		{
			_Result = mBusinessCategory.insertCategory(mCategory);
		}
		else {
			_Result = mBusinessCategory.UpdateCategoryByCategoryID(mCategory);
		}

		if(_Result)
		{
			ShowMsg("保存成功");
			finish();
		}
		else {
			ShowMsg("保存失败");
		}
	}
}
