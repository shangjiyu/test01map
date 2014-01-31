/*******************************************************
 * @Title: SlidingmenuFragment.java
 * @Package com.test01map
 * @Description: TODO(用一句话描述该文件做什么)
 * @author shangjiyu
 * @date 2013-10-7 下午7:01:13
 * @version V1.0
 ********************************************************/

package com.test01map;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/********************************************************
 * @ClassName: SlidingmenuFragment
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author jiyu
 * @date 2013-10-7 下午7:01:13
 */

public class SlidingmenuFragment extends ListFragment {
	
	public static final String[] MENU_STRING = {"卫星地图","矢量地图","计算距离","计算面积","GPS定位","手动画点","删除最新一点","删除所有OVERLAY","退出程序"};
	
	public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, MENU_STRING));
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, container, false);
	}
	
	public void onListItemClick(ListView parent, View v, int position, long id){
		Toast.makeText(getActivity(),   
	            "You have selected " + MENU_STRING[position],   
	            Toast.LENGTH_SHORT).show();
	}
}

