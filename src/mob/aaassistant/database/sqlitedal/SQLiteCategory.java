package mob.aaassistant.database.sqlitedal;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.util.Log;
import mob.aaassistant.database.base.SQLiteDalBase;
import mob.aaassistant.database.base.SQLiteHelper.SQLiteTable;
import mob.aaassistant.entity.Category;
import mob.aaassistant.utils.DateTools;

public class SQLiteCategory extends SQLiteDalBase{


	private String sql="Create TABLE Category ([CategoryID] integer PRIMARY KEY AUTOINCREMENT NOT NULL,[CategoryName] varchar(50) NOT NULL,[TypeFlag] varchar(20) NOT NULL, [ParentID] integer NOT NULL,[Path] text NOT NULL,[CreatDate] datetime NOT NULL,[State] integer NOT NULL)";
	public SQLiteCategory(Context context) {
		super(context);
	}

	public boolean insertCategory(Category category){
		ContentValues values=createParams(category);
		long successTag=getDataBase().insert("Category", null, values);
		category.setId((int)successTag);
		//if true,successfully insert
		return successTag>0;

	}
	
	public boolean deleteCategory(String condition){
		return deleteSql("Category", condition);
	}
	public boolean updateCategory(Category Category,String condition){
		ContentValues values=createParams(Category);
		return getDataBase().update("Category", values, condition, null) >0;
	}
	public boolean updateCategory(ContentValues values,String condition){
		return getDataBase().update("Category", values, condition, null) >0;
	}
	public  List<Category> getCategory(String condition){
		String sql="Select * From Category " +"Where 1=1 "+condition;
		return GetList(sql);
	}
	public  List<Category> getCategory(Cursor cursor){

		//		String sql="Select * From Where 1=1 "+condition;
		return CursorToList(cursor);
	}
	@Override
	protected String[] GetTableNameAndPK() {
		
		return new String[]{"Category","CategoryID"};
	}

	@Override
	protected Object FindModel(Cursor cursor) {
		Category category=new Category();
		category.setId(cursor.getInt(cursor.getColumnIndex("CategoryID")));
		category.setName(cursor.getString(cursor.getColumnIndex("CategoryName")));
		category.setState(cursor.getInt(cursor.getColumnIndex("State")));
		category.setDate(DateTools.getDate
				(cursor.getString(cursor.getColumnIndex("CreatDate")),
						"yyyy-MM-dd HH:mm:ss"));
		category.setParentID(cursor.getInt(cursor.getColumnIndex("ParentID")));
		category.setPath(cursor.getString(cursor.getColumnIndex("Path")));
		category.setTypeFlag(cursor.getString(cursor.getColumnIndex("TypeFlag")));
		return category;
	}

	private ContentValues createParams(Category category){
		ContentValues values=new ContentValues();
		values.put("CategoryName", category.getName());
		values.put("CreatDate", DateTools.getFormatDateTime(category.getDate(),  "yyyy-MM-dd HH:mm:ss"));
		values.put("State", category.getState());
		values.put("ParentID", category.getParentID());
		values.put("Path", category.getPath());
		values.put("TypeFlag", category.getTypeFlag());
		return values;

	}
	public void initDefaltData(SQLiteDatabase database){
		Category category=new Category();
		String[] datas=new String[]{"买菜","购物","交通","早餐","中餐","晚餐","通讯","房租","水电气费","借贷","其它"};
		for (int i = 0; i < datas.length; i++) {
			String name = datas[i];
			category.setName(name);
			category.setTypeFlag("消费");
			category.setParentID(0);
			category.setPath("");
			ContentValues values=createParams(category);
			long newId= database.insert("Category", null, values);
			if (newId!=-1) {
				category.setPath(newId+".");
				values=createParams(category);
				database.update("Category", values, " CategoryID = " + newId, null);
			}
		}
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		//		StringBuilder s_CreateTableScript = new StringBuilder();
		//
		//		s_CreateTableScript.append("		Create  TABLE Category(");
		//		s_CreateTableScript.append("				[CategoryID] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		//		s_CreateTableScript.append("				,[CategoryName] varchar(10) NOT NULL");
		//		s_CreateTableScript.append("				,[CreateDate] datetime NOT NULL");
		//		s_CreateTableScript.append("				,[State] integer NOT NULL");
		//		s_CreateTableScript.append("				)");

		Log.i("uniquefrog", "创建Category表");
		//		BeginTransaction();
		database.execSQL(sql);
		//		endTransaction();
		initDefaltData(database);
	}

	@Override
	public void onUpdate(SQLiteDatabase database) {

	}

}
