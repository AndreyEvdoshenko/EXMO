package ru.exmo.process;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.exmo.model.data.currencyPair;

/**
 * Created by Andrash on 09.01.2018.
 */
@Component
public class exmoTradeProcess {

    private final Logger logger = Logger.getLogger(exmoTradeProcess.class);

    public exmoTradeProcess(){

    }
    public void buy(currencyPair pair) {
        pair.setBuy(true);
        logger.info("Ордер на покупку " + pair.name() + " выставлен");
    }

    public void sell(currencyPair pair) {
        pair.setBuy(false);
        logger.info("ордер на продажу " + pair.name() + " выставлен");
    }

}
