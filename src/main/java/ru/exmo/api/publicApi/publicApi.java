package ru.exmo.api.publicApi;

import ru.exmo.model.data.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrash on 17.12.2017.
 */
public interface publicApi {

    public final String EXMO_TRADES_URL         = "https://api.exmo.com/v1/trades/";
    public final String EXMO_ORDER_BOOK_URL     = "https://api.exmo.com/v1/order_book/";
    public final String EXMO_TICKER_URL         = "https://api.exmo.com/v1/ticker/";
    public final String EXMO_PAIR_SETTINGS_URL  = "https://api.exmo.com/v1/pair_settings/";

    /**
     Список сделок по валютной паре
     */
    List<exmoTrade> returnTrades();

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
