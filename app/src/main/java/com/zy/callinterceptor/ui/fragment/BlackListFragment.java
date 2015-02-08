package com.zy.callinterceptor.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zy.callinterceptor.AppApplication;
import com.zy.callinterceptor.R;
import com.zy.callinterceptor.dao.InterceptorContactDao;
import com.zy.callinterceptor.pojo.InterceptorContact;
import com.zy.callinterceptor.ui.InterceptorContactAdapt;
import com.zy.callinterceptor.util.DBUtils;
import com.zy.callinterceptor.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlackListFragment extends InterceptorInfoFragment {

    private ListView listView;

    List<InterceptorContact> interceptorContactList;
    private InterceptorContactAdapt interceptorContactAdapt;
    private List<InterceptorContact> deleteList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        interceptorContactAdapt = new InterceptorContactAdapt();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_black_list, container, false);
        //
        listView = (ListView) viewRoot.findViewById(R.id.black_list_content);

        setupView();
        return viewRoot;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            showData();
        }else {
            if (mActionMode != null){
                mActionMode.finish();
            }
        }
    }

    private void showData(){
        InterceptorContactDao interceptorContactDao = AppApplication.getDaoSession().getInterceptorContactDao();
        QueryBuilder<InterceptorContact> qb = interceptorContactDao.queryBuilder();
        qb.where(InterceptorContactDao.Properties.Type.eq(InterceptorContact.Type.BLACK_LIST));
        interceptorContactList = qb.list();

        LogUtil.d("interceptorContactList -->black=" + interceptorContactList.size());

        interceptorContactAdapt.setData(interceptorContactList);
        listView.setAdapter(interceptorContactAdapt);
    }

    ActionMode mActionMode;
    void setupView(){
//        listView.setSelector(R.drawable.bg_interceptor_list);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mActionMode == null){
                    //正常流程
                }else {
                    //已经长按过 进入选择模式
//                    listView.setItemChecked(position, true);
                }
            }
        });
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            boolean isCheckedAll = true;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                updateTitle(mode);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mActionMode = mode;
                getActivity().getMenuInflater().inflate(R.menu.menu_multi_choice, menu);

                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_delete:
                        SparseBooleanArray checkedPositions = listView.getCheckedItemPositions();
                        int size = checkedPositions.size();
                        for (int i=0; i<size; i++){
                            if (checkedPositions.valueAt(i)){
                                deleteList.add(interceptorContactAdapt.getItem(checkedPositions.keyAt(i)));
                            }
                        }
                        DBUtils.deleteInterceptorContact(deleteList);
                        mode.finish();
                        return true;
                    case R.id.menu_select:
                        if (isCheckedAll){
                            item.setTitle("取消");
                            isCheckedAll = false;
                        }else {
                            item.setTitle("全选");
                            isCheckedAll = true;
                        }

                        int N = listView.getCount();
                        for (int i=0; i<N; i++){
                            listView.setItemChecked(i, !isCheckedAll);
                        }
                        updateTitle(mode);
                        return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mActionMode = null;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            void updateTitle(ActionMode actionMode){
                actionMode.setTitle("有" + listView.getCheckedItemCount() + "项被选中");
            }
        });
    }
}
