package mob.aaassistant.bussiness;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.content.ContentValues;
import android.content.Context;
import mob.aaassistant.database.sqlitedal.SQLiteUser;
import mob.aaassistant.entity.User;

public class BussinessUser extends BussinessBase{

	private SQLiteUser mSqLiteUser;
	public BussinessUser(Context context) {
		super(context);
		mSqLiteUser=new SQLiteUser(context);
	}

	public String insertUser(User user){
		boolean bResult=mSqLiteUser.insertUser(user);
		if (bResult) {
			return "添加数据成功";
		}
		return "添加数据失败";
	}
	public String deleteUserByID(int userId){
		String condition=" And UserID="+userId;
		boolean bResult=mSqLiteUser.deleteUser(condition);
		if (bResult) {
			return "删除数据成功";
		}
		return "删除数据失败";
		
	}
	public String updateUser(User user){
		String condition=" UserID="+user.getId();
		boolean bResult=mSqLiteUser.updateUser(user, condition);
		if (bResult) {
			return "修改数据成功";
		}
		return "修改数据失败";
		
	}
	private List<User> getUserList(String condition){
		return mSqLiteUser.getUser(condition);
	}
	public List<User> getUserHideList(){
		return getUserList(" And State=1 ");
	}
	private User getUser(int uerId){
		String condition=" And UserID="+uerId;
		List<User> list=mSqLiteUser.getUser(condition);
		if (list.size()==1) {
			return list.get(0);
		}
		return null;
		
	}
	
	private List<User> getUserList(int[] userIds){
		List<User> list=new ArrayList<User>();
		for (int i = 0; i < userIds.length; i++) {
			User user= getUser(userIds[i]);
			list.add(user);
		}
		
		
		return list;
		
		
	}
	public User GetModelUserByUserID(int pUserID) {
		List<User> _List = mSqLiteUser.getUser(" And UserID = " + pUserID);
		if (_List.size() == 1) {
			return _List.get(0);
		}
		else {
			return null;
		}
	}
	public List<User> GetUserListByUserID(String pUserID[]) {
		List<User> _List = new ArrayList<User>();
		for (int i = 0; i < pUserID.length; i++) {
			_List.add(GetModelUserByUserID(Integer.valueOf(pUserID[i])));
		}
		
		return _List;
	}
	public String GetUserNameByUserID(String p_UserID)
	{
		List<User> _List = GetUserListByUserID(p_UserID.split(","));
		String _Name = "";
		
		for(int i=0;i<_List.size();i++)
		{
			_Name += _List.get(i).getName() + ",";
		}
		return _Name;
	}
	public boolean IsExistUserByUserName(String pUserName,Integer pUserID)
	{
		String condition = " And UserName = '" + pUserName + "'";
		if(pUserID != null)
		{
			condition += " And UserID <> " + pUserID;
		}
		List list = mSqLiteUser.getUser(condition);
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean HideUserByUserId(int userId){
		String condition=" UserID="+userId;
		ContentValues values=new ContentValues();
		values.put("State", 0);
		return mSqLiteUser.updateUser(values, condition);
	}
	
	
}
