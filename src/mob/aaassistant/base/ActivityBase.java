package mob.aaassistant.base;

import java.lang.reflect.Field;
import java.security.PublicKey;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
/**
 * some commen method for activity
 * @author Administrator
 *
 */
public class ActivityBase extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	//show the tip in the context in 2500ms
	public void ShowMsg(String msg){
		Toast.makeText(getBaseContext(), msg, 2500).show();
	}
	
	public void OpenActivity(Class target){
		Intent intent=new Intent(getBaseContext(),target);
		startActivity(intent);
	}
	//close the warning window
	public void setAlertDialogClose(DialogInterface dialog,boolean isClose){
		try {
			Field field=dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, isClose);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
