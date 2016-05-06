package mob.aaassistant.bussiness;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.ArrayAdapter;
import mob.aaassistant.database.sqlitedal.SQLiteCategory;
import mob.aaassistant.entity.Category;
import mob.aaassistant.entity.CategoryTotal;

public class BussinessCategory extends BussinessBase{

	private SQLiteCategory mSqLiteCategory;
	private final String  TYPE_FLAG = " And TypeFlag= '消费'";
	public BussinessCategory(Context context) {
		super(context);
		mSqLiteCategory=new SQLiteCategory(context);
	}

	public boolean insertCategory(Category category){
		mSqLiteCategory.BeginTransaction();
		boolean bResult1=mSqLiteCategory.insertCategory(category);
		boolean bResult2=false;
		Category parentCategory=getCategory(category.getParentID());
		String path;
		try {
			if (parentCategory!=null) {
				path=parentCategory.getPath()+category.getId()+".";
			}
			else{
				path=category.getId()+".";
			}
			category.setPath(path);
			bResult2=UpdateCategoryInsertTypeByCategoryID(category);
			if(bResult1 && bResult2)
			{
				mSqLiteCategory.setTransactionSuccessful();
				return true;
			}
			else {
				return false;
			}

		} catch (Exception e) {
			return false;		
		}
		finally {
			mSqLiteCategory.endTransaction();
		}
	}
	//	private boolean setIsDefault(int categoryId) {
	//		String condition1=" IsDefault = 1";
	//		ContentValues values1=new ContentValues();
	//		values1.put("IsDefault", 0);
	//		boolean result1=mSqLiteCategory.updateCategory(values1, condition1);
	//		String condition2="CategoryId =" +categoryId;
	//		ContentValues values2=new ContentValues();
	//		values2.put("IsDefault", 1);
	//		boolean result2=mSqLiteCategory.updateCategory(values2, condition2);
	//		if (result1 && result2) {
	//			return true;
	//		}
	//		else{
	//			return false;
	//		}
	//		
	//		
	//	}

	public String deleteCategoryByID(int categoryId){
		String condition=" And CategoryID="+categoryId;
		boolean bResult=mSqLiteCategory.deleteCategory(condition);
		if (bResult) {
			return "删除数据成功";
		}
		return "删除数据失败";

	}
	public Boolean DeleteCategoryByPath(String p_Path) throws Exception
	{
		/*int _Count = m_SqLiteDALCategory.GetCount("PayoutID", "v_Payout", " And Path Like '" + p_Path + "%'");
		
		if(_Count != 0)
		{
			throw new Exception(GetString(R.string.ErrorMessageExistPayout));
		}
		
		String _Condition = " And Path Like '" + p_Path + "%'";
		Boolean _Result = m_SqLiteDALCategory.DeleteCategory(_Condition);
		return _Result;*/
		
		return true;
	}
	public String updateCategory(Category category){
		String condition=" CategoryID="+category.getId();
		boolean bResult=mSqLiteCategory.updateCategory(category, condition);
		if (bResult) {
			return "修改数据成功";
		}
		return "修改数据失败";

	}
	private List<Category> getCategoryList(String condition){
		return mSqLiteCategory.getCategory(condition);
	}
	public List<Category> getCategoryHideList(){
		return getCategoryList(" And State=1 ");
	}
	private Category getCategory(int categoryId){
		String condition=" And CategoryID="+categoryId;
		List<Category> list=mSqLiteCategory.getCategory(condition);
		if (list.size()==1) {
			return list.get(0);
		}
		return null;

	}

