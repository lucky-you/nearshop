package com.baishan.nearshop.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by RayYeung on 2016/12/7.
 */

@Entity
public class SearchHistory {
    @Id(autoincrement = true)
    private Long id;

    public SearchHistory(String key) {
        this.key = key;
    }

    @Generated(hash = 1180562979)
    public SearchHistory(Long id, String key) {
        this.id = id;
        this.key = key;
    }

    @Generated(hash = 1905904755)
    public SearchHistory() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

   

}
