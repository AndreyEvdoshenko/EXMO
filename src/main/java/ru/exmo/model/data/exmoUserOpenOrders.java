package ru.exmo.model.data;

/**
 * Created by Андрей on 17.01.2018.
 */
public class exmoUserOpenOrders {

    private int order_id;             // идентификатор ордера
    private String  created;          // дата и время создания ордера
    private String  type;             // тип ордера
    private currencyPair   pair;      // валютная пара
    private float   price;            // цена по ордеру
    private float   quantity;         // кол-во по ордеру
    private float   amount;           // сумма по ордеру

    public exmoUserOpenOrders(){

    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public currencyPair getPair() {
        return pair;
    }

    public void setPair(currencyPair pair) {
        this.pair = pair;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }




}
