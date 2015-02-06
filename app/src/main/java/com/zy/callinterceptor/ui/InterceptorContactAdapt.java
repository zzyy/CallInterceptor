package com.zy.callinterceptor.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zy.callinterceptor.AppApplication;
import com.zy.callinterceptor.R;
import com.zy.callinterceptor.pojo.InterceptorContact;

import java.util.List;

/**
 * Created by Simon on 2015/2/4.
 */
public class InterceptorContactAdapt extends ArrayAdapter<InterceptorContact>{
    private LayoutInflater inflater;

    public InterceptorContactAdapt() {
        super(AppApplication.getContext(), android.R.layout.simple_list_item_multiple_choice);

        inflater = LayoutInflater.from(AppApplication.getContext());
    }

    public void setData(List<InterceptorContact> data){
        if (data != null){
            clear();
            addAll(data);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if ( convertView == null){
            convertView = inflater.inflate(R.layout.item_interceptor_contact, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.display_name);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        InterceptorContact interceptorContact = getItem(position);
        viewHolder.textView.setText(interceptorContact.getName());

        /*if (position % 2 == 0)
        {
            convertView.setBackgroundResource(R.drawable.bg_item_interceptor_even);
        }else {
            convertView.setBackgroundResource(R.drawable.bg_item_interceptor_odd);
        }*/

        return convertView;
    }

    static class ViewHolder{
        TextView textView;
    }
}
