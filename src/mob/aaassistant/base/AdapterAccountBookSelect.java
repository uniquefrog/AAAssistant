package mob.aaassistant.base;

import java.util.List;

import mob.aaassistant.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import mob.aaassistant.bussiness.BussinessAccountBook;
import mob.aaassistant.entity.AccountBook;

public class AdapterAccountBookSelect extends AdapterBase {

	private class Holder
	{
		ImageView Icon;
		TextView Name;
	}
	
	public AdapterAccountBookSelect(Context p_Context)
	{
		this(p_Context,null);
		BussinessAccountBook _BusinessAccountBook = new BussinessAccountBook(p_Context);
		List list = _BusinessAccountBook.getAccountBook("");
		SetList(list);
	}
	
	public AdapterAccountBookSelect(Context p_Context, List p_List) {
		super(p_Context, p_List);
	}

	@Override
	public View getView(int p_Position, View p_ConvertView, ViewGroup p_Parent) {
		Holder holder;
		
		if (p_ConvertView == null) {
			p_ConvertView = GetLayoutInflater().inflate(R.layout.account_book_select_list_item, null);
			holder = new Holder();
			holder.Icon = (ImageView)p_ConvertView.findViewById(R.id.AccountBookIcon);
			holder.Name = (TextView)p_ConvertView.findViewById(R.id.AccountBookName);
			p_ConvertView.setTag(holder);
		}
		else {
			holder = (Holder) p_ConvertView.getTag();
		}
		
		AccountBook accountBook = (AccountBook)getItem(p_Position);
		holder.Icon.setImageResource(R.drawable.account_book_small_icon);
		holder.Name.setText(accountBook.getName());
		
		return p_ConvertView;
	}
	
}
