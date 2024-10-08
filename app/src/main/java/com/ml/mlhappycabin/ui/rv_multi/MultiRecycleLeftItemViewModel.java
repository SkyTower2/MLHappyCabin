package com.ml.mlhappycabin.ui.rv_multi;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import me.ml.mvvmhabit.base.MultiItemViewModel;

import me.ml.mvvmhabit.binding.command.BindingAction;
import me.ml.mvvmhabit.binding.command.BindingCommand;
import me.ml.mvvmhabit.utils.ToastUtils;

public class MultiRecycleLeftItemViewModel extends MultiItemViewModel<MultiRecycleViewModel> {
    public ObservableField<String> text = new ObservableField<>("");

    public MultiRecycleLeftItemViewModel(@NonNull MultiRecycleViewModel viewModel, String text) {
        super(viewModel);
        this.text.set(text);
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //拿到position
            int position = viewModel.observableList.indexOf(MultiRecycleLeftItemViewModel.this);
            ToastUtils.showShort("position：" + position);
        }
    });
}
