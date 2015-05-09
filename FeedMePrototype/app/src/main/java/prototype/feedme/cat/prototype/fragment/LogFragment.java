package prototype.feedme.cat.prototype.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import prototype.feedme.cat.prototype.R;
import prototype.feedme.cat.prototype.activity.FeedingActivitiy;
import prototype.feedme.cat.prototype.db.DBHelper;
import prototype.feedme.cat.prototype.db.SimpleCursorLoader;
import prototype.feedme.cat.prototype.util.LogItemAdapter;


public class LogFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ListView listView;
    private EditText date_query_edit;
    private Button date_query_btn;
    private TextView hinttext;
    private LogItemAdapter adapter;
    //private SimpleAdapter adapter;
    private SimpleCursorAdapter mAdapter;
    private ProgressBar p_bar;
    private SQLiteDatabase database;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static LogFragment newInstance(String param1, String param2) {
        LogFragment fragment = new LogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log, container, false);
        //fakeData();
        listView = (ListView) view.findViewById(R.id.fragmnent_log_list);
        date_query_btn = (Button) view.findViewById(R.id.log_search_btn);
        date_query_edit = (EditText) view.findViewById(R.id.date_search_edit);
        //hinttext = (TextView) view.findViewById(R.id.hintText);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        date_query_edit.setText(format.format(Calendar.getInstance().getTime()));
        System.out.println("refreshing list");
        ArrayList<HashMap<String, Object>> data_list = fillList(format.format(Calendar.getInstance().getTime()));
        //fillAdapter(data_list);

        adapter = new LogItemAdapter(getActivity(), data_list);
        listView.setAdapter(adapter);

        date_query_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
               // hinttext.setVisibility(View.INVISIBLE);
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(date_query_edit.getWindowToken(), 0);
                System.out.println("get: " + date_query_edit.getText().toString());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date_checker = null;
                try {
                    date_checker = new SimpleDateFormat("yyyy-MM-dd").parse(date_query_edit.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date_checker == null) {
                    Toast.makeText(getActivity(), "正確格式:yyyy-MM-dd", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println(format.format(date_checker));
                    refresh(format.format(date_checker));
                }
            }
        });
        return view;
    }

    private ArrayList<HashMap<String, Object>> fillList (String date_input) {
        DBHelper db_opener= new DBHelper(getActivity());
        SQLiteDatabase database = db_opener.getReadableDatabase();
        ArrayList<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();

        try{

            //Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME, null);

            Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME +  " WHERE " +
                    "DATETIME(DATE)>=DATETIME('" + date_input + " 00:00') and DATETIME(DATE)<=DATETIME('" + date_input + " 23:59')" , null);
            if(cursor.moveToLast()) {
                String food = cursor.getString(cursor.getColumnIndex(DBHelper.COL_FOOD_NAME));
                String date = cursor.getString(cursor.getColumnIndex(DBHelper.COL_DATE));
                float score = cursor.getFloat(cursor.getColumnIndex(DBHelper.COL_SCOR_TOT));
                HashMap<String, Object> map = new HashMap<String, Object>();

                map.put(DBHelper.COL_FOOD_NAME, food);
                map.put(DBHelper.COL_DATE, date);
                map.put(DBHelper.COL_SCOR_TOT, score);

                dataList.add(map);

                while (cursor.moveToPrevious()) {
                    food = cursor.getString(cursor.getColumnIndex(DBHelper.COL_FOOD_NAME));
                    date = cursor.getString(cursor.getColumnIndex(DBHelper.COL_DATE));
                    score = cursor.getFloat(cursor.getColumnIndex(DBHelper.COL_SCOR_TOT));
                    map = new HashMap<String, Object>();

                    map.put(DBHelper.COL_FOOD_NAME, food);
                    map.put(DBHelper.COL_DATE, date);
                    map.put(DBHelper.COL_SCOR_TOT, score);
                    dataList.add(map);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            database.close();
            db_opener.close();
        }
        return dataList;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void refresh(String date) {
        System.out.println("refreshing list");
        ArrayList<HashMap<String, Object>> data_list = fillList(date);
        if(data_list.isEmpty()) Toast.makeText(getActivity(), "查無紀錄", Toast.LENGTH_SHORT).show();
        //fillAdapter(data_list);

        adapter = new LogItemAdapter(getActivity(), data_list);
        listView.setAdapter(adapter);
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
}
