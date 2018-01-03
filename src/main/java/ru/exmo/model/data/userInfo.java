package ru.exmo.model.data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Андрей on 03.01.2018.
 */
public class userInfo {

    private String uid;
    private String server_date;
    private Map<String,BigDecimal> balances;
    private Map<String,BigDecimal> reserved;

    public userInfo(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getServer_date() {
        return server_date;
    }

    public void setServer_date(String server_date) {
        this.server_date = server_date;
    }

    public Map<String, BigDecimal> getBalances() {
        return balances;
    }

    public void setBalances(Map<String, BigDecimal> balances) {
        this.balances = balances;
    }

    public Map<String, BigDecimal> getReserved() {
        return reserved;
    }

    public void setReserved(Map<String, BigDecimal> reserved) {
        this.reserved = reserved;
    }
}
