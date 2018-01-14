package ru.exmo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.exmo.dao.tradingDAO;
import ru.exmo.model.data.currencyPair;
import ru.exmo.model.data.exmoOrderCreate;
import ru.exmo.model.data.exmoTypeOrder;

/**
 * Created by Andrash on 13.01.2018.
 */
@EnableScheduling
@Component
public class testClass {

    @Autowired
    private tradingDAO dao;


    public void buy() {

        exmoOrderCreate buyOrder = new exmoOrderCreate();
        buyOrder.setPair("LTC_USD");
        buyOrder.setQuantity(100);
        buyOrder.setType(exmoTypeOrder.buy);
        buyOrder.setPrice(34);
        buyOrder.setMedium_price(1251);
        buyOrder.setExclusion_medium(123);
        buyOrder.setExclusion_buy(0);
        dao.createOrder(buyOrder);
    }

}
