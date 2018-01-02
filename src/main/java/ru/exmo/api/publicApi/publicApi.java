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

    List<exmoTrade> returnTrades(List <currencyPair> pairs);
    Map<currencyPair, exmoOrderBook> returnOrderBook(List <currencyPair> pairs, int limit) throws IOException;
    List<exmoTicker> returnTicker();
    Map<currencyPair,currencyPairSettings> returnPairSettings() throws IOException;
}
