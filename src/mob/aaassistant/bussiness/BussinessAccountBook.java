package mob.aaassistant.bussiness;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import mob.aaassistant.database.sqlitedal.SQLiteAccountBook;
import mob.aaassistant.entity.AccountBook;

public class BussinessAccountBook extends BussinessBase{

	private SQLiteAccountBook mSqLiteAccountBook;
	public BussinessAccountBook(Context context) {
		super(context);
		mSqLiteAccountBook=new SQLiteAccountBook(context);
	}

	public boolean insertAccountBook(AccountBook accountBook){
		mSqLiteAccountBook.BeginTransaction();
		boolean bResult1=mSqLiteAccountBook.insertAccountBook(accountBook);
		boolean bResult2=true;
		try {
			if (accountBook.getIsDefault()==1 && bResult1) {
				bResult2=setIsDefault(accountBook.getId());
			}
			if (bResult1 && bResult2 ) {
				mSqLiteAccountBook.setTransactionSuccessful();
				return true;
			}
			else{
				return false;
			}
		} catch (Exception e) {
			return false;		
		}
		finally {
			mSqLiteAccountBook.endTransaction();
		}
	}
	private boolean setIsDefault(int accountBookId) {
		String condition1=" IsDefault = 1";
		ContentValues values1=new ContentValues();
		values1.put("IsDefault", 0);
		boolean result1=mSqLiteAccountBook.updateAccountBook(values1, condition1);
		String condition2="AccountBookId =" +accountBookId;
		ContentValues values2=new ContentValues();
		values2.put("IsDefault", 1);
		boolean result2=mSqLiteAccountBook.updateAccountBook(values2, condition2);
		if (result1 && result2) {
			return true;
			
		}
		else{
			return false;
		}
	}

	public String deleteAccountBookByID(int accountBookId){
		String condition=" And AccountBookID="+accountBookId;
		boolean bResult=mSqLiteAccountBook.deleteAccountBook(condition);
		if (bResult) {
			return "删除数据成功";
		}
		return "删除数据失败";

	}
	public String updateAccountBook(AccountBook accountBook){
		String condition=" AccountBookID="+accountBook.getId();
//		if (bResult) {
//			return "修改数据成功";
//		}
//		return "修改数据失败";
		mSqLiteAccountBook.BeginTransaction();
		boolean bResult1=mSqLiteAccountBook.updateAccountBook(accountBook, condition);
		boolean bResult2=true;
		try {
			if (accountBook.getIsDefault()==1 && bResult1) {
				bResult2=setIsDefault(accountBook.getId());
			}
			if (bResult1 && bResult2 ) {
				mSqLiteAccountBook.setTransactionSuccessful();
				return "修改数据成功";
			}
			else{
				return "修改数据失败";
			}
		} catch (Exception e) {
			return "修改数据异常";		
		}
		finally {
			mSqLiteAccountBook.endTransaction();
		}
	}
	private List<AccountBook> getAccountBookList(String condition){
		return mSqLiteAccountBook.getAccountBook(condition);
	}
	public List<AccountBook> getAccountBookHideList(){
		return getAccountBookList(" And State=1 ");
	}
	public List<AccountBook> getAccountBook(String condition) {
		return mSqLiteAccountBook.getAccountBook(condition);
	}
	private AccountBook getAccountBook(int accountBook){
		String condition=" And AccountBookID="+accountBook;
		List<AccountBook> list=mSqLiteAccountBook.getAccountBook(condition);
		if (list.size()==1) {
			return list.get(0);
		}
		return null;

	}

	private List<AccountBook> getAccountBookList(int[] accountBookIds){
		List<AccountBook> list=new ArrayList<AccountBook>();
		for (int i = 0; i < accountBookIds.length; i++) {
			AccountBook accountBook= getAccountBook(accountBookIds[i]);
			list.add(accountBook);
		}


		return list;


	}
	public AccountBook GetDefaultModelAccountBook() {
		List _List = mSqLiteAccountBook
				.getAccountBook(" And IsDefault = 1");
		if (_List.size() == 1) {
			return (AccountBook) _List.get(0);
		} else {
			return null;
		}
	}
	public boolean IsExistAccountBookByAccountBookName(String pAccountBookName,Integer pAccountBookID)
	{
		String condition = " And AccountBookName = '" + pAccountBookName + "'";
		if(pAccountBookID != null)
		{
			condition += " And AccountBookID <> " + pAccountBookID;
		}
		List list = mSqLiteAccountBook.getAccountBook(condition);
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean HideAccountBookByAccountBookId(int AccountBookId){
		String condition=" AccountBookID="+AccountBookId;
		ContentValues values=new ContentValues();
		values.put("State", 0);
		return mSqLiteAccountBook.updateAccountBook(values, condition);
	}


}
