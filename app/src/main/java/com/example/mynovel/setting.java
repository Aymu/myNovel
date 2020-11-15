package com.example.mynovel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;


public class setting extends Fragment implements View.OnClickListener{
    private ArrayAdapter<String> settingAdapter;
    private ArrayList<String> settingitem;
    private static final String[] items = {"修改密码"};
    private ListView settingList;
    private Button btn_exit;

    public setting() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_setting,container,false);
        settingList = (ListView)layout.findViewById(R.id.setting_listview);
        btn_exit = (Button)layout.findViewById(R.id.exit_login);
        btn_exit.setOnClickListener(this);
        settingitem = new ArrayList<String>();
        for (int i=0;i<items.length;i++){
            settingitem.add(items[i]);
        }
        settingAdapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_list_item_1,items);
        settingList.setAdapter(settingAdapter);
        settingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //修改密码
                        Intent intent3 = new Intent(getActivity(),reader_modifypwd.class);
                        startActivity(intent3);
                        break;
                }
            }
        });
        return layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exit_login:
                Intent intent_exit = new Intent(this.getActivity(),MainActivity.class);
                startActivity(intent_exit);
        }
    }
}
