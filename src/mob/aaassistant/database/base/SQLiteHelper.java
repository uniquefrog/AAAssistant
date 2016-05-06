package mob.aaassistant.database.base;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import mob.aaassistant.utils.Reflection;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static SQLiteDataBaseConfig config;
	private static SQLiteHelper INSTANCE;
	private Reflection reflection;
	private Context mcontext;
	public SQLiteHelper(Context context){
		super(context, config.getDatabaseName(), null, config.getVersion());
		this.mcontext=context;
		
	}
	public static SQLiteHelper getInstance(Context context){
		if (INSTANCE==null) {
			config=SQLiteDataBaseConfig.getInstance(context);
			INSTANCE=new SQLiteHelper(context);
			
		}
		return INSTANCE;
		
	}

	public interface SQLiteTable{
		public void onCreate(SQLiteDatabase database);
		public void onUpdate(SQLiteDatabase database);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		ArrayList<String> list=config.getTableList();
		reflection=new Reflection();
		for (int i = 0; i < list.size(); i++) {
			try {
//				SQLiteTable sqliteDatatable=(SQLiteTable) reflection.newInstance
//						(list.get(i), 
//								new Object[]{mcontext}, 
//								new Class[]{Context.class});
				Log.i("uniquefrog", "SQLHlper ´´½¨±í");
//				sqliteDatatable.onCreate(db);
				((SQLiteTable)reflection.newInstance(list.get(i), new Object[]{mcontext}, new Class[]{Context.class})).onCreate(db);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			
		}
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
