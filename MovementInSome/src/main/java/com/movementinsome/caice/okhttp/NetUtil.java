package com.movementinsome.caice.okhttp;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author chenzj
 * @Title: NetUtil
 * @Description: 类的描述 -
 * @date 2017/3/1 16:32
 * @email admin@chenzhongjin.cn
 */
public class NetUtil {


    public static  <T> ObservableTransformer<T,T> io_main(){
        return new ObservableTransformer<T,T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
