package com.example.mynovel;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class index_admin extends AppCompatActivity {

    private TextView mTextMessage;
    private ListView listView;
    private ArrayList<novelAndAuthor>novelAndAuthors;
    private ArrayList<User> users;
    private ArrayList<novelInfo> novelInfos;

    private static final String url = "jdbc:mysql://10.30.20.32:3306/mynovel";
    private static final String userss = "root";//用户名
    private static final String password = "15279909969";
    private Connection connection;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText("书籍管理");
                    myAdapter adapter = new myAdapter(index_admin.this);
                    listView.setAdapter(adapter);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText("用户管理");
                    userAdapter adapter1 = new userAdapter(index_admin.this);
                    listView.setAdapter(adapter1);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_admin);
        //初始化数据队列
        novelAndAuthors = new ArrayList<novelAndAuthor>();
        users = new ArrayList<User>();
        novelInfos = new ArrayList<novelInfo>();
        //更新列表数据
        listView = (ListView)findViewById(R.id.novel_list);
        //初始化底部导航栏
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        novelAndAuthors = getNovelAndAuthors();
        users = getUsers();
        novelInfos = getNovelInfos();
    }
    //获取作品和小说信息
    public ArrayList<novelAndAuthor> getNovelAndAuthors(){
        ArrayList<novelAndAuthor> naas=  new ArrayList<novelAndAuthor>();
        //从数据库获取数据操作
        naas = novelAndAuthorImple.getDbserver().getNoveldata();
        return naas;
    }
    //获取小说url信息
    public ArrayList<novelInfo> getNovelInfos(){
        ArrayList<novelInfo> nis = new ArrayList<novelInfo>();
        //从数据库获取小说url信息
        nis = novelInfoImple.getDB().getData("novelurl");
        return nis;
    }
    //获取用户信息
    public ArrayList<User> getUsers(){
        ArrayList<User> us = new ArrayList<User>();
        //从数据库获取用户信息
        us = readerImple.getDBserver().getUserdata();
        return us;
    }
    //重写一个新的用户适配器
    public class userAdapter extends BaseAdapter{
        private LayoutInflater uInflater;
        public userAdapter(Context context){
            this.uInflater = LayoutInflater.from(context);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder=new ViewHolder();

                convertView = uInflater.inflate(R.layout.listview, null);
                holder.novelname = (TextView)convertView.findViewById(R.id.name_novel);
                holder.authorname = (TextView)convertView.findViewById(R.id.name_author);
                holder.read = (Button)convertView.findViewById(R.id.btn_read);
                holder.update = (Button)convertView.findViewById(R.id.btn_update);
                holder.delete = (Button)convertView.findViewById(R.id.btn_delete);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.novelname.setText(users.get(position).getName());
            holder.authorname.setText(users.get(position).getPwd());
            holder.read.setTag(position);
            holder.update.setTag(position);
            holder.delete.setTag(position);
            //给Button添加单击事件 添加Button之后ListView将失去焦点 需要的直接把Button的焦点去掉
            //点击阅读跳转事件
            holder.read.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            //更新Item事件
            holder.update.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                }
            });
            //删除Item事件
            holder.delete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                }
            });
            return convertView;
        }
    }
    //重写一个新的小说适配器
    public class myAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        public myAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public int getCount() {
            return novelAndAuthors.size();
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
          ViewHolder holder;
           if (convertView == null) {
                holder=new ViewHolder();

                convertView = mInflater.inflate(R.layout.listview, null);
                holder.novelname = (TextView)convertView.findViewById(R.id.name_novel);
                holder.authorname = (TextView)convertView.findViewById(R.id.name_author);
                holder.read = (Button)convertView.findViewById(R.id.btn_read);
                holder.update = (Button)convertView.findViewById(R.id.btn_update);
                holder.delete = (Button)convertView.findViewById(R.id.btn_delete);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.novelname.setText("书名："+novelAndAuthors.get(position).getNovel_name());
            holder.authorname.setText("作者:"+novelAndAuthors.get(position).getAuthor_name());
            holder.read.setTag(position);
            holder.update.setTag(position);
            holder.delete.setTag(position);
            //给Button添加单击事件 添加Button之后ListView将失去焦点 需要的直接把Button的焦点去掉
            //点击阅读跳转事件
            holder.read.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            //更新Item事件
            holder.update.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                }
            });
            //删除Item事件
            holder.delete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                }
            });
            return convertView;
        }
    }
    public final class ViewHolder{
        public TextView novelname;
        public TextView authorname;
        public Button read;
        public Button update;
        public Button delete;
    }
}


