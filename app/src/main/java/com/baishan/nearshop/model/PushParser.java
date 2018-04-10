package com.baishan.nearshop.model;

import com.baishan.nearshop.dao.DaoSession;
import com.baishan.nearshop.dao.PushParserDao;
import com.baishan.nearshop.dao.PushValueDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * Created by Administrator on 2016/5/27 0027.
 */
@Entity
public class PushParser {
    @Id(autoincrement = true)
    private Long id;

    private String PushType;
    @ToOne(joinProperty = "valueId")
    public PushValue PushValue;
    private long valueId;
    private String PushTime;
    private String PushTitle;
    private String PushContent;

    private String loginToken;
    private boolean isRead;
    // 1 系统  2  商品  3  服务  4  社区
    private int type;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1041600839)
    private transient PushParserDao myDao;
    @Generated(hash = 764170770)
    public PushParser(Long id, String PushType, long valueId, String PushTime,
            String PushTitle, String PushContent, String loginToken,
            boolean isRead, int type) {
        this.id = id;
        this.PushType = PushType;
        this.valueId = valueId;
        this.PushTime = PushTime;
        this.PushTitle = PushTitle;
        this.PushContent = PushContent;
        this.loginToken = loginToken;
        this.isRead = isRead;
        this.type = type;
    }
    @Generated(hash = 1566008479)
    public PushParser() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPushType() {
        return this.PushType;
    }
    public void setPushType(String PushType) {
        this.PushType = PushType;
    }
    public long getValueId() {
        return this.valueId;
    }
    public void setValueId(long valueId) {
        this.valueId = valueId;
    }
    public String getPushTime() {
        return this.PushTime;
    }
    public void setPushTime(String PushTime) {
        this.PushTime = PushTime;
    }
    public String getPushTitle() {
        return this.PushTitle;
    }
    public void setPushTitle(String PushTitle) {
        this.PushTitle = PushTitle;
    }
    public String getPushContent() {
        return this.PushContent;
    }
    public void setPushContent(String PushContent) {
        this.PushContent = PushContent;
    }
    public String getLoginToken() {
        return this.loginToken;
    }
    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }
    public boolean getIsRead() {
        return this.isRead;
    }
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    @Generated(hash = 2012285968)
    private transient Long PushValue__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 427079104)
    public PushValue getPushValue() {
        long __key = this.valueId;
        if (PushValue__resolvedKey == null || !PushValue__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PushValueDao targetDao = daoSession.getPushValueDao();
            PushValue PushValueNew = targetDao.load(__key);
            synchronized (this) {
                PushValue = PushValueNew;
                PushValue__resolvedKey = __key;
            }
        }
        return PushValue;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 726392414)
    public void setPushValue(@NotNull PushValue PushValue) {
        if (PushValue == null) {
            throw new DaoException(
                    "To-one property 'valueId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.PushValue = PushValue;
            valueId = PushValue.getId();
            PushValue__resolvedKey = valueId;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1503541246)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPushParserDao() : null;
    }





}
