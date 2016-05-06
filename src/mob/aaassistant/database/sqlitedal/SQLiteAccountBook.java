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
import mob.aaassistant.entity.AccountBook;
import mob.aaassistant.utils.DateTools;

public class SQLiteAccountBook extends SQLiteDalBase{


	private String sql="Create TABLE AccountBook ([AccountBookID] integer PRIMARY KEY AUTOINCREMENT NOT NULL,[AccountBookName] varchar(50) NOT NULL,[CreatDate] datetime NOT NULL,[State] integer NOT NULL,[IsDefault] integer NOT NULL)";
	public SQLiteAccountBook(Context context) {
		super(context);

	}

	public boolean insertAccountBook(AccountBook AccountBook){
		ContentValues values=createParams(AccountBook);
		long successTag=getDataBase().insert("AccountBook", null, values);
		AccountBook.setId((int)successTag);
		//if true,successfully insert
		return successTag>0;

	}
	
	public boolean deleteAccountBook(String condition){
		return deleteSql("AccountBook", condition);
	}
	public boolean updateAccountBook(AccountBook AccountBook,String condition){
		ContentValues values=createParams(AccountBook);
		return getDataBase().update("AccountBook", values, condition, null) >0;
	}
	public boolean updateAccountBook(ContentValues values,String condition){
		return getDataBase().update("AccountBook", values, condition, null) >0;
	}
	public  List<AccountBook> getAccountBook(String condition){
		String sql="Select * From AccountBook " +"Where 1=1 "+condition;
		return GetList(sql);
	}
	public  List<AccountBook> getAccountBook(Cursor cursor){

		//		String sql="Select * From Where 1=1 "+condition;
		return CursorToList(cursor);
	}
	@Override
	protected String[] GetTableNameAndPK() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object FindModel(Cursor cursor) {
		AccountBook AccountBook=new AccountBook();
		AccountBook.setId(cursor.getInt(cursor.getColumnIndex("AccountBookID")));
		AccountBook.setName(cursor.getString(cursor.getColumnIndex("AccountBookName")));
		AccountBook.setState(cursor.getInt(cursor.getColumnIndex("State")));
		AccountBook.setDate(DateTools.getDate
				(cursor.getString(cursor.getColumnIndex("CreatDate")),
						"yyyy-MM-dd HH:mm:ss"));
		AccountBook.setIsDefault(cursor.getInt(cursor.getColumnIndex("IsDefault") ));
		return AccountBook;
	}

	private ContentValues createParams(AccountBook AccountBook){
		ContentValues values=new ContentValues();
		values.put("AccountBookName", AccountBook.getName());
		values.put("CreatDate", DateTools.getFormatDateTime(AccountBook.getDate(),  "yyyy-MM-dd HH:mm:ss"));
		values.put("State", AccountBook.getState());
		values.put("IsDefault", AccountBook.getIsDefault());
		return values;

	}
	public void initDefaltData(SQLiteDatabase database){
		AccountBook AccountBook=new AccountBook();
		String[] datas=new String[]{"默认账本"};
		String name = datas[0];
		AccountBook.setName(name);
		AccountBook.setIsDefault(1);
		ContentValues values=createParams(AccountBook);
		database.insert("AccountBook", null, values);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		//		StringBuilder s_CreateTableScript = new StringBuilder();
		//
		//		s_CreateTableScript.append("		Create  TABLE AccountBook(");
		//		s_CreateTableScript.append("				[AccountBookID] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		//		s_CreateTableScript.append("				,[AccountBookName] varchar(10) NOT NULL");
		//		s_CreateTableScript.append("				,[CreateDate] datetime NOT NULL");
		//		s_CreateTableScript.append("				,[State] integer NOT NULL");
		//		s_CreateTableScript.append("				)");

		Log.i("uniquefrog", "创建AccountBook表");
		//		BeginTransaction();
		database.execSQL(sql);
		//		endTransaction();
		initDefaltData(database);
	}

	@Override
	public void onUpdate(SQLiteDatabase database) {

	}

}
