package com.example.mynovel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class collectionFragment extends Fragment implements View.OnClickListener{
    private static final int COMPLETED=0;
    private ListView collection_list;
    private Button refresh;
    private ArrayList<collection> collections;
    private ArrayList<collection> mycollections;
    private myAdapter myadapter;
    private String user;
    //记录当前列表位置
    private int current_position;

    public collectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.refresh_collection) {
            getCollections();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_collection,container,false);
        collection_list = (ListView) layout.findViewById(R.id.collection_listview);
        refresh = (Button)layout.findViewById(R.id.refresh_collection);
        collections = new ArrayList<collection>();
        mycollections = new ArrayList<collection>();
        //sharedPreference取出当前用户
        user = getCurrent_user();
        getCollections();
        refresh.setOnClickListener(this);
        return layout;
    }

    public String getCurrent_user(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("current",Context.MODE_PRIVATE);
        String current = sharedPreferences.getString("current_user",null);
        return current;
    }
    //处理插入数据库线程
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==COMPLETED) {
                getMycollection();
                if (myadapter==null) {
                    myadapter = new myAdapter(collectionFragment.this.getActivity());
                    collection_list.setAdapter(myadapter);
                }else {
                    myadapter.notifyDataSetChanged();
                }
                collection_list.setAdapter(myadapter);
            }
        }
    };
    //处理删除数据库线程
    private Handler handler_delete = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (myadapter==null){
                myadapter = new myAdapter(collectionFragment.this.getActivity());
                collection_list.setAdapter(myadapter);
            }else {
                mycollections.remove(current_position);
                myadapter.notifyDataSetChanged();
            }
            collection_list.setAdapter(myadapter);
        }
    };
    //获取所有收藏数据
    public void getCollections(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                collections = collectionImple.getInstance().getCollectionData();
                Message msg = new Message();
                msg.what = COMPLETED;
                handler.sendMessage(msg);
            }
        }).start();
    }

    //分析整合我的收藏小说
    public void getMycollection(){
        mycollections.clear();
        if(collections.size()!=0) {
            for (collection co : collections) {
                if (co.getUser().equals(user)) {
                    mycollections.add(co);
                }
            }
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
            if(mycollections.size()==0){
                return 0;
            }else {
                Log.d("error1", "getCount: "+String.valueOf(mycollections.size()));
                return mycollections.size();
            }
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            viewholder holder;
            if (convertView == null) {
                holder=new viewholder();

                convertView = mInflater.inflate(R.layout.listview_reader, null);
                holder.novelName_reader = (TextView)convertView.findViewById(R.id.novel_name);
                holder.authorName_reader = (TextView)convertView.findViewById(R.id.novel_author);
                holder.typeName_reader = (TextView)convertView.findViewById(R.id.novel_type);
                holder.startRead = (Button)convertView.findViewById(R.id.read_reader);
                holder.deleteCollection = (Button)convertView.findViewById(R.id.reader_collection);
                convertView.setTag(holder);
            }else {
                holder = (collectionFragment.viewholder)convertView.getTag();
            }

            holder.novelName_reader.setText("书名:"+mycollections.get(position).getName());
            holder.authorName_reader.setText("作者:"+mycollections.get(position).getAuthor());
            holder.typeName_reader.setText("类型:"+mycollections.get(position).getType());
            holder.deleteCollection.setText("取消收藏");

            holder.startRead.setTag(position);
            holder.startRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //添加URL跳转事件
                    Intent intent = new Intent();
                    //Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(mycollections.get(position).getUrl());
                    intent.setData(content_url);
                    startActivity(intent);
                }
            });
            holder.deleteCollection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            collectionImple.getInstance().deleteCollection(mycollections.get(position));
                            current_position = position;
                            Message msg = new Message();
                            msg.what = COMPLETED;
                            handler_delete.sendMessage(msg);
                        }
                    }).start();
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
        private Button deleteCollection;
    }
}
