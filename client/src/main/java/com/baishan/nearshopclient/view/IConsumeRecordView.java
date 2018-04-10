package com.baishan.nearshopclient.view;

import com.baishan.nearshopclient.model.ConsumeRecord;

import java.util.List;

/**
 * 作者：ZhouBin  2017/3/17 14:02
 * 邮箱：1021237228@qq.com
 * 作用：金额明细的实体类
 */

public interface IConsumeRecordView {

    /**
     * 获取金额明细的返回数据
     *
     * @param response
     */
    void getDataSuccess(List<ConsumeRecord> response);


}
