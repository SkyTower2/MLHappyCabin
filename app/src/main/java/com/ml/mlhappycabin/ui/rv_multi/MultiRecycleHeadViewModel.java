package com.ml.mlhappycabin.ui.rv_multi;

import androidx.annotation.NonNull;
import me.ml.mvvmhabit.base.BaseViewModel;
import me.ml.mvvmhabit.base.MultiItemViewModel;
import me.ml.mvvmhabit.binding.command.BindingAction;
import me.ml.mvvmhabit.binding.command.BindingCommand;
import me.ml.mvvmhabit.utils.ToastUtils;

public class MultiRecycleHeadViewModel extends MultiItemViewModel {

    public MultiRecycleHeadViewModel(@NonNull BaseViewModel viewModel) {
        super(viewModel);
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtils.showShort("我是头布局");
        }
    });
}
