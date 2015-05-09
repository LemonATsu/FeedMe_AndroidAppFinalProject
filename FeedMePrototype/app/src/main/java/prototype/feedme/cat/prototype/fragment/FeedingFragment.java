package prototype.feedme.cat.prototype.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import prototype.feedme.cat.prototype.R;
import prototype.feedme.cat.prototype.activity.CatActivity;
import prototype.feedme.cat.prototype.activity.FeedingActivitiy;
import prototype.feedme.cat.prototype.cat.Cat;
import prototype.feedme.cat.prototype.db.DBHelper;
import prototype.feedme.cat.prototype.food.Food;


public class FeedingFragment extends android.support.v4.app.Fragment{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final float RATIO_GOOD = 4.5f, RATIO_BAD = 3.3f;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Toast toast;
    private ToggleButton[] toggleBtn;
    private ImageView feed_btn;
    private Food[] food;
    private List<Food> list;



    public static FeedingFragment newInstance(String param1, String param2) {
        FeedingFragment fragment = new FeedingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FeedingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        toggleBtn = new ToggleButton[15];
        food = initFood();
        list = new ArrayList<Food>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeding, container, false);

        //makeToast("選擇食物種類並記錄!");
        feed_btn = (ImageView) view.findViewById(R.id.feed_btn);


        toggleBtn[0] = (ToggleButton) view.findViewById(R.id.toggleButton_1);
        toggleBtn[1] = (ToggleButton) view.findViewById(R.id.toggleButton_2);
        toggleBtn[2] = (ToggleButton) view.findViewById(R.id.toggleButton_3);

        toggleBtn[3] = (ToggleButton) view.findViewById(R.id.toggleButton_4);
        toggleBtn[4] = (ToggleButton) view.findViewById(R.id.toggleButton_5);
        toggleBtn[5] = (ToggleButton) view.findViewById(R.id.toggleButton_6);

        toggleBtn[6] = (ToggleButton) view.findViewById(R.id.toggleButton_7);
        toggleBtn[7] = (ToggleButton) view.findViewById(R.id.toggleButton_8);
        toggleBtn[8] = (ToggleButton) view.findViewById(R.id.toggleButton_9);

        toggleBtn[9] = (ToggleButton) view.findViewById(R.id.toggleButton_10);
        toggleBtn[10] = (ToggleButton) view.findViewById(R.id.toggleButton_11);
        toggleBtn[11] = (ToggleButton) view.findViewById(R.id.toggleButton_12);

        feed_btn.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Cat.getStatus() != Cat.CAT_DEAD) feedCat();
                else {
                    makeToast("死貓是吃不了東西的...");
                    Intent intent = new Intent();
                    FeedingActivitiy.activity.setResult(FeedingActivitiy.FEEDING_RESULT - 1, intent);
                    FeedingActivitiy.activity.finish();
                }
            }
        });


        return view;
    }

    private void addLog(String food, float result) {
        DBHelper db_opener = new DBHelper(getActivity());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        SQLiteDatabase db = db_opener.getWritableDatabase();
        ContentValues values = new ContentValues();

        System.out.println("Start adding data to db");
        System.out.println("Food String List: " + food);
        values.put(DBHelper.COL_DATE, format.format(Calendar.getInstance().getTime()));
        values.put(DBHelper.COL_FOOD_NAME, food);
        values.put(DBHelper.COL_SCOR_TOT, result);
        db.insert(DBHelper.TABLE_NAME, null, values);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void feedCat() {
        list = new ArrayList<Food>();
        StringBuilder str_builder = new StringBuilder();
        System.out.println("Start feeding");

        //check toggle button
        for (int i = 0; i <= 11; i++)
            if (toggleBtn[i].isChecked()) {
                list.add(food[i]);
                str_builder.append(food[i].getName() + " ");
            }
        //check list
        if (!list.isEmpty()) {
            float size = (float) list.size();
            float[] result = {0, 0, 0, 0, 0, 0, 0};

            for (Food iterator : list) {
                float[] temp = iterator.getIngredient();
                for (int i = 0; i <= 5; i++) result[i] += temp[i];
            }
            for (int i = 0; i <= 5; i++) result[i] = result[i] / size + size / 4;
            //feeding process
            Cat.feed(result);
            addLog(str_builder.toString(), result[5]);
            System.out.println("finished");

            makeToast("餵食成功!");
            //setting result;
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt("from", 1);
            intent.putExtras(bundle);

            //back to CatActivity
            FeedingActivitiy.activity.setResult(FeedingActivitiy.FEEDING_RESULT, intent);
            FeedingActivitiy.activity.finish();


        } else {
            System.out.println("failed feeding: no food to feed");
            makeToast("您沒有選擇食物唷!");
        }
    }

    private Food[] initFood() {
        Food[] result = new Food[15];
        result[0] = new Food("便當", 0.5f * RATIO_GOOD, 0.25f * RATIO_GOOD, 0.75f * RATIO_GOOD, 0.0f * RATIO_GOOD, 0.0f * RATIO_GOOD);
        result[1] = new Food("蔬果", 1.5f * RATIO_GOOD, 0.0f * RATIO_GOOD, 0.0f * RATIO_GOOD, 0.0f * RATIO_GOOD, 0.0f * RATIO_GOOD);
        result[2] = new Food("雜糧", 0.75f * RATIO_GOOD, 0.0f * RATIO_GOOD, 0.75f * RATIO_GOOD, 0.0f * RATIO_GOOD, 0.0f * RATIO_GOOD);
        result[3] = new Food("豆類", 0.0f * RATIO_GOOD, 1.0f * RATIO_GOOD, 0.0f * RATIO_GOOD, 0.0f * RATIO_GOOD, 0.0f * RATIO_GOOD);
        result[4] = new Food("瘦肉", 0.0f * RATIO_GOOD, 1.0f * RATIO_GOOD, 0.0f * RATIO_GOOD, 0.0f * RATIO_GOOD, 0.0f * RATIO_GOOD);
        result[5] = new Food("奶類", 0.0f * RATIO_GOOD, 0.0f * RATIO_GOOD, 0.0f * RATIO_GOOD, 0.0f * RATIO_GOOD, 1.5f * RATIO_GOOD);

        result[6] = new Food("炸物", -3.0f * RATIO_BAD, -3.0f * RATIO_BAD, -3.0f * RATIO_BAD, 0.0f * RATIO_BAD, 0.0f * RATIO_BAD);
        result[7] = new Food("速食", -3.0f * RATIO_BAD, -3.0f * RATIO_BAD, -3.0f * RATIO_BAD, 0.0f * RATIO_BAD, 0.0f * RATIO_BAD);
        result[8] = new Food("泡麵", -3.0f * RATIO_BAD, -3.0f * RATIO_BAD, -3.0f * RATIO_BAD, 0.0f * RATIO_BAD, 0.0f * RATIO_BAD);
        result[9] = new Food("零嘴", -0.5f * RATIO_BAD, -0.5f * RATIO_BAD, -0.5f * RATIO_BAD, 0.0f * RATIO_BAD, 0.0f * RATIO_BAD);
        result[10] = new Food("甜食", -0.5f * RATIO_BAD, -0.5f * RATIO_BAD, -0.5f * RATIO_BAD, 0.0f * RATIO_BAD, 0.0f * RATIO_BAD);
        result[11] = new Food("醃製品",  -3.0f * RATIO_BAD, -3.0f * RATIO_BAD, -3.0f * RATIO_BAD, 0.0f * RATIO_BAD, 0.0f * RATIO_BAD);
        return result;
    }
    private void makeToast(String text) {
        toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
