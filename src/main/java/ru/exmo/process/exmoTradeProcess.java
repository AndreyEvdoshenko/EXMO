package ru.exmo.process;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.exmo.api.publicApi.publicApi;
import ru.exmo.api.tradingApi.tradingApi;
import ru.exmo.dao.tradingDAO;
import ru.exmo.model.data.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.exmo.model.data.currencyPairCondition.SELL;
import static ru.exmo.model.data.currencyPairCondition.BUY;
import static ru.exmo.model.data.currencyPairCondition.CREATE_SELL_ORDER;
import static ru.exmo.model.data.currencyPairCondition.CREATE_BUY_ORDER;

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
    private publicApi publicApi;

    @Autowired
    private exmoTickerProcess tickerProcess;

    private ExecutorService executorTradePair;

    private Map<String, tradePair> activeTradePair;

    public exmoTradeProcess() {

    }

    @PostConstruct
    public void initTickerProcess() {
        logger.info("@PostConstruct initProcess() invoke");
        executorTradePair = Executors.newCachedThreadPool();
        activeTradePair = new ConcurrentHashMap<>();
    }

    public void runTradePair(currencyPair pair) {
        logger.info(pair.getName() + ": запуск потока на торговлю");
        tradePair tradePair = new tradePair(pair);
        Thread currentTradePair =  new Thread(tradePair);
        activeTradePair.put(pair.name(), tradePair);
        executorTradePair.execute(currentTradePair);
    }

    public void stopTradePair(currencyPair pair) {
        if (activeTradePair.containsKey(pair.name())) {
            tradePair currentTradePair = activeTradePair.get(pair.name());
            currentTradePair.interrupt();
            activeTradePair.remove(pair.name());
        } else {
            logger.error(pair.getName() + ": поток на торговлю не найденб останавливать нечего");
        }

    }

    public void debugEnabled(currencyPair pair, boolean enabled){
        if (activeTradePair.containsKey(pair.name())) {
            tradePair currentTradePair = activeTradePair.get(pair.name());
            currentTradePair.debugEnabled(enabled);
            logger.info(pair.getName() + ": дебаг режим "+enabled);
        }else{
            logger.error(pair.getName() + ": поток на торговлю не найден ");
        }
    }
