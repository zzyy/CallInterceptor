package com.zy.callinterceptor.pojo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.zy.callinterceptor.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 2015/2/2.
 */
public class ContactInfo {
    public String contactId;
    public String name;
    public ArrayList<String> phoneNumberList = new ArrayList<>();

    public ArrayList<String> getPhoneNumberList(){
        return null;
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                "contactId='" + contactId + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumberList=" + phoneNumberList +
                '}';
    }

    public ArrayList<InterceptorContact> convertToInterceptorContacts(int interceptorContactType){
        final ArrayList<InterceptorContact> result = new ArrayList<>();
        for (String num : phoneNumberList){
            InterceptorContact interceptorContact = new InterceptorContact();
            interceptorContact.phoneNumber = num;
            interceptorContact.setName(name);
            switch (interceptorContactType){
                case InterceptorContact.Type.BLACK_LIST:
                    interceptorContact.setType(InterceptorContact.Type.BLACK_LIST);
                    break;
                case InterceptorContact.Type.WHITE_LIST:
                    interceptorContact.setType(InterceptorContact.Type.WHITE_LIST);
                    break;
            }
            result.add(interceptorContact);
        }
        return result;
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

                LogUtil.d("getAllContacts >> " + contactInfo);
                result.add(contactInfo);
            }
        }
        contactsCursor.close();

        return result;
    }
}
