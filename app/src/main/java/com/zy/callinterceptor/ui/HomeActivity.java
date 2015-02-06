package com.zy.callinterceptor.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.astuetz.PagerSlidingTabStrip;
import com.zy.callinterceptor.R;
import com.zy.callinterceptor.pojo.InterceptorContact;
import com.zy.callinterceptor.ui.fragment.BlackListFragment;
import com.zy.callinterceptor.ui.fragment.WhiteListFragment;
import com.zy.callinterceptor.util.DBUtils;
import com.zy.callinterceptor.util.LogUtil;
import com.zy.callinterceptor.util.TelephoneUtil;

import java.lang.reflect.Field;

public class HomeActivity extends Activity {

    private ViewPager contentViewPager;
    private FragmentManager fragmentManager;
    private TelephoneUtil telephoneUtil;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;

        fragmentManager = getFragmentManager();

        contentViewPager = (ViewPager) findViewById(R.id.view_pager_content);
        FragmentPagerAdapter pagerAdapter = new HomeActivityFragmentAdapt(fragmentManager);
        contentViewPager.setAdapter(pagerAdapter);


        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(contentViewPager);
        tabs.setShouldExpand(true);
        tabs.setDividerColor(Color.TRANSPARENT);
        tabs.setIndicatorColor(Color.parseColor("#45c01a"));

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Switch toggle = (Switch) findViewById(R.id.toggle);
        toggle.setLayoutParams( new Toolbar.LayoutParams(Gravity.END));*/

        ActionBar actionBar = getActionBar();
        Switch toggle = new Switch(this);
        if (actionBar != null){
            setOverflowShowingAlways();
            actionBar.setCustomView(toggle, new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
            ((ActionBar.LayoutParams)toggle.getLayoutParams()).gravity = Gravity.END;
            actionBar.setDisplayShowCustomEnabled(true);
        }

        telephoneUtil = TelephoneUtil.getInstance();
        toggle.setChecked(telephoneUtil.isStartInterceptor());
        toggle.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    telephoneUtil.startCallInterceptor();
                }else {
                    telephoneUtil.endCallInterceptor();
                }
            }
        });

    }

    public void updateList(){
        ((HomeActivityFragmentAdapt) contentViewPager.getAdapter()).updateChild();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.add_white_list:
                showAddContactDialog(InterceptorContact.Type.WHITE_LIST);
                return true;
            case R.id.add_black_list:
                showAddContactDialog(InterceptorContact.Type.BLACK_LIST);
                return true;
            case R.id.clear_data:
                DBUtils.deleteAllData();
                updateList();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static class HomeActivityFragmentAdapt extends FragmentPagerAdapter{
        String[] titles = {"WhiteList", "BlackList"};

        public HomeActivityFragmentAdapt(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new WhiteListFragment();
                case 1:
                    return new BlackListFragment();
                case 2:
                    LogUtil.d("不应该啊");
            }
            return null;
        }

        public void updateChild(){
            ((WhiteListFragment)getItem(0)).update();
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    //设置不存在menu键, 始终显示overflow button
    private void setOverflowShowingAlways(){
        try{
            // true if a permanent menu key is present, false otherwise.
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //添加联系人 对话框
    private void showAddContactDialog(final int addType){
        (new DialogFragment(){
            @NonNull
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                return new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.choice_add_type)
                        .setItems(R.array.chioce_add_method, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LogUtil.d("chioce method >>> " + which);
                                switch (which){
                                    case 0:
                                        addNumber(addType);
                                        break;
                                    case 1:
                                        choiceGroup(addType);
                                        break;
                                    case 2:
                                        choiceContacts(addType);
                                        break;
                                }
                            }
                        })
                        .create();

            }
        }).show(getFragmentManager(), "choiceDialog");
    }

    void addNumber(int addType){
        new DialogFragment(){
            @NonNull
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                LogUtil.d("onCreateDialog; view=" + view);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                view = inflater.inflate(R.layout.add_num_view, null);

                return new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.add_num_dialog_title)
                        .setView(view)
                        .setPositiveButton(R.string.add_num_dialog_add, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(R.string.add_num_dialog_cancle, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
            }

            private View view;
        }.show(getFragmentManager(), "addNumber");
    }

    void choiceGroup(int addType){
        DialogFragment dialog = new ChoiceGroupDialogFragment();

        dialog.show(getFragmentManager(), "choiceGroup");
    }

    void choiceContacts(int addType){
        ChoiceContactsDialogFragment dialog = new ChoiceContactsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", addType);
        dialog.setArguments(bundle);
        dialog.setListener(new ChoiceContactsDialogFragment.Listener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                updateList();
            }
        });
        dialog.show(getFragmentManager(), "choiceContacts");
    }
}
