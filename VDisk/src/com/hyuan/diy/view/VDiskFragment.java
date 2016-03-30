package com.hyuan.diy.view;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.hyuan.diy.R;
import com.hyuan.diy.adapter.FileListAdapter;
import com.hyuan.diy.myapplication.MyApplicaotion;
import com.hyuan.diy.utils.Constant;
import com.hyuan.diy.utils.ShowFilesAsyncTask;
import com.vdisk.android.VDiskAuthSession;
import com.vdisk.net.VDiskAPI;
import com.vdisk.net.VDiskAPI.Entry;
import com.vdisk.net.VDiskAPI.VDiskLink;
import com.vdisk.net.session.AppKeyPair;
import com.vdisk.net.session.Session.AccessType;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class VDiskFragment extends Fragment {

	public static View view;
	public static VDiskAuthSession session;
	public static List<Entry> contents = new ArrayList<VDiskAPI.Entry>();
	
	public static void setContents(List<Entry> contents) {
		VDiskFragment.contents = contents;
	}

	/**
	 * 使用VDiskAPI，可以调用所有的微盘接口，非常重要。
	 * 
	 * Use VDiskAPI for calling all the API of VDisk, IMPOPTANT.
	 */
	public static VDiskAPI<VDiskAuthSession> mApi;
	private ImageView ib_back;
	public static FileListAdapter adapter;
	public static ListView files_list;
	private String filename = "";
	private String FileName;
	private String file;

	public VDiskFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_vdisk, container, false);
		
		AppKeyPair appKeyPair = new AppKeyPair(VDiskActivity.CONSUMER_KEY,
				VDiskActivity.CONSUMER_SECRET);

		session = VDiskAuthSession.getInstance(getActivity(), appKeyPair,
				AccessType.APP_FOLDER);

		mApi = new VDiskAPI<VDiskAuthSession>(session);
		
		
		
		initView();
		setListener();
		
		
		return view;
	}
	
	
	private void setListener() {
		files_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try 
				{
					if (Constant.playingUrllList != Constant.VDiskList)
					{
						Constant.playingUrllList = Constant.VDiskList;
					}
					if (contents.get(position).isDir) {
						Constant.VDiskList.clear();
						FileName ="/"+contents.get(position).fileName();
						queryData(FileName);
					}else 
					{
						file = contents.get(position).fileName();
						String type = file.substring(file.lastIndexOf('.'));
						if (type.equals(".mp3")||type.equals(".wav")||type.equals(".flac")||type.equals(".m4a")) {
							getDownloadUrl(file);
						}
					}
					Constant.playingIndex = position;
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
		});
		ib_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try 
				{
					Constant.VDiskList.clear();
					filename = FileName.substring(0,FileName.lastIndexOf("/"));
					queryData(filename);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	private void initView() {
		files_list = (ListView) view.findViewById(R.id.vdisk_lv);
		ib_back = (ImageView) view.findViewById(R.id.ib_back);
		queryData(filename);
		adapter = new FileListAdapter(getActivity());
		files_list.setAdapter(adapter);
		
	}

	//query micro disk data
	private void queryData(String url){
		new ShowFilesAsyncTask(getActivity(), mApi, url).execute();
	}
	public static void getDownloadUrl(final String path) {
		new Thread() {
			@Override
			public void run() {
				try {
					VDiskLink media = mApi.media(path, false);

					String downloadUrl = media.url;
					MyApplicaotion.getPlayService().start(downloadUrl,path);

				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		}.start();
	}

}
