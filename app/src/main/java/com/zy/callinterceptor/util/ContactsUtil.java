package com.zy.callinterceptor.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;

import com.zy.callinterceptor.AppApplication;
import com.zy.callinterceptor.pojo.ContactInfo;
import com.zy.callinterceptor.pojo.GroupInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 2015/1/23.
 */
public class ContactsUtil {
    private static Context mContext = AppApplication.getContext();
    private static ContentResolver contentResolver = AppApplication.getContext().getContentResolver();

    private static Uri groupContentUri = ContactsContract.Groups.CONTENT_URI;
    private static String groupIdColumn = ContactsContract.Groups._ID;
    private static String groupNameColumn = ContactsContract.Groups.TITLE;

    private ContactsUtil() {

    }


    /**
     * used to get group info, include the id and the name;
     * @param context
     * @return
     */
    public static List<GroupInfo> getAllGroupInfo(Context context){
        final ArrayList<GroupInfo> groupEntityList = new ArrayList<>();

        Cursor cursor = null;
        ContentResolver contentResolver = context.getContentResolver();
        cursor = contentResolver.query(groupContentUri, null, null, null, null);

        while ( cursor.moveToNext()){
            final GroupInfo groupEntity = new GroupInfo();
            groupEntity.id = cursor.getInt( cursor.getColumnIndex(groupIdColumn) );
            groupEntity.name = cursor.getString( cursor.getColumnIndex( groupNameColumn));
            groupEntityList.add(groupEntity);

            LogUtil.d(groupEntity.toString());
        }
        cursor.close();

        return groupEntityList;
    }


    /**
     * 获取所有联系人信息
     * @param context
     * @return
     */
    public static List<ContactInfo> getAllContacts(Context context){
        final ArrayList<ContactInfo> result = new ArrayList<>();
        final ContentResolver contentResolver = context.getContentResolver();

        final String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };

        Cursor contactsCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, projection,
                null, null, null);

        //获取所有联系人
        while (contactsCursor.moveToNext()){
            boolean isHasNum = "1".equals( contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) );
            if (isHasNum){
                ContactInfo contactInfo = new ContactInfo();
                String contactId = contactsCursor.getString( contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
                String diaplayName = contactsCursor.getString( contactsCursor.getColumnIndex( ContactsContract.Contacts.DISPLAY_NAME) );
                contactInfo.contactId = contactId;
                contactInfo.name = diaplayName;
                //获取联系人号码
                Cursor phoneNumberCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?" ,
                        new String[]{contactId} , null);
                while (phoneNumberCursor.moveToNext()){
                    String phoneNum = phoneNumberCursor.getString( phoneNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactInfo.phoneNumberList.add(phoneNum);
                }
                phoneNumberCursor.close();

//                LogUtil.d("getAllContacts >> " +  contactInfo);
                result.add(contactInfo);
            }
        }
        contactsCursor.close();

        return result;
    }

    /**
     * 查看 phoneNum 是否在通讯录中存在
     * @param phoneNum
     * @return if exist,return true; otherwise return false;
     */
    public static boolean isInContacts(String phoneNum) {
        Cursor phoneNumCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                    null, null, null);
        try{
            while (phoneNumCursor.moveToNext()){
                String num = phoneNumCursor.getString( phoneNumCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if (PhoneNumberUtils.compare(phoneNum, num)){
                    return true;
                }
            }
        }finally {
            phoneNumCursor.close();
        }
        return false;
    }

    /**
     *
     * @param phoneNumber
     */
    public static void queryGroupByPhoneNumber(Context context, String phoneNumber){

        ContentResolver contentResolver = context.getContentResolver();

        Cursor c = null;

        c = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        int raw_contact_id = 0;
        while (c.moveToNext()){
            raw_contact_id = c.getInt( c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
            String display_name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            if (PhoneNumberUtils.compare(number, phoneNumber)){
                LogUtil.d("raw_contact_id=" + raw_contact_id + "; display_name=" + display_name +"; number=" + number);
                break;
            }
        }
        c.close();

        c = contentResolver.query(ContactsContract.Data.CONTENT_URI, null,
                ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE +"='" + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'",
                new String[]{String.valueOf(raw_contact_id)}, null);

        while (c.moveToNext()){
            int groupId = c.getInt(c.getColumnIndex(ContactsContract.Data.DATA1));
            LogUtil.d("groupId=" + groupId);
        }
        c.close();

    }


}
