package com.hyuan.diy.adapter;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyuan.diy.R;
import com.hyuan.diy.utils.Constant;
import com.hyuan.diy.utils.DownloadFile;
import com.hyuan.diy.view.VDiskFragment;

public class FileListAdapter extends BaseAdapter{

	private Context          context;
	private LayoutInflater inflater;

	public FileListAdapter(Context context){
		this.context     = context;
	    this.inflater    = LayoutInflater.from(context);
		   
	}
	@Override
	public int getCount() {
		return VDiskFragment.contents.size();
	}

	@Override
	public Object getItem(int position) {
		return VDiskFragment.contents.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class ViewHolder{
		TextView tv_name;
		ImageView ib_type,iv_download;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		if (convertView == null) {
			inflater = LayoutInflater.from(context);
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.file_list, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.name);
			holder.ib_type = (ImageView) convertView.findViewById(R.id.type);
			holder.iv_download = (ImageView) convertView.findViewById(R.id.iv_download);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		final String filename = VDiskFragment.contents.get(position).fileName();
		if (!Constant.VDiskList.contains(filename)) {
			Constant.VDiskList.add(filename);
		}
		if (VDiskFragment.contents.get(position).isDir) {
			holder.ib_type.setImageResource(R.drawable.dir);
			holder.iv_download.setEnabled(false);
		}else 
		{
			String type = filename.substring(filename.lastIndexOf('.'));
			if (type.equals(".mp3")||type.equals(".wav")||type.equals(".flac")||type.equals(".")) {
				holder.ib_type.setImageResource(R.drawable.micon);
				holder.iv_download.setImageResource(R.drawable.download);
			}else if (type.equals(".jpg")) {
				holder.ib_type.setImageResource(R.drawable.img);
			}{
				//VDiskFragment.contents.remove(position);
			}
		}
		if (position == Constant.mHighlightIndex.get(2))
		{
			holder.tv_name.setTextColor(context.getResources().getColor(R.color.listColor));
		}
		else
		{
			holder.tv_name.setTextColor(context.getResources().getColor(R.color.text));
		}
		holder.tv_name.setText(filename);
		holder.iv_download.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("tag", "filename = "+filename);
				String targetPath = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/VDisk_SDK_cache/" + filename;
				DownloadFile downloadFile = new DownloadFile(context,
						VDiskFragment.mApi, filename, targetPath);
				downloadFile.execute();
			}
		});
		
		
		return convertView;
	}

}
