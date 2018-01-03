package ru.exmo.api.publicApi;

/**
 * Created by Андрей on 03.01.2018.
 */
public enum publicApiMethods {

    EXMO_TRADES("https://api.exmo.com/v1/trades/"),
    EXMO_ORDER_BOOK("https://api.exmo.com/v1/order_book/"),
    EXMO_TICKER("https://api.exmo.com/v1/ticker/"),
    EXMO_PAIR_SETTINGS("https://api.exmo.com/v1/pair_settings/");

    publicApiMethods(String url){
        this.url = url;
    }
    private String url;
    public String getUrl(){
        return url;
    }
}
