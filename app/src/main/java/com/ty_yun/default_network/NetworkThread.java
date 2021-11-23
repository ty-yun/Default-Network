package com.ty_yun.default_network;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NetworkThread extends Thread {

    public static final String API_KEY1 = "upBV%2FEBO5SKN0d7wYqrxhbjsq3VMxD%2BP8wktRWrun28lRxWI1HEk9mzDZaOyr1XD6MWjsRfBtHOdjkWrp6QRHA%3D%3D";
    public static final String API_KEY2 = "upBV/EBO5SKN0d7wYqrxhbjsq3VMxD+P8wktRWrun28lRxWI1HEk9mzDZaOyr1XD6MWjsRfBtHOdjkWrp6QRHA==";
    public static final String API_URL = "https://api.odcloud.kr/api/15077586/v1/centers?";
    public static final String API_TYPE = "JSON";

    Handler handler;
    String query;

    public NetworkThread (Handler handler, String query) {
        this.handler = handler;
        this.query = query;
    }

    @Override
    public void run() {
        String apiUrl = API_URL + query;

        try {
            URL url = new URL(apiUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/json");

            BufferedReader reader = null;
            if (connection.getResponseCode() == 200) {
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                reader = new BufferedReader(inputStreamReader);
            } else {
                System.out.println("실패");
            }
            StringBuilder builder = new StringBuilder();
            String temp;
            while ((temp = reader.readLine()) != null){
                builder.append(temp);
            }

            reader.close();
            connection.disconnect();
            String result = builder.toString();

            Message msg = handler.obtainMessage();
            msg.obj = result;
            handler.sendMessage(msg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