//**********************************************************************************************************************
    private class tradePair implements Runnable {


        private currencyPair pair;
        private volatile exmoTicker ticker;

        private boolean interrupted = false;
        private boolean debugEnabled = false;

        tradePair(currencyPair pair) {
            this.pair = pair;
        }

        private void init() {
            logger.info(pair.getName() + ": иницилизация настроект для торговли по паре....");
            publicApi.loadPairSettings(pair);
            dao.initCurrencyPairSettings(pair);
            Map<String, List<exmoUserOpenOrders>> userOpenOrders = tradingApi.returnUserOpenOrders();
            if (userOpenOrders.containsKey(pair.name())) {
                exmoUserOpenOrders openOrder = userOpenOrders.get(pair.name()).get(0);
                if ("sell".equals(openOrder.getType())) {
                    logger.info(pair.getName() + ": имеются не закрытые ордера на продажу" + openOrder);
                    pair.setCurrentCondition(currencyPairCondition.CREATE_SELL_ORDER);
                } else if ("buy".equals(openOrder.getType())) {
                    logger.info(pair.getName() + ": имеются не закрытые ордера на покупку" + openOrder);
                    pair.setCurrentCondition(currencyPairCondition.CREATE_BUY_ORDER);
                }
            } else {
                exmoTrade lastTrade = tradingApi.returnLastUserTrades(pair);
                if ("sell".equals(lastTrade.getType())) {
                    logger.info(pair.getName() + ": последняя операция - продажа актива");
                    pair.setCurrentCondition(currencyPairCondition.SELL);
                } else if ("buy".equals(lastTrade.getType())) {
                    logger.info(pair.getName() + ": последняя операция - покупка актива");
                    pair.setBuyValues(Float.parseFloat(lastTrade.getPrice()));
                    pair.setCurrentCondition(currencyPairCondition.BUY);
                }
            }
        }

        public void interrupt() {
            this.interrupted = true;
        }

        public void debugEnabled(boolean enabled){
            this.debugEnabled = enabled;
        }

        private boolean createBuyOrder(float buyPrice) {
            exmoOrderCreate buyOrder = new exmoOrderCreate();
            buyOrder.setPair(pair.getName());
            buyOrder.setQuantity(pair.getMin_quantity() * 2);
            buyOrder.setType(exmoTypeOrder.buy);
            buyOrder.setPrice(buyPrice);
            buyOrder.setMedium_price(pair.getMediumValues());
            buyOrder.setExclusion_medium(pair.getExclusion_medium());
            buyOrder.setExclusion_buy(0);
            buyOrder = tradingApi.createOrder(buyOrder);
            if ("true".equals(buyOrder.getResult())) {
                dao.createOrder(buyOrder);
                pair.setBuyOrderId(buyOrder.getOrder_id());
                logger.info(pair.name() + ": ордер на покупку выставлен");
                return true;
            } else {
                error(pair.name() + ": ордер на покупку  не получилось, причина " + buyOrder.getError());
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
                info(pair.getName() + ": цена упала: средняя за 5 минут: " + mediumValue + ", текущаяя: " + currentValue + ", отклонение: -" + deviation);
                if (deviation > percentageOfExclusionBuy) {
                    logger.info(pair.getName() + ": выставляем ордер на покупку по цене  " + currentValue);
                    logger.info("ticker " + pair.getName() + ": " + ticker);
                    boolean isBuyOrderCreate = createBuyOrder(currentValue);
                    if (isBuyOrderCreate) {
                        pair.setBuy(true);
                        pair.setCurrentCondition(CREATE_BUY_ORDER);
                        pair.setExclusion_medium(deviation);
                        pair.setBuyValues(currentValue);
                        pair.setSellValues(currentValue + (currentValue / 100 * pair.getPercentageOfExclusionSell()));
                    }
                }
            } else if (mediumValue - currentValue < 0) {
                deviation = ((currentValue * 100) / mediumValue) - 100;
                info(pair.getName() + ": цена поднялась: средняя за 5 минут: " + mediumValue + ", текущаяя: " + currentValue + ", отклонение: " + deviation);
            } else {
                info(pair.getName() + ": цена за 5 минут не изменилась от средней: " + mediumValue + ", текущаяя: " + currentValue);
            }
        }

        private boolean createSellOrder(float sellPrice) {
            exmoUserInfo user = tradingApi.returnUserInfo();

            exmoOrderCreate sellOrder = new exmoOrderCreate();
            sellOrder.setPair(pair.getName());
            String str = String.valueOf(user.getBalances().get(pair.getName1()));
            Float amount = Float.valueOf(str);
            sellOrder.setQuantity(amount); // продавать все что есть
            sellOrder.setType(exmoTypeOrder.sell);
            sellOrder.setPrice(sellPrice);
            sellOrder.setMedium_price(pair.getMediumValues());
            sellOrder.setExclusion_medium(pair.getExclusion_medium());
            sellOrder.setExclusion_buy(pair.getExclusion_buy());
            sellOrder = tradingApi.createOrder(sellOrder);
            if ("true".equals(sellOrder.getResult())) {
                pair.setBuyOrderId("0");
                dao.createOrder(sellOrder);
                logger.info(pair.name() + ": ордер на продажу  выставлен");
                return true;
            } else {
                error(pair.name() + ": ордер на продажу не получилось, причина " + sellOrder.getError());
                return false;
            }
        }

        private void sell() {
            float currentValue = ticker.getSell_price();
            float buyValue = pair.getBuyValues();
            float percentageOfExclusionSell = pair.getPercentageOfExclusionSell();
            float getPercentageOfNoReturn = pair.getPercentageOfNoReturn();
            if (buyValue - currentValue > 0) {
                float deviation = 100 - ((currentValue * 100) / buyValue);
                info(pair.getName() + ": цена ниже закупки: " + buyValue + ", текущаяя:  " + currentValue + ", отклонение: -" + deviation);
                if (deviation > getPercentageOfNoReturn) {
                    logger.info(pair.getName() + ": выставляем ордер на продажу по цене, выходим в минус" + currentValue);
                    boolean isSellOrderCreate = createSellOrder(currentValue);
                    if (isSellOrderCreate) {
                        pair.setSellProfit(false);
                        pair.setExclusion_buy(deviation);
                        pair.setSellValues(currentValue);
                        pair.setCurrentCondition(CREATE_SELL_ORDER);
                    }
                }
            } else if (buyValue - currentValue < 0) {
                float deviation = ((currentValue * 100) / buyValue) - 100;
                info(pair.getName() + ": цена выше закупки: " + buyValue + ", текущаяя: " + currentValue + ", отклонение: " + deviation);
                if (deviation > percentageOfExclusionSell) {
                    logger.info(pair.getName() + ": выставляем ордер на продажу по цене, выходим в плюс " + currentValue);
                    boolean isSellOrderCreate = createSellOrder(currentValue);
                    if (isSellOrderCreate) {
                        pair.setSellProfit(true);
                        pair.setExclusion_buy(deviation);
                        pair.setSellValues(currentValue);
                        pair.setCurrentCondition(CREATE_SELL_ORDER);
                    }
                }
            } else {
                info(pair.getName() + ": цена не изменилась от закупки: " + buyValue + ", текущаяя: " + currentValue);
            }
        }

        @Override
        public void run() {
            init();
            while (!interrupted) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    info(pair.getName() + ": поток на торговлю остановлен");
                }
                ticker = tickerProcess.returnTicker(pair);
                switch (pair.getCurrentCondition()) {
                    case SELL:
                        buy();
                        break;
                    case CREATE_BUY_ORDER:
                        checkBuyOrderNotClose();
                        break;
                    case BUY:
                        sell();
                        break;
                    case CREATE_SELL_ORDER:
                        checkSellOrderNotClose();
                        break;
                }
            }
            logger.info(pair.getName() + ": поток на торговлю завершился interrupted = "+interrupted);
        }

        private void checkBuyOrderNotClose() {
            Map<String, List<exmoUserOpenOrders>> userOpenOrders = tradingApi.returnUserOpenOrders();
            if (userOpenOrders.containsKey(pair.name())) {
                info(pair.getName() + ": имеются не закрытые ордера на покупку: " + userOpenOrders.get(pair.name()));
                //todo проверять не надо ли их отменять
            } else {
                pair.setCurrentCondition(currencyPairCondition.BUY);            //куплено, ордера закрыты
                info(pair.getName() + ": ордера на покупку закрыты");
            }
        }

        private void checkSellOrderNotClose() {
            Map<String, List<exmoUserOpenOrders>> userOpenOrders = tradingApi.returnUserOpenOrders();
            if (userOpenOrders.containsKey(pair.name())) {
                info(pair.getName() + ": имеются не закрытые ордера на продажу: " + userOpenOrders.get(pair.name()));
                //todo проверять не надо ли их отменять
            } else {
                pair.setCurrentCondition(currencyPairCondition.SELL);            //продано, ордера закрыты
                info(pair.getName() + ": ордера на продажу закрыты");
            }
        }

        private void info(String log){
            if(debugEnabled){
                logger.info(log);
            }
        }

        private void error(String log){
                logger.error(log);
        }


    }
//**********************************************************************************************************************

}
