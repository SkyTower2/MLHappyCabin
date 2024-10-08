package com.ml.mlhappycabin.app;

import android.annotation.SuppressLint;
import android.app.Application;

import com.ml.mlhappycabin.data.DemoRepository;
import com.ml.mlhappycabin.ui.login.LoginViewModel;
import com.ml.mlhappycabin.ui.network.NetWorkViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * ViewModel工厂
 * ：用于创建和管理Android应用中的ViewModel实例
 */
public class AppViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile AppViewModelFactory INSTANCE;
    private final Application mApplication;
    private final DemoRepository mRepository;

    public static AppViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (AppViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppViewModelFactory(application, Injection.provideDemoRepository());
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private AppViewModelFactory(Application application, DemoRepository repository) {
        this.mApplication = application;
        this.mRepository = repository;
    }

    /**
     * 根据传入的ViewModel类创建对应的ViewModel实例
     * :@NonNull Class<T> modelClass：泛型参数T必须是ViewModel的子类，表示要创建的ViewModel的具体类型，根据参数的类型返回相应的ViewModel实例；
     *
     * @param modelClass a {@code Class} whose instance is requested
     * @param <T>
     * @return
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NetWorkViewModel.class)) {
            return (T) new NetWorkViewModel(mApplication, mRepository);
        } else if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(mApplication, mRepository);
        }
        //如果不是以上情况，抛出异常
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
