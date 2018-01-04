package ru.exmo.process;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.exmo.api.tradingApi.tradingApi;
import ru.exmo.api.publicApi.publicApi;
import javax.annotation.PostConstruct;

/**
 * Created by Andrash on 17.12.2017.
 */

@Component
public class exmoProcess {
    private  final Logger logger = Logger.getLogger(exmoProcess.class);

    @Autowired
    private publicApi publicApi;

    @Autowired
    private tradingApi tradingApi;

    public exmoProcess()  {

    }

    @PostConstruct
    public void initProcess(){
            logger.info("@PostConstruct initProcess() invoke");
//            publicApi.returnTrades();
//            publicApi.returnPairSettings();
//            publicApi.returnOrderBook(100);
//            publicApi.returnTicker();
            tradingApi.returnUserInfo();

    }

}
