package com.example.progress;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Trace;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private TextView text;
    private ProgressBar progressBar;
    private ImageView start,stop;
    private MyTask myTask;
    private Boolean isOnProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_bar);
        text = findViewById(R.id.text);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        myTask = new MyTask();
        isOnProgress = false;




        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(isOnProgress){
                    isOnProgress = false;
                    myTask.cancel(true);
                    start.setImageResource(R.mipmap.start);
                }else{
                    if(myTask.isCancelled()){
                        myTask = new MyTask();
                    }
                    myTask.execute();
                    start.setImageResource(R.mipmap.stop);
                    isOnProgress = true;
                }
            }
        });

    }


    //定义一个asyncTask子类 实例化并启动
    //onPreExecute(text:加载中) -->doInBackGround()--->onProgressUpdate----->onPostExecute
    //取消方法 onCancelled

    /**
     * params excute 方法的参数类型，doinbackground的参数
     * progress 任务执行的进度，Int
     * result 返回的结果 String
     */
    class MyTask extends AsyncTask<Void,Integer,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            text.setText("加载中");
        }

        /**
         * 接收输入的参数，执行任务中的耗时操作，返回线程任务的执行结果
         * 其他方法都可以处理ui界面显示，只有这个处理网络请求和后台操作
         * @param voids
         * @return
         */
        @Override
        protected String doInBackground(Void... voids) {
            try {
                for( int i = 0;i<100;i++){
                    publishProgress(i);//发布进度
                    Thread.sleep(50);//模拟耗时操作
                }
                start.setImageResource(R.mipmap.start);
                isOnProgress= false;
                return "加载完毕";
            } catch (InterruptedException e) {
                start.setImageResource(R.mipmap.start);
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 在主线程中显示线程任务的执行进度，在doinBackground方法中调用publishProgress方法触发该方法
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
            text.setText("加载...."+values[0]+"%");
        }

        /**
         * 接收线程任务的执行结果将结果显示在界面上
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(null != s){
                text.setText(s);//doInBackground的返回值加载完毕

            }
            myTask = new MyTask();
        }

        /**
         * 取消异步任务时，触发该方法
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
            text.setText("已取消");
            isOnProgress= false;
            progressBar.setProgress(0);
            start.setImageResource(R.mipmap.start);
        }
    }
}