package mob.aaassistant.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import android.R.integer;

public class Payout implements Serializable {
	//֧��������ID
	private int m_PayoutID;
	//�˱�ID���
	private int m_AccountBookID;
	//�˱�����
	private String m_AccountBookName;
	//֧�����ID���
	private int m_CategoryID;
	//�������
	private String m_CategoryName;
	//·��
	private String m_Path;
	//���ʽID���
	private int m_PayWayID;
	//���ѵص�ID���
	private int m_PlaceID;
	//���ѽ��
	private BigDecimal m_Amount;
	//��������
	private Date m_PayoutDate;
	//���㷽ʽ
	private String m_PayoutType;
	//������ID���
	private String m_PayoutUserID;
	//��ע
	private String m_Comment;
	//�������
	private Date m_CreateDate = new Date();
	//״̬ 0ʧЧ 1����
	private int m_State = 1;
	/**
	 * ֧��������ID
	 */
	public int getPayoutID() {
		return m_PayoutID;
	}
	/**
	 * ֧��������ID
	 */
	public void setPayoutID(int payoutID) {
		this.m_PayoutID = payoutID;
	}
	/**
	 * �˱�����ID���
	 */
	public int getAccountBookID() {
		return m_AccountBookID;
	}
	/**
	 * �˱�ID���
	 */
	public void setAccountBookID(int accountBookID) {
		this.m_AccountBookID = accountBookID;
	}
	/**
	 * �˱�����
	 */
	public String getAccountBookName() {
		return m_AccountBookName;
	}
	/**
	 * �˱�����
	 */
	public void setAccountBookName(String accountBookName) {
		this.m_AccountBookName = accountBookName;
	}
	/**
	 * ֧�����ID���
	 */
	public int getCategoryID() {
		return m_CategoryID;
	}
	/**
	 * ֧�����ID���
	 */
	public void setCategoryID(int categoryID) {
		this.m_CategoryID = categoryID;
	}
	/**
	 * ·��
	 */
	public String getPath() {
		return m_Path;
	}
	/**
	 * ·��
	 */
	public void setPath(String path) {
		this.m_Path = path;
	}
	/**
	 * �˱�����
	 */
	public String getCategoryName() {
		return m_CategoryName;
	}
	/**
	 * �˱�����
	 */
	public void setCategoryName(String categoryName) {
		this.m_CategoryName = categoryName;
	}
	/**
	 * 	���ʽID���
	 */
	public int getPayWayID() {
		return m_PayWayID;
	}
	/**
	 * 	���ʽID���
	 */
	public void setPayWayID(int payWayID) {
		this.m_PayWayID = payWayID;
	}
	/**
	 * ���ѵص�ID���
	 */
	public int getPlaceID() {
		return m_PlaceID;
	}
	/**
	 * ���ѵص�ID���
	 */
	public void setPlaceID(int placeID) {
		this.m_PlaceID = placeID;
	}
	/**
	 * ���ѽ��
	 */
	public BigDecimal getAmount() {
		return m_Amount;
	}
	/**
	 * ���ѽ��
	 */
	public void setAmount(BigDecimal amount) {
		this.m_Amount = amount;
	}
	/**
	 * ��������
	 */
	public Date getPayoutDate() {
		return m_PayoutDate;
	}
	/**
	 * ��������
	 */
	public void setPayoutDate(Date payoutDate) {
		this.m_PayoutDate = payoutDate;
	}
	/**
	 * ���㷽ʽ
	 */
	public String getPayoutType() {
		return m_PayoutType;
	}
	/**
	 * ���㷽ʽ
	 */
	public void setPayoutType(String payoutType) {
		this.m_PayoutType = payoutType;
	}
	/**
	 * ������ID���
	 */
	public String getPayoutUserID() {
		return m_PayoutUserID;
	}
	/**
	 * ������ID���
	 */
	public void setPayoutUserID(String payoutUserID) {
		this.m_PayoutUserID = payoutUserID;
	}	
	/**
	 * ��ע
	 */
	public String getComment() {
		return m_Comment;
	}
	/**
	 * ��ע
	 */
	public void setComment(String comment) {
		this.m_Comment = comment;
	}
	/**
	 * �������
	 */
	public Date getCreateDate() {
		return m_CreateDate;
	}
	/**
	 * �������
	 */
	public void setCreateDate(Date createDate) {
		this.m_CreateDate = createDate;
	}
	/**
	 * ״̬ 0ʧЧ 1����
	 */
	public int getState() {
		return m_State;
	}
	/**
	 * ״̬ 0ʧЧ 1����
	 */
	public void setState(int state) {
		this.m_State = state;
	}
}
