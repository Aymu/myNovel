package com.example.mynovel;

import android.util.Log;

import com.mysql.jdbc.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class collectionImple {
    private static final String url = "jdbc:mysql://cdb-h1ubzfre.bj.tencentcdb.com:10129/mynovel?useUnicode=true&characterEncoding=UTF-8";
    private static final String user = "root";//用户名
    private static final String password = "15279909969aqw";

    private Connection conn =null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    private static collectionImple coImple;
    public  collectionImple(){

    }
    public static collectionImple getInstance(){
        if (coImple==null){
            coImple = new collectionImple();
        }
        return coImple;
    }
    //查询所有收藏信息
    public ArrayList<collection> getCollectionData(){
        ArrayList<collection> collections = new ArrayList<collection>();
        String sql = "select * from collection";
        conn =  DButil.getSqlConnection(url,user,password);
        try{
            if (conn!=null && !(conn.isClosed())){
                ps = (PreparedStatement)conn.prepareStatement(sql);
                if (ps!=null){
                    rs = ps.executeQuery();
                    if (rs!=null){
                        while (rs.next()){
                            collection co= new collection();
                            co.setUser(rs.getString(1));
                            co.setName(rs.getString(2));
                            co.setAuthor(rs.getString(3));
                            co.setType(rs.getString(4));
                            co.setUrl(rs.getString(5));
                            collections.add(co);
                            Log.d("error1", "getCollectionData: 取数据成功");
                        }
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        DButil.closeAll(conn,ps,rs);
        return  collections;
    }
    //插入收藏小说数据库
    public void insertCollection(collection co){
        int result = -1;
        String sql = "insert into collection(username,name,author,type,url)values(?,?,?,?,?)";
        conn = DButil.getInstance().getSqlConnection(url,user,password);

        try {

            boolean closed = conn.isClosed();
            if ((conn!=null)&& (!closed)){
                ps = (PreparedStatement)conn.prepareStatement(sql);
                String username = co.getUser();
                String name = co.getName();
                String author = co.getAuthor();
                String type = co.getType();
                String url = co.getUrl();
                ps.setString(1,username);
                ps.setString(2,name);
                ps.setString(3,author);
                ps.setString(4,type);
                ps.setString(5,url);
                result = ps.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        DButil.closeAll(conn,ps);
    }
    //取消收藏数据库
    public int deleteCollection(collection co){
        int result = -1;
        if (!StringUtils.isEmptyOrWhitespaceOnly(co.getName())){
            conn = DButil.getSqlConnection(url,user,password);
            String sql = "delete from collection where name = ?";
            try{
                boolean closed = conn.isClosed();
                if((conn!=null)&& (!closed)){
                    ps = (PreparedStatement)conn.prepareStatement(sql);
                    ps.setString(1,co.getName());
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
