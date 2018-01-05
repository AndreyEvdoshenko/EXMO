package ru.exmo.api.tradingApi;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import ru.exmo.model.data.exmoUserInfo;
import ru.exmo.utils.HTTPClient;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by Андрей on 03.01.2018.
 */

public class tradingApiClient implements tradingApi {

    private final Logger logger = Logger.getLogger(tradingApiClient.class);

    private  String _key;
    private  String _secret;

    private static long _nonce;

    @Autowired
    private HTTPClient httpClient;


    public tradingApiClient() {
        _nonce = System.nanoTime();
    }

    @Override
    public exmoUserInfo returnUserInfo() {
        logger.info("invoke returnUserInfo()");
        exmoUserInfo info = new exmoUserInfo();
        String resultJson = request(tradingApiMethods.EXMO_USER_INFO, null);
        try {
            JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(resultJson);
            info.setUid(String.valueOf(jsonObject.get("uid")));
            info.setServer_date(String.valueOf(jsonObject.get("server_date")));
            info.setBalances((Map<String, BigDecimal>) jsonObject.get("balances"));
            info.setReserved((Map<String, BigDecimal>) jsonObject.get("reserved"));
            logger.info("userInfo: "+info);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return info;
    }


    private String request(tradingApiMethods method, Map<String, String> arguments) {

        List<NameValuePair> params = new ArrayList<>();
        List<NameValuePair> headers = new ArrayList<>();

        Mac mac;
        SecretKeySpec key;
        String sign;

        if (arguments == null) {  // If the user provided no arguments, just create an empty argument array.
            arguments = new HashMap<>();
        }

        arguments.put("nonce", "" + ++_nonce);  // Add the dummy nonce.

        String postData = "";

        for (Map.Entry<String, String> stringStringEntry : arguments.entrySet()) {
            Map.Entry argument = (Map.Entry) stringStringEntry;

            if (postData.length() > 0) {
                postData += "&";
            }
            postData += argument.getKey() + "=" + argument.getValue();
            params.add(new pair(String.valueOf(argument.getKey()), String.valueOf(argument.getValue())));
        }

        // Create a new secret key
        try {
            key = new SecretKeySpec(_secret.getBytes("UTF-8"), "HmacSHA512");
        } catch (UnsupportedEncodingException uee) {
            logger.error("Unsupported encoding exception: " + uee.toString());
            return null;
        }

        // Create a new mac
        try {
            mac = Mac.getInstance("HmacSHA512");
        } catch (NoSuchAlgorithmException nsae) {
            logger.error("No such algorithm exception: " + nsae.toString());
            return null;
        }

        // Init mac with key.
        try {
            mac.init(key);
        } catch (InvalidKeyException ike) {
            logger.error("Invalid key exception: " + ike.toString());
            return null;
        }

        // Encode the post data by the secret and encode the result as base64.
        try {
            sign = Hex.encodeHexString(mac.doFinal(postData.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException uee) {
            logger.error("Unsupported encoding exception: " + uee.toString());
            return null;
        }

        headers.add(new pair("Key", _key));
        headers.add(new pair("Sign", sign));

        try {
            return httpClient.postHttp(method.getUrl(), params, headers);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    @Override
    public void setKey(String _key) {
        this._key = _key;
    }

    @Override
    public void setSecret(String _secret) {
        this._secret = _secret;
    }


    private class pair implements NameValuePair {
        private String name;
        private String value;

        pair(String _name, String _value) {
            this.name = _name;
            this.value = _value;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getValue() {
            return this.value;
        }
    }

}
