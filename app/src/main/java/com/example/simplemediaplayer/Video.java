package com.example.simplemediaplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Video extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    VideoView vdv;    //播放视频组件
    int pos = 0;      //记录播放位置

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏系统的状态栏
//        getSupportActionBar().hide();     //隐藏标题栏

        setContentView(R.layout.activity_video);
        //保持屏幕一直开着 (不要自动休眠)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent it = getIntent();                       //获取传入的 Intent 对象
        Uri uri = Uri.parse(it.getStringExtra("uri")); //获取要播放视频的 Uri
        if(savedInstanceState != null)                 //如果是因旋转而重新启动 Activity
            pos = savedInstanceState.getInt("pos", 0); //获取旋转前所保存的播放位置

        vdv = (VideoView)findViewById(R.id.videoView); //引用到画面中的 VideoView 组件
        MediaController mediaCtrl = new MediaController(this); //新建播放控制对象
        vdv.setMediaController(mediaCtrl);             //设置要用播放控制对象来控制播放
        vdv.setVideoURI(uri);                          //设置要播放视频的 Uri

        vdv.setOnCompletionListener(this);             //设置播放完毕时的监听器
    }

    @Override
    protected void onResume() { //当 Activity 启动、或从暂停状态回到互动状态时
        super.onResume();
        vdv.seekTo(pos);        //移到 pos 的播放位置
        vdv.start();            //开始播放
    }

    @Override
    protected void onPause() { //当 Activity 进入暂停状态时 (例如切换到其他程序)
        super.onPause();
        pos = vdv.getCurrentPosition(); //保存播放位置
        vdv.stopPlayback();             //停止播放
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("pos", pos);     //将暂停时所获取的当前播放位置保存起来
    }

    @Override
    public void onCompletion(MediaPlayer mp) {  //播放完毕 结束活动
        finish();
    }
}
