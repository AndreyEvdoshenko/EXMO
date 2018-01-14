package ru.exmo.process;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.exmo.api.tradingApi.tradingApi;
import ru.exmo.api.publicApi.publicApi;
import ru.exmo.dao.tradingDAO;
import ru.exmo.model.data.currencyPair;
import ru.exmo.model.data.exmoOrderCreate;
import ru.exmo.model.data.exmoTicker;
import ru.exmo.model.data.exmoTypeOrder;
import ru.exmo.model.data.stub.stubUser;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Andrash on 17.12.2017.
 */

@Component
@EnableScheduling
public class exmoProcess {
    private final Logger logger = Logger.getLogger(exmoProcess.class);


    @Autowired
    private publicApi publicApi;

    @Autowired
    private exmoTradeProcess exmoTradeProcess;

    @Autowired
    private tradingApi tradingApi;

    @Autowired
    private tradingDAO dao;

    private volatile Map<String, exmoTicker> ticker;


    public exmoProcess() {

    }

    @PostConstruct
    public void initProcess() {
        logger.info("@PostConstruct initProcess() invoke");
        updateCurrnecPair();
        evaluateMedium();
        tradingApi.returnUserInfo();
//        exmoOrderCreate order = new exmoOrderCreate();
//        order.setPair(currencyPair.BTC_USD.getName());
//        order.setQuantity(0.001996F);
//        order.setType(exmoTypeOrder.market_sell);
//        order.setPrice(0);
//        tradingApi.createOrder(order);
    }

    @Scheduled(fixedRate = 600000)
    private void evaluateMedium() {
        int interval = 600000;
        float medium = 0;
        synchronized (exmoProcess.class) {
            Map<String, List<Float>> tradeHistory = dao.dataOverPeriodOfTime(new Timestamp(System.currentTimeMillis() - interval),
                    new Timestamp(System.currentTimeMillis()),
                    Arrays.asList(currencyPair.BTC_USD));
            for (Map.Entry entry : tradeHistory.entrySet()) {
                String pair = (String) entry.getKey();
                currencyPair currentPair = currencyPair.valueOf(pair);
                medium = 0;
                List<Float> list = (List<Float>) entry.getValue();
                for (int i = 0; i < list.size(); i++) {
                    medium += Float.valueOf(String.valueOf(list.get(i)));
                }
                currentPair.setMediumValues(medium / list.size());
                logger.info("среднее для валютной пары " + pair + " за последние " + interval / 60000 + " минут = " + medium / list.size());
            }
        }
    }


    @Scheduled(fixedRate = 2000)
    public void process() {
        ticker = publicApi.returnTicker();
        analise();
    }


    private synchronized void analise() {
        for (currencyPair pair : currencyPair.values()) {
            if (pair.isActive()) {
                if (!pair.isBuy()) {
                    boolean buy = buyAnalise(pair);
                    if (buy) {
                        logger.info("ticker " + ticker.get(pair.name()) + ": " + ticker);
                        exmoTradeProcess.buy(pair);
                    }
                } else {
                    boolean sell = sellAnalise(pair);
                    if (sell) {
                        logger.info("ticker " + ticker.get(pair.name()) + ": " + ticker);
                        exmoTradeProcess.sell(pair);
                    }
                }
            }
        }
    }


    private boolean buyAnalise(currencyPair pair) {
        float currentValue = ticker.get(pair.name()).getSell_price();
        float mediumValue = pair.getMediumValues();
        double percentageOfExclusionBuy = pair.getPercentageOfExclusionBuy();
        double deviation;
        if (mediumValue - currentValue > 0) {
            deviation = 100 - ((currentValue * 100) / mediumValue);
            logger.info(pair.getName() + " цена упала: средняя за 5 минут: " + mediumValue + ", текущаяя: " + currentValue + ", отклонение: -" + deviation);
            if (deviation > percentageOfExclusionBuy) {
                pair.setExclusion_medium((float) deviation);
                pair.setBuyValues(currentValue);
                logger.info(pair.getName() + " выставляем ордер на покупку по цене  " + currentValue);
                return true;
            }
        } else if (mediumValue - currentValue < 0) {
            deviation = ((currentValue * 100) / mediumValue) - 100;
            logger.info(pair.getName() + " цена поднялась: средняя за 5 минут: " + mediumValue + ", текущаяя: " + currentValue + ", отклонение: " + deviation);
        } else {
            logger.info(pair.getName() + " цена за 5 минут не изменилась от средней: " + mediumValue + ", текущаяя: " + currentValue);
        }
        return false;
    }

    private boolean sellAnalise(currencyPair pair) {
        float currentValue = ticker.get(pair.name()).getBuy_price();
        float buyValue = pair.getBuyValues();
        float percentageOfExclusionSell = (float) pair.getPercentageOfExclusionSell();
        float getPercentageOfNoReturn = (float) pair.getPercentageOfNoReturn();
        if (buyValue - currentValue > 0) {
            double deviation = 100 - ((currentValue * 100) / buyValue);
            logger.info(pair.getName() + " цена упала от закупки: " + buyValue + ", текущаяя:  " + currentValue + ", отклонение: -" + deviation);
            if (deviation > getPercentageOfNoReturn) {
                pair.setExclusion_buy((float) deviation);
                pair.setSellValues(currentValue);
                pair.setSellProfit(false);
                logger.info(pair.getName() + " выставляем ордер на продажу по цене, выходим в минус" + buyValue);
                return true;
            }
        } else if (buyValue - currentValue < 0) {
            double deviation = ((currentValue * 100) / buyValue) - 100;
            logger.info(pair.getName() + " цена поднялась от закупки: " + buyValue + ", текущаяя: " + currentValue + ", отклонение: " + deviation);
            if (deviation > percentageOfExclusionSell) {
                pair.setExclusion_buy((float) deviation);
                pair.setSellValues(currentValue);
                pair.setSellProfit(true);
                logger.info(pair.getName() + " выставляем ордер на продажу по цене, выходим в плюс " + buyValue);
                return true;
            }
        } else {
            logger.info(pair.getName() + " цена не изменилась от закупки: " + buyValue + ", текущаяя: " + currentValue);
        }
        return false;
    }

    private void updateCurrnecPair() {
        publicApi.loadPairSettings();
        dao.initCurrencyPairSettings();
    }


}
