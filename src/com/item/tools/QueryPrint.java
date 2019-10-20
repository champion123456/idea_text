package com.item.tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.item.dao.GoodsDao;
import com.item.db.DbClose;
import com.item.db.DbConn;
import com.item.entity.Goods;
import com.item.entity.SalesMan;

public final class QueryPrint {
	Connection conn=null;
	PreparedStatement pstmt=null;
	ResultSet rs=null;
	
	/**
	 * 模糊查询并陈列查询信息函数小工具
	 * @param oper 调用者
	 * @return 查询到的信息的gid,如果返回值等于-1，则代表查询异常。	  
	 */
	public static int query(String oper) {
		int gid=-1;
		String shopping=ScannerChoice.ScannerInfoString();
		ArrayList<Goods> goodsList=new QueryPrint().queryGoodsKey(-1,shopping);
		if(goodsList==null||goodsList.size()<0)
		{
			System.err.println("\t!!查无此商品！！");
			
			ScannerChoice.changedInfoNext(oper);
		}else {//查到此物后，实现更改商品 信息操作
			Goods goods=goodsList.get(0);
			System.out.println("\t\t\t\t\t商品列表\n\n");
			System.out.println("\t商品编号\t\t商品名称\t\t商品价格\t\t商品数量\t\t备注\n");
			System.out.print("\t"+goods.getGid()+"\t\t"+goods.getGname()+"\t\t"+goods.getGprice()+"\t\t"+goods.getGnum());
			if (goods.getGnum()==0)
			{
				System.out.println("\t\t该商品已售空");
			}else if (goods.getGnum()<10) {
				System.out.println("\t\t该商品已不足10件");
			}else {
				System.out.println("\t\t-");
			}
			gid=goods.getGid();
		}
		return gid;
	}
	/**
	 * 模拟查询函数小工具
	 * @return int 当商品件数有且只有一件的时候返回商品gid,商品已售空时返回 -1. >1件时返回-2 . 查无此商品时返回-3
	 * 
	 */
	public static int querySettlement()
	{
		int gid=-1;
		ArrayList<Goods> goodsSettlement=new GoodsDao().queryGoods(3);//調用 关键字查询函数
		if(goodsSettlement==null||goodsSettlement.size()<0) {
			System.err.println("\t!!查无此商品！！");
			gid=-3;
		}else {
			System.out.println("\t\t\t\t\t商品列表\n\n");
			System.out.println("\t商品编号\t\t商品名称\t\t商品价格\t\t商品数量\t\t备注\n");
			for(int i=0;i<goodsSettlement.size();i++)
			{
				Goods goods=goodsSettlement.get(i);
				if(goods.getGnum()>0)
				{
					System.out.print("\t"+goods.getGid()+"\t\t"+goods.getGname()+"\t\t"+goods.getGprice()+"\t\t"+goods.getGnum());
				if(goods.getGnum()==0)
				{
					System.out.println("\t\t该商品已售空");
				}else if (goods.getGnum()<10) {
					System.out.println("\t\t商品不足10件！！");
				}else {
					System.out.println("\t\t-");
					  }
				if(goodsSettlement.size()==1)
				{
					gid=goods.getGid();
				}else {
					gid=-2;
					  }
				}
			}
			}
			return gid;
		}
		/**
		 * 根据商品的 gid or gname 查询商品
		 * @param 商品的id 商品名称
		 * @return 商品名称
		 */
	public ArrayList<Goods> queryGoodsKey(int gId,String gName)
	{
		ArrayList<Goods> goodsList=new ArrayList<Goods>();
		conn=DbConn.getconn();
		String sql="SELECT * FROM GOODS WHERE GID=? OR GNAME=?";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, gId);
			pstmt.setString(2, gName);
			rs=pstmt.executeQuery();
			while (rs.next()) {
				int gid=rs.getInt("gid");
				String name=rs.getString(2);
				double gprice=rs.getDouble(3);
				int gnum=rs.getInt(4);
				Goods goods=new Goods(gid,gName,gprice,gnum);
				goodsList.add(goods);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DbClose.queryClose(pstmt, rs, conn);
		}
		return goodsList;
	}
	/**
	 * 精确查询售货员信息
	 * @param 售货员名字
	 * @return 
	 */
		public ArrayList<SalesMan> querySalesMan(String sName)
		{
			ArrayList<SalesMan> SalesManList = new ArrayList<SalesMan>();
			conn = DbConn.getconn();	
			String sql = "SELECT * FROM SALESMAN WHERE SNAME=?";
			   try
			   {
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, sName);
					rs = pstmt.executeQuery();
					while (rs.next())
					{
						int sid = rs.getInt("sid");
						String sname = rs.getString(2);
						String sPassWord = rs.getString(3);
						
						SalesMan salesMan = new SalesMan(sid,sname,sPassWord);
						SalesManList.add(salesMan);
					}
				} catch (SQLException e)
				{
					e.printStackTrace();
				}finally
						{
							DbClose.queryClose(pstmt, rs, conn);
						}
			return SalesManList;	
		}
	}
	