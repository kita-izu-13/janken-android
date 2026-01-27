package com.example.myapplication;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.databinding.ActivityMainBinding;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    MediaPlayer bgm;
    //boolean isBgmOn = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.btng.setOnClickListener(v ->{
            play="グー";
            binding.playimg.setImageResource(R.drawable.janken_gu);
            random();
            if(ran==1){
                draw();
            }else if(ran==2){
                win();
            }else{
                lose();
            }
            show();
        });
        binding.btnc.setOnClickListener(v ->{
            play="チョキ";
            binding.playimg.setImageResource(R.drawable.janken_choki);
            random();
            if(ran==1){
                lose();
            }else if(ran==2){
                draw();
            }else{
                win();
            }
            show();
        });

        binding.btnp.setOnClickListener(v ->{
            play="パー";
            binding.playimg.setImageResource(R.drawable.janken_pa);
            random();
            if(ran==1){
                win();
            }else if(ran==2){
                lose();
            }else{
                draw();
            }
            show();
        });

    }

    int ran=0;
    String play="";
    String com="";

    int count=0;



    public void random(){
        RadioGroup group=findViewById(R.id.group);
        int SelectedId=group.getCheckedRadioButtonId();
        RadioButton rbeasy = (RadioButton)findViewById(R.id.easy);
        RadioButton rbnomal = (RadioButton)findViewById(R.id.nomal);
        RadioButton rbdeffault = (RadioButton)findViewById(R.id.deffault);
        RadioButton rbstar = (RadioButton)findViewById(R.id.star);
        int r= (int) (Math.random()*100+1);//有利確率２０までは有利
        if(rbnomal.isChecked()) {
            ran = (int) (Math.random() * 3 + 1); //1グー2チョキ3パー
        }else if(rbeasy.isChecked()) {
            if(r<20) {
                //有利
                if (play == "グー") {
                   ran = (int) (Math.random() * 2 + 1); //1グー2チョキ
                }else if (play == "チョキ") {
                    ran = (int) (Math.random() * 2 + 2); //2チョキ3パー
                }else if (play == "パー") {
                    ran = 1; //1グー
                }
            }else{
                ran = (int) (Math.random() * 3 + 1); //1グー2チョキ3パー
            }
        }else if(rbdeffault.isChecked()) {
            if(r<20) {
                //有利
                if (play == "グー") {
                    ran = 3; //1グー3パー
                }else if (play == "チョキ") {
                    ran = (int) (Math.random() * 2 + 1); //1グー2チョキ
                }else if (play == "パー") {
                    ran = (int) (Math.random() * 2 + 2); //2チョキ3パー
                }
            }else{
                ran = (int) (Math.random() * 3 + 1); //1グー2チョキ3パー
            }
        }if(rbstar.isChecked()) {
            if (play == "グー") {
                ran = 2; //2チョキ
            }else if (play == "チョキ") {
                ran = 3; //3パー
            }else if (play == "パー") {
                ran = 1; //1グー
            }
        }

        if(ran==1){
            com="グー";
                binding.comimg.setImageResource(R.drawable.janken_gu);
        }else if(ran==2){
            com="チョキ";
                binding.comimg.setImageResource(R.drawable.janken_choki);
        }else{
            com="パー";
                binding.comimg.setImageResource(R.drawable.janken_pa);
        }
    }
    public void win(){
        binding.result.setText("勝ちです！");
        count++;
        binding.resultimg.setImageResource(R.drawable.winimg);
        CheckBox sound=findViewById(R.id.sound);

        //２連勝以上
        if(count>=2) {
            Toast toast = Toast.makeText(this, count + "連勝中です！", Toast.LENGTH_SHORT);
            toast.show();
        }

        if(sound.isChecked()){
            /*
            //BGM
            bgm=MediaPlayer.create(this,R.raw.bgm);
            bgm.setLooping(true);   //常に流す
            bgm.setVolume(0.05f,0.05f);   //音量０に近いほど小さい
            //BGMスタート
            bgm.start();
            */

        //音声入れる
        MediaPlayer mpw=MediaPlayer.create(this,R.raw.yappy);
        mpw.setOnCompletionListener(mediaPlayer-> {
            //音声片付け
            mediaPlayer.release();

            if (count >= 5) {
                //5連勝以上の時だけ動画に切り替え
                showVideo();
            }
        });
        mpw.start();
        }else{
        //bgm.pause();
        //5連勝以上
        if(count>=5) {
            //5連勝以上の時だけ動画に切り替え
            showVideonosound();
        }
        }
    }
    public void lose(){
        binding.result.setText("負けました…");
        count=0;
        showImage();
        binding.resultimg.setImageResource(R.drawable.maketa);
        CheckBox sound=findViewById(R.id.sound);
        if(sound.isChecked()) {
            //音声入れる
            MediaPlayer mpl = MediaPlayer.create(this, R.raw.achar);
            mpl.setOnCompletionListener(mediaPlayer -> {
                //音声片付け
                mediaPlayer.release();
            });
            //音声停止
            mpl.start();
        }
    }
    public void draw(){
        binding.result.setText("あいこです");
        binding.resultimg.setImageResource(R.drawable.onemore);
        CheckBox sound=findViewById(R.id.sound);
        if(sound.isChecked()) {
            //音声入れる
            MediaPlayer mpd = MediaPlayer.create(this, R.raw.aikode);
            mpd.setOnCompletionListener(mediaPlayer -> {
                //音声片付け
                mediaPlayer.release();
            });
            //音声停止
            mpd.start();
        }
    }
    public void show(){
        binding.result2.setText("あなたは"+play+"、COMは"+com+"を出しました");
    }

    public void showVideo(){
        //動画ID
        ImageView imageView = findViewById(R.id.resultimg);
        VideoView videoView = findViewById(R.id.video);
        //5連勝以上の時だけ動画に切り替え
        //画像と動画
        imageView.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        videoView.bringToFront();

        //画像と動画
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sugoisugoianime);
        videoView.setVideoURI(uri);

        videoView.start();
    }

    public void showVideonosound(){
        //動画ID
        ImageView imageView = findViewById(R.id.resultimg);
        VideoView videoView = findViewById(R.id.video);
        //5連勝以上の時だけ動画に切り替え
        //画像と動画
        imageView.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        videoView.bringToFront();

        //画像と動画
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sugoinosound);
        videoView.setVideoURI(uri);

        videoView.start();
    }

    public void showImage(){
        //動画ID
        ImageView imageView = findViewById(R.id.resultimg);
        VideoView videoView = findViewById(R.id.video);
        videoView.stopPlayback();
        videoView.setVisibility(View.GONE);
        imageView.setVisibility((View.VISIBLE));
    }

     @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bgm != null) {
            bgm.stop();
            bgm.release();
            bgm = null;
        }
    }


}