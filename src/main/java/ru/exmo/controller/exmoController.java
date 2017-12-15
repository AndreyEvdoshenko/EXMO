package ru.exmo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Андрей on 15.12.2017.
 */

@RestController
public class exmoController {

    @RequestMapping("/getTrade")
    public String getTrade() {
        return "getTrade methods";
    }
}
