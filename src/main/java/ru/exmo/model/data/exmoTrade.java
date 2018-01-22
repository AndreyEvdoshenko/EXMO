package ru.exmo.model.data;

import org.json.simple.JSONObject;

/**
 * Created by Андрей on 15.12.2017.
 */
public class exmoTrade {

    private String pair;
    private String trade_id;
    private String order_id;
    private String type;
    private String price;
    private String quantity;
    private String amount;
    private String date;

    public exmoTrade() {
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(String trade_id) {
        this.trade_id = trade_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public  String toString(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pair",pair);
        return jsonObject.toJSONString();
    }
}
