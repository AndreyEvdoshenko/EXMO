package ru.exmo.api.publicApi;

import ru.exmo.model.data.*;

import java.io.IOException;
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
    Map<currencyPair, exmoOrderBook> returnOrderBook(int limit) throws IOException;

    /**
     Cтатистика цен и объемов торгов по валютным парам
     */
    List<exmoTicker> returnTicker();

    /**
     Настройки валютных пар
     */
    Map<currencyPair,currencyPairSettings> returnPairSettings() throws IOException;
}
