package com.baishan.nearshop.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.baishan.nearshop.model.PushValue;

import com.baishan.nearshop.model.PushParser;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PUSH_PARSER".
*/
public class PushParserDao extends AbstractDao<PushParser, Long> {

    public static final String TABLENAME = "PUSH_PARSER";

    /**
     * Properties of entity PushParser.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PushType = new Property(1, String.class, "PushType", false, "PUSH_TYPE");
        public final static Property ValueId = new Property(2, long.class, "valueId", false, "VALUE_ID");
        public final static Property PushTime = new Property(3, String.class, "PushTime", false, "PUSH_TIME");
        public final static Property PushTitle = new Property(4, String.class, "PushTitle", false, "PUSH_TITLE");
        public final static Property PushContent = new Property(5, String.class, "PushContent", false, "PUSH_CONTENT");
        public final static Property LoginToken = new Property(6, String.class, "loginToken", false, "LOGIN_TOKEN");
        public final static Property IsRead = new Property(7, boolean.class, "isRead", false, "IS_READ");
        public final static Property Type = new Property(8, int.class, "type", false, "TYPE");
    }

    private DaoSession daoSession;


    public PushParserDao(DaoConfig config) {
        super(config);
    }
    
    public PushParserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PUSH_PARSER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"PUSH_TYPE\" TEXT," + // 1: PushType
                "\"VALUE_ID\" INTEGER NOT NULL ," + // 2: valueId
                "\"PUSH_TIME\" TEXT," + // 3: PushTime
                "\"PUSH_TITLE\" TEXT," + // 4: PushTitle
                "\"PUSH_CONTENT\" TEXT," + // 5: PushContent
                "\"LOGIN_TOKEN\" TEXT," + // 6: loginToken
                "\"IS_READ\" INTEGER NOT NULL ," + // 7: isRead
                "\"TYPE\" INTEGER NOT NULL );"); // 8: type
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PUSH_PARSER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PushParser entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String PushType = entity.getPushType();
        if (PushType != null) {
            stmt.bindString(2, PushType);
        }
        stmt.bindLong(3, entity.getValueId());
 
        String PushTime = entity.getPushTime();
        if (PushTime != null) {
            stmt.bindString(4, PushTime);
        }
 
        String PushTitle = entity.getPushTitle();
        if (PushTitle != null) {
            stmt.bindString(5, PushTitle);
        }
 
        String PushContent = entity.getPushContent();
        if (PushContent != null) {
            stmt.bindString(6, PushContent);
        }
 
        String loginToken = entity.getLoginToken();
        if (loginToken != null) {
            stmt.bindString(7, loginToken);
        }
        stmt.bindLong(8, entity.getIsRead() ? 1L: 0L);
        stmt.bindLong(9, entity.getType());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PushParser entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String PushType = entity.getPushType();
        if (PushType != null) {
            stmt.bindString(2, PushType);
        }
        stmt.bindLong(3, entity.getValueId());
 
        String PushTime = entity.getPushTime();
        if (PushTime != null) {
            stmt.bindString(4, PushTime);
        }
 
        String PushTitle = entity.getPushTitle();
        if (PushTitle != null) {
            stmt.bindString(5, PushTitle);
        }
 
        String PushContent = entity.getPushContent();
        if (PushContent != null) {
            stmt.bindString(6, PushContent);
        }
 
        String loginToken = entity.getLoginToken();
        if (loginToken != null) {
            stmt.bindString(7, loginToken);
        }
        stmt.bindLong(8, entity.getIsRead() ? 1L: 0L);
        stmt.bindLong(9, entity.getType());
    }

    @Override
    protected final void attachEntity(PushParser entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public PushParser readEntity(Cursor cursor, int offset) {
        PushParser entity = new PushParser( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // PushType
            cursor.getLong(offset + 2), // valueId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // PushTime
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // PushTitle
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // PushContent
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // loginToken
            cursor.getShort(offset + 7) != 0, // isRead
            cursor.getInt(offset + 8) // type
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PushParser entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPushType(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setValueId(cursor.getLong(offset + 2));
        entity.setPushTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPushTitle(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPushContent(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setLoginToken(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setIsRead(cursor.getShort(offset + 7) != 0);
        entity.setType(cursor.getInt(offset + 8));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(PushParser entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(PushParser entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(PushParser entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getPushValueDao().getAllColumns());
            builder.append(" FROM PUSH_PARSER T");
            builder.append(" LEFT JOIN PUSH_VALUE T0 ON T.\"VALUE_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected PushParser loadCurrentDeep(Cursor cursor, boolean lock) {
        PushParser entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        PushValue PushValue = loadCurrentOther(daoSession.getPushValueDao(), cursor, offset);
         if(PushValue != null) {
            entity.setPushValue(PushValue);
        }

        return entity;    
    }

    public PushParser loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<PushParser> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<PushParser> list = new ArrayList<PushParser>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<PushParser> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<PushParser> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
