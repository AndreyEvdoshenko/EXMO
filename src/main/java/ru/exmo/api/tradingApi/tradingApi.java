package ru.exmo.api.tradingApi;

import ru.exmo.model.data.userInfo;

/**
 * Created by Andrash on 17.12.2017.
 */
public interface tradingApi {

    /**
     Запрос информации о пользователе
     */
    userInfo returnUserInfo();
}
