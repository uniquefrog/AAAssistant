package mob.aaassistant.entity;

import java.io.Serializable;
import java.util.Date;

public class Category implements Serializable {
	//��������ID
	private int mCategoryID;
	//�������
	private String mCategoryName;
	//���ͱ������
	private String mTypeFlag;
	//������ID
	private int mParentID = 0;
	//·��
	private String mPath;	
	//�������
	private Date mCreateDate = new Date();
	//״̬ 0ʧЧ 1����
	private int mState = 1;
	/**
	 * �˱�������ID
	 */
	public int getId() {
		return mCategoryID;
	}
	/**
	 * �˱�������ID
	 */
	public void setId(int categoryID) {
		this.mCategoryID = categoryID;
	}
	/**
	 * �˱�����
	 */
	public String getName() {
		return mCategoryName;
	}
	/**
	 * �˱�����
	 */
	public void setName(String categoryName) {
		this.mCategoryName = categoryName;
	}
	/**
	 * ���ͱ������
	 */
	public String getTypeFlag() {
		return mTypeFlag;
	}
	/**
	 * ���ͱ������
	 */
	public void setTypeFlag(String typeFlag) {
		this.mTypeFlag = typeFlag;
	}
	/**
	 * ������ID
	 */
	public int getParentID() {
		return mParentID;
	}
	/**
	 * ������ID
	 */
	public void setParentID(int parentID) {
		this.mParentID = parentID;
	}
	/**
	 * ·��
	 */
	public String getPath() {
		return mPath;
	}
	/**
	 * ·��
	 */
	public void setPath(String path) {
		this.mPath = path;
	}
	/**
	 * �������
	 */
	public Date getDate() {
		return mCreateDate;
	}
	/**
	 * �������
	 */
	public void setDate(Date createDate) {
		this.mCreateDate = createDate;
	}
	/**
	 * ״̬ 0ʧЧ 1����
	 */
	public int getState() {
		return mState;
	}
	/**
	 * ״̬ 0ʧЧ 1����
	 */
	public void setState(int state) {
		this.mState = state;
	}
	
	@Override
	public String toString() {
		return mCategoryName;
	}
}
