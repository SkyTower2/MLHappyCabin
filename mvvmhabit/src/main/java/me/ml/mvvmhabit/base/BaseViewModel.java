package me.ml.mvvmhabit.base;

import android.app.Application;
import android.os.Bundle;

import com.trello.rxlifecycle2.LifecycleProvider;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.ml.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * 基类BaseViewModel
 *
 * @param <M>
 */
public class BaseViewModel<M extends BaseModel> extends AndroidViewModel implements IBaseViewModel, Consumer<Disposable> {
    // 代表与该 ViewModel 关联的数据模型
    protected M model;
    // 用于管理 UI 变化的 UIChangeLiveData 实例
    private UIChangeLiveData uc;
    // 弱引用持有
    private WeakReference<LifecycleProvider> lifecycle;
    // 管理RxJava，主要针对RxJava异步操作造成的内存泄漏
    private CompositeDisposable mCompositeDisposable;

    public BaseViewModel(@NonNull Application application) {
        this(application, null);
    }

    public BaseViewModel(@NonNull Application application, M model) {
        super(application);
        this.model = model;
        mCompositeDisposable = new CompositeDisposable();
    }

    /**
     * addSubscribe 方法用于将 RxJava 的 Disposable 对象添加到 CompositeDisposable 中，方便统一管理。
     *
     * @param disposable
     */
    protected void addSubscribe(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    /**
     * 注入RxLifecycle，用于同步视图模型的生命周期
     * 接受一个 LifecycleProvider，并通过弱引用存储，以便在需要时获取生命周期相关信息。
     *
     * @param lifecycle
     */
    public void injectLifecycleProvider(LifecycleProvider lifecycle) {
        this.lifecycle = new WeakReference<>(lifecycle);
    }

    /**
     * 获取RxLifecycle，用于同步视图模型的生命周期
     * 返回存储的 LifecycleProvider，以便与 RxJava 生命周期同步。
     */
    public LifecycleProvider getLifecycleProvider() {
        return lifecycle.get();
    }

    /**
     * 获取或创建UIChangeLiveData实例，用于处理UI变更事件
     * 返回 UIChangeLiveData 实例，如果尚未创建，则初始化一个新实例
     *
     * @return
     */
    public UIChangeLiveData getUC() {
        if (uc == null) {
            uc = new UIChangeLiveData();
        }
        return uc;
    }

    /**
     * 显示一个对话框
     */
    public void showDialog() {
        showDialog("请稍后...");
    }

    /**
     * 显示一个对话框
     */
    public void showDialog(String title) {
        uc.showDialogEvent.postValue(title);
    }

    /**
     * 隐藏一个对话框
     */
    public void dismissDialog() {
        uc.dismissDialogEvent.call();
    }

    /**
     * 跳转页面（启动一个新的Activity）
     *
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     * 跳转页面（启动一个新的Activity，可以携带额外信息）
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.CLASS, clz);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        //通过 uc.startActivityEvent 将事件发送到 UI 层，触发相应的跳转操作
        uc.startActivityEvent.postValue(params);
    }

    /**
     * 跳转容器页面（启动一个新的Fragment）
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     */
    public void startContainerActivity(String canonicalName) {
        startContainerActivity(canonicalName, null);
    }

    /**
     * 跳转容器页面（启动一个新的Fragment，可以携带额外信息）
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     * @param bundle        跳转所携带的信息
     */
    public void startContainerActivity(String canonicalName, Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.CANONICAL_NAME, canonicalName);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        uc.startContainerActivityEvent.postValue(params);
    }

    /**
     * 用于关闭当前界面
     */
    public void finish() {
        uc.finishEvent.call();
    }

    /**
     * 模拟返回按钮的操作（返回按钮行为）
     */
    public void onBackPressed() {
        uc.onBackPressedEvent.call();
    }

    @Override
    public void onAny(LifecycleOwner owner, Lifecycle.Event event) {
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void registerRxBus() {
    }

    @Override
    public void removeRxBus() {
    }

    /**
     * 在 ViewModel 被销毁时调用，负责清理资源，取消所有订阅，调用 model 的 onCleared 方法
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        if (model != null) {
            model.onCleared();
        }
        //ViewModel销毁时会执行，同时取消所有异步任务
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    /**
     * 实现 Consumer<Disposable> 接口的方法，用于将 Disposable 添加到 CompositeDisposable 中。
     *
     * @param disposable the value
     * @throws Exception
     */
    @Override
    public void accept(Disposable disposable) throws Exception {
        addSubscribe(disposable);
    }

    /**
     * 内部类，继承自 SingleLiveEvent，用于管理 UI 事件，如显示对话框、启动 Activity 等
     * 各个 SingleLiveEvent 成员变量负责不同的 UI 事件，通过 createLiveData 方法确保事件对象的初始化
     */
    public final class UIChangeLiveData extends SingleLiveEvent {
        private SingleLiveEvent<String> showDialogEvent;
        private SingleLiveEvent<Void> dismissDialogEvent;
        private SingleLiveEvent<Map<String, Object>> startActivityEvent;
        private SingleLiveEvent<Map<String, Object>> startContainerActivityEvent;
        private SingleLiveEvent<Void> finishEvent;
        private SingleLiveEvent<Void> onBackPressedEvent;

        public SingleLiveEvent<String> getShowDialogEvent() {
            return showDialogEvent = createLiveData(showDialogEvent);
        }

        public SingleLiveEvent<Void> getDismissDialogEvent() {
            return dismissDialogEvent = createLiveData(dismissDialogEvent);
        }

        public SingleLiveEvent<Map<String, Object>> getStartActivityEvent() {
            return startActivityEvent = createLiveData(startActivityEvent);
        }

        public SingleLiveEvent<Map<String, Object>> getStartContainerActivityEvent() {
            return startContainerActivityEvent = createLiveData(startContainerActivityEvent);
        }

        public SingleLiveEvent<Void> getFinishEvent() {
            return finishEvent = createLiveData(finishEvent);
        }

        public SingleLiveEvent<Void> getOnBackPressedEvent() {
            return onBackPressedEvent = createLiveData(onBackPressedEvent);
        }

        private <T> SingleLiveEvent<T> createLiveData(SingleLiveEvent<T> liveData) {
            if (liveData == null) {
                liveData = new SingleLiveEvent<>();
            }
            return liveData;
        }

        @Override
        public void observe(LifecycleOwner owner, Observer observer) {
            super.observe(owner, observer);
        }
    }

    /**
     * 静态内部类，定义传递给其他组件的参数键名
     */
    public static final class ParameterField {
        public static String CLASS = "CLASS";
        public static String CANONICAL_NAME = "CANONICAL_NAME";
        public static String BUNDLE = "BUNDLE";
    }
}
