package com.hyuan.diy.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyuan.diy.R;
import com.hyuan.diy.entity.Musics;
import com.hyuan.diy.utils.Constant;
import com.hyuan.diy.utils.SaveData;
import com.hyuan.diy.view.MainActivity;

public class CollectAdapter extends BaseAdapter{
	
	private int resource;
	private Context context;
	private ArrayList<Musics> musics;
	public CollectAdapter(Context context,int resource, ArrayList<Musics> musics) {
		this.context=context;
		this.resource=resource;
		this.musics = musics;
	}
	@Override
	public int getCount() {
		return musics.size();
	}
	@Override
	public Object getItem(int position) {
		return musics.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	class ViewHolder{
		TextView   nameTv;
		TextView   sizeTv;
		ImageView  iv_collect;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		//1.item����
		if(convertView==null){//�жϵ�Ŀ�ģ�����item���ֵĹ�������
		   convertView=View.inflate(context, resource, null);
		   //����˶����Ŀ��:����convertView������findViewById�����ĵ��ô���
		   vh=new ViewHolder();
		   //��converView�е���ʾ���ݵ�view��Ȼ�󽫴�view�ĵ�ַ���浽viewholder������
		   vh.nameTv=(TextView)convertView.findViewById(R.id.text1);
		   vh.sizeTv=(TextView)convertView.findViewById(R.id.text2);
		   vh.iv_collect = (ImageView) convertView.findViewById(R.id.collect);
		   //��convertView����ViewHolder
		   convertView.setTag(vh);//Object obj=vh;
		}else{
		   vh=(ViewHolder)convertView.getTag();
		}//convertViewΪ���Ƴ����б���ֶ���(�˶��������)
		//2.item����
		final Musics music = musics.get(position);
		//3.�����ݷŵ�item������
		vh.nameTv.setText(music.getName());
		vh.sizeTv.setText(music.getSinger());
		if (position == Constant.mHighlightIndex.get(1))
		{
			vh.nameTv.setTextColor(context.getResources().getColor(R.color.listColor));
			vh.sizeTv.setTextColor(context.getResources().getColor(R.color.listColor));
		}
		else
		{
			vh.nameTv.setTextColor(context.getResources().getColor(R.color.text));
			vh.sizeTv.setTextColor(context.getResources().getColor(R.color.text));
		}
		
		if (Constant.musicsUrl.contains(music.getData())) {
			vh.iv_collect.setImageResource(R.drawable.collect);
		}else {
			vh.iv_collect.setImageResource(R.drawable.uncollect);
		}
		
		final ViewHolder finalLHholder = vh;
		vh.iv_collect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!Constant.collectlist.contains(music)) {
					Constant.collectlist.add(music);
					Constant.musicsUrl.add(music.getData());
					finalLHholder.iv_collect.setImageResource(R.drawable.collect);
					
					/**
                     * ��������
                     */
                    SaveData.updataDatabase();
				}
				else 
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("�Ƿ�Ҫ�Ƴ�"+"\n"+music.getName());
                    builder.setTitle("�Ƴ��ղ�");
                    builder.setPositiveButton("�Ƴ�", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        	Constant.collectlist.remove(music);
        					Constant.musicsUrl.remove(music.getData());
                        	if (Constant.musics == musics) {
                        		finalLHholder.iv_collect.setImageResource(R.drawable.uncollect);
							}
                        	MainActivity.playingadapter.notifyDataSetChanged();
                        	MainActivity.collectadapter.notifyDataSetChanged();
                        	
                        	
                        	/**
                             * ��������
                             */
                            SaveData.updataDatabase();
                        	
        					}
                    });

                    builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
				}
			}
		});
		
		return convertView;
	}

}
