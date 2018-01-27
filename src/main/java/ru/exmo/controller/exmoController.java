package ru.exmo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.exmo.model.data.currencyPair;
import ru.exmo.process.exmoProcess;
import ru.exmo.utils.HTTPClient;

import java.lang.reflect.Field;

/**
 * Created by Андрей on 15.12.2017.
 */

@RestController
public class exmoController {

    @Autowired
    HTTPClient httpClient;

    @Autowired
    exmoProcess process;

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
        for (Field field : currencyPair.class.getFields()) {
            if (field.getName().equals(pair)) {
                return field.getName();
            }
        }
        throw new Exception("Pair " + pair + " not found");
    }

    @RequestMapping("/runTradePair")
    public String runTradePair(@RequestParam(value = "pair") String name) throws Exception {
        if (currencyPair.valueOf(name) != null) {
            process.runTradePair(name);
            return "pair run success";
        }
        throw new Exception("Pair " + name + " not found");
    }
    @RequestMapping("/stopTradePair")
    public String stopTradePair(@RequestParam(value = "pair") String name) throws Exception {
        if (currencyPair.valueOf(name) != null) {
            process.stopTradePair(name);
            return "pair stop success";
        }
        throw new Exception("Pair " + name + " not found");
    }

}
