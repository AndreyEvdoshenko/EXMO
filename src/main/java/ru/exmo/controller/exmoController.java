package ru.exmo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.exmo.model.data.currencyPair;
import ru.exmo.process.utils.HTTPClient;

import java.lang.reflect.Field;

/**
 * Created by Андрей on 15.12.2017.
 */

@RestController
public class exmoController {

    @Autowired
    HTTPClient httpClient;

    @RequestMapping("/getTrade")
    public String getTrade() {
        return "getTrade methods";
    }

    @RequestMapping("/getOrderBook")
    public String getOrderBook() {
        return "getTrade methods";
    }

    @RequestMapping("/getPairSettings")
    public String getPairSettings(String pair) throws Exception {
        for(Field field: currencyPair.class.getFields()){
            if(field.getName().equals(pair)){

                return field.getName();
            }
        }
        throw new Exception("Pair "+pair+" not found");
    }

}
