package ru.exmo.dao;

import ru.exmo.model.data.currencyPair;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrash on 07.01.2018.
 */
public interface tradingDAO {
    List<Map<String, Object>> dataOverPeriodOfTime(Timestamp startTime, Timestamp endTime, List<currencyPair> pairs);
}
