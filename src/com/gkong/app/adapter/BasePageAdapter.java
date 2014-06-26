package com.gkong.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;

public class BasePageAdapter extends FragmentStatePagerAdapter {

	public List<String> tabs = new ArrayList<String>();
	public ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
	
	private Activity mActivity;

	public BasePageAdapter(FragmentActivity activity) {
		super(activity.getSupportFragmentManager());
		this.mActivity = activity;
	}
	
	public void addFragment(List<String> mlist,List<Object> listObject){
		tabs.addAll(mlist);
		for (int i = 0; i < listObject.size(); i++) {
		}
	}
	
	public void addNullFragment(){
		
	}
	public void Clear(){
		mFragments.clear();
		tabs.clear();	
	}
	
	public void addTab(Fragment fragment) {
		mFragments.add(fragment);
		notifyDataSetChanged();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return tabs.get(position);
	}
	
	@Override
	public Fragment getItem(int arg0) {
		return mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}
