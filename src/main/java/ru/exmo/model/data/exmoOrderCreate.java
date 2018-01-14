package ru.exmo.model.data;

import org.json.simple.JSONObject;

import java.sql.Timestamp;

/**
 * Created by Andrash on 05.01.2018.
 */
public class exmoOrderCreate {

    public exmoOrderCreate(){
        this.updated = new Timestamp(System.currentTimeMillis());
    }

    //запрос
    private String pair;         // валютная пара
    private float quantity;      // кол-во по ордеру
    private float price;         // цена по ордеру
    private String type;         // тип ордера, может принимать следующие значения:

    //ответ
    private String result;
    private String error;
    private String order_id;


    private float medium_price;              // средня цена на момент покупки
    private float exclusion_medium;          // отклонение от среднего на момент покупки
    private float exclusion_buy;            //  отклонение от закупочной цены на момент продажи
    private String status;                  //  open / close
    private String profit;                  //  в плюс или в минус

    private Timestamp updated;              //  дата и время создания ордера


    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getMedium_price() {
        return medium_price;
    }

    public void setMedium_price(float medium_price) {
        this.medium_price = medium_price;
    }

    public float getExclusion_medium() {
        return exclusion_medium;
    }

    public void setExclusion_medium(float exclusion_medium) {
        this.exclusion_medium = exclusion_medium;
    }

    public float getExclusion_buy() {
        return exclusion_buy;
    }

    public void setExclusion_buy(float exclusion_buy) {
        this.exclusion_buy = exclusion_buy;
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

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(exmoTypeOrder type) {
        this.type = type.name();
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public  String toString(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pair",pair);
        jsonObject.put("quantity",quantity);
        jsonObject.put("price",price);
        jsonObject.put("type",type);
        jsonObject.put("result",result);
        jsonObject.put("error",error);
        jsonObject.put("order_id",order_id);
        return jsonObject.toJSONString();
    }
}
