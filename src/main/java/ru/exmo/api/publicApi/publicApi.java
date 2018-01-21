package ru.exmo.api.publicApi;

import ru.exmo.model.data.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrash on 17.12.2017.
 */
public interface publicApi {

    /**
     Список сделок по валютной паре
     */
    Map<currencyPair, List<exmoTrade>> returnTrades();

    /**
     Книга ордеров по валютной паре
     limit - кол-во отображаемых позиций
     */
    Map<currencyPair, exmoOrderBook> returnOrderBook(int limit);

    /**
     Cтатистика цен и объемов торгов по валютным парам
     */
    Map<String, exmoTicker> returnTicker(Map<String, exmoTicker> oldTicker);

    /**
     Настройки валютных пар
     */
    void loadPairSettings();
    /**
     Настройки конкретной валютной пары
     */
    void loadPairSettings(currencyPair pair);
}
