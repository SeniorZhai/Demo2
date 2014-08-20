package com.gkong.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gkong.app.R;
import com.gkong.app.model.BBSBoard;

public class MyArrayAdapter extends BaseAdapter {

	private ArrayList<BBSBoard> list;
	private Context context;
	public MyArrayAdapter(Context context,ArrayList<BBSBoard> list) {
		this.list = list;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.above_list_item, null);
		}
		TextView titleView = (TextView)convertView.findViewById(R.id.item_title);
		TextView timeView = (TextView)convertView.findViewById(R.id.item_time);
		TextView childView = (TextView)convertView.findViewById(R.id.item_child);
		TextView hitsView = (TextView)convertView.findViewById(R.id.item_hits);
		BBSBoard obj = list.get(position);
		titleView.setText(obj.getTitle());
		timeView.setText(obj.getDateAndTime());
		childView.setText("回复:"+obj.getChild());
		hitsView.setText("人气:"+obj.getHits());
		return convertView;
	}
	

}
