package mob.aaassistant.database.base;

import java.util.ArrayList;

import mob.aaassistant.R;

import android.content.Context;

public class SQLiteDataBaseConfig {
	private static final String DATABASE_NAME="AAAssistantDatabase";
	private static final int VERSION=1;
	private static SQLiteDataBaseConfig INSTANCE;
	private static Context mcontext;
	private SQLiteDataBaseConfig(){
		
	}
	
	public  static SQLiteDataBaseConfig getInstance(Context context){
		mcontext=context;
		if (INSTANCE==null) {
			INSTANCE=new SQLiteDataBaseConfig();
		}
		return INSTANCE;
		
	}
	public static String getDatabaseName() {
		return DATABASE_NAME;
	}
	public static int getVersion() {
		return VERSION;
	}
	public ArrayList<String> getTableList(){
		ArrayList<String> tables=new ArrayList<String>();
		String[] tabaleNames=mcontext.getResources().getStringArray(R.array.SQLiteDALClassName);
		String path=mcontext.getPackageName()+".database.sqlitedal.";
		for (int i = 0; i < tabaleNames.length; i++) {
			
			tables.add(path+tabaleNames[i]);
		}
		return tables;
		
	}
	
	
	
}
