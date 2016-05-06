package mob.aaassistant.base;

import java.util.ArrayList;

import mob.aaassistant.R;

import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import mob.aaassistant.control.SlideMenuItem;
import mob.aaassistant.control.SlideMenuView;
import mob.aaassistant.control.SlideMenuView.OnSlideMenuListner;

public class ActivityFrame extends ActivityBase  {
	SlideMenuView slideMenuView;
	private ProgressDialog m_ProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//no title for the activity
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
	}
	public void CreateSlideMenu(ArrayList<SlideMenuItem> dataList){
		slideMenuView=new SlideMenuView(ActivityFrame.this, dataList);
	}
	public LayoutInflater getLayoutInflater(Context context){
		
		return LayoutInflater.from(context);
	}
	public void SlideToggle(){
		slideMenuView.Toggle();
	}
//	protected void RemoveBottomBox()
//	{
//		slideMenuView = new SlideMenuView(ActivityFrame.this, dataList);
//		slideMenuView.RemoveBottomBox();
//	}
	public void CreatMenu(Menu menu){
		int[] item=new int[]{1,2};
		int group=0;
		for (int i = 0; i < item.length; i++) {
			switch (item[i]) {
			case 1:
				menu.add(group, item[i], 0, "ÐÞ¸Ä");
				break;
			case 2:
				menu.add(group, item[i], 0, "É¾³ý");
				break;
			default:
				
				break;
			}
		}
	}
	protected void ShowProgressDialog(String p_Title,String p_Message) {
		m_ProgressDialog = new ProgressDialog(this);
		m_ProgressDialog.setTitle(p_Title);
		m_ProgressDialog.setMessage(p_Message);
		m_ProgressDialog.show();
	}
	protected void DismissProgressDialog() {
		if(m_ProgressDialog != null)
		{
			m_ProgressDialog.dismiss();
		}
	}
	

}
