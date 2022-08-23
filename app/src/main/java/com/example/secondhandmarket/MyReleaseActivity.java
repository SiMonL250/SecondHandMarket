package com.example.secondhandmarket;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.secondhandmarket.release.newrelease.NewReleaseFragment;
import com.example.secondhandmarket.release.soldout.SoldOutFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MyReleaseActivity extends AppCompatActivity {
    private final int  NUM_PAGES = 2;
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private FragmentStateAdapter myFragmentStateAdapter;
    private TabLayoutMediator mediator;

//    private int activeSize = 20;
//    private int normalSize = 14;
    final String[] tabs = new String[]{"新上传","已售出"};

    private TabLayout.Tab newRelease, soldOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_release);

        mTabLayout = findViewById(R.id.tap_layout);
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(mViewPager.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        myFragmentStateAdapter = new ScreenSlidePagerAdapter(this);
        mViewPager.setAdapter(myFragmentStateAdapter);
        //绑定tabLayout
        mViewPager.registerOnPageChangeCallback(changeCallback);
        mediator = new TabLayoutMediator(mTabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {

            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                TextView tabView = new TextView(MyReleaseActivity.this);
                int[][] states = new int[2][];
                states[0] = new int[]{android.R.attr.state_selected};
                states[1] = new int[]{};

                tabView.setText(tabs[position]);

                tab.setCustomView(tabView);
            }
        });
        mediator.attach();
    }
    private ViewPager2.OnPageChangeCallback changeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            //可以来设置选中时tab的大小
            int tabCount = mTabLayout.getTabCount();
//            for (int i = 0; i < tabCount; i++) {
//                TabLayout.Tab tab = mTabLayout.getTabAt(i);
//                TextView tabView = (TextView) tab.getCustomView();
//                if (tab.getPosition() == position) {
//                    tabView.setTextSize(activeSize);
//                    tabView.setTypeface(Typeface.DEFAULT_BOLD);
//                } else {
//                    tabView.setTextSize(normalSize);
//                    tabView.setTypeface(Typeface.DEFAULT);
//                }
           // }
        }
    };
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(MyReleaseActivity myReleaseActivity) {
            super(myReleaseActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if(position == 1){
                return new SoldOutFragment();
            }
            return new NewReleaseFragment();
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}