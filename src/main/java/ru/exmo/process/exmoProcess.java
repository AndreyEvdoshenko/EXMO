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
    private tradingApi tradingApi;

    @Autowired
    private tradingDAO dao;

    private volatile Map<String, exmoTicker> ticker;


    public exmoProcess() {

    }

    @PostConstruct
    public void initProcess() {
        logger.info("@PostConstruct initProcess() invoke");
       // updateCurrnecPair();
        tradingApi.returnUserOpenOrders();
//        tradingApi.returnUserInfo();
//        exmoOrderCreate order = new exmoOrderCreate();
//        order.setPair(currencyPair.BTC_USD.getName());
//        order.setQuantity(0.001996F);
//        order.setType(exmoTypeOrder.market_sell);
//        order.setPrice(0);
//        tradingApi.createOrder(order);
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
