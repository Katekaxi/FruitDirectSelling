package com.example.fruitdirectselling.fruitlistview;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fruitdirectselling.FruitActivity;
import com.example.fruitdirectselling.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FruitAdapter extends ArrayAdapter<FruitInfo> {
    private int newResourceId;
    private boolean isMember;
    private Map<Integer, Bitmap> bitmaps = null;
    public FruitAdapter(Context context, int resourceId, List<FruitInfo> fruitList, boolean isMember){
        super(context, resourceId, fruitList);
        newResourceId = resourceId;
        this.isMember = isMember;
        bitmaps = new HashMap<>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        FruitInfo fruitinfo = getItem (position);
        View view = LayoutInflater.from (getContext ()).inflate (newResourceId, parent, false);

        TextView fruitName = view.findViewById (R.id.fruit_name);
        TextView fruitPrice = view.findViewById(R.id.fruit_price);
        TextView fruitUnit = view.findViewById (R.id.fruit_unit);
        TextView fruitOrginplace = view.findViewById(R.id.fruit_orginplace);
        TextView fruitDescr = view.findViewById (R.id.fruit_descr);
        ImageView fruitImg = view.findViewById(R.id.fruit_img);


        //设置网络图片
        new DownloadImageTask(fruitImg, position).execute(fruitinfo.getImg());
        fruitName.setText (fruitinfo.getName ());
        if(this.isMember){
            fruitPrice.setText(fruitinfo.getMprice());
        }else {
            fruitPrice.setText(fruitinfo.getPrice());
        }
        fruitUnit.setText(fruitinfo.getUnit());
        fruitOrginplace.setText (fruitinfo.getOrginplace());
        fruitDescr.setText (fruitinfo.getDescr());
       // Glide.with(getContext()).load(fruitinfo.getImg()).into(fruitImg);
        return view;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        int position;

        public DownloadImageTask(ImageView bmImage, int position) {
            this.bmImage = bmImage;
            this.position = position;
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap bitmap = bitmaps.get(position);
            if(bitmap != null){
                return bitmap;
            }
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                bitmaps.put(position, mIcon11);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

/*
    public void returnBitMap(String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL myFileUrl = null;
                try {
                    myFileUrl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);

                    InputStream is = conn.getInputStream();
                    k = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/
}