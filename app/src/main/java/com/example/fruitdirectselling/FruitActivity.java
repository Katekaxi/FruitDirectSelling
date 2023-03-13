package com.example.fruitdirectselling;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.fruitdirectselling.fruitlistview.FruitAdapter;
import com.example.fruitdirectselling.fruitlistview.FruitInfo;
import com.example.fruitdirectselling.fruitlistview.SearchFruitsUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FruitActivity extends AppCompatActivity {

    public String[] nameby = new String[12];
    public String[] priceby = new String[12];
    public String[] mpriceby = new String[12];
    public String[] unitby = new String[12];
    public String[] orginplaceby = new String[12];
    public String[] descrby = new String[12];
    public String[] imgby = new String[12];
    private Button searchButton;
    private ListView fruitlist;
    private static final String TAG = "MainActivity";
    Boolean member = false;
    private List<FruitInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit);

        searchButton = findViewById(R.id.search_button);

        Intent i = getIntent();
        this.member = i.getBooleanExtra("Ismember",false);
        sendRequest();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fruitlist =(ListView)findViewById(R.id.fruitList);
                EditText editText = findViewById(R.id.search_text);
                List<FruitInfo> searchFruitsByName = SearchFruitsUtil.searchFruitsByName(editText.getText().toString(), list);
                List<FruitInfo> searchFruitsByOriginPlace = SearchFruitsUtil.searchFruitsByOriginPlace(editText.getText().toString(), list);
                searchFruitsByName.addAll(searchFruitsByOriginPlace);
                FruitAdapter adapter = new FruitAdapter(FruitActivity.this, R.layout.fruit_item, searchFruitsByName, member);
                fruitlist.setAdapter(adapter);
            }
        });
    }

    public List<FruitInfo> getData(){
        List<FruitInfo> list=new ArrayList<FruitInfo>();
        for (int i = 0; i < 12; i++) {
            FruitInfo fruit = new FruitInfo("xx", 0);
            fruit.name = nameby[i];
            fruit.price = priceby[i];
            fruit.mprice = mpriceby[i];
            fruit.unit = unitby[i];
            fruit.orginplace = orginplaceby[i];
            fruit.descr = descrby[i];
            fruit.img = imgby[i];
            list.add(fruit);
        }
        return list;
    }

    private void sendRequest(){
        //Android不允许在主线程中进行网络访问,要另起线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try{

                    URL url = new URL("http://47.105.60.171:8086/listfruits");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    InputStream in = connection.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(in));

                    StringBuffer response = new StringBuffer();
                    String line;
                    while ((line = reader.readLine())!=null){
                        response.append(line);
                    }

                    Log.d(TAG, response.toString());
                    String strBody = response.toString();
                    parseWeatherJson(strBody);
                    handler.sendEmptyMessage(0);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (null!=reader){
                        try{
                            reader.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if (null!=connection){
                        //关闭HTTP连接
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            fruitlist =(ListView)findViewById(R.id.fruitList);
            switch (msg.what) {
                case 0:
                    list = getData();
                    fruitlist.setAdapter(new FruitAdapter(FruitActivity.this, R.layout.fruit_item, list, member));
                    break;
                default:
                    break;
            }
        }
    };

    public void parseWeatherJson(String json) {

        try{
            // 整个最大的JSON对象
            JSONArray jsonArrayALL = new JSONArray(json);
            for (int k = 0; k < 12; k++) {
                JSONObject id1 = jsonArrayALL.getJSONObject(k);

                String name = id1.optString("name", null);
                String price = id1.optString("price", null);
                String mprice = id1.optString("mprice", null);
                String unit = id1.optString("unit", null);
                String orginplace = id1.optString("orginplace", null);
                String descr = id1.optString("descr", null);
                String img = id1.optString("img", null);
                int id = id1.getInt("id");
                nameby[k] = name;
                priceby[k] = price;
                mpriceby[k] = mprice;
                unitby[k] = unit;
                orginplaceby[k] = orginplace;
                descrby[k] = descr;
                imgby[k] = img;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}