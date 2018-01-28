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


    @RequestMapping("/startTradePair")
    public String runTradePair(@RequestParam(value = "pair") String name) throws Exception {
        if (currencyPair.valueOf(name) != null) {
            process.runTradePair(name);
            return "pair start success";
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

    @RequestMapping("/debugTradePair")
    public String debugTradePair(@RequestParam(value = "pair") String name, @RequestParam(value = "enabled") String debugEnabled) throws Exception {
        if (currencyPair.valueOf(name) != null) {
            if ("true".equals(debugEnabled) || "false".equals(debugEnabled)) {
                process.debugTradePair(name,Boolean.parseBoolean(debugEnabled));
                return "debug enabled success";
            }
            throw new Exception("enabled true or false "+debugEnabled);
        }
        throw new Exception("Pair " + name + " not found");
    }

}
