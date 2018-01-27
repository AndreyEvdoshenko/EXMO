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
    }


}
