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
		
		//���õ�����Ϣ����ת�����Է���ۿ�
		for (int i = 0; i < listModelStatisticsTotal.size(); i++) {
			ModelStatistics modelStatistics = listModelStatisticsTotal.get(i);
			if(modelStatistics.getPayoutType().equals("����")) {
				result += modelStatistics.PayerUserID + "�������� " + modelStatistics.Cost.toString() + "Ԫ\r\n";
			} else if(modelStatistics.getPayoutType().equals("����")) {
				if(modelStatistics.PayerUserID.equals(modelStatistics.ConsumerUserID)) {
					result += modelStatistics.PayerUserID + "�������� " + modelStatistics.Cost.toString() + "Ԫ\r\n";
				} else {
					result += modelStatistics.ConsumerUserID + "Ӧ֧����" + modelStatistics.PayerUserID + modelStatistics.Cost + "Ԫ\r\n";
				}
			}
		}
		
		return result;
	}
	
	public List<ModelStatistics> GetPayoutUserID(String p_Condition)
	{
		//�õ���ֺõ�ͳ����Ϣ
		List<ModelStatistics> listModelStatistics = GetModelStatisticsList(p_Condition);
		//��Ű������˷������ʱͳ����Ϣ
		List<ModelStatistics> listModelStatisticsTemp = new ArrayList<ModelStatistics>();
		//���ͳ�ƺõĻ���
		List<ModelStatistics> listModelStatisticsTotal = new ArrayList<ModelStatistics>();
		String result = "";
		//������ֺõ�ͳ����Ϣ
		for (int i = 0; i < listModelStatistics.size(); i++) {
			//�õ���ֺõ�һ����Ϣ
			ModelStatistics modelStatistics = listModelStatistics.get(i);
			result += modelStatistics.PayerUserID + "#" + modelStatistics.ConsumerUserID + "#" + modelStatistics.Cost + "\r\n";
			//���浱ǰ�ĸ�����ID
			String currentPayerUserID = modelStatistics.PayerUserID;
			
			//�ѵ�ǰ��Ϣ�������˷������ʱ����
			ModelStatistics modelStatisticsTemp = new ModelStatistics();
			modelStatisticsTemp.PayerUserID = modelStatistics.PayerUserID;
			modelStatisticsTemp.ConsumerUserID = modelStatistics.ConsumerUserID;
			modelStatisticsTemp.Cost = modelStatistics.Cost;
			modelStatisticsTemp.setPayoutType(modelStatistics.getPayoutType());
			listModelStatisticsTemp.add(modelStatisticsTemp);
			
			//������һ�е�����
			int nextIndex;
			//�����һ������С��ͳ����Ϣ����������Լ�1
			if((i+1) < listModelStatistics.size())
			{
				nextIndex = i+1;
			}
			else {
				//����֤���Ѿ���β����������ֵΪ��ǰ��
				nextIndex = i;
			}
			
			//�����ǰ����������һλ�����˲�ͬ����֤������ͳ���Ѿ���β�������Ѿ�ѭ����ͳ���������һλ����Ϳ�ʼ�������ͳ��
			if (!currentPayerUserID.equals(listModelStatistics.get(nextIndex).PayerUserID) || nextIndex == i) {
				
				//��ʼ���б������е�ǰ����ͳ�������ͳ��
				for (int j = 0; j < listModelStatisticsTemp.size(); j++) {
					//ȡ����һ��
					ModelStatistics modelStatisticsTotal = listModelStatisticsTemp.get(j);
					//�ж�����ͳ�����鵱���Ƿ��Ѿ����ڸø����˺������˵���Ϣ
					int index = GetPostionByConsumerUserID(listModelStatisticsTotal,modelStatisticsTotal.PayerUserID, modelStatisticsTotal.ConsumerUserID);
					//����Ѿ����ڣ���ʼ��ԭ���������Ͻ����ۼ�
					if(index != -1)
					{
						listModelStatisticsTotal.get(index).Cost = listModelStatisticsTotal.get(index).Cost.add(modelStatisticsTotal.Cost);
					}
					else {
						//�������һ������Ϣ����ӵ�ͳ�����鵱��
						listModelStatisticsTotal.add(modelStatisticsTotal);
					}
				}
				//ȫ�����������յ�ǰ����ͳ�����飬������һ������ͳ������ļ��㣬ֱ��β
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
		//��֧����ID����ȡ�����Ѽ�¼
		List<Payout> listPayout = mBusinessPayout.GetPayoutOrderByPayoutUserID(p_Condition);
		
		//��ȡ���㷽ʽ����
		String payoutTypeArr[] = GetContext().getResources().getStringArray(R.array.PayoutType);
		
		List<ModelStatistics> listModelStatistics = new ArrayList<ModelStatistics>();
		
		if(listPayout != null)
		{
			//�������Ѽ�¼�б�
			for (int i = 0; i < listPayout.size(); i++) {
				//ȡ��һ�����Ѽ�¼
				Payout payout = listPayout.get(i);
				//��������IDת��Ϊ��ʵ����
				String payoutUserName[] = mBusinessUser.GetUserNameByUserID(payout.getPayoutUserID()).split(",");
				String payoutUserID[] = payout.getPayoutUserID().split(",");
				
				//ȡ�����㷽ʽ
				String payoutType = payout.getPayoutType();
				
				//��ż��������ѽ��
				BigDecimal cost;
				
				//�жϱ������Ѽ�¼���������ͣ�����Ǿ��֣�����Ա��������˵ĸ��������ƽ�����ѽ��
				if(payoutType.equals(payoutTypeArr[0]))
				{
					//�õ���������
					int payoutTotal = payoutUserName.length;
					
					/*��������������BigDecimal 
					ͨ��BigDecimal��divide�������г���ʱ������������������ѭ��С��ʱ���ͻ����쳣�ģ��쳣���£�java.lang.ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result. at java.math.BigDecimal.divide(Unknown Source) 

					Ӧ�ó�����һ���й��ͻ��ĵ�����1000Ԫ/�꣬������¼���Ļ�1000/12=83.3333333333.... 

					���֮�������Ǹ�divide���þ�ȷ��С����divide(xxxxx,2, BigDecimal.ROUND_HALF_EVEN) */
					//�õ�������ƽ�����ѽ��
					cost = payout.getAmount().divide(new BigDecimal(payoutTotal),2, BigDecimal.ROUND_HALF_EVEN);
				}
				//����ǽ�����Ǹ������ѣ���ֱ��ȡ�����ѽ��
				else {
					cost = payout.getAmount();
				}
				
				//��������������
				for (int j = 0; j < payoutUserID.length; j++) {
					
					//����ǽ����������һ����������Ϊ��һ�����ǽ�����Լ�
					if (payoutType.equals(payoutTypeArr[1]) && j == 0) {
						continue;
					}
					
					//����һ��ͳ����
					ModelStatistics modelStatistics = new ModelStatistics();
					//��ͳ�����֧��������Ϊ����������ĵ�һ����
					modelStatistics.PayerUserID = payoutUserName[0];
					//����������
					modelStatistics.ConsumerUserID = payoutUserName[j];
					//������������
					modelStatistics.setPayoutType(payoutType);
					//������õ����ѽ��
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
//		//����������
//		wBookData = Workbook.createWorkbook(_File);
//		//����������
//		WritableSheet wsAccountBook = wBookData.createSheet(_AccountBookName, 0);
//		
//		String[] _Titles = {"���", "����", "���", "������Ϣ", "��������"};
//		Label _Label;
//		//��ӱ�����
//		for (int i = 0; i < _Titles.length; i++) {
//			_Label = new Label(i, 0, _Titles[i]);
//			wsAccountBook.addCell(_Label);
//		}
//		
//		/*
//		 * �����
//		 */
//		List<ModelStatistics> _ListModelStatisticsTotal =  GetPayoutUserID(" And AccountBookID = " + p_AccountBookID);
//		
//		for(int i = 0;i < _ListModelStatisticsTotal.size(); i++) {
//			ModelStatistics _ModelStatistics = _ListModelStatisticsTotal.get(i);
//			
//			//��ӱ����
//			jxl.write.Number _IdCell = new Number(0, i+1, i+1);
//			wsAccountBook.addCell(_IdCell);
//			
//			//�������
//			Label _lblName = new Label(1, i+1, _ModelStatistics.PayerUserID);
//			wsAccountBook.addCell(_lblName);
//			
//			//��ʽ�����������ʾ
//			NumberFormat nfMoney = new NumberFormat("#.##");
//			WritableCellFormat wcfFormat = new WritableCellFormat(nfMoney);
//			
//			//��ӽ��
//			Number _CostCell = new Number(2, i+1, _ModelStatistics.Cost.doubleValue(), wcfFormat);
//			wsAccountBook.addCell(_CostCell);
//			
//			//���������Ϣ
//			String _Info = "";
//			if(_ModelStatistics.getPayoutType().equals("����")) {
//				_Info = _ModelStatistics.PayerUserID + "�������� " + _ModelStatistics.Cost.toString() + "Ԫ";
//			} else if(_ModelStatistics.getPayoutType().equals("����")) {
//				if(_ModelStatistics.PayerUserID.equals(_ModelStatistics.ConsumerUserID)) {
//					_Info = _ModelStatistics.PayerUserID + "�������� " + _ModelStatistics.Cost.toString() + "Ԫ";
//				} else {
//					_Info = _ModelStatistics.ConsumerUserID + "Ӧ֧����" + _ModelStatistics.PayerUserID + _ModelStatistics.Cost + "Ԫ";
//				}
//			} 
//			Label _lblInfo = new Label(3, i+1, _Info);
//			wsAccountBook.addCell(_lblInfo);
//			
//			//�����������
//			Label _lblPayoutType = new Label(4, i+1, _ModelStatistics.getPayoutType());
//			wsAccountBook.addCell(_lblPayoutType);
//		}
//		
//		wBookData.write();
//		wBookData.close();
//		_Result = "�����Ѿ�������λ���ڣ�/sdcard/GoDutch/Export/" + _FileName;
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
