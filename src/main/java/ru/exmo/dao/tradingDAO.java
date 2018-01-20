package ru.exmo.dao;

import ru.exmo.model.data.currencyPair;
import ru.exmo.model.data.exmoOrderCreate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrash on 07.01.2018.
 */
public interface tradingDAO {

    Map<String, List<Float>> dataOverPeriodOfTime(Timestamp startTime, Timestamp endTime, List<currencyPair> pairs);
    void createOrder(exmoOrderCreate order);
    void initCurrencyPairSettings();
    void initCurrencyPairSettings(currencyPair pair);
    void updateCurrencyPairSettings(currencyPair pair);
}
