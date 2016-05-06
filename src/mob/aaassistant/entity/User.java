package mob.aaassistant.entity;

import java.util.Date;

public class User {
	//the primary key
	private int mId;
	//the name of user
	private String mName;
	//the state of the user data
	//1:ok
	//0:out
	private int mState=1;
	//the time of user create
	private Date mDate=new Date();
	
	public int getId() {
		return mId;
	}
	public void setId(int Id) {
		this.mId = Id;
	}
	public String getName() {
		return mName;
	}
	public void setName(String Name) {
		this.mName = Name;
	}
	public int getState() {
		return mState;
	}
	public void setState(int State) {
		this.mState = State;
	}
	public Date getDate() {
		return mDate;
	}
	public void setDate(Date Date) {
		this.mDate = Date;
	}

}
