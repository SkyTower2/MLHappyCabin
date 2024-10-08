package com.ml.mlhappycabin.ui.network.detail;

import android.app.Application;

import com.ml.mlhappycabin.entity.DemoEntity;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import me.ml.mvvmhabit.base.BaseViewModel;

public class DetailViewModel extends BaseViewModel {
    public ObservableField<DemoEntity.ItemsEntity> entity = new ObservableField<>();

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void setDemoEntity(DemoEntity.ItemsEntity entity) {
        this.entity.set(entity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        entity = null;
    }
}
