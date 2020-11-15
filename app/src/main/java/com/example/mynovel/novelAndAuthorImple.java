package com.example.mynovel;

import android.util.Log;

import com.mysql.jdbc.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class novelAndAuthorImple {

    private static final String url = "jdbc:mysql://cdb-h1ubzfre.bj.tencentcdb.com:10129/mynovel?useUnicode=true&characterEncoding=UTF-8";
    private static final String user = "root";//用户名
    private static final String password = "15279909969aqw";

    private Connection conn = null;
    private ResultSet rs = null;
    private PreparedStatement ps = null;
    private static novelAndAuthorImple naa = null;
    private novelAndAuthorImple(){

    }
    public static novelAndAuthorImple getDbserver(){
        if (naa==null){
            naa = new novelAndAuthorImple();
        }
        return naa;
    }
    //查询所有小说数据
    public ArrayList<novelAndAuthor> getNoveldata(){
        ArrayList<novelAndAuthor> novelAndAuthors = new ArrayList<novelAndAuthor>();
        String sql = "select * from novel";
        conn = DButil.getSqlConnection(url,user,password);

        try {
            if (conn != null && !conn.isClosed()) {
                ps = (PreparedStatement) conn.prepareStatement(sql);
                if (ps != null) {
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        novelAndAuthor novelAndAuthor = new novelAndAuthor();
                        novelAndAuthor.setId(rs.getInt(1));
                        novelAndAuthor.setNovel_name(rs.getString(2));
                        novelAndAuthor.setAuthor_name(rs.getString(3));
                        novelAndAuthor.setNovel_type(rs.getString(4));
                        novelAndAuthors.add(novelAndAuthor);
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        DButil.closeAll(conn,ps,rs);
        return novelAndAuthors;
    }
    //更新数据，以author作为关键点
    public int updateNovelAndAuthor(int id,String novel,String author,String type,String modify){
        int result = -1;
        if((!StringUtils.isEmptyOrWhitespaceOnly(modify))){
            conn = DButil.getSqlConnection(url,user,password);
            String sql = "update novel set id=?,name=?,author=?,type=? where author =?";
            try {
                boolean closed = conn.isClosed();
                if (conn!=null && !closed){
                    ps = (PreparedStatement) conn.prepareStatement(sql);
                    ps.setInt(1,id);
                    ps.setString(2,novel);
                    ps.setString(3,author);
                    ps.setString(4,type);
                    ps.setString(5,modify);
                    result = ps.executeUpdate();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            return result;
        }
        return result;
    }
    //以作者名作为关键字进行删除
    public int deleteNovelAndAuthor(String author){
        int result = -1;
        if(!StringUtils.isEmptyOrWhitespaceOnly(author)){
            conn = DButil.getSqlConnection(url,user,password);
            String sql = "delete from novel where author = ?";
            try {
                if (conn!=null && !(conn.isClosed())){
                    ps = (PreparedStatement)conn.prepareStatement(sql);
                    if (ps!=null){
                        ps.setString(1,author);
                        result = ps.executeUpdate();
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        DButil.closeAll(conn,ps);
        return result;
    }

}
