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
import ru.exmo.model.data.exmoTicker;
import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    tradingDAO dao;

    private volatile float mediumValue;
    private volatile float buyValue;           //цена по которой купили

    private boolean isBuy = false;

    public exmoProcess() {

    }

    @PostConstruct
    public void initProcess() {
        logger.info("@PostConstruct initProcess() invoke");
//            publicApi.returnTrades()
//            publicApi.returnPairSettings();
//            publicApi.returnOrderBook(100);
//            publicApi.returnTicker();
//            tradingApi.returnUserInfo();

    }

    @Scheduled(fixedRate = 600000)
    private void evaluateMedium() {
        int interval = 600000;
        float medium = 0;
        List<Map<String, Object>> list = dao.dataOverPeriodOfTime(new Timestamp(System.currentTimeMillis() - interval),
                new Timestamp(System.currentTimeMillis()),
                Arrays.asList(currencyPair.LTC_RUB));

        for (int i = 0; i < list.size(); i++) {
            medium += Double.valueOf(String.valueOf(list.get(i).get("last_trade")));
        }
        logger.info("среднее для валютной пары LTC_RUB за последние 5 минут = " + medium / list.size());
        mediumValue = medium / list.size();
    }


    @Scheduled(fixedRate = 2000)
    public void process() {

        double percentageOfExclusionBuy = 0.3; //процент отклонения для покупки (от среднего)
        float percentageOfExclusionSell = 1; //процент отклонения для продажи(от запучной цены)


        exmoTicker ticker = publicApi.returnTicker().get("LTC_RUB");
        float currentValue = ticker.getLast_trade();

        if (!isBuy) {
            if (mediumValue - currentValue > 0) {
                double deviation = 100 - ((currentValue * 100) / mediumValue);
                logger.info("Цена упала: средняя за 5 минут: " + mediumValue + ", текущаяя: " + currentValue + ", отклонение: -" + deviation);
                if(deviation>percentageOfExclusionBuy){
                    buyValue = currentValue;
                    logger.info("Выставляем ордер по цене "+buyValue);
                    buy();
                }
           } else if (mediumValue - currentValue < 0) {
                double deviation = ((currentValue * 100) / mediumValue) - 100;
                logger.info("Цена поднялась: средняя за 5 минут: " + mediumValue + ", текущаяя: " + currentValue + ", отклонение: " + deviation);
            } else {
                logger.info("Цена за 5 минут не изменилась от средней: " + mediumValue + ", текущаяя: " + currentValue);
            }
        }else{
            if (buyValue - currentValue > 0) {
                double deviation = 100 - ((currentValue * 100) / buyValue);
                logger.info("Цена упала от закупки: " + buyValue + ", текущаяя:  " + currentValue + ", отклонение: -" + deviation);
            } else if (buyValue - currentValue < 0) {
                double deviation = ((currentValue * 100) / buyValue) - 100;
                logger.info("Цена поднялась от закупки: " + buyValue + ", текущаяя: " + currentValue + ", отклонение: " + deviation);
                if(deviation>percentageOfExclusionSell){
                    buyValue = currentValue;
                    logger.info("Выставляем ордер по цене "+buyValue);
                    sell();
                }
            } else {
                logger.info("Цена не изменилась от закупки: " + buyValue + ", текущаяя: " + currentValue);
            }
        }
    }

    private void buy() {
        isBuy = true;
        logger.info("Ордер на покупку выставлен");
    }

    private void sell() {
        isBuy = false;
        logger.info("ордер на продажу выставлен");
    }


}
