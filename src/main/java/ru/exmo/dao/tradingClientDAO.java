package ru.exmo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.exmo.model.data.currencyPair;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrash on 07.01.2018.
 */
@Component
public class tradingClientDAO implements tradingDAO {


    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> dataOverPeriodOfTime(Timestamp startTime, Timestamp endTime, List<currencyPair> pairs) {

        Map<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        String sql = "select last_trade,pair from tikers where updated > :startTime and updated < :endTime";
        sql+= " and pair = 'LTC_RUB'";

        return jdbcTemplate.queryForList(sql, params);
    }

}
