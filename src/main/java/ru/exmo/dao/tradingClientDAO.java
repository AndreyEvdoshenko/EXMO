package ru.exmo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.exmo.model.data.currencyPair;
import ru.exmo.model.data.exmoOrderCreate;

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

        String sql = "select sell_price,buy_price,last_trade,pair from tikers where updated > :startTime and updated < :endTime";
//        sql += " and pair = 'LTC_RUB'" +
//                " or pair ='LTC_USD'" +
//                " or pair ='LTC_EUR'" +
//                " or pair ='BTC_RUB'" +
//                " or pair ='BTC_USD'" +
//                " or pair ='BTC_EUR'" +
//                " or pair ='ETH_RUB'" +
//                " or pair ='ETH_USD'" +
//                " or pair ='ETH_EUR'" ;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, params);
        for (int i = 0; i < list.size(); i++) {
            String pair = (String) list.get(i).get("pair");
            List<Float> listTrade;
            if (tradeHistory.get(pair) != null) {
                listTrade = tradeHistory.get(pair);
            } else {
                listTrade = new ArrayList<>();
            }
            listTrade.add(Float.valueOf(String.valueOf(list.get(i).get("sell_price"))));
            //listTrade.add(Float.valueOf(String.valueOf(list.get(i).get("buy_price"))));
            //listTrade.add(Float.valueOf(String.valueOf(list.get(i).get("last_trade"))));
            tradeHistory.put(pair, listTrade);
        }
        return tradeHistory;
    }

    @Override
    public void createOrder(exmoOrderCreate order) {

        Map<String, Object> params = new HashMap<>();
        params.put("pair", order.getPair());
        params.put("type", order.getType());
        params.put("quantity", order.getQuantity());
        params.put("price", order.getPrice());
        params.put("medium_price", order.getMedium_price());
        params.put("exclusion_medium", order.getExclusion_medium());
        params.put("exclusion_buy", order.getExclusion_buy());
        params.put("profit", order.getProfit());
        params.put("updated", order.getUpdated());

        String sql = "insert into exmoOrders (pair,type,quantity,price,medium_price,exclusion_medium,exclusion_buy,profit,updated) " +
                "values (:pair," +
                ":type," +
                ":quantity," +
                ":price," +
                ":medium_price," +
                ":exclusion_medium," +
                ":exclusion_buy," +
                ":profit," +
                ":updated)";

        jdbcTemplate.update(sql,params);
        //todo update купленного ордера на статус close если это на продажу
    }

    @Override
    public void initCurrencyPairSettings() {
        Map<String, Object> params = new HashMap<>();
        String sql = "select * from exmoCurrencyairSettings";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,params);
        for (int i = 0; i < list.size(); i++) {
            currencyPair currentPair = currencyPair.valueOf((String)list.get(i).get("pair"));
            currentPair.setPercentageOfExclusionBuy(Float.valueOf((String) list.get(i).get("percentageofexclusionbuy")));
            currentPair.setPercentageOfExclusionSell(Float.valueOf((String) list.get(i).get("percentageofexclusionsell")));
            currentPair.setPercentageOfNoReturn(Float.valueOf((String) list.get(i).get("percentageofnoreturn")));
            currentPair.setActive(Boolean.valueOf((String) list.get(i).get("active")));
            }
    }

}
