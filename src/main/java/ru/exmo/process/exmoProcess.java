package ru.exmo.process;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.exmo.model.data.currencyPair;
import ru.exmo.model.data.currencyPairSettings;
import ru.exmo.model.publicApi.publicApi;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Andrash on 17.12.2017.
 */

@Component
public class exmoProcess {

    @Autowired
    publicApi publicApi;

    private Map<currencyPair,currencyPairSettings> pairs;

    public exmoProcess()  {

    }

    @PostConstruct
    public void initProcess(){
        try {
            pairs =  publicApi.returnPairSettings();
            System.out.println(pairs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
