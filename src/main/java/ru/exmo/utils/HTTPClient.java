package ru.exmo.utils;

import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.util.List;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Andrash on 17.12.2017.
 */
@Component
public class HTTPClient {


    private final Logger logger = Logger.getLogger(HTTPClient.class);

    private volatile int currentCountCall;
    private final int allowedCountCall = 3;

    HTTPClient(){
        currentCountCall = 0;
    }

    public  boolean isCallAllowed(){
        if(currentCountCall<allowedCountCall){
            currentCountCall++;
      //      logger.info("currentCountCall = "+currentCountCall);
            return true;
        }
        return false;
    }

    @Scheduled(fixedRate =  1500)
    public void resetCount(){
        currentCountCall = 0;
    }


    public String postHttp(String url, List<NameValuePair> params, List<NameValuePair> headers) throws IOException {
        HttpPost post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
        post.getEntity().toString();

        if (headers != null) {
            for (NameValuePair header : headers) {
                post.addHeader(header.getName(), header.getValue());
            }
        }

        HttpClient httpClient = HttpClientBuilder.create().build();
      //  logger.info(url+": ожидание вызова postHttp...");
        while (!isCallAllowed()) {}
        HttpResponse response = httpClient.execute(post);
     //   logger.info(url+": вызвано ");

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            return EntityUtils.toString(entity);

        }
        return null;
    }

    public String getHttp(String url, List<NameValuePair> headers) throws IOException {
        HttpRequestBase request = new HttpGet(url);

        if (headers != null) {
            for (NameValuePair header : headers) {
                request.addHeader(header.getName(), header.getValue());
            }
        }
        HttpClient httpClient = HttpClientBuilder.create().build();
     //  logger.info(url+": ожидание вызова getHttp ...");
        while (!isCallAllowed()) {}
        HttpResponse response = httpClient.execute(request);
    //    logger.info(url+": вызвано ");

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            return EntityUtils.toString(entity);

        }
        return null;
    }
}
