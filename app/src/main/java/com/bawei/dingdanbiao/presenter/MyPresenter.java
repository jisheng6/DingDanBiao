package com.bawei.dingdanbiao.presenter;

import com.bawei.dingdanbiao.bean.DingdanBean;
import com.bawei.dingdanbiao.callback.MyViewCallBack;
import com.bawei.dingdanbiao.model.MyModel;

/**
 * Created by Menglucywhh on 2017/11/20.
 */
public class MyPresenter {

   MyModel myModel = new MyModel();
    MyViewCallBack myViewCallBack;
    public MyPresenter(MyViewCallBack myViewCallBack) {
        this.myViewCallBack = myViewCallBack;
    }

    //调用model层去访问数据
    public void getDataFromModel(int page){
        myModel.getData(page,new MyModel.ModelCallBack() {
            @Override
            public void success(DingdanBean dingdanBean) {
                myViewCallBack.viewSuccess(dingdanBean);
            }

            @Override
            public void fail(Exception e) {
            myViewCallBack.viewFail(e);
            }
        });
    }

    //调用model层去访问数据
    public void popUpQingqiu(int status){
        myModel.popUpGetData(status,new MyModel.ModelCallBack() {
            @Override
            public void success(DingdanBean dingdanBean) {
                myViewCallBack.viewSuccess(dingdanBean);
            }

            @Override
            public void fail(Exception e) {
                myViewCallBack.viewFail(e);
            }
        });
    }

    //防止内存泄露
    public void detach(){
        this.myViewCallBack = null;
    }
}
