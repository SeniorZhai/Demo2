package com.gkong.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gkong.app.MyApplication;
import com.gkong.app.R;
import com.gkong.app.model.ClassBoardHandle;
import com.gkong.app.model.ClassBoardHandle.GroupItem;
import com.gkong.app.model.ClassBoardSrc.Item;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class EditActivity extends Activity {
	private List<GroupItem> mList;
	private ExpandableListView listView;
	private Button save;
	private MyApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);

		app = (MyApplication) getApplication();
		ImageView aboveGoHome = (ImageView) findViewById(R.id.edit_title_gohome);
		aboveGoHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		save = (Button) findViewById(R.id.edit_title_save);
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				app.dataGroupList = mList;
				app.subscriboList.clear();
				for (int i = 0; i < app.dataGroupList.size(); i++) {
					for (Item item : app.dataGroupList.get(i).getItems()) {
						if (item.isSelect()) {
							app.subscriboList.add(item);
						}
					}
				}
				app.saveList();
				finish();
			}
		});
		mList = new ArrayList<ClassBoardHandle.GroupItem>(app.dataGroupList);
		listView = (ExpandableListView) findViewById(R.id.edit_list);
		listView.setAdapter(new MyAdapter(mList, this));
		listView.expandGroup(0);
	}

	class MyAdapter extends BaseExpandableListAdapter {
		private List<GroupItem> mList;
		private Context mContext;

		public MyAdapter(List<GroupItem> list, Context context) {
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
						R.layout.eidt_expand_list_father_item, null);
			}
			TextView tv = (TextView) view.findViewById(R.id.expand_tv);
			tv.setText(mList.get(groupPosition).getBoardName());
			return view;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.edit_expand_list_item, null);
			final Item item = mList.get(groupPosition).getItems()
					.get(childPosition);
			TextView tv = (TextView) convertView.findViewById(R.id.expand_tv);
			tv.setText(item.getBoardName());
			CheckBox view = (CheckBox) convertView
					.findViewById(R.id.expand_check);
			view.setChecked(item.isSelect());
			view.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					item.setSelect(isChecked);
				}
			});
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}
}
