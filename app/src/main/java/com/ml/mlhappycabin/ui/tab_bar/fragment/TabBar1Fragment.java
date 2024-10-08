package com.ml.mlhappycabin.ui.tab_bar.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ml.mlhappycabin.BR;
import com.ml.mlhappycabin.R;

import androidx.annotation.Nullable;
import me.ml.mvvmhabit.base.BaseFragment;

public class TabBar1Fragment extends BaseFragment {
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_tab_bar_1;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
