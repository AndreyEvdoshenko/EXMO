package ru.exmo.process;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.exmo.api.publicApi.publicApi;
import ru.exmo.api.tradingApi.tradingApi;
import ru.exmo.dao.tradingDAO;
import ru.exmo.model.data.currencyPair;
import ru.exmo.model.data.exmoTicker;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrash on 16.01.2018.
 */
@Component
@EnableScheduling
public class exmoTickerProcess {

    private final Logger logger = Logger.getLogger(exmoTickerProcess.class);
    private final String URL_TICKER = "http://localhost:8080/ticker";

    @Autowired
    private publicApi publicApi;

    RestTemplate restTemplate;
    @Autowired
    private tradingApi tradingApi;

    @Autowired
    private tradingDAO dao;

    private volatile Map<String, exmoTicker> ticker;

    @PostConstruct
    public void initTickerProcess() {
        logger.info("@PostConstruct initProcess() invoke");
        restTemplate = new RestTemplate();
        evaluateMedium();
        tradingApi.returnUserInfo();
    }

    @Scheduled(fixedRate = 600000)
    private void evaluateMedium() {
        int interval = 600000;
        float medium = 0;
        synchronized (exmoTickerProcess.class) {
            Map<String, List<Float>> tradeHistory = dao.dataOverPeriodOfTime(new Timestamp(System.currentTimeMillis() - interval),
                    new Timestamp(System.currentTimeMillis()),
                    Arrays.asList(currencyPair.BTC_USD));
            if(tradeHistory.isEmpty()){
                logger.warn("История торгов пуста!!! вероятно сломался тикер");
            }
            for (Map.Entry entry : tradeHistory.entrySet()) {
                String pair = (String) entry.getKey();
                currencyPair currentPair = currencyPair.valueOf(pair);
                medium = 0;
                List<Float> list = (List<Float>) entry.getValue();
                for (int i = 0; i < list.size(); i++) {
                    medium += Float.valueOf(String.valueOf(list.get(i)));
                }
                currentPair.setMediumValues(medium / list.size());
                logger.info(currentPair.name()+": среднее значение за последние " + interval / 60000 + " минут = " + medium / list.size());
            }
        }
    }



    public exmoTicker returnTicker(currencyPair pair){
        StringBuilder url = new StringBuilder(URL_TICKER);
        url.append("?pair=").append(pair.name());
        exmoTicker ticker = restTemplate.getForObject(url.toString(), exmoTicker.class);
        return ticker;
    }


}
