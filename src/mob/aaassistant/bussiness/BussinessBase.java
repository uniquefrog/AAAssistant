package mob.aaassistant.bussiness;

import android.content.Context;

public class BussinessBase {

	private Context mContext;
	public BussinessBase(Context context){
		this.mContext=context;
	}
	protected Context GetContext() {
		return mContext;
	}
}
