package com.mymorningroutine;

import java.util.ArrayList;

import com.mymorningroutine.obj.DrawerItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerItemAdapter extends BaseAdapter{
	    private Context context;
    private ArrayList<DrawerItem> drawerItems;
     
    public DrawerItemAdapter(Context context, ArrayList<DrawerItem> drawerItems){
        this.context = context;
        this.drawerItems = drawerItems;
    }
 
    @Override
    public int getCount() {
        return drawerItems.size();
    }
 
    @Override
    public Object getItem(int position) {      
        return drawerItems.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_drawer_item, null);
        }
          
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		if(drawerItems.get(position).getIcon() != null){
        	imgIcon.setImageResource(drawerItems.get(position).getIcon());   
		}
        txtTitle.setText(drawerItems.get(position).getTitle());
         convertView.setFocusable(false);
		 convertView.setClickable(false);
        return convertView;
    }


}
