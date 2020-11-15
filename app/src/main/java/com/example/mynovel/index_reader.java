package com.example.mynovel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class index_reader extends AppCompatActivity {
    //    private ArrayList<novelAndAuthor> novelAndAuthors;
//    private ArrayList<novelInfo> novelInfos;
//    private ArrayList<novelAndAuthor> displayNovelAndAuthors;
//
//    private TextView titleName;
//    private EditText searchLog;
//    private ListView allNovel_list;
//    private myAdapter myadapter;
    //设置4个fragment
    private reader_read rrd;
    private setting sets;
    private messageFragment messagefragment;
    private collectionFragment collectionfragment;

    private Fragment isFragment;
    private String current_user;
    private Intent intent2;

    private boolean index_flag = true;

    private ArrayAdapter<String> settingAdapter;
    private ArrayList<String> settingitem;
    private static final String[] items = {"我的收藏", "留言反馈", "修改密码"};
    private ListView settingList;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_main:
                    if (rrd == null) {
                        rrd = new reader_read();
                    }
                    switchContent(isFragment, rrd);
                    return true;
                case R.id.navigation_setup:
                    if (sets == null) {
                        sets = new setting();
                    }
                    switchContent(isFragment, sets);
                    return true;
                case R.id.navigation_collection:
                    if (collectionfragment==null){
                        collectionfragment = new collectionFragment();
                    }
                    switchContent(isFragment,collectionfragment);
                    return true;
                case R.id.navigation_message:
                    if (messagefragment==null){
                        messagefragment = new messageFragment();
                    }
                    switchContent(isFragment,messagefragment);
                    return true;
            }
            return false;
        }
    };

    public void switchContent(Fragment from, Fragment to) {
        if (isFragment != to) {
            isFragment = to;
            FragmentManager fm = getSupportFragmentManager();
            //添加渐隐渐现的动画
            FragmentTransaction ft = fm.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                ft.hide(from).add(R.id.container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                ft.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    public void initFragment(Bundle savedInstanceState) {
        //判断activity是否重建，如果不是，则不需要重新建立fragment.
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (rrd == null) {
                rrd = new reader_read();
            }
            isFragment = rrd;
            ft.replace(R.id.container, rrd).commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_reader);
        intent2 = getIntent();
        //初始化两个数组数据
//        novelAndAuthors = new ArrayList<novelAndAuthor>();
//        novelInfos = new ArrayList<novelInfo>();
//        displayNovelAndAuthors = new ArrayList<novelAndAuthor>();
        initFragment(savedInstanceState);
        //初始化底部导航栏
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_reader);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        index_flag = getFlag();

    }

    public void saveFlag(boolean current_flag){
        SharedPreferences sharedPreferences =getSharedPreferences("flag",MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putBoolean("flag",current_flag);
        editor.commit();
    }
    public boolean getFlag(){
        boolean bg = true;
        SharedPreferences sharedPreferences =getSharedPreferences("flag",MODE_PRIVATE);
        bg = sharedPreferences.getBoolean("flag",true);
        return bg;
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveFlag(index_flag);
    }
}

        //初始化所有控件
//        titleName = (TextView) findViewById(R.id.title);
//        searchLog = (EditText)findViewById(R.id.info_search);
//        allNovel_list = (ListView)findViewById(R.id.novel_user);


//        dataConn();

//    public void search(View view){
//        //清空显示列表
//        if (displayNovelAndAuthors.size()!=0){
//            displayNovelAndAuthors.clear();
//        }
//        //查询特定小说
//        String keyword = searchLog.getText().toString();
//        //先从小说书名开始检索
//        for (novelAndAuthor naal:novelAndAuthors){
//            if (naal.getNovel_name().contains(keyword)){
//                displayNovelAndAuthors.add(naal);
//            }else if (naal.getAuthor_name().contains(keyword)){
//                displayNovelAndAuthors.add(naal);
//            }else if (naal.getNovel_type().contains(keyword)){
//                displayNovelAndAuthors.add(naal);
//            }
//        }
//        myadapter = new myAdapter(index_reader.this);
//        allNovel_list.setAdapter(myadapter);
//    }
//
//    public void dataConn(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                novelAndAuthors=novelAndAuthorImple.getDbserver().getNoveldata();
//                novelInfos = novelInfoImple.getDB().getData("novelinfo");
//            }
//        }).start();
//    }
//    public void displayAll(View view){
//        //查询所有小说
//        for(novelAndAuthor naa:novelAndAuthors){
//            displayNovelAndAuthors.add(naa);
//        }
//        myadapter = new myAdapter(index_reader.this);
//        allNovel_list.setAdapter(myadapter);
//
//    }
//
//
//    public class myAdapter extends BaseAdapter
//    {
//
//        private LayoutInflater mInflater;
//        public myAdapter(Context context){
//            this.mInflater = LayoutInflater.from(context);
//        }
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public int getCount() {
//            if(displayNovelAndAuthors.size()==0){
//                return 0;
//            }else {
//                return displayNovelAndAuthors.size();
//            }
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            index_reader.viewholder holder;
//            if (convertView == null) {
//                holder=new viewholder();
//
//                convertView = mInflater.inflate(R.layout.listview_reader, null);
//                holder.novelName_reader = (TextView)convertView.findViewById(R.id.novel_name);
//                holder.authorName_reader = (TextView)convertView.findViewById(R.id.novel_author);
//                holder.typeName_reader = (TextView)convertView.findViewById(R.id.novel_type);
//                holder.startRead = (Button)convertView.findViewById(R.id.read_reader);
//                convertView.setTag(holder);
//            }else {
//                holder = (viewholder)convertView.getTag();
//            }
//
//            holder.novelName_reader.setText("书名:"+displayNovelAndAuthors.get(position).getNovel_name());
//            holder.authorName_reader.setText("作者:"+displayNovelAndAuthors.get(position).getAuthor_name());
//            holder.typeName_reader.setText("类型:"+displayNovelAndAuthors.get(position).getNovel_type());
//
//            holder.startRead.setTag(position);
//            holder.startRead.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //添加URL跳转事件
//                    Intent intent = new Intent();
//                    //Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//                    intent.setAction("android.intent.action.VIEW");
//                    Uri content_url = Uri.parse(novelInfos.get(position).getUrl());
//                    intent.setData(content_url);
//                    startActivity(intent);
//                }
//            });
//            return convertView;
//        }
//    }
//    public final class viewholder{
//        private TextView novelName_reader;
//        private TextView authorName_reader;
//        private TextView typeName_reader;
//        private Button startRead;
//    }
//    //setting设置功能
//    //退出登录
//    public void exit(View view){
//        Intent intent_exit = new Intent(index_reader.this,MainActivity.class);
//        startActivity(intent_exit);
//    }


