package mob.aaassistant.bussiness;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import mob.aaassistant.utils.FileUtil;

public class BusinessDataBackup extends BussinessBase {

	public BusinessDataBackup(Context p_Context) {
		super(p_Context);
	}

	
	public boolean DatabaseBackup(Date p_Backup) {
		boolean result = false;
		try {
			File sourceFile = new File("/data/data/" + GetContext().getPackageName() + "/databases/AAAssistantDatabase");
			
			if(sourceFile.exists())
			{
				File fileDir = new File("/sdcard/AAAssistant/DataBaseBak/");
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				//Log.i("uniquefrog", "数据库地址："+"/data/data/" + GetContext().getPackageName() + "/databases/AAAssistantDatabase");
				//Log.i("uniquefrog", "备份地址："+"/sdcard/AAAssistant/DataBaseBak/AAAssistantDatabase");
				FileUtil.cp("/data/data/" + GetContext().getPackageName() + "/databases/AAAssistantDatabase", "/sdcard/AAAssistant/DataBaseBak/AAAssistantDatabase");

			}
			
			SaveDatabaseBackupDate(p_Backup.getTime());
			
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public boolean DatabaseRestore() {
		boolean result = false;
		try {
			long databaseBackupDate = LoadDatabaseBackupDate();
			
			if(databaseBackupDate != 0)
			{
				FileUtil.cp("/sdcard/AAAssistant/DataBaseBak/AAAssistantDatabase", "/data/data/mob.aaassistant/databases/AAAssistantDatabase");
			}
			
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public void SaveDatabaseBackupDate(long millise)
	{		
//		MODE_PRIVATE：为默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下，写入的内容会覆盖原文件的内容，如果想把新写入的内容追加到原文件中，可以使用MODE_APPEND
//		MODE_APPEND：模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件
//		MODE_WORLD_READABLE和MODE_WORLD_WRITEABLE用来控制其他应用是否有权限读写该文件
//		MODE_WORLD_READABLE：表示当前文件可以被其他应用读取；MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入
		
		//获取指定Key的SharedPreferences对象
		SharedPreferences sP = GetContext().getSharedPreferences("DatabaseBackupDate",Context.MODE_PRIVATE);
		//获取编辑
		SharedPreferences.Editor editor = sP.edit();
		//按照指定Key放入数据
		editor.putLong("DatabaseBackupDate", millise);
		//提交保存数据
		editor.commit();
	}
	
	public long LoadDatabaseBackupDate()
	{
		long databaseBackupDate = 0;
		//获取指定Key的SharedPreferences对象
		SharedPreferences sP = GetContext().getSharedPreferences("DatabaseBackupDate",Context.MODE_PRIVATE);
		//数据为空证明还不存在
		if (sP != null) {
			//否则就获取指定Key的数据
			databaseBackupDate = sP.getLong("DatabaseBackupDate", 0);
		}
		
		return databaseBackupDate;
	}
}
