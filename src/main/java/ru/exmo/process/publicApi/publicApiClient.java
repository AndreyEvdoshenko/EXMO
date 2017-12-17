package ru.exmo.process.publicApi;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.omg.CORBA.MARSHAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.exmo.model.data.*;
import ru.exmo.model.publicApi.publicApi;
import ru.exmo.process.utils.HTTPClient;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrash on 17.12.2017.
 */
@Component
public class publicApiClient implements publicApi {

    @Autowired
    HTTPClient httpClient;

    @Override
    public List<exmoTrade> returnTrades(List<currencyPair> pairs) {
        return null;
    }

    @Override
    public List<exmoOrderBook> returnOrderBook(List<currencyPair> pairs, int limit) {
        return null;
    }

    @Override
    public exmoTicker returnTicker() {
        return null;
    }

    @Override
    public Map<currencyPair, currencyPairSettings> returnPairSettings() throws IOException {
        Map<currencyPair, currencyPairSettings> pairSettings = new HashMap<>();
        String URL = EXMO_PAIR_SETTINGS_URL;
        try {
            String resultJson = httpClient.getHttp(URL, null);
            JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(resultJson);
            for (Field field : currencyPair.class.getFields()) {
                currencyPair currentPair = currencyPair.valueOf(field.getName());
                Map<String, String> currentExmoPair = (Map<String, String>) jsonObject.get(currentPair.name());
                currencyPairSettings currencyPairSettings = new currencyPairSettings();
                currencyPairSettings.setMin_quantity(new BigDecimal(currentExmoPair.get("min_quantity")));
                currencyPairSettings.setMax_quantity(new BigDecimal(currentExmoPair.get("max_quantity")));
                currencyPairSettings.setMin_price(new BigDecimal(currentExmoPair.get("min_price")));
                currencyPairSettings.setMax_price(new BigDecimal(currentExmoPair.get("max_price")));
                currencyPairSettings.setMin_amount(new BigDecimal(currentExmoPair.get("min_amount")));
                currencyPairSettings.setMax_amount(new BigDecimal(currentExmoPair.get("max_amount")));
                pairSettings.put(currentPair, currencyPairSettings);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pairSettings;
    }

}