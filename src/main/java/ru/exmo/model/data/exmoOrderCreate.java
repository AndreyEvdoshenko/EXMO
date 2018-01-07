package ru.exmo.model.data;

/**
 * Created by Andrash on 05.01.2018.
 */
public class exmoOrderCreate {

    //запрос
    private String pair;         // валютная пара
    private double quantity;     // кол-во по ордеру
    private double price;        // цена по ордеру
    private exmoTypeOrder type;      //тип ордера, может принимать следующие значения:

    //ответ
    private boolean result;
    private String error;
    private String order_id;

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public exmoTypeOrder getType() {
        return type;
    }

    public void setType(exmoTypeOrder type) {
        this.type = type;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
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
}
