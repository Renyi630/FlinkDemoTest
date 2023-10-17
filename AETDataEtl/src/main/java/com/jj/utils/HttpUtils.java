package com.jj.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

@Slf4j
public class HttpUtils {
    private static HttpClientBuilder httpClientBuilder;
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY = 100; // 毫秒
    static {
        httpClientBuilder = HttpClientBuilder.create();
    }
    /**
     * 发送post请求
     * @param url  请求的url
     * @param body json串
     * @return
     */
    public static String sendPostJsonBody(String url, String body) {

        int retryCount = 0;
        boolean requestSuccessful = false;

        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");
        StringEntity entity = new StringEntity(body, "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        while (retryCount < MAX_RETRIES && !requestSuccessful) {
            try {
                HttpClient client = httpClientBuilder.build();
                HttpResponse response = client.execute(httpPost);
                if (response.getStatusLine() != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String result = EntityUtils.toString(response.getEntity(), "utf-8");
//                    LogUtils.info("[HttpClientUtil][sendPostJsonBody] 结果 result={"+result+"}");
                    requestSuccessful = true;
//                    System.out.println(result);
                    return result;
                }
                LogUtils.error("[HttpClientUtil][sendPostJsonBody] 请求失败 response={"+response.toString()+"}");
            }
            catch (IOException ex) {
                // 请求异常，等待重试
                try {
                    Thread.sleep(RETRY_DELAY);
                } catch (InterruptedException e) {
                    ex.printStackTrace();
                }
                LogUtils.error("[HttpClientUtil][sendPostJsonBody] 请求异常 ");
            }
            retryCount++;
        }
        if (!requestSuccessful) {
            // 重试次数达到上限，记录错误日志
            LogUtils.error("POST 请求失败，已达到最大重试次数");
        }

        return "";
    }

}
