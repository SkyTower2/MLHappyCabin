package com.ml.mlhappycabin.ui.rv_multi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ml.mlhappycabin.BR;
import com.ml.mlhappycabin.R;
import com.ml.mlhappycabin.databinding.FragmentMultiRvBinding;

import androidx.annotation.Nullable;
import me.ml.mvvmhabit.base.BaseFragment;

public class MultiRecycleViewFragment extends BaseFragment<FragmentMultiRvBinding, MultiRecycleViewModel> {
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_multi_rv;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        super.initData();
    }
}
