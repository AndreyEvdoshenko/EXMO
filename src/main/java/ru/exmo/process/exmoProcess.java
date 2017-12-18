package ru.exmo.process;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.exmo.model.data.currencyPair;
import ru.exmo.model.data.currencyPairSettings;
import ru.exmo.model.data.exmoOrderBook;
import ru.exmo.api.publicApi.publicApi;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by Andrash on 17.12.2017.
 */

@Component
public class exmoProcess {
    private  final Logger logger = Logger.getLogger(exmoProcess.class);

    @Autowired
    publicApi publicApi;
    private final String ORDER_TYPE_BUY     = "buy";
    private final String ORDER_TYPE_SELL    = "sell";

    private Map<currencyPair,currencyPairSettings> pairs;
    private exmoOrderBook orderBook;

    public exmoProcess()  {

    }

    @PostConstruct
    public void initProcess(){
        try {
            logger.info("@PostConstruct initProcess() invoke");
            pairs =  publicApi.returnPairSettings();
            logger.info(pairs);
            publicApi.returnOrderBook(Arrays.asList(currencyPair.LTC_USD), 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private BigDecimal averagePrice(String type){
        BigDecimal agvPrice;
        if(type.equals(ORDER_TYPE_BUY)){
            agvPrice = orderBook.averagebBidPrice();
        }else{
            agvPrice = orderBook.averagebAskPrice();
        }
    return agvPrice;
    }



}
