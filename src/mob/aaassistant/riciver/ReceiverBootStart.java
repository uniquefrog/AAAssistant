package mob.aaassistant.riciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import mob.aaassistant.service.ServiceDatabaseBackup;

public class ReceiverBootStart extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context p_Context, Intent p_Intent) {
		Intent _Intent = new Intent(p_Context, ServiceDatabaseBackup.class);
		p_Context.startService(_Intent);
	}
}
