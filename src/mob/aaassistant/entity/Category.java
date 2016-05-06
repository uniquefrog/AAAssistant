package mob.aaassistant.entity;

import java.io.Serializable;
import java.util.Date;

public class Category implements Serializable {
	//类别表主键ID
	private int mCategoryID;
	//类别名称
	private String mCategoryName;
	//类型标记名称
	private String mTypeFlag;
	//父类型ID
	private int mParentID = 0;
	//路径
	private String mPath;	
	//添加日期
	private Date mCreateDate = new Date();
	//状态 0失效 1启用
	private int mState = 1;
	/**
	 * 账本表主键ID
	 */
	public int getId() {
		return mCategoryID;
	}
	/**
	 * 账本表主键ID
	 */
	public void setId(int categoryID) {
		this.mCategoryID = categoryID;
	}
	/**
	 * 账本名称
	 */
	public String getName() {
		return mCategoryName;
	}
	/**
	 * 账本名称
	 */
	public void setName(String categoryName) {
		this.mCategoryName = categoryName;
	}
	/**
	 * 类型标记名称
	 */
	public String getTypeFlag() {
		return mTypeFlag;
	}
	/**
	 * 类型标记名称
	 */
	public void setTypeFlag(String typeFlag) {
		this.mTypeFlag = typeFlag;
	}
	/**
	 * 父类型ID
	 */
	public int getParentID() {
		return mParentID;
	}
	/**
	 * 父类型ID
	 */
	public void setParentID(int parentID) {
		this.mParentID = parentID;
	}
	/**
	 * 路径
	 */
	public String getPath() {
		return mPath;
	}
	/**
	 * 路径
	 */
	public void setPath(String path) {
		this.mPath = path;
	}
	/**
	 * 添加日期
	 */
	public Date getDate() {
		return mCreateDate;
	}
	/**
	 * 添加日期
	 */
	public void setDate(Date createDate) {
		this.mCreateDate = createDate;
	}
	/**
	 * 状态 0失效 1启用
	 */
	public int getState() {
		return mState;
	}
	/**
	 * 状态 0失效 1启用
	 */
	public void setState(int state) {
		this.mState = state;
	}
	
	@Override
	public String toString() {
		return mCategoryName;
	}
}
