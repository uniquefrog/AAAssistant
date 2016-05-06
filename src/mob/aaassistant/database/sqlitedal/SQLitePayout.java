package mob.aaassistant.database.sqlitedal;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import mob.aaassistant.database.base.SQLiteDalBase;
import mob.aaassistant.entity.Payout;
import mob.aaassistant.utils.DateTools;

public class SQLitePayout extends  SQLiteDalBase{
	public SQLitePayout(Context p_Context) {
		super(p_Context);
	}

	public Boolean insertPayout(Payout payout) {
		ContentValues _ContentValues = CreateParms(payout);
		Long successTag = getDataBase().insert("Payout", null, _ContentValues);
		payout.setPayoutID(successTag.intValue());
		return successTag > 0;
	}

	public Boolean DeletePayout(String p_Condition)
	{
		return deleteSql(GetTableNameAndPK()[0], p_Condition);
	}
	
	public Boolean UpdatePayout(String p_Condition, Payout p_Info)
	{
		ContentValues _ContentValues = CreateParms(p_Info);
		return getDataBase().update("Payout", _ContentValues, p_Condition, null) > 0;
	}
	
	public Boolean UpdatePayout(String p_Condition,ContentValues p_ContentValues)
	{
		return getDataBase().update("Payout", p_ContentValues, p_Condition, null) > 0;
	}
	
	public List<Payout> GetPayout(String p_Condition)
	{
		String _SqlText = "Select * From v_Payout Where  1=1 " + p_Condition;
		Log.i("uniquefrog", _SqlText);
		return GetList(_SqlText);
	}
	
	protected Payout FindModel(Cursor p_Cursor)
	{
		Payout _ModelPayout = new Payout();
		_ModelPayout.setPayoutID(p_Cursor.getInt(p_Cursor.getColumnIndex("PayoutID")));
		_ModelPayout.setAccountBookID(p_Cursor.getInt((p_Cursor.getColumnIndex("AccountBookID"))));
		_ModelPayout.setAccountBookName((p_Cursor.getString(p_Cursor.getColumnIndex("AccountBookName"))));
		_ModelPayout.setCategoryID(p_Cursor.getInt((p_Cursor.getColumnIndex("CategoryID"))));
		_ModelPayout.setCategoryName((p_Cursor.getString(p_Cursor.getColumnIndex("CategoryName"))));
		_ModelPayout.setPath((p_Cursor.getString(p_Cursor.getColumnIndex("Path"))));
		_ModelPayout.setPayWayID(p_Cursor.getInt((p_Cursor.getColumnIndex("PayWayID"))));
		_ModelPayout.setPlaceID(p_Cursor.getInt((p_Cursor.getColumnIndex("PlaceID"))));
		_ModelPayout.setAmount(new BigDecimal(p_Cursor.getString(((p_Cursor.getColumnIndex("Amount"))))));
		Date _PayoutDate = DateTools.getDate(p_Cursor.getString(p_Cursor.getColumnIndex("PayoutDate")), "yyyy-MM-dd");	
		_ModelPayout.setPayoutDate(_PayoutDate);
		_ModelPayout.setPayoutType((p_Cursor.getString(p_Cursor.getColumnIndex("PayoutType"))));
		_ModelPayout.setPayoutUserID((p_Cursor.getString(p_Cursor.getColumnIndex("PayoutUserID"))));
		_ModelPayout.setComment((p_Cursor.getString(p_Cursor.getColumnIndex("Comment"))));
		Date _CreateDate = DateTools.getDate(p_Cursor.getString(p_Cursor.getColumnIndex("CreateDate")), "yyyy-MM-dd HH:mm:ss");	
		_ModelPayout.setCreateDate(_CreateDate);
		_ModelPayout.setState((p_Cursor.getInt(p_Cursor.getColumnIndex("State"))));
		
		return _ModelPayout;
	}

	@Override
	public void onCreate(SQLiteDatabase p_DataBase) {
		StringBuilder s_CreateTableScript = new StringBuilder();
		
		s_CreateTableScript.append("		Create  TABLE Payout(");
		s_CreateTableScript.append("				[PayoutID] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		s_CreateTableScript.append("				,[AccountBookID] integer NOT NULL");
		s_CreateTableScript.append("				,[CategoryID] integer NOT NULL");
		s_CreateTableScript.append("				,[PayWayID] integer");
		s_CreateTableScript.append("				,[PlaceID] integer");
		s_CreateTableScript.append("				,[Amount] decimal NOT NULL");
		s_CreateTableScript.append("				,[PayoutDate] datetime NOT NULL");
		s_CreateTableScript.append("				,[PayoutType] varchar(20) NOT NULL");
		s_CreateTableScript.append("				,[PayoutUserID] text NOT NULL");
		s_CreateTableScript.append("				,[Comment] text");
		s_CreateTableScript.append("				,[CreateDate] datetime NOT NULL");
		s_CreateTableScript.append("				,[State] integer NOT NULL");
		s_CreateTableScript.append("				)");
		
		p_DataBase.execSQL(s_CreateTableScript.toString());
	}

	@Override
	public void onUpdate(SQLiteDatabase p_DataBase) {
		
	}

	@Override
	protected String[] GetTableNameAndPK() {
		return new String[]{"Payout","PayoutID"};
	}

	public ContentValues CreateParms(Payout p_Info) {
		ContentValues _ContentValues = new ContentValues();
		_ContentValues.put("AccountBookID",p_Info.getAccountBookID());
		_ContentValues.put("CategoryID",p_Info.getCategoryID());
		_ContentValues.put("PayWayID",p_Info.getPayWayID());
		_ContentValues.put("PlaceID",p_Info.getPlaceID());
		_ContentValues.put("Amount",p_Info.getAmount().toString());
		_ContentValues.put("PayoutDate",DateTools.getFormatDateTime(p_Info.getPayoutDate(),"yyyy-MM-dd"));
		_ContentValues.put("PayoutType",p_Info.getPayoutType());
		_ContentValues.put("PayoutUserID",p_Info.getPayoutUserID());
		_ContentValues.put("Comment",p_Info.getComment());
		_ContentValues.put("CreateDate",DateTools.getFormatDateTime(p_Info.getCreateDate(),"yyyy-MM-dd HH:mm:ss"));
		_ContentValues.put("State",p_Info.getState());
		
		return _ContentValues;
	}

}
