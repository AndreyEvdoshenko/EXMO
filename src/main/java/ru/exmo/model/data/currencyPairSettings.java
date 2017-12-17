package ru.exmo.model.data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Andrash on 17.12.2017.
 */
public class currencyPairSettings {

    private BigDecimal min_quantity;    //минимальное кол-во по ордеру
    private BigDecimal max_quantity;    //максимальное кол-во по ордеру
    private BigDecimal min_price;       //минимальная цена по ордеру
    private BigDecimal max_price;       //максимальная цена по ордеру
    private BigDecimal min_amount;      //минимальная сумма по ордеру
    private BigDecimal max_amount;      //максимальная сумма по ордеру

    public BigDecimal getMin_quantity() {
        return min_quantity;
    }

    public void setMin_quantity(BigDecimal min_quantity) {
        this.min_quantity = min_quantity;
    }

    public BigDecimal getMax_quantity() {
        return max_quantity;
    }

    public void setMax_quantity(BigDecimal max_quantity) {
        this.max_quantity = max_quantity;
    }

    public BigDecimal getMin_price() {
        return min_price;
    }

    public void setMin_price(BigDecimal min_price) {
        this.min_price = min_price;
    }

    public BigDecimal getMax_price() {
        return max_price;
    }

    public void setMax_price(BigDecimal max_price) {
        this.max_price = max_price;
    }

    public BigDecimal getMin_amount() {
        return min_amount;
    }

    public void setMin_amount(BigDecimal min_amount) {
        this.min_amount = min_amount;
    }

    public BigDecimal getMax_amount() {
        return max_amount;
    }

    public void setMax_amount(BigDecimal max_amount) {
        this.max_amount = max_amount;
    }

}
