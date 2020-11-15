package com.example.mynovel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class reader_read extends Fragment implements View.OnClickListener {
    private static final int COMPLETED=0;
    private ArrayList<novelAndAuthor> novelAndAuthors;
    private ArrayList<novelInfo> novelInfos;
    private ArrayList<novelAndAuthor> displayNovelAndAuthors;
    private ArrayList<novelInfo> displayNovelInfos;

    //获取现有的收藏数据
    private ArrayList<collection> current_collections;

    private collection mycollection;
    private String current_user;
    private TextView titleName;
    private EditText searchLog;
    private ListView allNovel_list;
    private myAdapter myadapter;
    private Button searchall;
    private Button searchUni;
    public reader_read() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //初始化两个数组数据
        View layout =inflater.inflate(R.layout.fragment_reader_read,container,false);
        novelAndAuthors = new ArrayList<novelAndAuthor>();
        novelInfos = new ArrayList<novelInfo>();
        displayNovelAndAuthors = new ArrayList<novelAndAuthor>();
        displayNovelInfos = new ArrayList<novelInfo>();
        current_collections = new ArrayList<collection>();
        //初始化所有控件
        titleName = (TextView) layout.findViewById(R.id.title);
        searchLog = (EditText)layout.findViewById(R.id.info_search);
        allNovel_list = (ListView)layout.findViewById(R.id.novel_user);
        searchall = (Button)layout.findViewById(R.id.btn_all);
        searchUni = (Button)layout.findViewById(R.id.btn_search);
        searchUni.setOnClickListener(this);
        searchall.setOnClickListener(this);
        getCurrentuser();
        return layout;
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //判断小说是否重复
            if (msg.what==COMPLETED){
                ArrayList<collection> myCurrentCollelctions = new ArrayList<collection>();
                boolean isRepeat=false;
                for (collection co:current_collections){
                    if (co.getUser().equals(current_user)){
                        myCurrentCollelctions.add(co);
                    }
                }
                for (collection co:myCurrentCollelctions){
                    if (mycollection.getUser().equals(co.getUser()) && mycollection.getName().equals(co.getName())){
                        isRepeat = true;
                    }
                }
                if (isRepeat==false) {
                    Log.d("error3", "handleMessage: ");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            collectionImple.getInstance().insertCollection(mycollection);
                        }
                    }).start();
                    Toast.makeText(getContext(),"收藏成功",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getContext(),"此小说已收藏",Toast.LENGTH_LONG).show();
                }
            }
        }
    };
    public void getCurrentuser(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("current",MODE_PRIVATE);
        this.current_user = sharedPreferences.getString("current_user",null);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataConn();
    }
    public void dataConn(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                novelAndAuthors=novelAndAuthorImple.getDbserver().getNoveldata();
                novelInfos = novelInfoImple.getDB().getData("novelinfo");
            }
        }).start();
    }
    public void getCurrentCollection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                current_collections = collectionImple.getInstance().getCollectionData();
                //向handle发送消息并处理
                Message msg = new Message();
                msg.what=COMPLETED;
                handler.sendMessage(msg);
            }
        }).start();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_all:
                //查询所有小说
                for(novelAndAuthor naa:novelAndAuthors){
                    displayNovelAndAuthors.add(naa);
                }
                for (novelInfo nif:novelInfos){
                    displayNovelInfos.add(nif);
                }
                myadapter = new myAdapter(this.getContext());
                allNovel_list.setAdapter(myadapter);
                break;
            case R.id.btn_search:
                //清空显示列表
                if (displayNovelAndAuthors.size()!=0){
                    displayNovelAndAuthors.clear();
                }
                if (displayNovelInfos.size()!=0){
                    displayNovelInfos.clear();
                }
                //查询特定小说
                String keyword = searchLog.getText().toString();
                //先从小说书名开始检索
                for (novelAndAuthor naal:novelAndAuthors){
                    if (naal.getNovel_name().contains(keyword)){
                        displayNovelAndAuthors.add(naal);
                        //添加对应的网址
                        for (novelInfo nif:novelInfos){
                            if (nif.getName().equals(naal.getNovel_name())){
                                displayNovelInfos.add(nif);
                            }
                        }
                    }else if (naal.getAuthor_name().contains(keyword)){
                        displayNovelAndAuthors.add(naal);
                        //添加对应的网址
                        for (novelInfo nif:novelInfos){
                            if (nif.getName().equals(naal.getNovel_name())){
                                displayNovelInfos.add(nif);
                            }
                        }
                    }else if (naal.getNovel_type().contains(keyword)){
                        displayNovelAndAuthors.add(naal);
                        //添加对应的网址
                        for (novelInfo nif:novelInfos){
                            if (nif.getName().equals(naal.getNovel_name())){
                                displayNovelInfos.add(nif);
                            }
                        }
                    }
                }
                if (displayNovelAndAuthors.size()==0){
                    Toast.makeText(getActivity(),"搜索内容不存在",Toast.LENGTH_LONG).show();
                }
                myadapter = new myAdapter(this.getContext());
                allNovel_list.setAdapter(myadapter);
                break;
        }
    }
    public class myAdapter extends BaseAdapter
    {

        private LayoutInflater mInflater;
        public myAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            if(displayNovelAndAuthors.size()==0){
                return 0;
            }else {
                return displayNovelAndAuthors.size();
            }
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            reader_read.viewholder holder;
            if (convertView == null) {
                holder=new viewholder();

                convertView = mInflater.inflate(R.layout.listview_reader, null);
                holder.novelName_reader = (TextView)convertView.findViewById(R.id.novel_name);
                holder.authorName_reader = (TextView)convertView.findViewById(R.id.novel_author);
                holder.typeName_reader = (TextView)convertView.findViewById(R.id.novel_type);
                holder.startRead = (Button)convertView.findViewById(R.id.read_reader);
                holder.addCollection = (Button)convertView.findViewById(R.id.reader_collection);
                convertView.setTag(holder);
            }else {
                holder = (reader_read.viewholder)convertView.getTag();
            }

            holder.novelName_reader.setText("书名:"+displayNovelAndAuthors.get(position).getNovel_name());
            holder.authorName_reader.setText("作者:"+displayNovelAndAuthors.get(position).getAuthor_name());
            holder.typeName_reader.setText("类型:"+displayNovelAndAuthors.get(position).getNovel_type());

            holder.startRead.setTag(position);
            holder.startRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //添加URL跳转事件
                    Intent intent = new Intent();
                    //Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(displayNovelInfos.get(position).getUrl());
                    intent.setData(content_url);
                    startActivity(intent);
                }
            });
            holder.addCollection.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    mycollection = new collection();
                    mycollection.setUser(current_user);
                    mycollection.setName(displayNovelAndAuthors.get(position).getNovel_name());
                    mycollection.setAuthor(displayNovelAndAuthors.get(position).getAuthor_name());
                    mycollection.setUrl(displayNovelInfos.get(position).getUrl());
                    mycollection.setType(displayNovelAndAuthors.get(position).getNovel_type());
                    //判断是否重复收藏
                    getCurrentCollection();


                }
            });
            return convertView;
        }
    }
    public final class viewholder{
        private TextView novelName_reader;
        private TextView authorName_reader;
        private TextView typeName_reader;
        private Button startRead;
        private Button addCollection;
    }
}
