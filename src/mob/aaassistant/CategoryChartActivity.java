package mob.aaassistant;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import mob.aaassistant.base.ActivityFrame;
import mob.aaassistant.entity.CategoryTotal;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import mob.aaassistant.R;

public class CategoryChartActivity extends ActivityFrame {
	private List<CategoryTotal> mModelCategoryTotal;
	private LinearLayout layBody;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_chart_layout);
		mModelCategoryTotal=(List<CategoryTotal>) getIntent().getSerializableExtra("Total");
		if (mModelCategoryTotal!=null) {
			if (mModelCategoryTotal.size()>0) {
				layBody=(LinearLayout) this.findViewById(R.id.layBody);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
				layBody.addView(CategoryStatistics(),layoutParams);

			}
			else{
				ShowMsg("请先添加消费记录");
			}
			Log.i("uniquefrog", "饼形图数据源不为空，个数为"+mModelCategoryTotal.size());
		}
		else{
			ShowMsg("请先添加消费记录");
			Log.i("uniquefrog", "饼形图数据源为空");
		}

		//		RemoveBottomBox();
	}
	private View CategoryStatistics() {
		int[] _Color = new int[] { Color.parseColor("#FF5552"), Color.parseColor("#2A94F1"), Color.parseColor("#F12792"), Color.parseColor("#FFFF52"), Color.parseColor("#84D911"),Color.parseColor("#5255FF") };
		DefaultRenderer _DefaultRenderer = BuildCategoryRenderer(_Color);
		CategorySeries _CategorySeries = BuildCategoryDataset("消费类别统计", mModelCategoryTotal);
		View _PieView = ChartFactory.getPieChartView(CategoryChartActivity.this, _CategorySeries, _DefaultRenderer);
		return _PieView;
	}
	protected DefaultRenderer BuildCategoryRenderer(int[] colors) {
		DefaultRenderer _Renderer = new DefaultRenderer();
		_Renderer.setZoomButtonsVisible(true);
		_Renderer.setLabelsTextSize(15);
		_Renderer.setLegendTextSize(15);
		_Renderer.setLabelsColor(Color.BLUE);
		_Renderer.setMargins(new int[] { 20, 30, 15, 10 });
		int _Color = 0;
		for (int i = 0;i<mModelCategoryTotal.size();i++) {
			SimpleSeriesRenderer _R = new SimpleSeriesRenderer();
			_R.setColor(colors[_Color]);
			_Renderer.addSeriesRenderer(_R);
			_Color++;
			if (_Color > colors.length) {
				_Color = 0;
			}
		}
		return _Renderer;
	}

	protected CategorySeries BuildCategoryDataset(String title, List<CategoryTotal> values) {
		CategorySeries series = new CategorySeries(title);
		for (CategoryTotal value : values) {
			series.add("数量： " + value.Count, Double.parseDouble(value.Count));
		}
		return series;
	}

}
