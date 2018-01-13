package ru.exmo.model.data;

/**
 * Created by Андрей on 15.12.2017.
 */
public enum  currencyPair {
    LTC_USD("LTC_USD"),
    LTC_RUB("LTC_RUB");

    currencyPair(String name){
        this.name = name;
    }

    private String name;
    private float min_quantity;    //минимальное кол-во по ордеру
    private float max_quantity;    //максимальное кол-во по ордеру
    private float min_price;       //минимальная цена по ордеру
    private float max_price;       //максимальная цена по ордеру
    private float min_amount;      //минимальная сумма по ордеру
    private float max_amount;      //максимальная сумма по ордеру

    private float      mediumValues;         //среднее значение за интервал времени
    private float      buyValues;            //цена по коротой купили
    private boolean    isBuy = false;                //куплено?

    private double percentageOfExclusionBuy = 0.3; //процент отклонения для покупки (от среднего)
    private float percentageOfExclusionSell = 1; //процент отклонения для продажи(от запучной цены)

    public double getPercentageOfExclusionBuy() {
        return percentageOfExclusionBuy;
    }

    public void setPercentageOfExclusionBuy(double percentageOfExclusionBuy) {
        this.percentageOfExclusionBuy = percentageOfExclusionBuy;
    }

    public float getPercentageOfExclusionSell() {
        return percentageOfExclusionSell;
    }

    public void setPercentageOfExclusionSell(float percentageOfExclusionSell) {
        this.percentageOfExclusionSell = percentageOfExclusionSell;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMediumValues() {
        return mediumValues;
    }

    public void setMediumValues(float mediumValues) {
        this.mediumValues = mediumValues;
    }

    public float getBuyValues() {
        return buyValues;
    }

    public void setBuyValues(float buyValues) {
        this.buyValues = buyValues;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public float getMin_quantity() {
        return min_quantity;
    }

    public void setMin_quantity(float min_quantity) {
        this.min_quantity = min_quantity;
    }

    public float getMax_quantity() {
        return max_quantity;
    }

    public void setMax_quantity(float max_quantity) {
        this.max_quantity = max_quantity;
    }

    public float getMin_price() {
        return min_price;
    }

    public void setMin_price(float min_price) {
        this.min_price = min_price;
    }

    public float getMax_price() {
        return max_price;
    }

    public void setMax_price(float max_price) {
        this.max_price = max_price;
    }

    public float getMin_amount() {
        return min_amount;
    }

    public void setMin_amount(float min_amount) {
        this.min_amount = min_amount;
    }

    public float getMax_amount() {
        return max_amount;
    }

    public void setMax_amount(float max_amount) {
        this.max_amount = max_amount;
    }

    public String getPair() {
        return name;
    }

    public void setPair(String pair) {
        this.name = pair;
    }

}
