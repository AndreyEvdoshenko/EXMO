package ru.exmo.api.publicApi;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.exmo.model.data.*;
import ru.exmo.utils.HTTPClient;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrash on 17.12.2017.
 */
@Component
public class publicApiClient implements publicApi {

    private final Logger logger = Logger.getLogger(publicApiClient.class);

    @Autowired
    private HTTPClient httpClient;

    @Override
    public Map<currencyPair, List<exmoTrade>> returnTrades() {
        logger.info("invoke returnTrades()");
        Map<currencyPair, List<exmoTrade>> tradeMap = new HashMap<>();

        StringBuilder URL = new StringBuilder(publicApiMethods.EXMO_TRADES.getUrl());
        URL.append("?pair=");
        for (Field field : currencyPair.class.getFields()) {
            currencyPair pair = currencyPair.valueOf(field.getName());
            URL.append(pair.name()).append(",");
        }
        URL.deleteCharAt(URL.length() - 1);
        try {
            String resultJson = httpClient.getHttp(URL.toString(), null);
            JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(resultJson);
            for (Field field : currencyPair.class.getFields()) {
                currencyPair pair = currencyPair.valueOf(field.getName());
                JSONArray exmoTradesList = (JSONArray) jsonObject.get(pair.name());
                List<exmoTrade> pairTradeList = new ArrayList<>();
                for (int i = 0; i < exmoTradesList.size(); i++) {
                    exmoTrade trade = new exmoTrade();
                    Map<String, Object> currentExmoTrade = (Map<String, Object>) exmoTradesList.get(i);

                    trade.setPair(pair.name());
                    trade.setTrade_id(String.valueOf(currentExmoTrade.get("trade_id")));
                    trade.setType((String) currentExmoTrade.get("type"));
                    trade.setPrice((String) currentExmoTrade.get("price"));
                    trade.setQuantity((String) currentExmoTrade.get("quantity"));
                    trade.setAmount((String) currentExmoTrade.get("amount"));
                    trade.setDate(String.valueOf(currentExmoTrade.get("date")));

                    pairTradeList.add(trade);
                }
                tradeMap.put(pair, pairTradeList);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return tradeMap;
    }

    @Override
    public Map<currencyPair, exmoOrderBook> returnOrderBook(int limit) {

        logger.info("invoke returnOrderBook()");
        Map<currencyPair, exmoOrderBook> orderBook = new HashMap<>();

        StringBuilder URL = new StringBuilder(publicApiMethods.EXMO_ORDER_BOOK.getUrl());
        URL.append("?pair=");
        for (Field field : currencyPair.class.getFields()) {
            currencyPair pair = currencyPair.valueOf(field.getName());
            URL.append(pair.name()).append(",");
        }
        URL.deleteCharAt(URL.length() - 1);

        try {
            String resultJson = httpClient.getHttp(URL.toString(), null);
            JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(resultJson);
            for (Field field : currencyPair.class.getFields()) {
                currencyPair pair = currencyPair.valueOf(field.getName());
                Map<String, Object> currentExmoPair = (Map<String, Object>) jsonObject.get(pair.name());
                exmoOrderBook orderBookPair = new exmoOrderBook();

                orderBookPair.setAsk_quantity(new BigDecimal(String.valueOf(currentExmoPair.get("ask_quantity"))));
                orderBookPair.setAsk_amount(new BigDecimal(String.valueOf(currentExmoPair.get("ask_amount"))));
                orderBookPair.setAsk_top(new BigDecimal(String.valueOf(currentExmoPair.get("ask_top"))));
                orderBookPair.setBid_quantity(new BigDecimal(String.valueOf(currentExmoPair.get("bid_quantity"))));
                orderBookPair.setBid_amount(new BigDecimal(String.valueOf(currentExmoPair.get("bid_amount"))));
                orderBookPair.setBid_top(new BigDecimal(String.valueOf(currentExmoPair.get("bid_top"))));
                orderBookPair.clearBid();
                orderBookPair.clearAsk();

                JSONArray bids = (JSONArray) currentExmoPair.get("bid");
                for (int i = 0; i < bids.size(); i++) {
                    JSONArray current = (JSONArray) bids.get(i);
                    orderBookPair.addBid(new BigDecimal(String.valueOf(current.get(0))),
                            new BigDecimal(String.valueOf(current.get(1))),
                            new BigDecimal(String.valueOf(current.get(2))));
                }
                JSONArray asks = (JSONArray) currentExmoPair.get("ask");
                for (int i = 0; i < asks.size(); i++) {
                    JSONArray current = (JSONArray) asks.get(i);
                    orderBookPair.addAsk(new BigDecimal(String.valueOf(current.get(0))),
                            new BigDecimal(String.valueOf(current.get(1))),
                            new BigDecimal(String.valueOf(current.get(2))));
                }
                orderBook.put(pair, orderBookPair);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return orderBook;
    }

    @Override
    public Map<String, exmoTicker> returnTicker() {
      //  logger.info("invoke returnTicker()");
        Map<String,exmoTicker> listTicker = new HashMap<>();
        try {
            String resultJson = httpClient.getHttp(publicApiMethods.EXMO_TICKER.getUrl(), null);
            JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(resultJson);
            Timestamp updatedTime = new Timestamp(System.currentTimeMillis());
            for (Field field : currencyPair.class.getFields()) {
                currencyPair currentPair = currencyPair.valueOf(field.getName());
                Map<String, String> currentExmoPair = (Map<String, String>) jsonObject.get(currentPair.name());
                exmoTicker ticker = new exmoTicker();
                ticker.setPair(currentPair.name());
                ticker.setHigh(new BigDecimal(String.valueOf(currentExmoPair.get("high"))));
                ticker.setLow(new BigDecimal(String.valueOf(currentExmoPair.get("low"))));
                ticker.setAvg(new BigDecimal(String.valueOf(currentExmoPair.get("avg"))));
                ticker.setVol(new BigDecimal(String.valueOf(currentExmoPair.get("vol"))));
                ticker.setVol_curr(new BigDecimal(String.valueOf(currentExmoPair.get("vol_curr"))));
                //ticker.setLast_trade(new BigDecimal(String.valueOf(currentExmoPair.get("last_trade"))));
                ticker.setLast_trade(Float.valueOf(String.valueOf(currentExmoPair.get("last_trade"))));
                //ticker.setBuy_price(new BigDecimal(String.valueOf(currentExmoPair.get("buy_price"))));
                ticker.setBuy_price(Float.valueOf(String.valueOf(currentExmoPair.get("buy_price"))));
             //   ticker.setSell_price(new BigDecimal(String.valueOf(currentExmoPair.get("sell_price"))));
                ticker.setSell_price(Float.valueOf(String.valueOf(currentExmoPair.get("sell_price"))));
                ticker.setUpdated(updatedTime);
                listTicker.put(currentPair.name(),ticker);
                //logger.info("ticker " + ticker.getPair() + ": " + ticker);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return listTicker;
    }

    @Override
    public void loadPairSettings() {
        logger.info("invoke loadPairSettings all");
        String URL = publicApiMethods.EXMO_PAIR_SETTINGS.getUrl();
        try {
            String resultJson = httpClient.getHttp(URL, null);
            JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(resultJson);
            for (currencyPair currentPair : currencyPair.values()) {
                Map<String, String> currentExmoPair = (Map<String, String>) jsonObject.get(currentPair.name());
                currentPair.setPair(currentPair.name());
                currentPair.setMin_quantity(Float.valueOf(currentExmoPair.get("min_quantity")));
                currentPair.setMax_quantity(Float.valueOf(currentExmoPair.get("max_quantity")));
                currentPair.setMin_price(Float.valueOf(currentExmoPair.get("min_price")));
                currentPair.setMax_price(Float.valueOf(currentExmoPair.get("max_price")));
                currentPair.setMin_amount(Float.valueOf(currentExmoPair.get("min_amount")));
                currentPair.setMax_amount(Float.valueOf(currentExmoPair.get("max_amount")));
                logger.info(currentPair.name() + ": " + currentPair);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public synchronized void loadPairSettings(currencyPair pair) {
        logger.info(pair.name() + ": invoke loadPairSettings");
        String URL = publicApiMethods.EXMO_PAIR_SETTINGS.getUrl();
        try {
            String resultJson = httpClient.getHttp(URL, null);
            JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(resultJson);
            Map<String, String> currentExmoPair = (Map<String, String>) jsonObject.get(pair.name());
            if (currentExmoPair != null) {
                pair.setPair(pair.name());
                pair.setMin_quantity(Float.valueOf(currentExmoPair.get("min_quantity")));
                pair.setMax_quantity(Float.valueOf(currentExmoPair.get("max_quantity")));
                pair.setMin_price(Float.valueOf(currentExmoPair.get("min_price")));
                pair.setMax_price(Float.valueOf(currentExmoPair.get("max_price")));
                pair.setMin_amount(Float.valueOf(currentExmoPair.get("min_amount")));
                pair.setMax_amount(Float.valueOf(currentExmoPair.get("max_amount")));
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
    }

}