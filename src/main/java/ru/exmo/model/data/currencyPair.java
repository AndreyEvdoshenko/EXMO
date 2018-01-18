package ru.exmo.model.data;

/**
 * Created by Андрей on 15.12.2017.
 */
public enum  currencyPair {
    BTC_RUB("BTC_RUB"),
    BTC_USD("BTC_USD"),
    BTC_EUR("BTC_EUR"),
    ETH_RUB("ETH_RUB"),
    ETH_USD("ETH_USD"),
    ETH_EUR("ETH_EUR"),
    LTC_RUB("LTC_RUB"),
    LTC_USD("LTC_USD"),
    LTC_EUR("LTC_EUR");

    currencyPair(String name){
        this.name = name;
        this.name1 = name.substring(0,3);
        this.name2 = name.substring(4,7);
    }

    private String name;
    private String name1;
    private String name2;

    private float min_quantity;    //минимальное кол-во по ордеру
    private float max_quantity;    //максимальное кол-во по ордеру
    private float min_price;       //минимальная цена по ордеру
    private float max_price;       //максимальная цена по ордеру
    private float min_amount;      //минимальная сумма по ордеру
    private float max_amount;      //максимальная сумма по ордеру

    private boolean active;        //торгуем ли данной парой
    private currencyPairCondition currentCondition = currencyPairCondition.SELL;   //текущее состояние

    private float      mediumValues;         //среднее значение за интервал времени
    private float      buyValues;            //цена по коротой купили
    private float      sellValues;           //цена по коротой надо продавать
    private boolean    isBuy = false;        //куплено?
    private boolean    sellProfit;             //Продано в плюс?

    private float exclusion_medium;          // отклонение от среднего на момент покупки
    private float exclusion_buy;             // отклонение от закупочной цены на момент продажи

    private float percentageOfExclusionBuy;    //процент отклонения для покупки (от среднего)
    private float percentageOfExclusionSell;   //процент отклонения для продажи(от запучной цены)
    private float percentageOfNoReturn;        //процент не возврата от закуп цены


    public boolean isSellProfit() {
        return sellProfit;
    }

    public void setSellProfit(boolean sellProfit) {
        this.sellProfit = sellProfit;
    }

    public float getPercentageOfNoReturn() {
        return percentageOfNoReturn;
    }

    public void setPercentageOfNoReturn(float percentageOfNoReturn) {
        this.percentageOfNoReturn = percentageOfNoReturn;
    }

    public currencyPairCondition getCurrentCondition() {
        return currentCondition;
    }

    public void setCurrentCondition(currencyPairCondition currentCondition) {
        this.currentCondition = currentCondition;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public float getPercentageOfExclusionBuy() {
        return percentageOfExclusionBuy;
    }

    public void setPercentageOfExclusionBuy(float percentageOfExclusionBuy) {
        this.percentageOfExclusionBuy = percentageOfExclusionBuy;
    }

    public float getPercentageOfExclusionSell() {
        return percentageOfExclusionSell;
    }

    public void setPercentageOfExclusionSell(float percentageOfExclusionSell) {
        this.percentageOfExclusionSell = percentageOfExclusionSell;
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


    public float getSellValues() {
        return sellValues;
    }

    public void setSellValues(float sellValues) {
        this.sellValues = sellValues;
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

    public String getName1() {
        return name1;
    }

    public String getName2() {
        return name2;
    }
}
