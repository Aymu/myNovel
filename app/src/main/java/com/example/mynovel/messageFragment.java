package com.example.mynovel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class messageFragment extends Fragment implements View.OnClickListener{
    private static final int COMPLETED=0;
    private int position;
    //记录当前用户和留言
    private String current_user;
    private String current_message;
    //插入数据库使用
    private userMessage um;
    //用于获取数据库所有留言
    private ArrayList<userMessage> userMessages;
    //我的留言
    private ArrayList<userMessage>messages;
    //适配器用于消息列表当中
    private myAdapter myadapter;
    //所有组件
    private EditText message;
    private Button commit_message;
    private Button click_myMessage;
    private ListView message_list;
    private TextView textView;
    public messageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.commit_mes:
                um = new userMessage();
                current_message = message.getText().toString();
                um.setUsername(current_user);
                um.setMessage(current_message);
                um.setTouser("");
                insertMessage(um);
                break;
            case R.id.click_message:
                getMessage();
                break;
            case R.id.all:
                Intent society = new Intent(getActivity(),society_message.class);
                startActivity(society);
                break;
        }
    }
    public void insertMessage(final userMessage us){
        new Thread(new Runnable() {
            @Override
            public void run() {
                messageImple.getMessageImple().insertMessage(us);
                Message msg = new Message();
                msg.what = COMPLETED;
                handler_insert.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_message,container,false);
        message = (EditText)layout.findViewById(R.id.reader_message);
        commit_message = (Button)layout.findViewById(R.id.commit_mes);
        click_myMessage = (Button)layout.findViewById(R.id.click_message);
        message_list = (ListView)layout.findViewById(R.id.message_listview);
        textView = (TextView) layout.findViewById(R.id.all);
        textView.setOnClickListener(this);
        //利用sharedPreferences取出当前用户
        current_user = getCurrent_user();
        userMessages = new ArrayList<userMessage>();
        //获取数据库所有留言信息
        messages = new ArrayList<userMessage>();
        commit_message.setOnClickListener(this);
        click_myMessage.setOnClickListener(this);
        return layout;
    }
    public String getCurrent_user(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("current",Context.MODE_PRIVATE);
        String current = sharedPreferences.getString("current_user",null);
        return current;
    }
    //从数据库中获取所有留言
    public void getMessage(){
        new Thread(new Runnable() {
            @Override
            public  void run() {
                userMessages = messageImple.getMessageImple().getUserMessagedata();
                Message msg = new Message();
                msg.what = COMPLETED;
                handler_refresh.sendMessage(msg);
            }
        }).start();

    }
    //筛选出我的留言
    public ArrayList<userMessage> myMessage(){
        ArrayList<userMessage> allMessages = new ArrayList<userMessage>();
        for (userMessage ums:userMessages){
            if (ums.getUsername().equals(current_user)){
                allMessages.add(ums);
            }
        }
        return  allMessages;
    }
    private void showAlterDialog(final int pos) {
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(messageFragment.this.getActivity());
        alterDiaglog.setIcon(R.drawable.warning);//图标
        alterDiaglog.setTitle("Warning");//文字
        alterDiaglog.setMessage("确定删除该条留言吗?");//提示消息
        //积极的选择
        alterDiaglog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                position = pos;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int result= messageImple.getMessageImple().deleteMessage(current_user,messages.get(pos).getMessage());
                        Message msg = new Message();
                        msg.what = COMPLETED;
                        handler.sendMessage(msg);
                    }
                }).start();


            }
        });
        //中立的选择
        alterDiaglog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        //显示
        alterDiaglog.show();
    }
    //更新UI处理
    private Handler handler_insert = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==COMPLETED){
                userMessage um = new userMessage();
                um.setUsername(current_user);
                um.setMessage(current_message);
                messages.add(um);
                for (userMessage rt:messages){
                    Log.d("error5", "handleMessage: "+um.getTouser());
                }
                message.setText("");
                if (myadapter==null){
                    myadapter = new myAdapter(messageFragment.this.getContext());
                    message_list.setAdapter(myadapter);
                }else {
                    myadapter.notifyDataSetChanged();
                }
            }

        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==COMPLETED){
                messages.remove(messages.get(position));
                myadapter.notifyDataSetChanged();
            }
        }
    };
    private Handler handler_refresh = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==COMPLETED){
                messages = myMessage();
                if (messages.size()!=0){
                    if (myadapter==null) {
                        myadapter = new myAdapter(messageFragment.this.getContext());
                        message_list.setAdapter(myadapter);
                    }else {
                        myadapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };
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
            if(messages.size()==0){
                return 0;
            }else {
                return messages.size();
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

                convertView = mInflater.inflate(R.layout.mymessagelistview, null);
                holder.novelName_reader = (TextView)convertView.findViewById(R.id.mymessage_user);
                holder.novelName_reader.setTextColor(Color.rgb(0,0,255));
                holder.authorName_reader = (TextView)convertView.findViewById(R.id.mymessage_content);
                holder.authorName_reader.setTextSize(17);
                holder.startRead = (Button)convertView.findViewById(R.id.memessage_delete);
                convertView.setTag(holder);
            }else {
                holder = (viewholder)convertView.getTag();
            }
            holder.novelName_reader.setText(current_user);

            holder.authorName_reader.setText(messages.get(position).getMessage());

            holder.startRead.setTag(position);
            holder.startRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //添加删除事件,显示dialog
                    showAlterDialog(position);

                }
            });
            return convertView;
        }
    }
    public final class viewholder{
        private TextView novelName_reader;
        private TextView authorName_reader;
        private Button startRead;
    }

}
