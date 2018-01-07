package ru.exmo.model.data.stub;

import org.springframework.stereotype.Component;

/**
 * Created by Andrash on 07.01.2018.
 */
@Component
public class stubUser {

    private float rub;

    public stubUser(){
        rub = 15000;
    }
    public float getRub() {
        return rub;
    }

    public void setRub(float rub) {
        this.rub = rub;
    }


}
