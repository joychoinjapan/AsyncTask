package com.example.asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 */
public class MainActivity extends AppCompatActivity {
    private TextView text;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        text = findViewById(R.id.text);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化AsyncTask
                MyTask myTask = new MyTask();
                //启动
                myTask.execute("http://www.imooc.com/api/teacher?type=3&cid=1");
            }
        });

    }

    /**
     * 参数一 传入的参数（execute方法的参数）
     * 参数二 进度
     * 参数三 结果
     * task.execute("http://........")execute的参数最终都会成为doInBackground的参数传入
     */
    class MyTask extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("TAG","执行了onPreExecute");
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.e("TAG","执行了doInBackground");
            String url_0 = strings[0];

            try {
                //1.实例化一个URL对象
                URL url = new URL(url_0);
                //2.获取HTTPConnection实例
                HttpURLConnection conn =(HttpURLConnection) url.openConnection();
                //3.设置和请求相关的属性
                // 请求方式，超时时长
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(6000);
                //4.发送请求，获取响应码 200 成功，404未请求到资源 500 服务器异常
                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    //5.判断响应吗并获取响应数据
                    InputStream in = conn.getInputStream();

                    byte[] b = new byte[1024];
                    //在循环中读取数据
                    int len = 0;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while ((len = in.read(b))>-1){
                        //写入缓存流
                        baos.write(b,0,len);
                    }

                    String msg = new String(baos.toByteArray());
                    Log.e("msgTAG",msg);
                    return msg;
                };

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }



            return null;
        }

        /**
         * 处理UI画面（线程）
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("TAG","onPostExecute");
            if(s != null){
                text.setText(s);
            }
        }
    }
}