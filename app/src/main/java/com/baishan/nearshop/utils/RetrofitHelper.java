package com.baishan.nearshop.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by RayYeung on 2016/10/31.
 */

public class RetrofitHelper {


    /**
     * 文件和文字混合参数
     * @param params
     * @return
     */
    public static Map<String, RequestBody> createMultipartParam(Map<String, Object> params) {
        Map<String, RequestBody> bodies = new HashMap<>();
        for (String s : params.keySet()) {
            Object o = params.get(s);
            if (o instanceof File) {
                RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), (File) o);
                bodies.put(s + "\"; filename=\"" + ((File) o).getName(), fileBody);
            } else {
                RequestBody textBody = RequestBody.create(MediaType.parse("text/plain"), o + "");
                bodies.put(s, textBody);
            }
        }
        return bodies;
    }

}
