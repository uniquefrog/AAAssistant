package mob.aaassistant.bussiness;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import mob.aaassistant.R;

import android.content.Context;
//import jxl.*;   
//import jxl.format.Alignment;   
//import jxl.format.Border;   
//import jxl.format.BorderLineStyle;   
//import jxl.format.CellFormat;   
//import jxl.write.Boolean;   
//import jxl.write.Label;   
//import jxl.write.Number;   
//import jxl.write.NumberFormat;
//import jxl.write.WritableCellFormat;   
//import jxl.write.WritableFont;   
//import jxl.write.WritableSheet;   
//import jxl.write.WritableWorkbook;   
import mob.aaassistant.entity.Payout;

public class BusinessStatistics extends BussinessBase {
	
	private BusinessPayout mBusinessPayout;
	private BussinessUser mBusinessUser;
	private BussinessAccountBook mBusinessAccountBook;
	
	public BusinessStatistics(Context p_Context)
	{
		super(p_Context);
		mBusinessPayout = new BusinessPayout(p_Context);
		mBusinessUser = new BussinessUser(p_Context);
		mBusinessAccountBook = new BussinessAccountBook(p_Context);
	}
	
	public String GetPayoutUserIDByAccountBookID(int p_AccountBookID)
	{
		String result = "";
		List<ModelStatistics> listModelStatisticsTotal =  GetPayoutUserID(" And AccountBookID = " + p_AccountBookID);
		
		//将得到的信息进行转换，以方便观看
		for (int i = 0; i < listModelStatisticsTotal.size(); i++) {
			ModelStatistics modelStatistics = listModelStatisticsTotal.get(i);
			if(modelStatistics.getPayoutType().equals("个人")) {
				result += modelStatistics.PayerUserID + "个人消费 " + modelStatistics.Cost.toString() + "元\r\n";
			} else if(modelStatistics.getPayoutType().equals("均分")) {
				if(modelStatistics.PayerUserID.equals(modelStatistics.ConsumerUserID)) {
					result += modelStatistics.PayerUserID + "个人消费 " + modelStatistics.Cost.toString() + "元\r\n";
				} else {
					result += modelStatistics.ConsumerUserID + "应支付给" + modelStatistics.PayerUserID + modelStatistics.Cost + "元\r\n";
				}
			}
		}
		
		return result;
	}
	
	public List<ModelStatistics> GetPayoutUserID(String p_Condition)
	{
		//得到拆分好的统计信息
		List<ModelStatistics> listModelStatistics = GetModelStatisticsList(p_Condition);
		//存放按付款人分类的临时统计信息
		List<ModelStatistics> listModelStatisticsTemp = new ArrayList<ModelStatistics>();
		//存放统计好的汇总
		List<ModelStatistics> listModelStatisticsTotal = new ArrayList<ModelStatistics>();
		String result = "";
		//遍历拆分好的统计信息
		for (int i = 0; i < listModelStatistics.size(); i++) {
			//得到拆分好的一条信息
			ModelStatistics modelStatistics = listModelStatistics.get(i);
			result += modelStatistics.PayerUserID + "#" + modelStatistics.ConsumerUserID + "#" + modelStatistics.Cost + "\r\n";
			//保存当前的付款人ID
			String currentPayerUserID = modelStatistics.PayerUserID;
			
			//把当前信息按付款人分类的临时数组
			ModelStatistics modelStatisticsTemp = new ModelStatistics();
			modelStatisticsTemp.PayerUserID = modelStatistics.PayerUserID;
			modelStatisticsTemp.ConsumerUserID = modelStatistics.ConsumerUserID;
			modelStatisticsTemp.Cost = modelStatistics.Cost;
			modelStatisticsTemp.setPayoutType(modelStatistics.getPayoutType());
			listModelStatisticsTemp.add(modelStatisticsTemp);
			
			//计算下一行的索引
			int nextIndex;
			//如果下一行索引小于统计信息索引，则可以加1
			if((i+1) < listModelStatistics.size())
			{
				nextIndex = i+1;
			}
			else {
				//否则证明已经到尾，则索引赋值为当前行
				nextIndex = i;
			}
			
			//如果当前付款人与下一位付款人不同，则证明分类统计已经到尾，或者已经循环到统计数组最后一位，则就开始进入进行统计
			if (!currentPayerUserID.equals(listModelStatistics.get(nextIndex).PayerUserID) || nextIndex == i) {
				
				//开始进行遍历进行当前分类统计数组的统计
				for (int j = 0; j < listModelStatisticsTemp.size(); j++) {
					//取出来一个
					ModelStatistics modelStatisticsTotal = listModelStatisticsTemp.get(j);
					//判断在总统计数组当中是否已经存在该付款人和消费人的信息
					int index = GetPostionByConsumerUserID(listModelStatisticsTotal,modelStatisticsTotal.PayerUserID, modelStatisticsTotal.ConsumerUserID);
					//如果已经存在，则开始在原来的数据上进行累加
					if(index != -1)
					{
						listModelStatisticsTotal.get(index).Cost = listModelStatisticsTotal.get(index).Cost.add(modelStatisticsTotal.Cost);
					}
					else {
						//否则就是一条新信息，添加到统计数组当中
						listModelStatisticsTotal.add(modelStatisticsTotal);
					}
				}
				//全部遍历完后清空当前分类统计数组，进入下一个分类统计数组的计算，直到尾
				listModelStatisticsTemp.clear();
			}
			
		}
		
		return listModelStatisticsTotal;
	}
	
