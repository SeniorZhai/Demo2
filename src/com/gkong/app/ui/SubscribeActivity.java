/**
 * 
 */
package com.gkong.app.ui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gkong.app.MyApplication;
import com.gkong.app.R;
import com.gkong.app.model.ClassBoardHandle.GroupItem;

public class SubscribeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subscri);
		ExpandableListView listView = (ExpandableListView)findViewById(R.id.expanList);
		MyAdapter adapter = new MyAdapter(((MyApplication)getApplication()).mList, this);
		listView.setAdapter(adapter);
	}
	class MyAdapter extends BaseExpandableListAdapter {
		private List<GroupItem> mList;
		private Context mContext;
		public MyAdapter(List<GroupItem> list,Context context) {
			mList = list;
			mContext = context;
		}
		@Override
		public int getGroupCount() {
			return mList.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return mList.get(groupPosition).getItems().size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return mList.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// 获取父类下的每一个子类项
			return mList.get(groupPosition).getItems().get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				view = LayoutInflater.from(mContext).inflate(
						R.layout.subscribe_expand_list_fitem, null);
			}
			TextView tv = (TextView) view.findViewById(R.id.expand_tv);
			tv.setText(mList.get(groupPosition).getBoardName());
			return view;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.subscribe_expand_list_item, null);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.expand_tv);
			tv.setText(mList.get(groupPosition).getItems().get(childPosition)
					.getBoardName());
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			Toast.makeText(
					mContext,
					"点击了"
							+ mList.get(groupPosition).getItems()
									.get(childPosition).getBoardName(), 1000)
					.show();
			return false;
		}
	}
}
