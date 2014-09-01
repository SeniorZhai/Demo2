package com.gkong.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gkong.app.R;
import com.gkong.app.model.ClassBoardSrc.Item;

public class ClassAdapter extends BaseAdapter {

	private List<Item> mList;
	private Context context;

	public ClassAdapter(Context context, List<Item> list) {
		this.mList = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return mList.size();
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.behind_list_show, null);
		}
		TextView titleView = (TextView) convertView
				.findViewById(R.id.textview_behind_title);
		Item obj = mList.get(position);
		titleView.setText(obj.getBoardName());
		return convertView;
	}

}
