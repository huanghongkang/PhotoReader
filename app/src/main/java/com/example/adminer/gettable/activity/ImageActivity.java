package com.example.adminer.gettable.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.example.adminer.gettable.R;
import com.example.adminer.gettable.dao.TagName;
import com.example.adminer.gettable.dao.photoInfomation;
import com.example.adminer.gettable.volley.VolleyManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import htmlviewer.tools.Tools;

import static com.example.adminer.gettable.R.layout.maketag;

public class ImageActivity extends AppCompatActivity {

    private int i=0;
    private String value=null;
    private String tagName1=null;
    private String tagName2=null;
    private String tagName3=null;
    private int tagId;
    private String photoName;
    private int time=0;
    String tagName[]=new String[3];

    private LayoutInflater mlayoutinflater;
    private View view;
    EditText editText2;
    Button btn2;
    EditText editText3;
    EditText editText1;
    private TagName tagname;

    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0) {
                String request = msg.obj.toString();
                String success = "\"成功\"";
                if (request.equals(success)) {
                    Toast.makeText(ImageActivity.this, "插入标签成功", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ImageActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ImageActivity.this, "此标签已经插入", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ImageView imageView= (ImageView) findViewById(R.id.image_main);

        List<photoInfomation> list=(List<photoInfomation>)this.getIntent().getSerializableExtra("key");





        for(photoInfomation photoInfomation:list)
        {
            value=photoInfomation.getConfirmname();

        }

        String imageUrl=value;

        ImageLoader.ImageListener listener = VolleyManager.getImageLoader() .getImageListener(imageView, android.R.drawable.ic_menu_rotate, android.R.drawable.ic_delete);

        VolleyManager.getImageLoader().get(imageUrl, listener);
    }


    public void MakeTag(View v)
    {

        List<photoInfomation> list=(List<photoInfomation>)this.getIntent().getSerializableExtra("key");



        String temp=new String();
        if (time==0) {
            value=value.substring(48);
            for (photoInfomation photoInfomation : list) {
                temp = photoInfomation.getFilename();
                if (value.equals(temp)) {
                    tagId = photoInfomation.getId();
                }

            }
        }
        time++;


        AlertDialog.Builder builder=new AlertDialog.Builder(this);

//        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("打标签");
        builder.setMessage("请输入你要打的标签");





        LinearLayout linearLayout= (LinearLayout) findViewById(R.id.ll_parent);
        mlayoutinflater=LayoutInflater.from(this);
        view=mlayoutinflater.inflate(maketag,null);

        editText1=(EditText)view.findViewById(R.id.et_make_tag1);
        editText2= (EditText) view.findViewById(R.id.et_make_tag2);
        btn2= (Button) view.findViewById(R.id.btn_make_tag2);

        editText3=(EditText)view.findViewById(R.id.et_make_tag3);


        editText2.setVisibility(View.GONE);
        btn2.setVisibility(View.GONE);
        editText3.setVisibility(View.GONE);




        builder.setView(view);


        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int photoId=tagId;
                makeTagForRequest(photoId);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();



    }


    public void makeTagForRequest(final int photoId)
    {


        tagName[0]=editText1.getText().toString();
        tagName[1]=editText2.getText().toString();
        tagName[2]=editText3.getText().toString();
        new Thread() {
            @Override
            public void run() {

                String path = "http://125.217.32.224:8080/PhotoRecognition/addMarkRecord.action?u_id=1&p_id=" + photoId + "&tagName=" + tagName[0];
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(8000);
                    conn.setConnectTimeout(8000);

                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        String text = Tools.getTextFromStream(is);
                        Message msg = new Message();
                        msg.obj = text;
                        handler.sendMessage(msg);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }.start();

        new Thread() {
            @Override
            public void run() {

                String path = "http://125.217.32.224:8080/PhotoRecognition/addMarkRecord.action?u_id=1&p_id=" + photoId + "&tagName=" + tagName[1];
                try {

                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(8000);
                    conn.setConnectTimeout(8000);

                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        String text = Tools.getTextFromStream(is);
                        Message msg = new Message();
                        msg.obj = text;
                        handler.sendMessage(msg);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }.start();


        new Thread() {
            @Override
            public void run() {
                String path = "http://125.217.32.224:8080/PhotoRecognition/addMarkRecord.action?u_id=1&p_id=" + photoId + "&tagName=" + tagName[2];
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(8000);
                    conn.setConnectTimeout(8000);
                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        String text = Tools.getTextFromStream(is);
                        Message msg = new Message();
                        msg.obj = text;
                        handler.sendMessage(msg);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }.start();


    }


    public void btn_make_tag1(View v)
    {
        editText2.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
    }

    public void btn_make_tag2(View v)
    {
        editText3.setVisibility(View.VISIBLE);
    }


}