package ru.exmo.process;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.exmo.api.tradingApi.tradingApi;
import ru.exmo.dao.tradingDAO;
import ru.exmo.model.data.currencyPair;
import ru.exmo.model.data.exmoOrderCreate;
import ru.exmo.model.data.exmoTypeOrder;
import ru.exmo.model.data.exmoUserInfo;

/**
 * Created by Andrash on 09.01.2018.
 */
@Component
public class exmoTradeProcess {

    private final Logger logger = Logger.getLogger(exmoTradeProcess.class);

    @Autowired
    private tradingDAO dao;

    @Autowired
    private tradingApi tradingApi;

    public exmoTradeProcess() {

    }

    public void buy(currencyPair pair) {

        exmoOrderCreate buyOrder = new exmoOrderCreate();
        buyOrder.setPair(pair.getName());
        buyOrder.setQuantity(pair.getMin_quantity()*2);
        buyOrder.setType(exmoTypeOrder.buy);
        buyOrder.setPrice(pair.getBuyValues());
        //buyOrder.setPrice(0);
        buyOrder.setMedium_price(pair.getMediumValues());
        buyOrder.setExclusion_medium(pair.getExclusion_medium());
        buyOrder.setExclusion_buy(0);
        buyOrder = tradingApi.createOrder(buyOrder);
        if("true".equals(buyOrder.getResult())) {
            pair.setBuy(true);
            dao.createOrder(buyOrder);
            logger.info("Ордер на покупку " + pair.name() + " выставлен");
        }else {
            logger.error("Ордер на покупку " + pair.name() + " не получилось, причина "+buyOrder.getError());
        }
    }

    public void sell(currencyPair pair) {
        exmoUserInfo user = tradingApi.returnUserInfo();
        pair.setBuy(false);
        exmoOrderCreate sellOrder = new exmoOrderCreate();
        sellOrder.setPair(pair.getName());
        sellOrder.setQuantity(user.getBalances().get(pair.getName1())); // продавать все что есть
        sellOrder.setType(exmoTypeOrder.sell);
        sellOrder.setPrice(pair.getSellValues());
      //  sellOrder.setPrice(0);
        sellOrder.setMedium_price(pair.getMediumValues());
        sellOrder.setExclusion_medium(pair.getExclusion_medium());
        sellOrder.setExclusion_buy(pair.getExclusion_buy());
        sellOrder = tradingApi.createOrder(sellOrder);
        if("true".equals(sellOrder.getResult())) {
            if(!pair.isSellProfit())sellOrder.setProfit("minus");
            else sellOrder.setProfit("plus");
            dao.createOrder(sellOrder);
            logger.info("ордер на продажу " + pair.name() + " выставлен");
        }else{
            logger.error("Ордер на продажу " + pair.name() + " не получилось, причина "+sellOrder.getError());
        }
    }

}
