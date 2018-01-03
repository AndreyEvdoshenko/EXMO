package ru.exmo.api.publicApi;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.exmo.model.data.*;
import ru.exmo.process.exmoProcess;
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

    private  final Logger logger = Logger.getLogger(exmoProcess.class);

    @Autowired
    HTTPClient httpClient;

    @Override
    public List<exmoTrade> returnTrades() {
        logger.info("invoke returnTrades()");
        return null;
    }

    @Override
    public Map<currencyPair, exmoOrderBook> returnOrderBook(int limit) throws IOException {

        logger.info("invoke returnOrderBook()");
        Map<currencyPair, exmoOrderBook> orderBook = new HashMap<>();

        StringBuilder URL = new StringBuilder(EXMO_ORDER_BOOK_URL);
        URL.append("?pair=");
        for (Field field : currencyPair.class.getFields()) {
            currencyPair pair = currencyPair.valueOf(field.getName());
            URL.append(pair.name()).append(",");
        }
        URL.deleteCharAt(URL.length() - 1);

        try {
            String resultJson = httpClient.getHttp(URL.toString(), null);
            JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(resultJson);
            for (Field field :  currencyPair.class.getFields()) {
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
                for(int i=0;i<bids.size();i++){
                    JSONArray current = (JSONArray) bids.get(i);
                    orderBookPair.addBid(new BigDecimal(String.valueOf(current.get(0))),
                            new BigDecimal(String.valueOf(current.get(1))),
                            new BigDecimal(String.valueOf(current.get(2))));
                }
                JSONArray asks = (JSONArray) currentExmoPair.get("ask");
                for(int i=0;i<asks.size();i++){
                    JSONArray current = (JSONArray) asks.get(i);
                    orderBookPair.addAsk(new BigDecimal(String.valueOf(current.get(0))),
                            new BigDecimal(String.valueOf(current.get(1))),
                            new BigDecimal(String.valueOf(current.get(2))));
                }
                orderBook.put(pair,orderBookPair);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return orderBook;
    }

    @Override
    public List<exmoTicker> returnTicker() {
        logger.info("invoke returnTicker()");
        List<exmoTicker> listTicker = new ArrayList<>();
        try {
            String resultJson = httpClient.getHttp(EXMO_TICKER_URL, null);
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
                ticker.setLast_trade(new BigDecimal(String.valueOf(currentExmoPair.get("last_trade"))));
                ticker.setBuy_price(new BigDecimal(String.valueOf(currentExmoPair.get("buy_price"))));
                ticker.setSell_price(new BigDecimal(String.valueOf(currentExmoPair.get("sell_price"))));
                ticker.setUpdated(updatedTime);
                listTicker.add(ticker);
                logger.info("ticker "+ticker.getPair()+": " + ticker);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return listTicker;
    }

    @Override
    public Map<currencyPair, currencyPairSettings> returnPairSettings() throws IOException {
        logger.info("invoke returnPairSettings()");
        Map<currencyPair, currencyPairSettings> pairSettings = new HashMap<>();
        String URL = EXMO_PAIR_SETTINGS_URL;
        try {
            String resultJson = httpClient.getHttp(URL, null);
            JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(resultJson);
            for (Field field : currencyPair.class.getFields()) {
                currencyPair currentPair = currencyPair.valueOf(field.getName());
                Map<String, String> currentExmoPair = (Map<String, String>) jsonObject.get(currentPair.name());
                currencyPairSettings currencyPairSettings = new currencyPairSettings();
                currencyPairSettings.setPair(currentPair.name());
                currencyPairSettings.setMin_quantity(new BigDecimal(currentExmoPair.get("min_quantity")));
                currencyPairSettings.setMax_quantity(new BigDecimal(currentExmoPair.get("max_quantity")));
                currencyPairSettings.setMin_price(new BigDecimal(currentExmoPair.get("min_price")));
                currencyPairSettings.setMax_price(new BigDecimal(currentExmoPair.get("max_price")));
                currencyPairSettings.setMin_amount(new BigDecimal(currentExmoPair.get("min_amount")));
                currencyPairSettings.setMax_amount(new BigDecimal(currentExmoPair.get("max_amount")));
                pairSettings.put(currentPair, currencyPairSettings);
                logger.info(currentPair.name() +": "+ currencyPairSettings);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pairSettings;
    }

}