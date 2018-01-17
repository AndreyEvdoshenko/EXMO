package ru.exmo.process;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.exmo.api.tradingApi.tradingApi;
import ru.exmo.dao.tradingDAO;
import ru.exmo.model.data.*;

import javax.annotation.PostConstruct;

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

    @Autowired
    private exmoTickerProcess tickerProcess;

    public exmoTradeProcess() {

    }

    @PostConstruct
    public void initTickerProcess() {
        logger.info("@PostConstruct initProcess() invoke");
        new Thread(new tradePair(currencyPair.BTC_USD)).start();
        new Thread(new tradePair(currencyPair.ETH_USD)).start();
    }


    public void buy(currencyPair pair) {


    }

    public void sell(currencyPair pair) {
        exmoUserInfo user = tradingApi.returnUserInfo();
        pair.setBuy(false);//sellOrder.setQuantity(Float.valueOf(String.valueOf(user.getBalances().get(pair.getName1()))))
        exmoOrderCreate sellOrder = new exmoOrderCreate();
        sellOrder.setPair(pair.getName());
        String str = String.valueOf(user.getBalances().get(pair.getName1()));
        Float amount = Float.valueOf(str);
        sellOrder.setQuantity(amount); // продавать все что есть
        sellOrder.setType(exmoTypeOrder.sell);
        sellOrder.setPrice(pair.getSellValues());
        //  sellOrder.setPrice(0);
        sellOrder.setMedium_price(pair.getMediumValues());
        sellOrder.setExclusion_medium(pair.getExclusion_medium());
        sellOrder.setExclusion_buy(pair.getExclusion_buy());
        sellOrder = tradingApi.createOrder(sellOrder);
        if ("true".equals(sellOrder.getResult())) {
            if (!pair.isSellProfit()) sellOrder.setProfit("minus");
            else sellOrder.setProfit("plus");
            dao.createOrder(sellOrder);
            logger.info("ордер на продажу " + pair.name() + " выставлен");
        } else {
            logger.error("Ордер на продажу " + pair.name() + " не получилось, причина " + sellOrder.getError());
        }
    }

    //**********************************************************************************************************************
    private class tradePair implements Runnable {


        private currencyPair pair;

        private volatile exmoTicker ticker;

        tradePair(currencyPair pair) {
            this.pair = pair;
        }

        void createSellOrder() {

        }

        private boolean createBuyOrder(float buyPrice) {
            exmoOrderCreate buyOrder = new exmoOrderCreate();
            buyOrder.setPair(pair.getName());
            buyOrder.setQuantity(pair.getMin_quantity() * 2);
            buyOrder.setType(exmoTypeOrder.buy);
            buyOrder.setPrice(buyPrice);
            //buyOrder.setPrice(0);
            buyOrder.setMedium_price(pair.getMediumValues());
            buyOrder.setExclusion_medium(pair.getExclusion_medium());
            buyOrder.setExclusion_buy(0);
            buyOrder = tradingApi.createOrder(buyOrder);
            if ("true".equals(buyOrder.getResult())) {
                dao.createOrder(buyOrder);
                logger.info("Ордер на покупку " + pair.name() + " выставлен");
                return true;
            } else {
                logger.error("Ордер на покупку " + pair.name() + " не получилось, причина " + buyOrder.getError());
                return false;
            }
        }

        private void buy() {
            float currentValue = ticker.getSell_price();
            float mediumValue = pair.getMediumValues();
            float percentageOfExclusionBuy = pair.getPercentageOfExclusionBuy();
            float deviation;
            if (mediumValue - currentValue > 0) {
                deviation = 100 - ((currentValue * 100) / mediumValue);
                logger.info(pair.getName() + " цена упала: средняя за 5 минут: " + mediumValue + ", текущаяя: " + currentValue + ", отклонение: -" + deviation);
                if (deviation > percentageOfExclusionBuy) {
                    logger.info(pair.getName() + " выставляем ордер на покупку по цене  " + currentValue);
                    logger.info("ticker " + pair.getName() + ": " + ticker);
                    boolean isBuy = createBuyOrder(currentValue);
                    if(isBuy){
                        pair.setBuy(true);
                        pair.setExclusion_medium(deviation);
                        pair.setBuyValues(currentValue);
                        pair.setSellValues(currentValue+(currentValue/100*pair.getPercentageOfExclusionSell()));
                    }
                }
            } else if (mediumValue - currentValue < 0) {
                deviation = ((currentValue * 100) / mediumValue) - 100;
                logger.info(pair.getName() + " цена поднялась: средняя за 5 минут: " + mediumValue + ", текущаяя: " + currentValue + ", отклонение: " + deviation);
            } else {
                logger.info(pair.getName() + " цена за 5 минут не изменилась от средней: " + mediumValue + ", текущаяя: " + currentValue);
            }
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ticker = tickerProcess.returnTicker(pair);
                if (!pair.isBuy()) {
                   buy();
                } else {
//                boolean sell = sellAnalise();
//                if (sell) {
//                    logger.info("ticker "+pair.getName()+": "+ticker);
//                    createSellOrder();
//                }
                }
            }
        }


    }
//**********************************************************************************************************************

}
