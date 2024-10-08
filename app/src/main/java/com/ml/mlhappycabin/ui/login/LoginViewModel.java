package com.ml.mlhappycabin.ui.login;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;

import com.ml.mlhappycabin.data.DemoRepository;
import com.ml.mlhappycabin.ui.main.DemoActivity;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.ml.mvvmhabit.base.BaseViewModel;
import me.ml.mvvmhabit.binding.command.BindingAction;
import me.ml.mvvmhabit.binding.command.BindingCommand;
import me.ml.mvvmhabit.binding.command.BindingConsumer;
import me.ml.mvvmhabit.bus.event.SingleLiveEvent;
import me.ml.mvvmhabit.utils.RxUtils;
import me.ml.mvvmhabit.utils.ToastUtils;

public class LoginViewModel extends BaseViewModel<DemoRepository> {
    //用户名的绑定
    public ObservableField<String> userName = new ObservableField<>("");
    //密码的绑定
    public ObservableField<String> password = new ObservableField<>("");
    //用户名清除按钮的显示隐藏绑定
    public ObservableInt clearBtnVisibility = new ObservableInt();
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    /**
     * 为什么此处使用一个内部类？
     * 目的是分离和管理 UI 事件，保持代码的整洁和模块化
     * 优点：
     * 1、代码的清晰性与可读性
     * 把 UI 相关的观察事件（如 pSwitchEvent）集中在一个内部类中，可以使 LoginViewModel 的主逻辑更清晰。这样做有助于将 UI事件处理 和 业务逻辑 分离开来，避免混乱。
     * UIChangeObservable 类中的变量通常用于 UI 交互事件。
     * LoginViewModel 其他部分则负责核心业务逻辑，比如处理登录操作。
     * 这样做可以清楚地区分哪些是与业务无关的 UI 状态变化（如显示/隐藏密码的切换），哪些是与业务相关的逻辑（如登录操作）。
     * ---------------------------------------------------------------------------------------------
     * 2. 逻辑层次的划分
     * 将 UI 事件放在 UIChangeObservable 中体现了代码层次的划分，有助于解耦：
     * ViewModel 负责业务逻辑和数据处理。
     * UIChangeObservable 负责 UI 的变化（例如密码显示切换）。
     * 这种组织结构可以让开发者更方便地在 ViewModel 中引入或移除 UI 变化处理，而不会影响到核心业务逻辑。
     * ---------------------------------------------------------------------------------------------
     * 3. 可扩展性
     * 如果项目变得更复杂，UI 需要处理的事件增多，比如登录页面除了密码切换外，还可能需要处理登录成功、登录失败、加载动画等 UI 状态。将这些状态都集中管理在一个类中（UIChangeObservable）可以方便扩展。
     * ---------------------------------------------------------------------------------------------
     * 4. 遵循 MVVM 模式
     * 在 MVVM 模式中，ViewModel 的主要任务是处理数据和业务逻辑，而将 UI 的控制分离出来有助于保持 ViewModel 的职责单一。把所有 UI 控制相关的状态集中在 UIChangeObservable 中，更符合 MVVM 的设计思路，确保 ViewModel 只做与数据处理相关的工作
     * ---------------------------------------------------------------------------------------------
     * 5. 减少 ViewModel 的膨胀
     * 随着 UI 事件越来越多，ViewModel 很容易变得臃肿。如果直接把所有 UI 相关的事件都放在 ViewModel 中，代码可读性会下降，维护难度也会增加。通过一个内部类将这些事件统一管理，可以防止 ViewModel 变得过于复杂。
     */
    public class UIChangeObservable {
        //密码开关观察者
        //SingleLiveEvent 通常用于事件（如点击、导航等）的处理。
        //它是一种特殊的 LiveData，确保数据只能被消费一次，常用来解决 LiveData 在屏幕旋转或配置更改时，重复发送相同事件的问题。
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();
    }

    public LoginViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
        //从本地取得数据绑定到View层
        userName.set(model.getUserName());
        password.set(model.getPassword());
    }

    //清除用户名的点击事件, 逻辑从View层转换到ViewModel层
    public BindingCommand clearUserNameOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            userName.set("");
        }
    });

    //密码显示开关  (你可以尝试着狂按这个按钮,会发现它有防多次点击的功能)
    public BindingCommand passwordShowSwitchOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //让观察者的数据改变,逻辑从ViewModel层转到View层，在View层的监听则会被调用
            uc.pSwitchEvent.setValue(uc.pSwitchEvent.getValue() == null || !uc.pSwitchEvent.getValue());
        }
    });

    //用户名输入框焦点改变的回调事件
    public BindingCommand<Boolean> onFocusChangeCommand = new BindingCommand<>(new BindingConsumer<Boolean>() {
        @Override
        public void call(Boolean hasFocus) {
            if (hasFocus) {
                clearBtnVisibility.set(View.VISIBLE);
            } else {
                clearBtnVisibility.set(View.INVISIBLE);
            }
        }
    });

    //登录按钮的点击事件
    public BindingCommand loginOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            login();
        }
    });

    /**
     * 网络模拟一个登陆操作
     **/
    private void login() {
        if (TextUtils.isEmpty(userName.get())) {
            ToastUtils.showShort("请输入账号！");
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtils.showShort("请输入密码！");
            return;
        }

        //RaJava模拟登录
        addSubscribe(model.login()
                .compose(RxUtils.schedulersTransformer()) //使用 RxUtils.schedulersTransformer() 进行线程调度
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        dismissDialog();
                        //保存账号密码
                        model.saveUserName(userName.get());
                        model.savePassword(password.get());
                        //进入DemoActivity页面
                        startActivity(DemoActivity.class);
                        //关闭页面
                        finish();
                    }
                }));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
