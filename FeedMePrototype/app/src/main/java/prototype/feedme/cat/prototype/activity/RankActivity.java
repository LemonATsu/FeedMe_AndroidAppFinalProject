package prototype.feedme.cat.prototype.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import netdb.course.softwarestudio.service.rest.RestManager;
import netdb.course.softwarestudio.service.rest.model.Image;
import prototype.feedme.cat.prototype.R;
import prototype.feedme.cat.prototype.cat.Cat;
import prototype.feedme.cat.prototype.rankingsystem.model.UserData;
import prototype.feedme.cat.prototype.util.RankItemAdapter;

public class RankActivity extends ActionBarActivity {
    private RestManager restMgr;
    private ProgressBar p_bar;
    private EditText user_name;
    private RankItemAdapter adapter;
    private ListView rank_list;
    private Button upload_btn;
    private ImageButton refresh_btn;
    private ArrayList<UserData> user_list = new ArrayList<UserData>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        readSharePreferences();
        refresh_btn = (ImageButton) findViewById(R.id.refresh_btn);
        upload_btn = (Button) findViewById(R.id.upload_btn);
        user_name = (EditText) findViewById(R.id.editText_upload);
        rank_list = (ListView) findViewById(R.id.rank_list);
        adapter = new RankItemAdapter(this, user_list);
        rank_list.setAdapter(adapter);

        p_bar = (ProgressBar) findViewById(R.id.rank_progressBar);

        restMgr = RestManager.getInstance(this);
        requestRank();

        upload_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(user_name.getWindowToken(), 0);
                uploadData();
            }
        });

        refresh_btn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(user_name.getWindowToken(), 0);
                requestRank();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rank, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void uploadData() {
        UserData userData;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        p_bar.setVisibility(View.VISIBLE);
        rank_list.setVisibility(View.INVISIBLE);
        try {
            Date born = format.parse(Cat.getBorn());
            Date today = format.parse(format.format(Calendar.getInstance().getTime()));
            long days =  (today.getTime() - born.getTime()) / (24 * 60 * 60 * 1000);
            userData = new UserData(user_name.getText().toString(), days);

            restMgr.postResource(UserData.class, userData, new RestManager.PostResourceListener() {
                @Override
                public void onResponse(int code, Map<String, String> headers) {
                    System.out.println("Success: 200 OK");
                }

                @Override
                public void onRedirect(int code, Map<String, String> headers, String url) {

                }

                @Override
                public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                    Toast.makeText(RankActivity.this, "網路似乎有些問題...唔", Toast.LENGTH_SHORT).show();
                    p_bar.setVisibility(View.INVISIBLE);
                }
            }, null);
        } catch (ParseException e) {
            e.printStackTrace();
            p_bar.setVisibility(View.INVISIBLE);
            return;
        }

        p_bar.setVisibility(View.INVISIBLE);
        requestRank();
    }
    private void requestRank() {
        p_bar.setVisibility(View.VISIBLE);
        rank_list.setVisibility(View.INVISIBLE);
        restMgr.listResource(UserData.class, null, new RestManager.ListResourceListener<UserData>() {
            @Override
            public void onResponse(int code, Map<String, String> headers, List<UserData> resources) {
                System.out.println("Success: 200 OK");
                rank_list.setVisibility(View.VISIBLE);
                p_bar.setVisibility(View.INVISIBLE);
                if(resources != null) {
                    user_list.clear();
                    for(UserData d : resources)
                        user_list.add(d);
                }
                RankActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {

            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                Toast.makeText(RankActivity.this, "網路似乎有些問題...唔", Toast.LENGTH_SHORT).show();
                p_bar.setVisibility(View.INVISIBLE);
            }
        },null);
    }

    private void readSharePreferences() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SharedPreferences sharedPref = getSharedPreferences( CatActivity.CAT_DAT, MODE_PRIVATE);
        Cat.setBorn(sharedPref.getString(CatActivity.CAT_LIFE, format.format(Calendar.getInstance().getTime())));
        Cat.setDiet(sharedPref.getFloat("veg", 0), sharedPref.getFloat("pro", 0),
                sharedPref.getFloat("sta", 0), sharedPref.getFloat("oil", 0),
                sharedPref.getFloat("dai", 0));
        Cat.setHealth(sharedPref.getFloat(CatActivity.CAT_HEALTH, 130));
    }


}
