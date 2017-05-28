package com.example.adminer.gettable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.adminer.gettable.R;
import com.example.adminer.gettable.dao.Photo;
import com.example.adminer.gettable.dao.photoInfomation;
import com.example.adminer.gettable.dao.photoJson;
import com.example.adminer.gettable.imagerlist.ImagerListAdapter3;
import com.example.adminer.gettable.util.ToastUtil;
import com.example.adminer.gettable.view.NoScrollViewPager;
import com.example.adminer.gettable.view.PullToRefreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by adminer on 2017/4/15.
 */

public class MainActivity extends AppCompatActivity {
    private ToastUtil toastUtil;
    private DrawerLayout mDrawerLayout;
    private ImageView iv_result;
    private PullToRefreshListView listview;
    private ImagerListAdapter3 imagerListAdapter;
    private String jsonString;
    private List<Photo> data;

    public List<Photo> getApplist() {
        return applist;
    }

    public void setApplist(List<Photo> applist) {
        this.applist = applist;
    }

    private List<Photo> applist;
    private NoScrollViewPager vp_content;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            photoJson photoJson = (com.example.adminer.gettable.dao.photoJson) msg.obj;
            final String[] imageThumbUrls = photoJson.getStrArray();
            final List<Photo> list = photoJson.getApplist();
            //假设这是联网请求的图片
            imagerListAdapter = new ImagerListAdapter3(MainActivity.this, imageThumbUrls, listview);
            listview.setAdapter(imagerListAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String value = imageThumbUrls[position];
                    List<Photo> list1 = list;
                    ArrayList<photoInfomation> list2 = new ArrayList<photoInfomation>();
                    for (Photo photo : list1) {
                        photoInfomation photoinfomation = new photoInfomation();
                        photoinfomation.setId(photo.getId());
                        photoinfomation.setFilename(photo.getFilename());
                        photoinfomation.setConfirmname(value);
                        list2.add(photoinfomation);
                    }
                    Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("key", list2);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    };
    private List<View> viewList;
private RadioGroup rg_main;
    private RadioButton rb_index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        loadPhoto();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        rb_index= (RadioButton) findViewById(R.id.rb_index);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navigationView.setCheckedItem(R.id.nav_call);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        rg_main= (RadioGroup) findViewById(R.id.rg_main);
        vp_content = (NoScrollViewPager) findViewById(R.id.vp_content);
        LayoutInflater inflater = getLayoutInflater();
        //新建一个viewlist对象来保存各个分页的内容
        viewList = new ArrayList<View>();
        //通过LayoutInflater来实例化各个分页
        View view1 = inflater.inflate(R.layout.hero_list, null);
        View view2 = inflater.inflate(R.layout.hero_list1, null);
        View view3= inflater.inflate(R.layout.hero_list2, null);
        //添加分页到list中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        vp_content.setAdapter(new MyPagerAdapter());
        rb_index.setChecked(true);
        listview = (PullToRefreshListView) view1.findViewById(R.id.listview1);
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_index:
                        // 首页
                        // mViewPager.setCurrentItem(0);
                        vp_content.setCurrentItem(0, false);// 参2:表示是否具有滑动动画
                        break;
                    case R.id.rb_news:
                        // 新闻中心
                        vp_content.setCurrentItem(1, false);
                        break;
                    case R.id.rb_task:
                        // 智慧服务
                        vp_content.setCurrentItem(2, false);
                        break;
                    default:
                        break;
                }
            }
        });
        vp_content.setCurrentItem(0, false);
        //浮动功能键
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
  /*      fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastUtil.show(MainActivity.this, "FAB clicked");
            }
        });*/


    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view=viewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
                toastUtil.show(this, "返回");
                break;
            case R.id.delete:
                toastUtil.show(this, "删除");
                break;
            case R.id.setting:
                toastUtil.show(this, "设置");
                break;
        }
        return true;
    }


    public void loadPhoto() {

        new Thread() {
            @Override
            public void run() {
                String path = "http://125.217.32.224:8080/PhotoRecognition/getPhoto.action";
                try {

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(path).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithGSON(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    jsonString = requestJson();
                    data = new Gson().fromJson(jsonString, new TypeToken<List<Photo>>() {
                    }.getType());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }

    private String requestJson() throws Exception {
        String result = null;
        String path = "http://125.217.32.224:8080/PhotoRecognition/photo/";
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        connection.connect();
        if (connection.getResponseCode() == 200) {
            InputStream is = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            ;
            is.close();
            result = baos.toString();
            connection.disconnect();
        } else {
        }

        return result;

    }


    public void parseJSONWithGSON(String jsondata) {
        photoJson photoJson = new photoJson();
        String url = "http://125.217.32.224:8080/PhotoRecognition/photo/";
        int i = 0;
        Gson gson = new Gson();
        applist = gson.fromJson(jsondata, new TypeToken<List<Photo>>() {
        }.getType());
        photoJson.setApplist(applist);
        for (Photo photo : applist) {
            i++;
        }

        String[] strArray = new String[i];
        photoJson.setStrArray(strArray);
        int j = 0;
        for (Photo photo : applist) {
            Log.d("TAG", "id is" + photo.getFilename());
            Log.d("TAG", "id is" + photo.getId());
            strArray[j] = (url + photo.getFilename());
            j++;
        }
        Message msg = handler.obtainMessage();
        msg.obj = photoJson;
        handler.sendMessage(msg);
    }

}
