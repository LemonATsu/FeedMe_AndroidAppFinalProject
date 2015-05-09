package prototype.feedme.cat.prototype.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import prototype.feedme.cat.prototype.R;
import prototype.feedme.cat.prototype.cat.Cat;
import prototype.feedme.cat.prototype.fragment.FeedingFragment;
import prototype.feedme.cat.prototype.fragment.TimeFragment;


public class CatActivity extends FragmentActivity {
    //about view
    private String fontpath = "fonts/krist.TTF";
    private ImageView[] cloud_unit = new ImageView[2];
    private ImageView cat_feed_btn, cat_log_btn, cat_clk_btn;
    private ImageView cat_act_status_ill, cat_act_status_nor, cat_act_status_wel, cat_act_status_dead;
    private TextView days_cnt, dead_text;
    //about Animation
    private Animation ill_cat_anim, cat_touching;
    //normal parameter
    private static Integer DAY_COUNT = 0;
    //data saving
    public static final String CAT_DAT = "CAT_DAT";
    public static final String CAT_HEALTH = "health";
    public static final String CAT_LIFE = "days";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cat_activity);



        dead_text = (TextView) findViewById(R.id.dead_text);
        //status initialization
        cat_act_status_nor = (ImageView) findViewById(R.id.cat_act_status_nor);
        cat_act_status_ill = (ImageView) findViewById(R.id.cat_act_status_ill);
        cat_act_status_wel = (ImageView) findViewById(R.id.cat_act_status_wel);
        cat_act_status_dead = (ImageView) findViewById(R.id.cat_act_status_dead);
        cat_touching = AnimationUtils.loadAnimation(this, R.anim.cat_touching);
        if(getIntent().getExtras() != null) {
            handleBundle(this.getIntent().getExtras().getInt("from"));


            //button initialization
            cat_feed_btn = (ImageView) findViewById(R.id.cat_btn_feed);
            cat_log_btn = (ImageView) findViewById(R.id.cat_btn_log);
            cat_clk_btn = (ImageView) findViewById(R.id.cat_btn_clk);

            //days and font initialization
            days_cnt = (TextView) findViewById(R.id.Days_count);
            Typeface tf = Typeface.createFromAsset(getAssets(),
                    fontpath);
            days_cnt.setTypeface(tf);
            days_cnt.setText(setDays());

            //cat_feed_btn click listener
            cat_feed_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startFeedingActivityToFeedingFragment();
                }
            });

            //cat_log_btn click listener
            cat_log_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startFeedingActivityToLogFragment();
                }
            });

            //cat_clk_btn click listener
            cat_clk_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TimeFragment newFragment = new TimeFragment();
                    newFragment.show(getSupportFragmentManager(), "dialog");
                }
            });
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cat, menu);
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

    private void startFeedingActivityToFeedingFragment() {
       Intent intent = new Intent(this, FeedingActivitiy.class);
       Bundle bundle = new Bundle();
       bundle.putInt("target", 0);
       intent.putExtras(bundle);
       startActivityForResult(intent, FeedingActivitiy.FEEDING_RESULT);
    }

    private void startFeedingActivityToLogFragment() {
        Intent intent = new Intent(this, FeedingActivitiy.class);
        Bundle bundle = new Bundle();
        bundle.putInt("target", 1);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void setCloudAnimation() {
        //anim initialization
        cloud_unit[0] = (ImageView) findViewById(R.id.cloud_img);
        cloud_unit[1] = (ImageView) findViewById(R.id.cloud_img_2);
        Animation animation_c1
                = AnimationUtils.loadAnimation(this, R.anim.cat_act_cloud_move);
        Animation animation_c2
                = AnimationUtils.loadAnimation(this, R.anim.cat_act_cloud_move);
        animation_c2.setDuration(12000);

        cloud_unit[0].startAnimation(animation_c1); cloud_unit[1].startAnimation(animation_c2);
    }
    public void changeCatStatus(int state) {

        cat_act_status_ill.clearAnimation();
       // cat_act_status_nor.clearAnimation();
       // cat_act_status_wel.clearAnimation();
        cat_act_status_nor.setVisibility(View.INVISIBLE);
        cat_act_status_ill.setVisibility(View.INVISIBLE);
        cat_act_status_wel.setVisibility(View.INVISIBLE);
        cat_act_status_dead.setVisibility(View.INVISIBLE);
        if(state == Cat.CAT_NOR) {
            cat_act_status_nor.setVisibility(View.VISIBLE);

            cat_act_status_nor.setOnClickListener(new ImageView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cat_act_status_nor.startAnimation(cat_touching);
                }
            });
        }
        else if(state == Cat.CAT_ILL) {
            cat_act_status_ill.setVisibility(View.VISIBLE);
            ill_cat_anim = AnimationUtils.loadAnimation( this, R.anim.ill_cat_anim);
            cat_act_status_ill.startAnimation(ill_cat_anim);
        }
        else if(state == Cat.CAT_WEL) {
            cat_act_status_wel.setVisibility(View.VISIBLE);

            cat_act_status_wel.setOnClickListener(new ImageView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cat_act_status_wel.startAnimation(cat_touching);
                }
            });
        }
        else {
            dead_text.setVisibility(View.VISIBLE);
            cat_act_status_dead.setVisibility(View.VISIBLE);
            cat_act_status_dead.setOnClickListener(new ImageView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Cat.setBorn(format.format(Calendar.getInstance().getTime()));
                    Cat.setDays(0);
                    Cat.setDiet(0, 0, 0, 0, 0);
                    Cat.setHealth(140);
                    dead_text.setVisibility(View.INVISIBLE);
                    days_cnt.setText(setDays());
                    changeCatStatus(Cat.getStatus());
                }
            });
        }
    }

    private void readSharePreferences() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SharedPreferences sharedPref = getSharedPreferences( CAT_DAT, MODE_PRIVATE);
        Cat.setBorn(sharedPref.getString(CAT_LIFE, format.format(Calendar.getInstance().getTime())));
        Cat.setDiet(sharedPref.getFloat("veg", 0), sharedPref.getFloat("pro", 0),
                  sharedPref.getFloat("sta", 0), sharedPref.getFloat("oil", 0),
                  sharedPref.getFloat("dai", 0));
        Cat.setHealth(sharedPref.getFloat( CAT_HEALTH, 130));
        changeCatStatus(Cat.getStatus());
    }
    private void writeSharePreferences() {
        SharedPreferences.Editor editor = getSharedPreferences(CAT_DAT, MODE_PRIVATE).edit();
        editor.putString(CAT_LIFE, Cat.getBorn());
        editor.putFloat(CAT_HEALTH, Cat.getHealth());
        editor.putFloat("veg", Cat.getVEG());
        editor.putFloat("pro", Cat.getPRO());
        editor.putFloat("sta", Cat.getSTA());
        editor.putFloat("oil", Cat.getOIL());
        editor.putFloat("dai", Cat.getDAI());

        editor.commit();
        System.out.println("Status saved");
    }
    protected void onStop() {
        super.onStop();
        cloud_unit[0].clearAnimation();
        cloud_unit[1].clearAnimation();
        cat_act_status_nor.clearAnimation();
        cat_act_status_wel.clearAnimation();
        cat_act_status_ill.clearAnimation();
        writeSharePreferences();
    }
    protected void onResume() {
        super.onResume();
        setCloudAnimation();
        changeCatStatus(Cat.getStatus());
    }

    private void handleBundle (int extra) {
        if(extra != 0) {
            System.out.println("from feeding activity");
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.cat_eating);
            int cat_status = Cat.getStatus();
            changeCatStatus(cat_status);

            if(cat_status == Cat.CAT_WEL)  cat_act_status_wel.startAnimation(anim);
            else if(cat_status == Cat.CAT_NOR) cat_act_status_nor.startAnimation(anim);
            else if(cat_status == Cat.CAT_DEAD) {
                Toast.makeText(this, "在你的細心照顧下你的貓死了，GOOD JOB!", Toast.LENGTH_SHORT).show();
            } else {
                ill_cat_anim = AnimationUtils.loadAnimation( this, R.anim.ill_cat_anim);
                cat_act_status_ill.startAnimation(ill_cat_anim);
            }

        }
        else {
            readSharePreferences();
            System.out.println("from other");
        }
    }
    private String setDays() {
        String result;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date born = format.parse(Cat.getBorn());
            Date today = format.parse(format.format(Calendar.getInstance().getTime()));
            long days =  (today.getTime() - born.getTime()) / (24 * 60 * 60 * 1000);
            System.out.println(Cat.getBorn());
            System.out.println(format.format(Calendar.getInstance().getTime()));
            System.out.println("days" + days);
            return Long.toString(days);
        } catch (ParseException e) {
            e.printStackTrace();
            return "0";
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            switch (requestCode) {
                case 1:
                    if(resultCode == FeedingActivitiy.FEEDING_RESULT) handleBundle(data.getExtras().getInt("from"));
                default:
                    ;
            }
        }
    }
}
