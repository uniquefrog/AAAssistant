package mob.aaassistant.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import android.R.integer;

public class Payout implements Serializable {
	//支出表主键ID
	private int m_PayoutID;
	//账本ID外键
	private int m_AccountBookID;
	//账本名称
	private String m_AccountBookName;
	//支出类别ID外键
	private int m_CategoryID;
	//类别名称
	private String m_CategoryName;
	//路径
	private String m_Path;
	//付款方式ID外键
	private int m_PayWayID;
	//消费地点ID外键
	private int m_PlaceID;
	//消费金额
	private BigDecimal m_Amount;
	//消费日期
	private Date m_PayoutDate;
	//计算方式
	private String m_PayoutType;
	//消费人ID外键
	private String m_PayoutUserID;
	//备注
	private String m_Comment;
	//添加日期
	private Date m_CreateDate = new Date();
	//状态 0失效 1启用
	private int m_State = 1;
	/**
	 * 支出表主键ID
	 */
	public int getPayoutID() {
		return m_PayoutID;
	}
	/**
	 * 支出表主键ID
	 */
	public void setPayoutID(int payoutID) {
		this.m_PayoutID = payoutID;
	}
	/**
	 * 账本名称ID外键
	 */
	public int getAccountBookID() {
		return m_AccountBookID;
	}
	/**
	 * 账本ID外键
	 */
	public void setAccountBookID(int accountBookID) {
		this.m_AccountBookID = accountBookID;
	}
	/**
	 * 账本名称
	 */
	public String getAccountBookName() {
		return m_AccountBookName;
	}
	/**
	 * 账本名称
	 */
	public void setAccountBookName(String accountBookName) {
		this.m_AccountBookName = accountBookName;
	}
	/**
	 * 支出类别ID外键
	 */
	public int getCategoryID() {
		return m_CategoryID;
	}
	/**
	 * 支出类别ID外键
	 */
	public void setCategoryID(int categoryID) {
		this.m_CategoryID = categoryID;
	}
	/**
	 * 路径
	 */
	public String getPath() {
		return m_Path;
	}
	/**
	 * 路径
	 */
	public void setPath(String path) {
		this.m_Path = path;
	}
	/**
	 * 账本名称
	 */
	public String getCategoryName() {
		return m_CategoryName;
	}
	/**
	 * 账本名称
	 */
	public void setCategoryName(String categoryName) {
		this.m_CategoryName = categoryName;
	}
	/**
	 * 	付款方式ID外键
	 */
	public int getPayWayID() {
		return m_PayWayID;
	}
	/**
	 * 	付款方式ID外键
	 */
	public void setPayWayID(int payWayID) {
		this.m_PayWayID = payWayID;
	}
	/**
	 * 消费地点ID外键
	 */
	public int getPlaceID() {
		return m_PlaceID;
	}
	/**
	 * 消费地点ID外键
	 */
	public void setPlaceID(int placeID) {
		this.m_PlaceID = placeID;
	}
	/**
	 * 消费金额
	 */
	public BigDecimal getAmount() {
		return m_Amount;
	}
	/**
	 * 消费金额
	 */
	public void setAmount(BigDecimal amount) {
		this.m_Amount = amount;
	}
	/**
	 * 消费日期
	 */
	public Date getPayoutDate() {
		return m_PayoutDate;
	}
	/**
	 * 消费日期
	 */
	public void setPayoutDate(Date payoutDate) {
		this.m_PayoutDate = payoutDate;
	}
	/**
	 * 计算方式
	 */
	public String getPayoutType() {
		return m_PayoutType;
	}
	/**
	 * 计算方式
	 */
	public void setPayoutType(String payoutType) {
		this.m_PayoutType = payoutType;
	}
	/**
	 * 消费人ID外键
	 */
	public String getPayoutUserID() {
		return m_PayoutUserID;
	}
	/**
	 * 消费人ID外键
	 */
	public void setPayoutUserID(String payoutUserID) {
		this.m_PayoutUserID = payoutUserID;
	}	
	/**
	 * 备注
	 */
	public String getComment() {
		return m_Comment;
	}
	/**
	 * 备注
	 */
	public void setComment(String comment) {
		this.m_Comment = comment;
	}
	/**
	 * 添加日期
	 */
	public Date getCreateDate() {
		return m_CreateDate;
	}
	/**
	 * 添加日期
	 */
	public void setCreateDate(Date createDate) {
		this.m_CreateDate = createDate;
	}
	/**
	 * 状态 0失效 1启用
	 */
	public int getState() {
		return m_State;
	}
	/**
	 * 状态 0失效 1启用
	 */
	public void setState(int state) {
		this.m_State = state;
	}
}
