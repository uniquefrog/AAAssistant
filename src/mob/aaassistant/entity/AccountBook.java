package mob.aaassistant.entity;

import java.util.Date;

public class AccountBook {
	//�˱�������ID
	private int mAccountBookID;
	//�˱�����
	private String mAccountBookName;
	//�������
	private Date mCreateDate = new Date();
	//״̬ 0ʧЧ 1����
	private int mState = 1;
	//�Ƿ�Ĭ�˱� 0��1��
	private int mIsDefault;
	/**
	 * �˱�������ID
	 */
	public int getId() {
		return mAccountBookID;
	}
	/**
	 * �˱�������ID
	 */
	public void setId(int pAccountBookID) {
		this.mAccountBookID = pAccountBookID;
	}
	/**
	 * �˱�����
	 */
	public String getName() {
		return mAccountBookName;
	}
	/**
	 * �˱�����
	 */
	public void setName(String pAccountBookName) {
		this.mAccountBookName = pAccountBookName;
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
	public void setDate(Date pCreateDate) {
		this.mCreateDate = pCreateDate;
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
	public void setState(int pState) {
		this.mState = pState;
	}
	/**
	 * �Ƿ�Ĭ�˱� 0��1��
	 */
	public int getIsDefault() {
		return mIsDefault;
	}
	/**
	 * �Ƿ�Ĭ�˱� 0��1��
	 */
	public void setIsDefault(int pIsDefault) {
		this.mIsDefault = pIsDefault;
	}
}
