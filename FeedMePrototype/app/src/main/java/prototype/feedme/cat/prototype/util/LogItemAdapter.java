package prototype.feedme.cat.prototype.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import prototype.feedme.cat.prototype.R;
import prototype.feedme.cat.prototype.db.DBHelper;


/**
 * Created by AT on 2015/1/19.
 */
public class LogItemAdapter extends BaseAdapter {
    private List<HashMap<String, Object>> dataList;
    private LayoutInflater inflater;

    public LogItemAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
        this.dataList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.log_item, null);
        HashMap<String, Object> mapdata = dataList.get(position);
        TextView food_content = (TextView) convertView.findViewById(R.id.food_content);
        TextView date_text = (TextView) convertView.findViewById(R.id.date_text);
        ImageView cat_feel = (ImageView) convertView.findViewById(R.id.cat_feel_view);
        float pts = (float)mapdata.get(DBHelper.COL_SCOR_TOT);

        //System.out.println((float)mapdata.get(DBHelper.COL_SCOR_TOT) + "pts");

        if(pts < 0.0f)
            cat_feel.setImageDrawable(convertView.getResources().getDrawable(R.drawable.log_cat_feel_bad));
        food_content.setText((String) mapdata.get(DBHelper.COL_FOOD_NAME));
        date_text.setText((String) mapdata.get(DBHelper.COL_DATE));
        return convertView;
    }
}
