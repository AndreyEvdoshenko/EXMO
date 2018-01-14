package ru.exmo.api.tradingApi;

/**
 * Created by Андрей on 03.01.2018.
 */
public enum tradingApiMethods {

    EXMO_USER_INFO("https://api.exmo.com/v1/user_info"),
    EXMO_ORDER_CREAT("https://api.exmo.com/v1/order_create");

    tradingApiMethods(String url){
        this.url = url;
    }

    private String url;

    public String getUrl(){
        return url;
    }
}
