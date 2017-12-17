package ru.exmo.model.data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Andrash on 17.12.2017.
 */
public class exmoOrderBook {

    private BigDecimal ask_quantity;    //объем всех ордеров на продажу
    private BigDecimal ask_amount;      //сумма всех ордеров на продажу
    private BigDecimal ask_top;         //минимальная цена продажи
    private BigDecimal bid_quantity;    //объем всех ордеров на покупку
    private BigDecimal bid_amount;      //сумма всех ордеров на покупку
    private BigDecimal bid_top;         //максимальная цена покупки
    private List<order> bid;            //список ордеров на покупку, где каждая строка это цена, количество и сумма
    private List<order> ask;            //список ордеров на продажу, где каждая строка это цена, количество и сумма

    private class order{
        private BigDecimal price;       //цена
        private BigDecimal quantity;    //количество
        private BigDecimal amount;      //сумма

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }

        public void setQuantity(BigDecimal quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }

    public BigDecimal getAsk_quantity() {
        return ask_quantity;
    }

    public void setAsk_quantity(BigDecimal ask_quantity) {
        this.ask_quantity = ask_quantity;
    }

    public BigDecimal getAsk_amount() {
        return ask_amount;
    }

    public void setAsk_amount(BigDecimal ask_amount) {
        this.ask_amount = ask_amount;
    }

    public BigDecimal getAsk_top() {
        return ask_top;
    }

    public void setAsk_top(BigDecimal ask_top) {
        this.ask_top = ask_top;
    }

    public BigDecimal getBid_quantity() {
        return bid_quantity;
    }

    public void setBid_quantity(BigDecimal bid_quantity) {
        this.bid_quantity = bid_quantity;
    }

    public BigDecimal getBid_amount() {
        return bid_amount;
    }

    public void setBid_amount(BigDecimal bid_amount) {
        this.bid_amount = bid_amount;
    }

    public BigDecimal getBid_top() {
        return bid_top;
    }

    public void setBid_top(BigDecimal bid_top) {
        this.bid_top = bid_top;
    }

    public List<order> getBid() {
        return bid;
    }

    public void setBid(List<order> bid) {
        this.bid = bid;
    }

    public List<order> getAsk() {
        return ask;
    }

    public void setAsk(List<order> ask) {
        this.ask = ask;
    }

}
