package com.mad.inclass06;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by mihai on 2/5/17.
 */

public class RequestParameters {
    String method, baseURL;
    HashMap<String, String> params = new HashMap<>();

    public RequestParameters(String method, String baseURL){
        super();
        this.method = method;
        this.baseURL = baseURL;
    }

    public void addParam(String key, String value){
        params.put(key, value);
    }

    public String getEncodedParams(){
        //loop over the key/value pairs of the params
        //append to a stringbuilder key=value
        //figure out how to add the &
        //param1=value1%20value1&param2=value2&param3=value3

        StringBuilder sb = new StringBuilder();
        for(String key: params.keySet()){
            try {
                String value = URLEncoder.encode(params.get(key), "UTF-8");
                if(sb.length()>0)
                    sb.append("&");
                sb.append(key+"="+value);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public String getEncodedURL(){
        return this.baseURL+"?"+getEncodedParams();
    }

    public HttpURLConnection setupConnection() throws IOException {
        if(method.equals("GET")){
            URL url = new URL(getEncodedURL());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            return con;
        }else{
            URL url = new URL(this.baseURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(getEncodedParams());
            writer.flush();
            return con;
        }

    }

}
