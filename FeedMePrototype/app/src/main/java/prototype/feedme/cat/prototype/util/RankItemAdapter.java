package prototype.feedme.cat.prototype.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import netdb.course.softwarestudio.service.rest.model.Image;
import prototype.feedme.cat.prototype.R;
import prototype.feedme.cat.prototype.rankingsystem.model.UserData;

/**
 * Created by AT on 2015/1/19.
 */
public class RankItemAdapter extends BaseAdapter {
    private List<UserData> dataList;
    private LayoutInflater inflater;

    public RankItemAdapter(Context context, ArrayList<UserData> list) {
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
        int place = position + 1;
        convertView = inflater.inflate(R.layout.rank_item, null);
        UserData userData = dataList.get(position);

        TextView username = (TextView) convertView.findViewById(R.id.UserName);
        TextView days = (TextView) convertView.findViewById(R.id.rank_days);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.test_image);

        imageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_launcher));
        username.setText("NO." + place + ": " + userData.getUser());
        days.setText("天數: " + Long.toString(userData.getDays()));
        return convertView;
    }
}
