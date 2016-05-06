package mob.aaassistant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mob.aaassistant.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import mob.aaassistant.CategoryActivity.CategoryExpandListAdapter;
import mob.aaassistant.base.ActivityFrame;
import mob.aaassistant.base.AdapterAccountBookSelect;
import mob.aaassistant.bussiness.BusinessPayout;
import mob.aaassistant.bussiness.BussinessAccountBook;
import mob.aaassistant.bussiness.BussinessCategory;
import mob.aaassistant.bussiness.BussinessUser;
import mob.aaassistant.control.NumberDialog;
import mob.aaassistant.control.NumberDialog.OnNumberDialogListener;
import mob.aaassistant.entity.AccountBook;
import mob.aaassistant.entity.Category;
import mob.aaassistant.entity.Payout;
import mob.aaassistant.entity.User;
import mob.aaassistant.utils.DateTools;
import mob.aaassistant.utils.RegexTools;
public class PayoutAddOrEditActivity extends ActivityFrame
implements android.view.View.OnClickListener,OnNumberDialogListener{

	private Button btnSave;
	private Button btnCancel;

	private Payout mPayout;
	private BusinessPayout  mBusinessPayout;
	private Integer mAccountBookID;
	private Integer mCategoryID;
	private String mPayoutUserID;
	private String mPayoutTypeArr[];

	private EditText etAccountBookName;
	private EditText etAmount;
	private AutoCompleteTextView actvCategoryName;
	private EditText etPayoutDate;
	private EditText etPayoutType;
	private EditText etPayoutUser;
	private EditText etComment;
	private Button btnSelectCategory;
	private Button btnSelectUser;
	private Button btnSelectAccountBook;
	private Button btnSelectAmount;
	private Button btnSelectPayoutDate;
	private Button btnSelectPayoutType;
	private BussinessAccountBook mBusinessAccountBook;
	private BussinessCategory mBusinessCategory;
	private AccountBook mAccountBook;
	private BussinessUser mBusinessUser;
	private List<LinearLayout> mItemColor;
	private List<User> mUserSelectedList;
	private TextView tvTopTitle;
	private ImageView ivAppBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payout_add_or_edit);
		//initialize the view 
		initView();
		initVal();
		//initialize the event
		initEvent();
		//bind data for the view
		BindData();
		SetTitle();
	}
	private void SetTitle() {
		String titel;
		if(mPayout == null)
		{
			titel = "新增消费";
		}
		else {
			titel = "修改消费";
			//initialize the data
			initData(mPayout);
		}

		tvTopTitle.setText(titel);
	}
	private void initVal() {
		//		mBusinessCategory=new BussinessCategory(PayoutAddOrEditActivity.this);
		//		Intent intent=getIntent();
		//		mPayout = (Payout) intent.getSerializableExtra("Category");
		//SELECTED_TYPE=intent.getIntExtra("SelectedType", SELECTED_TYPE);
		mBusinessPayout = new BusinessPayout(this);
		mPayout = (Payout) getIntent().getSerializableExtra("Payout");
		mBusinessAccountBook = new BussinessAccountBook(this);
		mBusinessCategory = new BussinessCategory(this);
		mAccountBook = mBusinessAccountBook.GetDefaultModelAccountBook();
		mBusinessUser = new BussinessUser(this);
	}

	private void initView() {
		btnSave = (Button)findViewById(R.id.btnSave);
		btnCancel = (Button)findViewById(R.id.btnCancel);
		btnSelectAccountBook = (Button)findViewById(R.id.btnSelectAccountBook);
		btnSelectAmount = (Button)findViewById(R.id.btnSelectAmount);
		btnSelectCategory = (Button)findViewById(R.id.btnSelectCategory);
		btnSelectPayoutDate = (Button)findViewById(R.id.btnSelectPayoutDate);
		btnSelectPayoutType = (Button)findViewById(R.id.btnSelectPayoutType);
		btnSelectUser = (Button)findViewById(R.id.btnSelectUser);
		etAccountBookName = (EditText) findViewById(R.id.etAccountBookName);
		etPayoutDate = (EditText) findViewById(R.id.etPayoutDate);
		etPayoutType = (EditText) findViewById(R.id.etPayoutType);
		etAmount = (EditText) findViewById(R.id.etAmount);
		actvCategoryName = (AutoCompleteTextView) findViewById(R.id.actvCategoryName);
		etPayoutUser = (EditText) findViewById(R.id.etPayoutUser);
		etComment = (EditText) findViewById(R.id.etComment);
		tvTopTitle=(TextView) findViewById(R.id.tvTopTitle);
		ivAppBack=(ImageView) findViewById(R.id.ivAppBack);
	}

	private void initData(Payout p_Payout)
	{
		etAccountBookName.setText(p_Payout.getAccountBookName());
		mAccountBookID = p_Payout.getAccountBookID();
		etAmount.setText(p_Payout.getAmount().toString());
		actvCategoryName.setText(p_Payout.getCategoryName());
		mCategoryID = p_Payout.getCategoryID();
		etPayoutDate.setText(DateTools.getFormatDateTime(p_Payout.getPayoutDate(), "yyyy-MM-dd"));
		etPayoutType.setText(p_Payout.getPayoutType());

		BussinessUser businessUser = new BussinessUser(this);
		String _UserNameString = businessUser.GetUserNameByUserID(p_Payout.getPayoutUserID());

		etPayoutUser.setText(_UserNameString);
		mPayoutUserID = p_Payout.getPayoutUserID();
		etComment.setText(p_Payout.getComment());
	}
	private void initEvent() {
		btnSave.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnSelectAmount.setOnClickListener(this);
		btnSelectCategory.setOnClickListener(this);
		btnSelectPayoutDate.setOnClickListener(this);
		btnSelectPayoutType.setOnClickListener(this);
		btnSelectUser.setOnClickListener(this);
		actvCategoryName.setOnItemClickListener(new OnAutoCompleteTextViewItemClickListener());
		btnSelectAccountBook.setOnClickListener(this);
		ivAppBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();				
			}
		});
	}

	@Override
	public void onClick(View v) {
		int _ID = v.getId();
		switch (_ID) {
		case R.id.btnSelectAccountBook:
			ShowAccountBookSelectDialog();
			break;
		case R.id.btnSelectAmount:
			(new NumberDialog(this)).show();
			break;
		case R.id.btnSelectCategory:
			ShowCategorySelectDialog();
			break;
		case R.id.btnSelectPayoutDate:
			Calendar calendar = Calendar.getInstance();
			ShowDateSelectDialog(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
			break;
		case R.id.btnSelectPayoutType:
			ShowPayoutTypeSelectDialog();
			break;
		case R.id.btnSelectUser:
			ShowUserSelectDialog(etPayoutType.getText().toString());
			break;
		case R.id.btnSave:
			AddOrEditPayout();
			break;
		case R.id.btnCancel:
			finish();
			break;
		default:
			break;
		}
	}

	private void ShowDateSelectDialog(int year, int month, int day) {
		(new DatePickerDialog(this, new OnDateSelectedListener(), year, month, day)).show();
	}
	private class OnDateSelectedListener implements OnDateSetListener
	{
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			Date _Date = new Date(year-1900, monthOfYear, dayOfMonth);
			etPayoutDate.setText(DateTools.getFormatDateTime(_Date,"yyyy-MM-dd"));
		}
	}
	private void ShowCategorySelectDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.category_select_list, null);
		ExpandableListView expandableListView = (ExpandableListView)view.findViewById(R.id.expandableListViewCategory);
		CategoryActivity.CategoryExpandListAdapter adapterCategorySelect = new CategoryActivity().new CategoryExpandListAdapter(PayoutAddOrEditActivity.this);
		expandableListView.setAdapter(adapterCategorySelect);
		builder.setIcon(R.drawable.category_small_icon);
		builder.setTitle("选择类别");
		builder.setNegativeButton("返回", null);
		builder.setView(view);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		expandableListView.setOnGroupClickListener(new OnCategoryGroupItemClickListener(alertDialog,adapterCategorySelect));
		expandableListView.setOnChildClickListener(new OnCategoryChildItemClickListener(alertDialog,adapterCategorySelect));
	}
	private class OnCategoryGroupItemClickListener implements OnGroupClickListener
	{
		private AlertDialog m_AlertDialog;
		private CategoryActivity.CategoryExpandListAdapter m_AdapterCategory;

		public OnCategoryGroupItemClickListener(AlertDialog p_AlertDialog,CategoryActivity.CategoryExpandListAdapter p_AdapterCategory)
		{
			m_AlertDialog = p_AlertDialog;
			m_AdapterCategory = p_AdapterCategory;
		}
		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			int _Count = m_AdapterCategory.getChildrenCount(groupPosition);
			if(_Count == 0)
			{
				Category category = (Category)m_AdapterCategory.getGroup(groupPosition);
				actvCategoryName.setText(category.getName());
				mCategoryID = category.getId();
				m_AlertDialog.dismiss();
			}
			return false;
		}

	}

	private class OnCategoryChildItemClickListener implements OnChildClickListener
	{
		private AlertDialog m_AlertDialog;
		private CategoryActivity.CategoryExpandListAdapter m_AdapterCategory;

		public OnCategoryChildItemClickListener(AlertDialog p_AlertDialog,CategoryActivity.CategoryExpandListAdapter p_AdapterCategory)
		{
			m_AlertDialog = p_AlertDialog;
			m_AdapterCategory = p_AdapterCategory;
		}
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			Category category = (Category)m_AdapterCategory.getChild(groupPosition, childPosition);
			actvCategoryName.setText(category.getName());
			mCategoryID = category.getId();
			m_AlertDialog.dismiss();
			return false;
		}

	}
	private void ShowUserSelectDialog(String payoutType) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.user, null);
		LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.linearLayoutMain);
		linearLayout.setBackgroundResource(R.drawable.blue);
		ListView listView = (ListView)view.findViewById(R.id.lvUserList);
		UserActivity.UserListViewAdapter adapterUser = new UserActivity().new UserListViewAdapter(PayoutAddOrEditActivity.this);
		listView.setAdapter(adapterUser);
		builder.setIcon(R.drawable.user_small_icon);
		builder.setTitle("选择消费人");
		builder.setNegativeButton("返回", new OnSelectUserBack());
		builder.setView(view);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		listView.setOnItemClickListener(new OnUserItemClickListener(alertDialog,payoutType));


	}
	private class OnSelectUserBack implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface dialog, int which) {
			etPayoutUser.setText("");
			String _Name = "";
			mPayoutUserID = "";
			if(mUserSelectedList != null)
			{
				for(int i=0;i<mUserSelectedList.size();i++)
				{
					_Name += mUserSelectedList.get(i).getName() + ",";
					mPayoutUserID += mUserSelectedList.get(i).getId() + ",";
				}
				etPayoutUser.setText(_Name);
			}

			mItemColor = null;
			mUserSelectedList = null;
		}
	}

	private class OnUserItemClickListener implements AdapterView.OnItemClickListener
	{
		private AlertDialog m_AlertDialog;
		private String m_PayoutType;

		public OnUserItemClickListener(AlertDialog p_AlertDialog,String p_PayoutType)
		{
			m_AlertDialog = p_AlertDialog;
			m_PayoutType = p_PayoutType;
		}
		@Override
		public void onItemClick(AdapterView p_AdapterView, View arg1, int p_Position,
				long arg3) {
			String _PayoutTypeArr[] = getResources().getStringArray(R.array.PayoutType);
			User user = (User)((Adapter)p_AdapterView.getAdapter()).getItem(p_Position);
			if(m_PayoutType.equals(_PayoutTypeArr[0]) || m_PayoutType.equals(_PayoutTypeArr[1]))
			{
				LinearLayout _Main = (LinearLayout)arg1.findViewById(R.id.linearLayoutUserItem);
				if(mItemColor == null && mUserSelectedList == null)
				{
					mItemColor = new ArrayList<LinearLayout>();
					mUserSelectedList = new ArrayList<User>();
				}

				if(mItemColor.contains(_Main))
				{
					_Main.setBackgroundResource(R.drawable.blue);
					mItemColor.remove(_Main);
					mUserSelectedList.remove(user);
				}
				else {
					_Main.setBackgroundResource(R.drawable.red);
					mItemColor.add(_Main);
					mUserSelectedList.add(user);
				}
				return;
			}

			if(m_PayoutType.equals(_PayoutTypeArr[2]))
			{
				mUserSelectedList = new ArrayList<User>();
				mUserSelectedList.add(user);
				etPayoutUser.setText("");
				String _Name = "";
				mPayoutUserID = "";
				for(int i=0;i<mUserSelectedList.size();i++)
				{
					_Name += mUserSelectedList.get(i).getName() + ",";
					mPayoutUserID += mUserSelectedList.get(i).getId() + ",";
				}
				etPayoutUser.setText(_Name);

				mItemColor = null;
				mUserSelectedList = null;
				m_AlertDialog.dismiss();
			}
		}
	}
	private void ShowPayoutTypeSelectDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.payout_type_select_list, null);
		ListView listView = (ListView)view.findViewById(R.id.ListViewPayoutType);

		builder.setTitle("计算方式");
		builder.setNegativeButton("返回", null);
		builder.setView(view);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		listView.setOnItemClickListener(new OnPayoutTypeItemClickListener(alertDialog));
	}

	private void ShowAccountBookSelectDialog() {
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
	private class OnPayoutTypeItemClickListener implements AdapterView.OnItemClickListener
	{
		private AlertDialog m_AlertDialog;
		public OnPayoutTypeItemClickListener(AlertDialog p_AlertDialog)
		{
			m_AlertDialog = p_AlertDialog;
		}
		@Override
		public void onItemClick(AdapterView p_AdapterView, View arg1, int p_Position,
				long arg3) {
			String _PayoutType = (String)p_AdapterView.getAdapter().getItem(p_Position);
			etPayoutType.setText(_PayoutType);
			etPayoutUser.setText("");
			mPayoutUserID = "";
			m_AlertDialog.dismiss();
		}
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
			AccountBook accountBook = (AccountBook)((Adapter)p_AdapterView.getAdapter()).getItem(p_Position);
			etAccountBookName.setText(accountBook.getName());
			mAccountBookID = accountBook.getId();
			m_AlertDialog.dismiss();
		}
	}

	private void BindData() {
		mAccountBookID = mAccountBook.getId();
		etAccountBookName.setText(mAccountBook.getName());
		actvCategoryName.setAdapter(null);
		etPayoutDate.setText(DateTools.getFormatDateTime(new Date(),"yyyy-MM-dd"));
		mPayoutTypeArr = getResources().getStringArray(R.array.PayoutType);
		etPayoutType.setText(mPayoutTypeArr[0]);
	}
	private class OnAutoCompleteTextViewItemClickListener
	implements AdapterView.OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView p_AdapterView, View arg1, int p_Postion,
				long arg3) {
			Category _ModelCategory = (Category)p_AdapterView.getAdapter().getItem(p_Postion);
			mCategoryID = _ModelCategory.getId();
		}

	}

	private void AddOrEditPayout() {
		Boolean _CheckResult = CheckData();
		if(_CheckResult == false)
		{
			return;
		}

		if(mPayout == null)
		{
			mPayout = new Payout();
		}
		mPayout.setAccountBookID(mAccountBookID);
		mPayout.setCategoryID(mCategoryID);
		mPayout.setAmount(new BigDecimal(etAmount.getText().toString().trim()));
		mPayout.setPayoutDate(DateTools.getDate(etPayoutDate.getText().toString().trim(), "yyyy-MM-dd"));
		mPayout.setPayoutType(etPayoutType.getText().toString().trim());
		mPayout.setPayoutUserID(mPayoutUserID);
		mPayout.setComment(etComment.getText().toString().trim());

		Boolean _Result = false;

		if(mPayout.getPayoutID() == 0)
		{
			_Result = mBusinessPayout.InsertPayout(mPayout);
		}
		else {
			_Result = mBusinessPayout.UpdatePayoutByPayoutID(mPayout);
		}

		if(_Result)
		{
			Toast.makeText(getApplicationContext(), "保存成功", 1).show();
			finish();
		}
		else {
			Toast.makeText(getApplicationContext(), "保存失败", 1).show();
		}
	}

	private Boolean CheckData() {
		Boolean _CheckResult = RegexTools.IsMoney(etAmount.getText().toString().trim());
		if(_CheckResult == false) 
		{
			etAmount.requestFocus();
			//			etAmount.setFocusable(false);
			Toast.makeText(getApplicationContext(), "金额必须是数字，并且不能超过小数点后两位数", 1).show();
			return _CheckResult;
		}

		_CheckResult = RegexTools.IsNull(mCategoryID);
		if(_CheckResult == false) 
		{
			btnSelectCategory.setFocusable(true);
			btnSelectCategory.setFocusableInTouchMode(true);
			btnSelectCategory.requestFocus();
			Toast.makeText(getApplicationContext(), "请选择一个消费类别", 1).show();
			return _CheckResult;
		}

		if(mPayoutUserID == null)
		{
			btnSelectUser.setFocusable(true);
			btnSelectUser.setFocusableInTouchMode(true);
			btnSelectUser.requestFocus();
			Toast.makeText(getApplicationContext(), "请选择消费人", 1).show();
			return false;
		}

		String _PayoutType = etPayoutType.getText().toString();
		if(_PayoutType.equals(mPayoutTypeArr[0]) || _PayoutType.equals(mPayoutTypeArr[1]))
		{
			if(mPayoutUserID.split(",").length <= 1)
			{
				btnSelectUser.setFocusable(true);
				btnSelectUser.setFocusableInTouchMode(true);
				btnSelectUser.requestFocus();
				Toast.makeText(getApplicationContext(),"均分或借贷请至少选择两个消费人", 1).show();
				return false;
			}
		}
		else {
			if(mPayoutUserID.equals(""))
			{
				btnSelectUser.setFocusable(true);
				btnSelectUser.setFocusableInTouchMode(true);
				btnSelectUser.requestFocus();
				Toast.makeText(getApplicationContext(), "个人消费请至少选择一个消费人", 1).show();
				return false;
			}
		}

		return true;
	}
	@Override
	public void SetNumberFinish(BigDecimal p_Number) {
		((EditText)findViewById(R.id.etAmount)).setText(p_Number.toString());
	}
}
