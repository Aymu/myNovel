package com.example.mynovel;

import android.util.Log;

import com.mysql.jdbc.StringUtils;
import com.mysql.jdbc.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class novelInfoImple {
    private static final String url = "jdbc:mysql://cdb-h1ubzfre.bj.tencentcdb.com:10129/mynovel?useUnicode=true&characterEncoding=UTF-8";
    private static final String user = "root";//用户名
    private static final String password = "15279909969aqw";

    private Connection conn =null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    private static novelInfoImple nii;

    public novelInfoImple(){}
    public static novelInfoImple getDB(){
        if(nii==null){
            nii=new novelInfoImple();
        }
        return nii;
    }
    public ArrayList<novelInfo> getData(String tablename){
        ArrayList<novelInfo> novelInfos = new ArrayList<novelInfo>();
        String sql = "select * from "+tablename;
        conn = DButil.getSqlConnection(url,user,password);
        try{
            if (conn!=null && !(conn.isClosed())){
                ps = (PreparedStatement)conn.prepareStatement(sql);
                if(ps!=null){
                    rs = ps.executeQuery();
                    if (rs!=null){
                        while (rs.next()){
                            novelInfo ni = new novelInfo();
                            ni.setId(rs.getInt(1));
                            ni.setName(rs.getString(2));
                            ni.setUrl(rs.getString(3));
                            novelInfos.add(ni);
                        }
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        DButil.closeAll(conn,ps,rs);
        return novelInfos;
    }
    public int updateNovelInfoData(int id,String name,String u,String modifyword){
        int result=-1;
        if(!StringUtils.isEmptyOrWhitespaceOnly(modifyword)){
            //获取链接数据库对象
            conn= DButil.getSqlConnection(url,user,password);
            //MySQL 语句
            String sql="update novelurl set id=?,name=?,url=? where name=?";
            try {
                boolean closed=conn.isClosed();
                if(conn!=null&&(!closed)){
                    ps= (PreparedStatement) conn.prepareStatement(sql);
                    ps.setInt(1,id);
                    ps.setString(2,name);
                    ps.setString(3,u);
                    ps.setString(4,modifyword);
                    result=ps.executeUpdate();//返回1 执行成功
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DButil.closeAll(conn,ps);//关闭相关操作
        return result;
    }
    public void insertnovelInfo(novelInfo ni1,Connection con){
        int result = -1;
        String sql = "insert into novelurl(name,url)values(?,?)";
        try {
            boolean closed = con.isClosed();
            if ((con!=null)&& (!closed)){
                ps = (PreparedStatement)con.prepareStatement(sql);
                String name = ni1.getName();
                String pwd = ni1.getUrl();
                ps.setString(1,name);
                ps.setString(2,pwd);
                result = ps.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    //删除用户信息
    public int deleteReaderData(String modifyData){
        int result = -1;
        if (!StringUtils.isEmptyOrWhitespaceOnly(modifyData)){
            conn = DButil.getSqlConnection(url,user,password);
            String sql = "delete from novelurl where name=?";
            try{
                boolean closed = conn.isClosed();
                if((conn!=null)&& (!closed)){
                    ps = (PreparedStatement)conn.prepareStatement(sql);
                    ps.setString(1,modifyData);
                    result = ps.executeUpdate();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        DButil.closeAll(conn,ps);
        return result;
    }
}
