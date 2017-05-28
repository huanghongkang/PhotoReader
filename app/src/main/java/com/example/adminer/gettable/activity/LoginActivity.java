package com.example.adminer.gettable.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.adminer.gettable.R;
import com.example.adminer.gettable.view.GifView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import htmlviewer.tools.Tools;

public class LoginActivity extends AppCompatActivity {


    private Button btn_login;
    private Button btn_loading;
    TextView textView;
private VideoView myVideoView;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String edit = msg.obj.toString();
            String success = "成功";
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            Toast.makeText(LoginActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
            if (success.equals(edit)) {
                startActivity(intent);
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        viewBackground();

        btn_loading = (Button) findViewById(R.id.bt_loading);
        textView = (TextView) findViewById(R.id.tv_login);


        btn_loading.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                EditText ed_user;
                EditText ed_pass;

                ed_user = (EditText) findViewById(R.id.et_user);
                ed_pass = (EditText) findViewById(R.id.et_password);

                final String name = ed_user.getText().toString();
                final String pass = ed_pass.getText().toString();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                new Thread() {
                    @Override
                    public void run() {
                        String path = "http://125.217.32.32:8080/PhotoRecognition/login?username=" + name + "&password=" + pass;
                        try {
                            URL url = new URL(path);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setReadTimeout(8000);
                            conn.setConnectTimeout(8000);

                            if (conn.getResponseCode() == 200) {
                                String title = "null";
                                InputStream is = conn.getInputStream();
                                String text = Tools.getTextFromStream(is);
                                Message msg = handler.obtainMessage();

                                Document doc = Jsoup.connect(path).get();
                                Elements element = doc.select("div.unit");
                                for (Element ele : element) {
                                    title = ele.getElementsByTag("h1").first().text();
                                }
                                msg.obj = title;
                                handler.sendMessage(msg);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }.start();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private GifView gif1;
    private GifView gif2;
    public void viewBackground(){
       /* myVideoView = (VideoView) findViewById(R.id.videoView);
        final String videoPath = Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.test_1).toString();
        myVideoView.setVideoPath(videoPath);
        myVideoView.start();
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }});
        myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                myVideoView.setVideoPath(videoPath);
                myVideoView.start();
            }
        });*/
        gif1 = (GifView) findViewById(R.id.gif1);
        // 设置背景gif图片资源
        gif1.setMovieResource(R.raw.test2);
    }
}
