package com.movementinsome.caice.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.movementinsome.R;
import com.movementinsome.caice.base.TitleBaseActivity;

import java.io.File;

/**
 * Created by zzc on 2018/3/15.
 */

public class PictureActivity extends TitleBaseActivity {
    public static final String EXTRA_IMAGE_URL = "image_url";
    public static final String EXTRA_IMAGE_TITLE = "image_title";
    public static final String TRANSIT_PIC = "picture";

    String mImageUrl, mImageTitle;

    private AppCompatImageView appCompatImageView;

    public static Intent newIntent(Context context, String filePath, String projectName) {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra(PictureActivity.EXTRA_IMAGE_URL, filePath);
        intent.putExtra(PictureActivity.EXTRA_IMAGE_TITLE, projectName);
        return intent;
    }


    private void parseIntent() {
        mImageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        mImageTitle = getIntent().getStringExtra(EXTRA_IMAGE_TITLE);
    }

    @Override
    protected void initTitle() {
        setTitle(mImageTitle+getString(R.string.project_share_QEcode));
    }

    @Override
    protected void initViews() {
        appCompatImageView= (AppCompatImageView) findViewById(R.id.appcompatImageView);
        parseIntent();
        ViewCompat.setTransitionName(appCompatImageView, TRANSIT_PIC);
        Glide
                .with(this)
                .load(new File(mImageUrl))
                .diskCacheStrategy(DiskCacheStrategy.NONE)  //不缓存
                .into(appCompatImageView)
        ;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_picture;
    }
}
