package com.example.adminer.gettable.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminer.gettable.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import htmlviewer.tools.Tools;

/**
 * Created by adminer on 2017/4/15.
 */

public class RegisterActivity extends AppCompatActivity {
    private DatePickerDialog dialog;
    private TextView birthday_text;
    private int year, month, day;
    String birthday;
    Button register;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        birthday_text = (TextView) findViewById(R.id.tv_birthday);
        register = (Button) findViewById(R.id.btn_register);
        birthday = get_day();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton man = (RadioButton) findViewById(R.id.rb_man);
                RadioButton woman = (RadioButton) findViewById(R.id.rb_woman);
                String sex = "none";
                if (woman.isChecked()) {
                    sex = "woman";
                }
                if (man.isChecked()) {
                    sex = "man";
                }
                EditText et_name = (EditText) findViewById(R.id.et_name);
                EditText et_pass = (EditText) findViewById(R.id.et_pass);
                EditText et_number = (EditText) findViewById(R.id.et_call_number);
                EditText et_confirm_pass = (EditText) findViewById(R.id.et_confirm_pass);
                final String name = et_name.getText().toString();
                final String pass = et_pass.getText().toString();
                final String confirm_pass = et_confirm_pass.getText().toString();
                final String number = et_number.getText().toString();
                //birthday
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        String path = "http://125.217.32.32:8080/PhotoRecognition/register?username=" + name + "&password=" + pass;
                        try {
                            URL url = new URL(path);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setReadTimeout(8000);
                            conn.setConnectTimeout(8000);

                            if (conn.getResponseCode() == 200) {
                                InputStream is = conn.getInputStream();
                                String text = Tools.getTextFromStream(is);
                                Message msg = handler.obtainMessage();
                                msg.obj = text;
                                handler.sendMessage(msg);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                };
                if (confirm_pass.equals(pass)) {
                    thread.start();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "密码和确认密码必须一致", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public String get_day() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birthday = year + "-" + month + "-" + day;
                birthday_text.setText(birthday);
            }
        }, year, month, day);
        return birthday;
    }

    //生日按钮监听
    public void choose_birthday(View v) {

        dialog.show();
    }

}
