package ru.exmo.model.data;

import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by Андрей on 15.12.2017.
 */
public class exmoTicker {

    private String pair;
    private BigDecimal high; // максимальная цена сделки за 24 часа
    private BigDecimal low; //  минимальная цена сделки за 24 часа
    private BigDecimal avg; // средняя цена сделки за 24 часа
    private BigDecimal vol; //  объем всех сделок за 24 часа
    private BigDecimal vol_curr; //  сумма всех сделок за 24 часа
    private float last_trade; //  цена последней сделки
    private float buy_price; //  текущая максимальная цена покупки
    private float sell_price; // текущая минимальная цена продажи
    private Timestamp updated; //  дата и время обновления данных
    private float mediumValue; // среднее значение

    public exmoTicker() {

    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getAvg() {
        return avg;
    }

    public void setAvg(BigDecimal avg) {
        this.avg = avg;
    }

    public BigDecimal getVol() {
        return vol;
    }

    public void setVol(BigDecimal vol) {
        this.vol = vol;
    }

    public BigDecimal getVol_curr() {
        return vol_curr;
    }

    public void setVol_curr(BigDecimal vol_curr) {
        this.vol_curr = vol_curr;
    }

    public float getLast_trade() {
        return last_trade;
    }

    public void setLast_trade(float last_trade) {
        this.last_trade = last_trade;
    }

    public float getBuy_price() {
        return buy_price;
    }

    public void setBuy_price(float buy_price) {
        this.buy_price = buy_price;
    }

    public float getSell_price() {
        return sell_price;
    }

    public void setSell_price(float sell_price) {
        this.sell_price = sell_price;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public float getMediumValue() {
        return mediumValue;
    }

    public void setMediumValue(float mediumValue) {
        this.mediumValue = mediumValue;
    }

    public  String toString(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sell_price",sell_price);
        jsonObject.put("buy_price",buy_price);
        jsonObject.put("last_trade",last_trade);
        jsonObject.put("vol_curr",vol_curr);
        jsonObject.put("vol",vol);
        jsonObject.put("avg",avg);
        jsonObject.put("low",low);
        jsonObject.put("high",high);
        jsonObject.put("pair",pair);
        return jsonObject.toJSONString();
    }
}