	private List<Category> getCategoryList(int[] categoryIds){
		List<Category> list=new ArrayList<Category>();
		for (int i = 0; i < categoryIds.length; i++) {
			Category category= getCategory(categoryIds[i]);
			list.add(category);
		}
		return list;


	}
	public List<CategoryTotal> CategoryTotalByParentID(int p_ParentID)
	{
		String _Condition = TYPE_FLAG + " And ParentID = " + p_ParentID;
		Log.i("uniquefrog", "查询条件："+_Condition);
		List<CategoryTotal> _ModelCategoryTotalList = CategoryTotal(_Condition);
		
		return _ModelCategoryTotalList;
	}
	public List<CategoryTotal> CategoryTotalByRootCategory()
	{
		String condition = TYPE_FLAG + " And ParentID = 0 And State = 1";
		List<CategoryTotal> categoryTotalList = CategoryTotal(condition);
		
		return categoryTotalList;
	}
	public List<CategoryTotal> CategoryTotal(String p_Condition)
	{
		String _Condition = p_Condition;
		Cursor cursor = mSqLiteCategory.excuteSql("Select Count(PayoutID) As Count, Sum(Amount) As SumAmount, CategoryName From v_Payout Where 1=1 " + _Condition + " Group By CategoryName");
		List<CategoryTotal> categoryTotalList = new ArrayList<CategoryTotal>();
		Log.i("uniquefrog", "得到的查询数据："+cursor.getCount());
		while (cursor.moveToNext()) {
			CategoryTotal categoryTotal = new CategoryTotal();
			categoryTotal.Count = cursor.getString(cursor.getColumnIndex("Count"));
			categoryTotal.SumAmount = cursor.getString(cursor.getColumnIndex("SumAmount"));
			categoryTotal.CategoryName = cursor.getString(cursor.getColumnIndex("CategoryName"));
			categoryTotalList.add(categoryTotal);
		}
		Log.i("uniquefrog", "数据库中查询到"+categoryTotalList.size()+"条");
		return categoryTotalList;
	}
	public boolean IsExistCategoryByCategoryName(String pCategoryName,Integer pCategoryID)
	{
		String condition = " And CategoryName = '" + pCategoryName + "'";
		if(pCategoryID != null)
		{
			condition += " And CategoryID <> " + pCategoryID;
		}
		List list = mSqLiteCategory.getCategory(condition);
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean HideCategoryByCategoryId(int CategoryId){
		String condition=" CategoryID="+CategoryId;
		ContentValues values=new ContentValues();
		values.put("State", 0);
		return mSqLiteCategory.updateCategory(values, condition);
	}
	public Boolean UpdateCategoryInsertTypeByCategoryID(Category category)
	{
		String condition = " CategoryID = " + category.getId();
		Boolean result = mSqLiteCategory.updateCategory(category,condition);		

		if(result)
		{
			return true;
		}
		else {
			return false;
		}
	}
	public Boolean UpdateCategoryByCategoryID(Category category)
	{
		mSqLiteCategory.BeginTransaction();
		try {
			String condition = " CategoryID = " + category.getId();
			Boolean result1 = mSqLiteCategory.updateCategory(category, condition);
			Boolean result2 = true;
			

			Category parentModelCategory = getCategory(category.getParentID());
			String path;
			if(parentModelCategory != null)
			{
				path = parentModelCategory.getPath() + category.getId() + ".";
			}
			else {
				path = category.getId() + ".";
			}
			
			category.setPath(path);
			result2 = UpdateCategoryInsertTypeByCategoryID(category);
			
			if(result1 && result2)
			{
				mSqLiteCategory.setTransactionSuccessful();
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			mSqLiteCategory.endTransaction();
		}
	}

	public List<Category> GetNotHideCategory()
	{
		return mSqLiteCategory.getCategory(TYPE_FLAG + " And State = 1");
	}
	
	public int GetNotHideCount()
	{
		return mSqLiteCategory.getCount(TYPE_FLAG + " And State = 1");
	}
	
	public int GetNotHideCountByParentID(int categoryID)
	{
		return mSqLiteCategory.getCount(TYPE_FLAG + " And ParentID = " + categoryID + " And State = 1");
	}
	
	public List<Category> GetNotHideRootCategory()
	{
		return mSqLiteCategory.getCategory(TYPE_FLAG + " And ParentID = 0 And State = 1");
	}
	
	public List<Category> GetNotHideCategoryListByParentID(int parentID)
	{
		return mSqLiteCategory.getCategory(TYPE_FLAG + " And ParentID = " + parentID + " And State = 1");
	}
	public ArrayAdapter GetRootCategoryArrayAdapter()
	{
		List list = GetNotHideRootCategory();
		list.add(0,"--请选择--");
		ArrayAdapter adapter = new ArrayAdapter(GetContext(), android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}

}
