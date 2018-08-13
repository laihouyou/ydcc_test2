package com.movementinsome.caice.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.caice.fragment.TaskListFragment;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.kernel.activity.ContainActivity;

/**
 * Created by Administrator on 2017/11/12.
 */

/**
 * 总任务列表
 */
public class TaskListActivity extends ContainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_pager);

        initview();

    }

    private void initview() {
        HeaderPagerAdapter adapter = new HeaderPagerAdapter(this.getSupportFragmentManager());

        ViewPager pager = (ViewPager) this.findViewById(R.id.pager);
        pager.setAdapter(adapter);
    }


    class HeaderPagerAdapter extends FragmentPagerAdapter {

        public HeaderPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    TaskListFragment taskListFragment=new TaskListFragment();
                    bundle.putString(OkHttpParam.TASK_TYPE, Constant.TASK_STUAS_N);
                    taskListFragment.setArguments(bundle);
                    return taskListFragment;

                case 1:
                    TaskListFragment taskListFragment2=new TaskListFragment();
                    bundle.putString(OkHttpParam.TASK_TYPE, Constant.TASK_STUAS_Y);
                    taskListFragment2.setArguments(bundle);
                    return taskListFragment2;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.undone);

                case 1:
                    return getString(R.string.completed);

                default:
                    return null;
            }
        }
    }
}
