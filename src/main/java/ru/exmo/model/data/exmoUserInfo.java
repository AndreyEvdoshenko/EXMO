package ru.exmo.model.data;

import javafx.beans.binding.DoubleBinding;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Андрей on 03.01.2018.
 */
public class exmoUserInfo {

    private String uid;
    private String server_date;
    private Map<String, BigDecimal> balances;
    private Map<String, BigDecimal> reserved;

    public exmoUserInfo() {

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

    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uid", uid);
        jsonObject.put("server_date", server_date);
        JSONObject jsonBalances = new JSONObject();
        for (Map.Entry entry : balances.entrySet()) {
            String value = (String) entry.getValue();
            if (!"0".equals(value)) {
                jsonBalances.put(entry.getKey(), entry.getValue());
            }
        }
        jsonObject.put("balances",jsonBalances);
        JSONObject jsonReserved = new JSONObject();
        for (Map.Entry entry : reserved.entrySet()) {
            String value = (String) entry.getValue();
            if (!"0".equals(value)) {
                jsonReserved.put(entry.getKey(), entry.getValue());
            }
        }
        jsonObject.put("reserved",jsonBalances);
        return jsonObject.toJSONString();
    }
}
