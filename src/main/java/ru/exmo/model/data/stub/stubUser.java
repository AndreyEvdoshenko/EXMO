package ru.exmo.model.data.stub;

import org.springframework.stereotype.Component;
import ru.exmo.model.data.currencyPair;

import java.util.Map;

/**
 * Created by Andrash on 07.01.2018.
 */

public class stubUser {

    public Map<String, Float> balances;

    public stubUser() {
        balances.put("RUB", new Float("10000"));
        balances.put("USD", new Float("178"));
        balances.put("EUR", new Float("147"));
    }

    public void buy(currencyPair pair) {
        float sum = balances.get(pair.getName2());
        float sumBuy = sum / 10;
        sum = sum - sum / 10;
        balances.put(pair.getName2(), sum);
        float sumCrypto = sumBuy/pair.getBuyValues();
        balances.put(pair.getName1(),sumCrypto);
    }

    public void sell(currencyPair pair) {
        float sumCrypto = balances.get(pair.getName1());
        balances.remove(pair.getName1());
        float sumProfit = sumCrypto*pair.getSellValues();
        float sum = balances.get(pair.getName2());
        sum = sum+sumProfit;
        balances.put(pair.getName2(), sum);
    }

}
