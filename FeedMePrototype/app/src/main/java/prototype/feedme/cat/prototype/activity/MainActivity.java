package prototype.feedme.cat.prototype.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import prototype.feedme.cat.prototype.R;
import prototype.feedme.cat.prototype.util.MusicService;


public class MainActivity extends ActionBarActivity {

    private ImageView main_cat_paw;
    private ImageView cat_btn, rank_btn, music_btn;
    private boolean button_vision = false, music_check = false;
    private Animation cat_paw_anim, cat_btn_anim, rank_btn_anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controlMusic();

        music_btn = (ImageView) findViewById(R.id.music_btn);


        cat_btn = (ImageView) findViewById(R.id.start_btn);
        cat_btn.setVisibility(View.INVISIBLE);


        rank_btn = (ImageView) findViewById(R.id.rank_btn);
        rank_btn.setVisibility(View.INVISIBLE);


        main_cat_paw = (ImageView) findViewById(R.id.main_paw_button);

        cat_paw_anim = AnimationUtils.loadAnimation(this, R.anim.main_button_scaling);
        cat_btn_anim = AnimationUtils.loadAnimation(this, R.anim.main_child_btn_anim);
        rank_btn_anim = AnimationUtils.loadAnimation(this, R.anim.main_child_btn_rank_anim);

        main_cat_paw.setAnimation(cat_paw_anim);
        main_cat_paw.startAnimation(cat_paw_anim);
        cat_btn.clearAnimation();
        rank_btn.clearAnimation();

        main_cat_paw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!button_vision) {
                    cat_btn.setVisibility(View.VISIBLE);
                    rank_btn.setVisibility(View.VISIBLE);
                    cat_btn.setAnimation(cat_btn_anim);
                    rank_btn.setAnimation(rank_btn_anim);

                    cat_btn.startAnimation(cat_btn_anim);
                    rank_btn.startAnimation(rank_btn_anim);
                }
                else {
                    cat_btn.clearAnimation();
                    rank_btn.clearAnimation();
                    cat_btn.setVisibility(View.INVISIBLE);
                    rank_btn.setVisibility(View.INVISIBLE);
                }
                button_vision = !button_vision;
            }
        });

        cat_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_btn.setVisibility(View.INVISIBLE);
                rank_btn.setVisibility(View.INVISIBLE);
                button_vision = !button_vision;

                startCatActivity();
            }
        });

        rank_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_btn.setVisibility(View.INVISIBLE);
                rank_btn.setVisibility(View.INVISIBLE);
                button_vision = !button_vision;

                startRankActivity();
            }
        });

        music_btn.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlMusic();
            }
        });
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
    private void startCatActivity() {
        Intent intent = new Intent(this, CatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("from", 0);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void startRankActivity() {
        Intent intent = new Intent(this, RankActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onStop();
        Intent serv = new Intent(this,MusicService.class);
        stopService(serv);
    }

    private void controlMusic() {
        if(music_check) {
            Intent serv = new Intent(this,MusicService.class);
            stopService(serv);
            ImageView imageView = (ImageView) findViewById(R.id.music_btn);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.music_btn_off));
        } else {
            Intent intent_music = new Intent(this, MusicService.class);
            startService(intent_music);
            ImageView imageView = (ImageView) findViewById(R.id.music_btn);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.music_btn_on));
        }

        music_check = !music_check;
    }
}
