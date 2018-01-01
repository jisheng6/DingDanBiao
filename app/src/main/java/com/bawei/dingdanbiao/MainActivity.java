package com.bawei.dingdanbiao;

import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bawei.dingdanbiao.adapter.MyRecyAdapter;
import com.bawei.dingdanbiao.bean.DingdanBean;
import com.bawei.dingdanbiao.callback.MyViewCallBack;
import com.bawei.dingdanbiao.callback.MyViewCallBack2;
import com.bawei.dingdanbiao.presenter.MyPresenter;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyViewCallBack {

    private RadioGroup radioGroup;
    private RecyclerView recyclerView;
    private List<DingdanBean.DataBean> listDingdan;
    private MyRecyAdapter myRecyAdapter;
    private ImageView imageView;
    private SpringView springView;
    int page=2;
    int status = 0;
    private MyPresenter myPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        springView = (SpringView) findViewById(R.id.springView);
        imageView = (ImageView) findViewById(R.id.image);
        imageView.setTag(1);//设置标记为1,,显示

        radioGroup = (RadioGroup) findViewById(R.id.group);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        //new出P层对象
        myPresenter = new MyPresenter(this);

        //调用p层的方法
        myPresenter.getDataFromModel(page);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        //springview的设置
        springView.setHeader(new DefaultHeader(MainActivity.this));
        springView.setFooter(new DefaultFooter(MainActivity.this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                myPresenter.getDataFromModel(page);
                springView.onFinishFreshAndLoad();

                Toast.makeText(MainActivity.this,"下拉刷新成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadmore() {
                page++;
                myPresenter.getDataFromModel(page);
                springView.onFinishFreshAndLoad();
                Toast.makeText(MainActivity.this,"上拉加载成功",Toast.LENGTH_SHORT).show();
            }
        });

        //引入popuowindow 的布局文件
        View contentView = View.inflate(MainActivity.this,R.layout.popup_window,null);
        //父窗体
        final View parent = View.inflate(MainActivity.this,R.layout.activity_main,null);
        //通过构造方法创建一个popupwindow
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //出现的问题,需要设置4个下面的
        popupWindow.setTouchable(true);//窗体可以触摸
        popupWindow.setFocusable(true);//让窗体获取焦点
        popupWindow.setOutsideTouchable(true);//设置窗体外部可以触摸
        popupWindow.setBackgroundDrawable(new BitmapDrawable());//设置背景

        //获取自定义的popupwindow里面的id,3个textview
        TextView daizhifu = (TextView) contentView.findViewById(R.id.daizhifu);
        TextView yizhifu = (TextView) contentView.findViewById(R.id.yizhifu);
        TextView yiquxiao = (TextView) contentView.findViewById(R.id.yiquxiao);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.btn1:
                        status=0;
                        listDingdan.clear();
                        myPresenter.popUpQingqiu(status);
                        break;
                    case R.id.btn2:
                        status=1;
                        listDingdan.clear();
                        myPresenter.popUpQingqiu(status);
                        break;
                    case R.id.btn3:
                        status=2;
                        listDingdan.clear();
                        myPresenter.popUpQingqiu(status);
                        break;
                }
            }
        });
        daizhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();//弹窗取消
                status=0;
                listDingdan.clear();
                myPresenter.popUpQingqiu(status);
                Toast.makeText(MainActivity.this,status+"",Toast.LENGTH_SHORT).show();
            }
        });

        yizhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                status=1;
                listDingdan.clear();
                myPresenter.popUpQingqiu(status);
                Toast.makeText(MainActivity.this,status+"",Toast.LENGTH_SHORT).show();
            }
        });

        yiquxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                status=2;
                listDingdan.clear();
                myPresenter.popUpQingqiu(status);
                Toast.makeText(MainActivity.this,status+"",Toast.LENGTH_SHORT).show();
            }
        });

        //点击右上角的图片 出来popupwindow
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //显示popupwindow
                popupWindow.showAsDropDown(imageView);//显示在控件的左下方

            }
        });

    }

    //实现接口重写的方法
    @Override
    public void viewSuccess(DingdanBean dingdanBean) {
        // System.out.println(dingdanBean.getData().get(0).getTitle());

        if(listDingdan==null){
            listDingdan=new ArrayList<>();
        }

        listDingdan.addAll(dingdanBean.getData());
        //new适配器
        if(myRecyAdapter==null) {
            myRecyAdapter = new MyRecyAdapter(MainActivity.this,listDingdan, new MyViewCallBack2() {
                @Override
                public void viewSuccess2(final String data) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,data,Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void viewFail2() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"网络慢",Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });
            // myRecyAdapter.addData(listDingdan);
            recyclerView.setAdapter(myRecyAdapter);
        }else{
            myRecyAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void viewFail(Exception e) {
        System.out.println("异常 : "+e);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        myPresenter.detach();
    }
}


