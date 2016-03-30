package com.hyuan.diy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyuan.diy.R;

public class MusicsAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater inflater;
	private String[] name;

	public MusicsAdapter(Context context,String[] name){
		this.context = context;
		this.name = name;
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return name.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	class ViewHolder{
		TextView tv;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;  
		if (convertView == null) {
			inflater = LayoutInflater.from(context); 
			convertView = inflater.inflate(R.layout.item, null);
			holder = new ViewHolder();  
            holder.tv = (TextView) convertView.findViewById(R.id.textView1);
            convertView.setTag(holder); 
		}else {
			holder =(ViewHolder) convertView.getTag(); 
		}
		
		holder.tv.setText(name[position]);
		
		return convertView;
	}

}
