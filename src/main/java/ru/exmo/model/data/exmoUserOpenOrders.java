package ru.exmo.model.data;

/**
 * Created by Андрей on 17.01.2018.
 */
public class exmoUserOpenOrders {

    private float order_id;          // идентификатор ордера
    private float  created;          // дата и время создания ордера
    private float  type;             // тип ордера
    private float   pair;            // валютная пара
    private float   price;           // цена по ордеру
    private float   quantity;        // кол-во по ордеру
    private float   amount;          // сумма по ордеру

    public exmoUserOpenOrders(){

    }

    public float getOrder_id() {
        return order_id;
    }

    public void setOrder_id(float order_id) {
        this.order_id = order_id;
    }

    public float getCreated() {
        return created;
    }

    public void setCreated(float created) {
        this.created = created;
    }

    public float getType() {
        return type;
    }

    public void setType(float type) {
        this.type = type;
    }

    public float getPair() {
        return pair;
    }

    public void setPair(float pair) {
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
