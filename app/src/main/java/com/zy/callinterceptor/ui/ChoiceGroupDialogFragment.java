package com.zy.callinterceptor.ui;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.zy.callinterceptor.AppApplication;
import com.zy.callinterceptor.R;
import com.zy.callinterceptor.pojo.GroupInfo;
import com.zy.callinterceptor.pojo.InterceptorContact;
import com.zy.callinterceptor.util.ContactsUtil;
import com.zy.callinterceptor.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Simon on 2015/1/30.
 */
public class ChoiceGroupDialogFragment extends BaseDialogFragment implements View.OnClickListener {
    private Context mContext;

    View rootView;
    LinearLayout contentView;
    Button cancelBtn;
    Button confirmBtn;

    private boolean isNeedDismiss;
    private int interceptorContactType;

    List<GroupInfo> groupList;
    List<Map<String, String>> data;

    private ArrayList<GroupInfo> addToDB = new ArrayList<>();

    public ChoiceGroupDialogFragment() {
        super();
        mContext = AppApplication.getContext();
        groupList = ContactsUtil.getAllGroupInfo(mContext);
        data = new ArrayList<>();
        for (GroupInfo groupEntity : groupList){
            HashMap<String, String> map = new HashMap<>();
            map.put("key", groupEntity.name);
            data.add(map);
        }

        if (data.isEmpty()){
            isNeedDismiss = true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //設置主題
        this.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Dialog);

//        getDialog().getWindow()
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_base_content, container, false);
        cancelBtn = (Button) rootView.findViewById(R.id.cancel);
        confirmBtn = (Button) rootView.findViewById(R.id.ok);
        contentView = (LinearLayout) rootView.findViewById(R.id.dialog_content);

        ListView listView = new ListView(mContext);
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setLayoutParams( new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        listView.setAdapter(new SimpleAdapter(mContext, data, android.R.layout.simple_list_item_multiple_choice,
                new String[]{"key"}, new int[]{android.R.id.text1} ));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                addToDB.add(groupList.get(position));
            }
        });

        contentView.addView(listView);


        //设置dialog标题
        this.getDialog().setTitle(R.string.choice_group);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (isNeedDismiss){
            this.dismiss();
            Toast.makeText(mContext, R.string.no_group_tips, Toast.LENGTH_SHORT).show();
        }

        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                this.dismiss();
                break;
            case R.id.ok :
                if (!addToDB.isEmpty()){
                    for ( GroupInfo group: addToDB){
                        InterceptorContact interceptorContact = new InterceptorContact();
                        interceptorContact.setGroupId(group.id);
                        LogUtil.d("which group will add to db: "+ group);
                    }
                }
                break;
        }
    }
}
