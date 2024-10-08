package com.ml.mlhappycabin.data.source;

import com.ml.mlhappycabin.entity.DemoEntity;

import io.reactivex.Observable;
import me.ml.mvvmhabit.http.BaseResponse;

public interface HttpDataSource {
    //模拟登录
    Observable<Object> login();

    //模拟上拉加载
    Observable<DemoEntity> loadMore();

    Observable<BaseResponse<DemoEntity>> demoGet();

    Observable<BaseResponse<DemoEntity>> demoPost(String catalog);


}
