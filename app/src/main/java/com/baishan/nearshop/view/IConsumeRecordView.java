package com.baishan.nearshop.view;

import com.baishan.nearshop.model.ConsumeRecord;

import java.util.List;

/**
 * Created by RayYeung on 2017/1/13.
 */

public interface IConsumeRecordView {

    void getDataSuccess(List<ConsumeRecord> response);
}
