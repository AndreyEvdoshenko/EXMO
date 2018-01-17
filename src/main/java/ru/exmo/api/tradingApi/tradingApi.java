package ru.exmo.api.tradingApi;

import ru.exmo.model.data.exmoOrderCreate;
import ru.exmo.model.data.exmoUserInfo;
import ru.exmo.model.data.exmoUserOpenOrders;

import java.util.List;
import java.util.Map;

/**
 * Created by Andrash on 17.12.2017.
 */
public interface tradingApi {

    /**
     Ключ EXMO
     */
    void setKey(String _key);

    /**
     Ключ EXMO
     */
    void setSecret(String _secret);

    /**
     Запрос информации о пользователе
     */
    exmoUserInfo returnUserInfo();

    /**
     Созание ордера
     */
    exmoOrderCreate createOrder(exmoOrderCreate order);

    /**
     Список открытых ордеров ордера
     */
   Map<String,List<exmoUserOpenOrders>> returnUserOpenOrders();


}
