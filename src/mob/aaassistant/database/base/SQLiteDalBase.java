package mob.aaassistant.database.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import mob.aaassistant.database.base.SQLiteHelper.SQLiteTable;

public abstract class SQLiteDalBase  implements SQLiteTable {

	private Context context;
	private SQLiteDatabase database;
	private SQLiteHelper sqLiteHelper;
	public SQLiteDalBase(Context context){
		this.context=context;
	}
	protected Context GetContext()
	{
		return context;
	}
	public SQLiteDatabase getDataBase(){
		if (database==null) {
			database=sqLiteHelper.getInstance(context).getWritableDatabase();
		}
		return database;
	}
	protected abstract String[] GetTableNameAndPK();
	public int getCount(String condition){
		String[] strNames=GetTableNameAndPK();

//		String sql="select "+strNames[1]+" From "+strNames[0]+" where 1=1 "+condition;
//		Log.i("uniquefrog", sql);
		Cursor cursor=
				excuteSql("select "+strNames[1]+" From "+strNames[0]+" where 1=1 "+condition);
		int count=cursor.getCount();
		cursor.close();
		return count;
	}
	public int getCount(String key,String tableName,String condition){
		Cursor cursor=
				excuteSql("select "+key+" From "+tableName+" where 1=1 "+condition);
		int count=cursor.getCount();
		cursor.close();
		return count;
	}

	public Cursor excuteSql(String sql){

		return getDataBase().rawQuery(sql, null);
	}

	public void BeginTransaction(){
		getDataBase().beginTransaction();
	}
	public void setTransactionSuccessful(){
		getDataBase().setTransactionSuccessful();
	}
	public void endTransaction(){
		getDataBase().endTransaction();
	}

	public boolean deleteSql(String tableName,String condition){
		return getDataBase().delete(tableName, " 1=1 "+condition, null)>0;
	}
	protected List GetList(String sql)
	{
		Cursor cursor = excuteSql(sql);
		List cursorList = new ArrayList();
		cursorList=CursorToList(cursor);
		cursor.close();
		return cursorList;
	}
	
	protected abstract Object FindModel(Cursor cursor);
	
	protected List CursorToList(Cursor cursor)
	{
		List cursorList = new ArrayList();
		while(cursor.moveToNext())
		{
			Object object = FindModel(cursor);
			cursorList.add(object);
		}
		cursor.close();
		return cursorList;
	}


}
