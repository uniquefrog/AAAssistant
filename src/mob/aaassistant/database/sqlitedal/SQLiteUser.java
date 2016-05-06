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
import mob.aaassistant.entity.User;
import mob.aaassistant.utils.DateTools;

public class SQLiteUser extends SQLiteDalBase{


	private String sql="Create TABLE User ([UserID] integer PRIMARY KEY AUTOINCREMENT NOT NULL,[UserName] varchar(50) NOT NULL,[CreatDate] datetime NOT NULL,[State] integer NOT NULL)";
	public SQLiteUser(Context context) {
		super(context);

	}

	public boolean insertUser(User user){
		ContentValues values=createParams(user);
		long successTag=getDataBase().insert("User", null, values);
		user.setId((int)successTag);
		//if true,successfully insert
		return successTag>0;

	}
	public boolean deleteUser(String condition){
		return deleteSql("User", condition);
	}
	public boolean updateUser(User user,String condition){
		ContentValues values=createParams(user);
		return getDataBase().update("User", values, condition, null) >0;
	}
	public boolean updateUser(ContentValues values,String condition){
		return getDataBase().update("User", values, condition, null) >0;
	}
	public  List<User> getUser(String condition){

		String sql="Select * From User " +"Where 1=1 "+condition;
		return GetList(sql);
	}
	public  List<User> getUser(Cursor cursor){

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
		User user=new User();
		user.setId(cursor.getInt(cursor.getColumnIndex("UserID")));
		user.setName(cursor.getString(cursor.getColumnIndex("UserName")));
		user.setState(cursor.getInt(cursor.getColumnIndex("State")));
		user.setDate(DateTools.getDate
				(cursor.getString(cursor.getColumnIndex("CreatDate")),
						"yyyy-MM-dd HH:mm:ss"));

		return user;
	}

	private ContentValues createParams(User user){
		ContentValues values=new ContentValues();
		values.put("UserName", user.getName());
		values.put("CreatDate", DateTools.getFormatDateTime(user.getDate(),  "yyyy-MM-dd HH:mm:ss"));
		values.put("State", user.getState());
		return values;

	}
	public void initDefaltData(SQLiteDatabase database){

		User user=new User();
		String[] datas=new String[]{"王青鹏","李兴明"};
		for (int i = 0; i < datas.length; i++) {
			String name = datas[i];
			user.setName(name);
			ContentValues values=createParams(user);
			database.insert("User", null, values);
		}
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
//		StringBuilder s_CreateTableScript = new StringBuilder();
//
//		s_CreateTableScript.append("		Create  TABLE User(");
//		s_CreateTableScript.append("				[UserID] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
//		s_CreateTableScript.append("				,[UserName] varchar(10) NOT NULL");
//		s_CreateTableScript.append("				,[CreateDate] datetime NOT NULL");
//		s_CreateTableScript.append("				,[State] integer NOT NULL");
//		s_CreateTableScript.append("				)");

		Log.i("uniquefrog", "创建User表");
//		BeginTransaction();
		database.execSQL(sql);
//		endTransaction();
		initDefaltData(database);
	}

	@Override
	public void onUpdate(SQLiteDatabase database) {

	}

}
