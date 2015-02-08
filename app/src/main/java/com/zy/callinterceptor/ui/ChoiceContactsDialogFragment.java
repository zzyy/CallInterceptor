package com.zy.callinterceptor.ui;

import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.zy.callinterceptor.AppApplication;
import com.zy.callinterceptor.R;
import com.zy.callinterceptor.dao.InterceptorContactDao;
import com.zy.callinterceptor.pojo.ContactInfo;
import com.zy.callinterceptor.pojo.InterceptorContact;
import com.zy.callinterceptor.util.ContactsUtil;
import com.zy.callinterceptor.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Simon on 2015/1/30.
 */
public class ChoiceContactsDialogFragment extends BaseDialogFragment implements View.OnClickListener {
    private Context mContext;

    View rootView;
    LinearLayout contentView;
    Button cancelBtn;
    Button okBtn;

    List<ContactInfo> contactInfoList;

    ArrayList<InterceptorContact> selectedInterceptorContacts = new ArrayList<>();
    private int interceptorContactType;

    public ChoiceContactsDialogFragment() {
        super();

        mContext = AppApplication.getContext();
        contactInfoList = ContactsUtil.getAllContacts(mContext);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Dialog);

        this.interceptorContactType = getArguments().getInt("type", 0);

//        getDialog().getWindow()
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_base_content, container, false);
        cancelBtn = (Button) rootView.findViewById(R.id.cancel);
        okBtn = (Button) rootView.findViewById(R.id.ok);
        contentView = (LinearLayout) rootView.findViewById(R.id.dialog_content);

        ListView listView = new ListView(mContext);
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setLayoutParams( new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        listView.setAdapter(new SimpleCursorAdapter(mContext,android.R.layout.simple_list_item_multiple_choice,
//                cursor, new String[]{ ContactsContract.Contacts.DISPLAY_NAME }, new int[]{android.R.id.text1} ));

        ContactInfoAdapt contactInfoAdapt = new ContactInfoAdapt(contactInfoList);
        listView.setAdapter( contactInfoAdapt);

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactInfo contactInfo = (ContactInfo) parent.getItemAtPosition(position);
                selectedInterceptorContacts.addAll(contactInfo.convertToInterceptorContacts(interceptorContactType));
            }
        });
        contentView.addView(listView);


        this.getDialog().setTitle(R.string.choice_contacts);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        cancelBtn.setOnClickListener(this);
        okBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                this.dismiss();
                break;
            case R.id.ok :
                if (!selectedInterceptorContacts.isEmpty()){
                    LogUtil.d("interceptorContactDao add to db");
                    InterceptorContactDao interceptorContactDao = AppApplication.getDaoSession().getInterceptorContactDao();
                    interceptorContactDao.insertInTx(selectedInterceptorContacts);
                }
                this.dismiss();
                break;
        }
    }

    static class ContactInfoAdapt extends ArrayAdapter<ContactInfo>{
        private LayoutInflater inflater;

        public ContactInfoAdapt( List<ContactInfo> data) {
            super(AppApplication.getContext(), android.R.layout.simple_list_item_multiple_choice);
            inflater = LayoutInflater.from(AppApplication.getContext());

            setData(data);
        }

        public void setData(List<ContactInfo> data){
            if (data != null) {
                this.clear();
                this.addAll(data);
            }
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null){
                convertView = inflater.inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.checkedTextView = (CheckedTextView) convertView.findViewById(android.R.id.text1);

                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            ContactInfo contactInfo = getItem(position);
            viewHolder.checkedTextView.setText(contactInfo.name);

            return convertView;
        }

        static class ViewHolder{
            CheckedTextView checkedTextView;
        }
    }

    //回掉函数
    public interface Listener{
        void onDismiss(DialogInterface dialog);
    }

}
