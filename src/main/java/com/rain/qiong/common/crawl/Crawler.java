package com.rain.qiong.common.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class Crawler {

	// 1.加载驱动
			static {
				try {
					Class.forName("com.mysql.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}

			// 2.获得数据库连接对象Connection
			public static Connection getConnection() {
				Connection conn = null;
				try {
					conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ceshi", "root", "root");
					if(!conn.isClosed()) {
						System.out.println("Succeeded connecting to the Database!");
					}
					else {
						System.out.println("Fail");
					}
				} catch (SQLException e) {

					e.printStackTrace();
				}
				return conn;
			}
		
		//建立HashMap存储对外投资文件地址	
		static LinkedHashMap<String,String> investmap = new LinkedHashMap<String,String>(){
			{
				
				put("C:/Users/wb/Desktop/企业文件/invest/1.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/invest/2.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/invest/3.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/invest/4.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/invest/5.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/invest/6.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/invest/7.html","阿里巴巴");
				
			}
		};
		
		static LinkedHashMap<String,String> legalmap = new LinkedHashMap<String,String>(){
			{
				
				put("C:/Users/wb/Desktop/企业文件/legal/1.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/legal/2.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/legal/3.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/legal/4.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/legal/5.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/legal/6.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/legal/7.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/legal/8.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/legal/9.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/legal/10.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/legal/11.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/legal/12.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/legal/13.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/legal/14.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/legal/15.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/legal/16.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/legal/17.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/legal/18.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/legal/19.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/legal/20.html","阿里巴巴");
				
			}
		};
		
		static LinkedHashMap<String,String> annmap = new LinkedHashMap<String,String>(){
			{
				put("C:/Users/wb/Desktop/企业文件/ann/1.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/ann/2.html","阿里巴巴");
				put("C:/Users/wb/Desktop/企业文件/ann/3.html","阿里巴巴");
			}
		};
		
			
	public static void main(String[] args) throws SQLException,IOException{
		// TODO 自动生成的方法存根
		String fileurl = "C:/Users/Administrator/Desktop/阿里巴巴.html";
		String charset = "utf-8";
		File input = new File(fileurl);
		int ID = 11; //使用迭代器Iterator,进行完一次，才+1，相当于插入一个公司的全部信息再进行下一家
		boolean flag = true;  //过滤掉第一行的标签,专用于水平类型表格
		//企业Id，每进行一次+1
		String sql1="insert into basicinformation(Name,Contact,Email,Website,Address,Representative,Id) values(?,?,?,?,?,?,?)";
		//获取企业基本信息并插入到basicinformation表
		String sql2="insert into detailedinformation(xxId,RegisteredCapital,DateSetup,OperatingState,UnifiedSocialCreditCode,TaxpayerNum,RegistrationNum,OrganizationCode,CompanyType,StaffSize,BusinessTerm,RegisterOff,ApprovalDate,EnglishName,Fname,Area,IndustryInvolved,Address,ScopeOfBusiness) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		//获取企业详细信息并插入到detailedinformation
		String sql3="insert into shareholdersinformation(Name,Proportion,OutMoney,SubscribedDate,ShareholdersType,CompanyId) values(?,?,?,?,?,?)";
		//获取企业股东信息并插入到shareholdersinformation
		String sql4="insert into enterpriseforeigninvestment(InvestedEnterpriseName,InvestedRepresent,RegisteredCapital,InvestmentProportion,DateSetup,OperatingState,CompanyId) values(?,?,?,?,?,?,?)";
		//获取企业对外投资信息并插入到enterpriseforeigninvestment
		String sql5="insert into mainstaff(Name,Position,CompanyId) values(?,?,?)";
		//获取企业主要人员并插入到mainstaff
		String sql6="insert into documents(CaseName,ReleaseTime,CaseNum,Identity,ExecuteCourt,CompanyId) values(?,?,?,?,?,?)";
		//获取企业裁判文书并插入到documents
		String sql7="insert into announcement(AboutMan,AType,AnnounceMan,ATime,Content,Publishpage,UploadTime,CompanyId) values(?,?,?,?,?,?,?,?)";
		//获取企业法院公告并插入到announcement
		Connection conn=getConnection();
		
			Document doc = Jsoup.parse(input, charset);
			String qyName = doc.getElementsByTag("h1").text();
			String frName = doc.getElementsByClass("bname").text();
			Elements basic = doc.getElementsByClass("cvlu");
			//打印数据，使用时清除
//			System.out.println(qyName);
//			System.out.println(basic.get(0).text());
//			System.out.println(basic.get(1).text());
//			System.out.println(basic.get(2).text());
//			System.out.println(basic.get(3).text().replaceAll(" 附近公司", ""));
//			System.out.println(frName);
//			System.out.println("");
//			System.out.print("\n");
			PreparedStatement pstmt1=conn.prepareStatement(sql1);
			pstmt1.setString(1, qyName);
			pstmt1.setString(2, basic.get(0).text());
			pstmt1.setString(3, basic.get(1).text());
			pstmt1.setString(4, basic.get(2).text());
			pstmt1.setString(5, basic.get(3).text().replaceAll(" 附近公司", ""));
			pstmt1.setString(6, frName);
			pstmt1.setInt(7, ID);
			pstmt1.executeUpdate();
			if(pstmt1 !=null && pstmt1.isClosed()) {
				pstmt1.close();
			}//信息插入到basicinformation表(Name,Contact,Email,Website,Address,Representative)
			
			doc.getElementsByClass("tb").remove();
			Elements detail = doc.select("table").get(3).getElementsByTag("td"); 
			//打印数据，使用时清除
//			System.out.println(ID);//xxId
//			System.out.println(detail.get(0).text());//RegisteredCapital
//			System.out.println(detail.get(3).text());//DateSetup
//			System.out.println(detail.get(2).text());//OperatingState
//			System.out.println(detail.get(7).text());//UnifiedSocialCreditCode
//			System.out.println(detail.get(6).text());//TaxpayerNum
//			System.out.println(detail.get(4).text());//RegistrationNum
//			System.out.println(detail.get(5).text());//OrganizationCode
//			System.out.println(detail.get(8).text());//CompanyType
//			System.out.println(detail.get(16).text());//StaffSize
//			System.out.println(detail.get(17).text());//BusinessTerm
//			System.out.println(detail.get(11).text());//RegisterOff
//			System.out.println(detail.get(10).text());//ApprovalDate
//			System.out.println(detail.get(13).text());//EnglishName
//			System.out.println(detail.get(14).text());//Fname
//			System.out.println(detail.get(12).text());//Area
//			System.out.println(detail.get(9).text());//IndustryInvolved
//			System.out.println(detail.get(18).text().replaceAll(" 查看地图 附近公司", ""));//Address
//			System.out.println(detail.get(19).text());//ScopeOfBusiness
//			System.out.println("");
//			System.out.print("\n");
			PreparedStatement pstmt2=conn.prepareStatement(sql2);
			pstmt2.setInt(1, ID);
			pstmt2.setString(2, detail.get(0).text());
			pstmt2.setString(3, detail.get(3).text());
			pstmt2.setString(4, detail.get(2).text());
			pstmt2.setString(5, detail.get(7).text());
			pstmt2.setString(6, detail.get(6).text());
			pstmt2.setString(7, detail.get(4).text());
			pstmt2.setString(8, detail.get(5).text());
			pstmt2.setString(9, detail.get(8).text());
			pstmt2.setString(10, detail.get(16).text());
			pstmt2.setString(11, detail.get(17).text());
			pstmt2.setString(12, detail.get(11).text());
			pstmt2.setString(13, detail.get(10).text());
			pstmt2.setString(14, detail.get(13).text());
			pstmt2.setString(15, detail.get(14).text());
			pstmt2.setString(16, detail.get(12).text());
			pstmt2.setString(17, detail.get(9).text());
			pstmt2.setString(18, detail.get(18).text().replaceAll(" 查看地图 附近公司", ""));
			pstmt2.setString(19, detail.get(19).text());
			pstmt2.executeUpdate();
			if(pstmt2 !=null && pstmt2.isClosed()) {
				pstmt2.close();
			}//信息插入到detailedinformation表(xxId,RegisteredCapital,DateSetup,OperatingState,UnifiedSocialCreditCode,TaxpayerNum,RegistrationNum,OrganizationCode,CompanyType,StaffSize,BusinessTerm,RegisterOff,ApprovalDate,EnglishName,Fname,Area,IndustryInvolved,Address,ScopeOfBusiness)
		
			
			Elements shareholders = doc.select("table").get(4).getElementsByTag("tr");
			PreparedStatement pstmt3=conn.prepareStatement(sql3);
			for(Element shareholder:shareholders) {
				if(flag) {
					flag = false;
				}else {
//				String td = shareholder.child(0).text()+"\t"+shareholder.child(1).text()+"\t"+shareholder.child(2).text()+"\t"+"\t"+shareholder.child(3).text()+"\t"+
//						shareholder.child(4).text()+"\t"+shareholder.child(5).text();
//				System.out.println(td);
			//}
				
				pstmt3.setString(1, shareholder.child(1).text());//Name
				pstmt3.setString(2, shareholder.child(2).text());//Proportion
				//判断有无值且持股比例为100%，是'-'时，则使用注册资本
				pstmt3.setString(3, shareholder.child(3).text());//OutMoney
				pstmt3.setString(4, shareholder.child(4).text());//SubscribedDate
				pstmt3.setString(5, shareholder.child(5).text());//ShareholdersType
				pstmt3.setInt(6, ID);//CompanyId
				pstmt3.executeUpdate();
				}
				}
				if(pstmt3 !=null && pstmt3.isClosed()) {
					pstmt3.close();
				}//信息并插入到shareholdersinformation(Name,Proportion,OutMoney,SubscribedDate,ShareholdersType,CompanyId)
//				System.out.println("");
//				System.out.print("\n");
				flag = true;
			
			Iterator<Entry<String, String>> iter = investmap.entrySet().iterator();
			while (iter.hasNext()) {
				flag = true;
				@SuppressWarnings("rawtypes")
				Entry entry = (Entry) iter.next();
				String foreignurl = (String) entry.getKey();  //每个分页的url
				File forinput = new File(foreignurl);
				Document fordoc = Jsoup.parse(forinput, charset);
				Elements foreigns = fordoc.select("table").get(5).getElementsByTag("tr");
				PreparedStatement pstmt4=conn.prepareStatement(sql4);
				for(Element foreign:foreigns) {
//				String td = foreign.child(0).text()+"\t"+foreign.child(1).text().replaceAll(" 对外投资与任职 >", "")+"\t"+foreign.child(2).text()+"\t"+"\t"+foreign.child(3).text()+"\t"+
//						foreign.child(4).text()+"\t"+foreign.child(5).text();
//				System.out.println(td);
//				}
//				System.out.println();
				if(flag) {
					flag = false;
				}else {
				pstmt4.setString(1, foreign.child(0).text());//InvestedEnterpriseName
				pstmt4.setString(2, foreign.child(1).text().replaceAll(" 对外投资与任职 >", ""));//InvestedRepresent
				pstmt4.setString(3, foreign.child(2).text());//RegisteredCapital
				pstmt4.setString(4, foreign.child(3).text());//InvestmentProportion
				pstmt4.setString(5, foreign.child(4).text());//DateSetup
				pstmt4.setString(6, foreign.child(5).text());//OperatingState
				pstmt4.setInt(7, ID);//CompanyId
				pstmt4.executeUpdate();
				}
			}
				if(pstmt4 !=null && pstmt4.isClosed()) {
					pstmt4.close();
				}//信息插入到enterpriseforeigninvestment(InvestedEnterpriseName,InvestedRepresent,RegisteredCapital,InvestmentProportion,DateSetup,OperatingState,CompanyId)
			}
				flag = true;
			
			Elements staff = doc.select("table").get(6).getElementsByTag("tr");
			PreparedStatement pstmt5=conn.prepareStatement(sql5);
			for(Element employee:staff) {
				
//				String td = employee.child(0).text()+"\t"+employee.child(1).text().replaceAll(" 他关联\\d\\d家公司 >", "")+"\t"+employee.child(2).text();
//				System.out.println(td);
//			}
				if(flag) {
					flag = false;
				}else {
				pstmt5.setString(1, employee.child(1).text().replaceAll(" 他关联\\d\\d家公司 >", ""));
				pstmt5.setString(2, employee.child(2).text());
				pstmt5.setInt(3, ID);
				pstmt5.executeUpdate();
				}
			}
				if(pstmt5 !=null && pstmt5.isClosed()) {
					pstmt5.close();
				}//并插入到mainstaff(Name,Position,CompanyId)
//			System.out.println("");
//			System.out.print("\n");
				flag = true;
			
//			String documenturl = "C:/Users/wb/Desktop/企业文件/legal/20.html";
//			File doinput = new File(documenturl);
//			Document dodoc = Jsoup.parse(doinput,charset);
//			String documents = dodoc.getElementsByClass("ntable ntable-odd").get(3).text();
//			System.out.println(documents);
			
			Iterator<Entry<String, String>> iter2 = legalmap.entrySet().iterator();
			while (iter2.hasNext()) {
				flag = true;
				@SuppressWarnings("rawtypes")
				Entry entry2 = (Entry) iter2.next();
				String documenturl = (String) entry2.getKey();  //每个分页的url
				File doinput = new File(documenturl);
				Document dodoc = Jsoup.parse(doinput, charset);
				Elements documents = dodoc.getElementsByClass("ntable ntable-odd").get(3).getElementsByTag("tr");
				PreparedStatement pstmt6=conn.prepareStatement(sql6);
				for(Element docu:documents) {
					
//				String td = docu.child(0).text()+"\t"+docu.child(1).text()+"\t"+docu.child(2).text()+"\t"+"\t"+docu.child(3).text()+"\t"+
//						docu.child(4).text()+"\t"+docu.child(5).text();
//				System.out.println(td);
//				
//				}
//				System.out.println();
				if(flag) {
					flag = false;
				}else {
				pstmt6.setString(1, docu.child(1).text());//CaseName
				pstmt6.setString(2, docu.child(2).text());//ReleaseTime
				pstmt6.setString(3, docu.child(3).text());//CaseNum
				pstmt6.setString(4, docu.child(4).text());//Identity
				pstmt6.setString(5, docu.child(5).text());//ExecuteCourt
				pstmt6.setInt(6, ID);//CompanyId
				pstmt6.executeUpdate();
				}
			}
				if(pstmt6 !=null && pstmt6.isClosed()) {
					pstmt6.close();
				}//插入到documents(CaseName,ReleaseTime,CaseNum,Identity,ExecuteCourt,CompanyId)
			}
//			System.out.println("");
//			System.out.print("\n");
			flag = true;
		
			
			Iterator<Entry<String, String>> iter3 = annmap.entrySet().iterator();
			while (iter3.hasNext()) {
				flag = true;
				@SuppressWarnings("rawtypes")
				Entry entry3 = (Entry) iter3.next();
				String annurl = (String) entry3.getKey();  //每个分页的url
				File anninput = new File(annurl);
				Document anndoc = Jsoup.parse(anninput, charset);
				anndoc.getElementsByClass("tb").remove();
				Elements announcement = anndoc.getElementsByClass("ntable").get(10).getElementsByTag("td");
				PreparedStatement pstmt7=conn.prepareStatement(sql7);
//				for(Element ann:announcement) {
//					System.out.println(ann.text());
//				}
//				System.out.println();
				pstmt7.setString(1, announcement.get(0).text());//AboutMan
				pstmt7.setString(2, announcement.get(1).text());//AType
				pstmt7.setString(3, announcement.get(5).text());//AnnounceMan
				pstmt7.setString(4, announcement.get(2).text());//ATime
				pstmt7.setString(5, announcement.get(6).text());//Content
				pstmt7.setString(6, announcement.get(3).text());//Publishpage
				pstmt7.setString(7, announcement.get(4).text());//UploadTime
				pstmt7.setInt(8, ID);//CompanyId
				pstmt7.executeUpdate();
				
			
				if(pstmt7 !=null && pstmt7.isClosed()) {
					pstmt7.close();
				}//并插入到announcement(AboutMan,AType,AnnounceMan,ATime,Content,Publishpage,UploadTime,CompanyId)
			}
			flag = true;
			
			if(conn!=null && conn.isClosed()) {
				conn.close();
			}
		}	

}
