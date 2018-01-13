package ru.exmo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.exmo.model.data.currencyPair;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Andrash on 07.01.2018.
 */
@Component
public class tradingClientDAO implements tradingDAO {


    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Map<String, List<Float>> dataOverPeriodOfTime(Timestamp startTime, Timestamp endTime, List<currencyPair> pairs) {

        Map<String, List<Float>> tradeHistory = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        String sql = "select last_trade,pair from tikers where updated > :startTime and updated < :endTime";
        sql += " and pair = 'LTC_RUB' or pair ='LTC_USD'";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, params);
        for (int i = 0; i < list.size(); i++) {
            String pair = (String) list.get(i).get("pair");
            List<Float> listTrade;
            if (tradeHistory.get(pair) != null) {
                listTrade = tradeHistory.get(pair);
            } else {
                listTrade = new ArrayList<>();
            }
            listTrade.add(Float.valueOf(String.valueOf(list.get(i).get("last_trade"))));
            tradeHistory.put(pair, listTrade);
        }
        return tradeHistory;
    }

}
