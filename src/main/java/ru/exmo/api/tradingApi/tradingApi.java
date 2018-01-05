package ru.exmo.api.tradingApi;

import ru.exmo.model.data.userInfo;

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
    userInfo returnUserInfo();
}
