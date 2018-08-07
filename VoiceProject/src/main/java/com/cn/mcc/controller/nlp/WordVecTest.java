package com.cn.mcc.controller.nlp;
import com.alibaba.fastjson.JSONObject;
import com.cn.mcc.bean.Iat;
import com.cn.mcc.utils.HttpClientUtil;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yyzc on 2018/8/7.
 */
public class WordVecTest {


    public static void main(String[] args){
        try {

            String url ="https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=2unfB0QOTK2Fq3GQmLh9YHqC&client_secret=9G5rhUnE4kuP98POEWYCjcWTP2WdBljr&" ;
            String res = HttpClientUtil.getHttpData(url, null,true);
            System.out.println(res);
            if (StringUtils.isNotEmpty(res)) {
                JSONObject jsonObject = JSONObject.parseObject(res);
                String accessToken = jsonObject.getString("access_token");
                System.out.println(accessToken);
                String url1 ="https://aip.baidubce.com/rpc/2.0/nlp/v2/comment_tag?access_token="+ accessToken;
                Map map=new HashMap();
                map.put("text","三星电脑电池不给力");
                map.put("type",13);
                String ress = HttpClientUtil.getHttpData(url1, map,true);
                System.out.println(ress);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
