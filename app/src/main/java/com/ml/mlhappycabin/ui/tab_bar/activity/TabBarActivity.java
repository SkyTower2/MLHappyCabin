package com.ml.mlhappycabin.ui.tab_bar.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.ml.mlhappycabin.BR;
import com.ml.mlhappycabin.R;
import com.ml.mlhappycabin.databinding.ActivityTabBarBinding;
import com.ml.mlhappycabin.ui.tab_bar.fragment.TabBar1Fragment;
import com.ml.mlhappycabin.ui.tab_bar.fragment.TabBar2Fragment;
import com.ml.mlhappycabin.ui.tab_bar.fragment.TabBar3Fragment;
import com.ml.mlhappycabin.ui.tab_bar.fragment.TabBar4Fragment;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import me.ml.mvvmhabit.base.BaseActivity;
import me.ml.mvvmhabit.base.BaseViewModel;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

/**
 * 底部tab按钮的例子
 * 所有例子仅做参考,理解如何使用才最重要。
 */
public class TabBarActivity extends BaseActivity<ActivityTabBarBinding, BaseViewModel> {
    private List<Fragment> mFragments;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_tab_bar;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        //初始化Fragment
        initFragment();
        //初始化底部Button
        initBottomTab();
    }

    private void initFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(new TabBar1Fragment());
        mFragments.add(new TabBar2Fragment());
        mFragments.add(new TabBar3Fragment());
        mFragments.add(new TabBar4Fragment());
        //默认选中第一个
        commitAllowingStateLoss(0);
    }

    /**
     * 创建一个底部导航栏的Item
     */
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text, int textColor) {
        NormalItemView normalItemView = new NormalItemView(this);
        normalItemView.initialize(drawable, checkedDrawable, text);
        normalItemView.setTextDefaultColor(Color.GRAY);
        normalItemView.setTextCheckedColor(textColor);
        return normalItemView;
    }

    private void initBottomTab() {

        /**
         *NavigationController navigationController = binding.pagerBottomTab.custom() //自定义
         *     .addItem(newItem(R.drawable.main_ic_home_unchecked, R.drawable.main_ic_home_checked, "主页", Color.RED))
         *     .addItem(newItem(R.drawable.main_ic_volunteer_unchecked, R.drawable.main_ic_volunteer_checked, "志愿",Color.RED))
         *     .addItem(newItem(R.drawable.main_ic_service_unchecked, R.drawable.main_ic_service_checked, "服务",Color.RED))
         *     .addItem(newItem(R.drawable.main_ic_person_unchecked, R.drawable.main_ic_person_checked, "个人",Color.RED))
         *     .build();
         */

        //配置底部导航栏信息，选择模式
        NavigationController navigationController = binding.pagerBottomTab.material()
                .addItem(R.mipmap.yingyong, "应用")
                .addItem(R.mipmap.huanzhe, "工作")
                .addItem(R.mipmap.xiaoxi_select, "消息")
                .addItem(R.mipmap.wode_select, "我的")
                .setDefaultColor(ContextCompat.getColor(this, R.color.textColorVice))
                .build();

        //设置消息数
        navigationController.setMessageNumber(1, 2);

        //设置显示小圆点
        navigationController.setHasMessage(0, true);

        //底部按钮的点击事件监听
        navigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.frameLayout, mFragments.get(index));
//                transaction.commitAllowingStateLoss();
                commitAllowingStateLoss(index);
            }

            @Override
            public void onRepeat(int index) {
            }
        });
    }

    private void commitAllowingStateLoss(int position) {
        hideAllFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(position + "");
        if (currentFragment != null) {
            transaction.show(currentFragment);
        } else {
            currentFragment = mFragments.get(position);
            transaction.add(R.id.frameLayout, currentFragment, position + "");
        }
        transaction.commitAllowingStateLoss();
    }

    //隐藏所有Fragment
    private void hideAllFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(i + "");
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
        }
        transaction.commitAllowingStateLoss();
    }
}
