package com.ty_yun.default_network;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends Activity {

    EditText editPage, editPerPage;
    Button requestBtn;
    TextView resultTv;

    Handler handler;
    ArrayList<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editPage = (EditText) findViewById(R.id.edit_page);
        editPerPage = (EditText) findViewById(R.id.edit_perPage);
        requestBtn = (Button) findViewById(R.id.request_btn);
        resultTv = (TextView) findViewById(R.id.result);

        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = "page="+Integer.parseInt(editPage.getText().toString())+"&perPage="+Integer.parseInt(editPerPage.getText().toString())+"&returnType="+NetworkThread.API_TYPE+"&serviceKey="+NetworkThread.API_KEY1;
                NetworkThread thread = new NetworkThread(handler, query);
                thread.start();
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj != null) {
                    String result = (String) msg.obj;
                    setJsonToData(result);
                }
            }
        };
    }

    private void setJsonToData(String result) {
        itemList = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray jsonArray = jsonObj.getJSONArray("data");
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Item item = new Item();
                item.setItemKey(obj.getInt("id"));
                item.setItem1(obj.getString("centerName"));
                item.setItem2(obj.getString("address"));
                item.setItem3(obj.getString("facilityName"));
                item.setItem4(obj.getString("phoneNumber"));
                itemList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String resultStr = "";
        for (int i=0; i<itemList.size(); i++) {
            resultStr += "\n\n";
            resultStr += ""+itemList.get(i).getItemKey();
            resultStr += " / "+itemList.get(i).getItem1();
            resultStr += " / "+itemList.get(i).getItem2();
            resultStr += " / "+itemList.get(i).getItem3();
            resultStr += " / "+itemList.get(i).getItem4();
        }
        resultTv.setText(resultStr);
    }
}