	private int GetPostionByConsumerUserID(List<ModelStatistics> p_ListModelStatisticsTotal,String p_PayerUserID,String p_ConsumerUserID)
	{
		int index = -1;
		for (int i = 0; i < p_ListModelStatisticsTotal.size(); i++) {
			if (p_ListModelStatisticsTotal.get(i).PayerUserID.equals(p_PayerUserID) && p_ListModelStatisticsTotal.get(i).ConsumerUserID.equals(p_ConsumerUserID)) {
				index = i;
			}
		}
		
		return index;
	}

	private List<ModelStatistics> GetModelStatisticsList(String p_Condition) {
		//按支付人ID排序取出消费记录
		List<Payout> listPayout = mBusinessPayout.GetPayoutOrderByPayoutUserID(p_Condition);
		
		//获取计算方式数组
		String payoutTypeArr[] = GetContext().getResources().getStringArray(R.array.PayoutType);
		
		List<ModelStatistics> listModelStatistics = new ArrayList<ModelStatistics>();
		
		if(listPayout != null)
		{
			//遍历消费记录列表
			for (int i = 0; i < listPayout.size(); i++) {
				//取出一条消费记录
				Payout payout = listPayout.get(i);
				//将消费人ID转换为真实名称
				String payoutUserName[] = mBusinessUser.GetUserNameByUserID(payout.getPayoutUserID()).split(",");
				String payoutUserID[] = payout.getPayoutUserID().split(",");
				
				//取出计算方式
				String payoutType = payout.getPayoutType();
				
				//存放计算后的消费金额
				BigDecimal cost;
				
				//判断本次消费记录的消费类型，如果是均分，则除以本次消费人的个数，算出平均消费金额
				if(payoutType.equals(payoutTypeArr[0]))
				{
					//得到消费人数
					int payoutTotal = payoutUserName.length;
					
					/*金额的数据类型是BigDecimal 
					通过BigDecimal的divide方法进行除法时当不整除，出现无限循环小数时，就会抛异常的，异常如下：java.lang.ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result. at java.math.BigDecimal.divide(Unknown Source) 

					应用场景：一批中供客户的单价是1000元/年，如果按月计算的话1000/12=83.3333333333.... 

					解决之道：就是给divide设置精确的小数点divide(xxxxx,2, BigDecimal.ROUND_HALF_EVEN) */
					//得到计算后的平均消费金额
					cost = payout.getAmount().divide(new BigDecimal(payoutTotal),2, BigDecimal.ROUND_HALF_EVEN);
				}
				//如果是借贷或是个人消费，则直接取出消费金额
				else {
					cost = payout.getAmount();
				}
				
				//遍历消费人数组
				for (int j = 0; j < payoutUserID.length; j++) {
					
					//如果是借贷则跳过第一个索引，因为第一个人是借贷人自己
					if (payoutType.equals(payoutTypeArr[1]) && j == 0) {
						continue;
					}
					
					//声明一个统计类
					ModelStatistics modelStatistics = new ModelStatistics();
					//将统计类的支付人设置为消费人数组的第一个人
					modelStatistics.PayerUserID = payoutUserName[0];
					//设置消费人
					modelStatistics.ConsumerUserID = payoutUserName[j];
					//设置消费类型
					modelStatistics.setPayoutType(payoutType);
					//设置算好的消费金额
					modelStatistics.Cost = cost;
					
					listModelStatistics.add(modelStatistics);
				}
			}
		}
		
		return listModelStatistics;
	}
	
//	public String ExportStatistics(int p_AccountBookID) throws Exception {
//		String _Result = "";
//		String _AccountBookName = m_BusinessAccountBook.getAccountBookNameByAccountId(p_AccountBookID);
//		Date _Date = new Date();
//		String _FileName = String.valueOf(_Date.getYear()) + String.valueOf(_Date.getMonth()) + String.valueOf(_Date.getDay()) + ".xls";
//		File _FileDir = new File("/sdcard/GoDutch/Export/");
//		if (!_FileDir.exists()) {
//			_FileDir.mkdirs();
//		}
//		File _File = new File("/sdcard/GoDutch/Export/" + _FileName);
//		if(!_File.exists()) {
//			_File.createNewFile();
//		}
//		
//		WritableWorkbook wBookData;
//		//创建工作簿
//		wBookData = Workbook.createWorkbook(_File);
//		//创建工作表
//		WritableSheet wsAccountBook = wBookData.createSheet(_AccountBookName, 0);
//		
//		String[] _Titles = {"编号", "姓名", "金额", "消费信息", "消费类型"};
//		Label _Label;
//		//添加标题行
//		for (int i = 0; i < _Titles.length; i++) {
//			_Label = new Label(i, 0, _Titles[i]);
//			wsAccountBook.addCell(_Label);
//		}
//		
//		/*
//		 * 添加行
//		 */
//		List<ModelStatistics> _ListModelStatisticsTotal =  GetPayoutUserID(" And AccountBookID = " + p_AccountBookID);
//		
//		for(int i = 0;i < _ListModelStatisticsTotal.size(); i++) {
//			ModelStatistics _ModelStatistics = _ListModelStatisticsTotal.get(i);
//			
//			//添加编号列
//			jxl.write.Number _IdCell = new Number(0, i+1, i+1);
//			wsAccountBook.addCell(_IdCell);
//			
//			//添加姓名
//			Label _lblName = new Label(1, i+1, _ModelStatistics.PayerUserID);
//			wsAccountBook.addCell(_lblName);
//			
//			//格式化金额类型显示
//			NumberFormat nfMoney = new NumberFormat("#.##");
//			WritableCellFormat wcfFormat = new WritableCellFormat(nfMoney);
//			
//			//添加金额
//			Number _CostCell = new Number(2, i+1, _ModelStatistics.Cost.doubleValue(), wcfFormat);
//			wsAccountBook.addCell(_CostCell);
//			
//			//添加消费信息
//			String _Info = "";
//			if(_ModelStatistics.getPayoutType().equals("个人")) {
//				_Info = _ModelStatistics.PayerUserID + "个人消费 " + _ModelStatistics.Cost.toString() + "元";
//			} else if(_ModelStatistics.getPayoutType().equals("均分")) {
//				if(_ModelStatistics.PayerUserID.equals(_ModelStatistics.ConsumerUserID)) {
//					_Info = _ModelStatistics.PayerUserID + "个人消费 " + _ModelStatistics.Cost.toString() + "元";
//				} else {
//					_Info = _ModelStatistics.ConsumerUserID + "应支付给" + _ModelStatistics.PayerUserID + _ModelStatistics.Cost + "元";
//				}
//			} 
//			Label _lblInfo = new Label(3, i+1, _Info);
//			wsAccountBook.addCell(_lblInfo);
//			
//			//添加消费类型
//			Label _lblPayoutType = new Label(4, i+1, _ModelStatistics.getPayoutType());
//			wsAccountBook.addCell(_lblPayoutType);
//		}
//		
//		wBookData.write();
//		wBookData.close();
//		_Result = "数据已经导出！位置在：/sdcard/GoDutch/Export/" + _FileName;
//		return _Result;
//	}
//	
	public class ModelStatistics
	{
		public String PayerUserID;
		public String ConsumerUserID;
		private String m_PayoutType;
		public BigDecimal Cost;
		
		public String getPayoutType() {
			return m_PayoutType;
		}
		
		public void setPayoutType(String p_Value) {
			m_PayoutType = p_Value;
		}
	}
}
