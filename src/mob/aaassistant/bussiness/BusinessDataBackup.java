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
				//Log.i("uniquefrog", "���ݿ��ַ��"+"/data/data/" + GetContext().getPackageName() + "/databases/AAAssistantDatabase");
				//Log.i("uniquefrog", "���ݵ�ַ��"+"/sdcard/AAAssistant/DataBaseBak/AAAssistantDatabase");
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
//		MODE_PRIVATE��ΪĬ�ϲ���ģʽ��������ļ���˽�����ݣ�ֻ�ܱ�Ӧ�ñ�����ʣ��ڸ�ģʽ�£�д������ݻḲ��ԭ�ļ������ݣ���������д�������׷�ӵ�ԭ�ļ��У�����ʹ��MODE_APPEND
//		MODE_APPEND��ģʽ�����ļ��Ƿ���ڣ����ھ����ļ�׷�����ݣ�����ʹ������ļ�
//		MODE_WORLD_READABLE��MODE_WORLD_WRITEABLE������������Ӧ���Ƿ���Ȩ�޶�д���ļ�
//		MODE_WORLD_READABLE����ʾ��ǰ�ļ����Ա�����Ӧ�ö�ȡ��MODE_WORLD_WRITEABLE����ʾ��ǰ�ļ����Ա�����Ӧ��д��
		
		//��ȡָ��Key��SharedPreferences����
		SharedPreferences sP = GetContext().getSharedPreferences("DatabaseBackupDate",Context.MODE_PRIVATE);
		//��ȡ�༭
		SharedPreferences.Editor editor = sP.edit();
		//����ָ��Key��������
		editor.putLong("DatabaseBackupDate", millise);
		//�ύ��������
		editor.commit();
	}
	
	public long LoadDatabaseBackupDate()
	{
		long databaseBackupDate = 0;
		//��ȡָ��Key��SharedPreferences����
		SharedPreferences sP = GetContext().getSharedPreferences("DatabaseBackupDate",Context.MODE_PRIVATE);
		//����Ϊ��֤����������
		if (sP != null) {
			//����ͻ�ȡָ��Key������
			databaseBackupDate = sP.getLong("DatabaseBackupDate", 0);
		}
		
		return databaseBackupDate;
	}
}
