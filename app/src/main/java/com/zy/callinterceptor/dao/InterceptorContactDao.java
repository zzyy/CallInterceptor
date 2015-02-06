package com.zy.callinterceptor.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.zy.callinterceptor.pojo.InterceptorContact;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table INTERCEPTOR_CONTACT.
*/
public class InterceptorContactDao extends AbstractDao<InterceptorContact, Long> {

    public static final String TABLENAME = "INTERCEPTOR_CONTACT";

    /**
     * Properties of entity InterceptorContact.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property PhoneNumber = new Property(2, String.class, "phoneNumber", false, "PHONE_NUMBER");
        public final static Property GroupId = new Property(3, Integer.class, "groupId", false, "GROUP_ID");
        public final static Property Type = new Property(4, Integer.class, "type", false, "TYPE");
        public final static Property TimeRef = new Property(5, Integer.class, "timeRef", false, "TIME_REF");
    };


    public InterceptorContactDao(DaoConfig config) {
        super(config);
    }
    
    public InterceptorContactDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'INTERCEPTOR_CONTACT' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'NAME' TEXT," + // 1: name
                "'PHONE_NUMBER' TEXT," + // 2: phoneNumber
                "'GROUP_ID' INTEGER," + // 3: groupId
                "'TYPE' INTEGER," + // 4: type
                "'TIME_REF' INTEGER);"); // 5: timeRef
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'INTERCEPTOR_CONTACT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, InterceptorContact entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String phoneNumber = entity.getPhoneNumber();
        if (phoneNumber != null) {
            stmt.bindString(3, phoneNumber);
        }
 
        Integer groupId = entity.getGroupId();
        if (groupId != null) {
            stmt.bindLong(4, groupId);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(5, type);
        }
 
        Integer timeRef = entity.getTimeRef();
        if (timeRef != null) {
            stmt.bindLong(6, timeRef);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public InterceptorContact readEntity(Cursor cursor, int offset) {
        InterceptorContact entity = new InterceptorContact( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // phoneNumber
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // groupId
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // type
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5) // timeRef
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, InterceptorContact entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPhoneNumber(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setGroupId(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setType(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setTimeRef(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(InterceptorContact entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(InterceptorContact entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}