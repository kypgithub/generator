package io.renren.utils;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.util.StringUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * HttpClient工具类
 * @author wangmx
 */
public class HttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class); // 日志记录

    private static RequestConfig requestConfig = null;

    static
    {
        requestConfig = RequestConfig.custom().build();
    }

    /**
     * post请求传输json参数
     * @param url  url地址
     * @return
     */
    public static String httpPost(String url, Map<String, String> param, Map<String, String> headers) {
        // post请求返回结果
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        // 设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        try {
            if (null != param) {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(JSON.toJSONString(param.toString()), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
                addHeaders(headers, httpPost);
            }
            CloseableHttpResponse result = httpClient.execute(httpPost);
            String res = EntityUtils.toString(result.getEntity(), "utf-8");
            // 请求发送成功，并得到响应
            if (result.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                logger.info("###请求提交失败! url ：{} \t 返回结果：{}",url, res);
            }
            return res;
        }
        catch (IOException e) {
            logger.info("###post请求提交失败:" + url, e);
        } finally {
            httpPost.releaseConnection();
        }
        return null;
    }

    /**
     * post请求传输String参数
     * Content-type:application/x-www-form-urlencoded
     * @param url url地址
     * @param strParam 参数
     * @param headers 头信息
     * @return
     */
    public static String httpPost(String url, String strParam, Map<String, String> headers)
    {
        // post请求返回结果
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        try {
            if (null != strParam) {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(strParam, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
                addHeaders(headers, httpPost);
            }
            CloseableHttpResponse result = httpClient.execute(httpPost);
            String res = EntityUtils.toString(result.getEntity(), "utf-8");
            if (result.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                logger.info("###请求提交失败! url ：{} \t 返回结果：{}",url, res);
            }
            return res;
        } catch (IOException e){
            logger.info("###post请求提交失败:" + url, e);
        } finally {
            httpPost.releaseConnection();
        }
        return null;
    }
    /**
     * 发送get请求
     * @param url 路径
     * @param headers 请求头
     * @return
     */
    public static String httpGet(String url, Map<String, String> headers)
    {
        // get请求返回结果
        CloseableHttpClient client = HttpClients.createDefault();
        // 发送get请求

        HttpGet request = new HttpGet(url);
        request.setConfig(requestConfig);
        try {
            addHeaders(headers, request);
            CloseableHttpResponse response = client.execute(request);
            // 请求发送成功，并得到响应
            HttpEntity entity = response.getEntity();
            String res = EntityUtils.toString(entity, "utf-8");
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                // 读取服务器返回过来的json字符串数据
                logger.info("###请求提交失败! url ：{} \t 返回结果：{}",url, res);
            }
            return res;
        } catch (IOException e) {
            logger.info("###get请求提交失败:" + url, e);
        }
        finally {
            request.releaseConnection();
        }
        return null;
    }

    /**
     * 发送get请求 请求页面专用
     * @param url 路径
     * @param headers 请求头
     * @return
     */
    public static String httpGetHtml(String url, Map<String, String> headers)
    {
        // get请求返回结果
        CloseableHttpClient client = HttpClients.createDefault();
        // 发送get请求

        HttpGet request = new HttpGet(url);
        request.setConfig(requestConfig);
        try {
            addHeaders(headers, request);
            CloseableHttpResponse response = client.execute(request);
            // 请求发送成功，并得到响应
            HttpEntity entity = response.getEntity();
            String res = EntityUtils.toString(entity, "utf-8");
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                // 读取服务器返回过来的json字符串数据
                logger.info("###get请求提交失败! url ：{} \t 返回结果：{}",url, res);
                return null;
            }
            return res;
        } catch (IOException e) {
            logger.info("###get请求提交失败:" + url, e);
        }
        finally {
            request.releaseConnection();
        }
        return null;
    }


    /**
     * 添加头信息
     * @param headers
     */
    private static void addHeaders(Map<String, String> headers, HttpPost httpPost){
        if (headers == null || headers.isEmpty()){
            return;
        }
        headers.keySet().forEach(key ->{
            if (StringUtil.isNotEmpty(key) && StringUtil.isNotEmpty(headers.get(key))){
                httpPost.addHeader(key, headers.get(key));
            }
        });
    }
    /**
     * 添加头信息
     * @param headers
     */
    private static void addHeaders(Map<String, String> headers, HttpGet httpGet){
        if (headers == null || headers.isEmpty()){
            return;
        }
        headers.keySet().forEach(key ->{
            if (StringUtil.isNotEmpty(key) && StringUtil.isNotEmpty(headers.get(key))){
                httpGet.addHeader(key, headers.get(key));
            }
        });
    }
}
