package com.hyuan.diy.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.hyuan.diy.view.VDiskFragment;
import com.vdisk.android.VDiskAuthSession;
import com.vdisk.net.VDiskAPI;
import com.vdisk.net.VDiskAPI.Entry;


public class ShowFilesAsyncTask extends AsyncTask<Void, Integer, Void>  
{  

	private LoadingDialog dialog;
	private Context context;
	private String url;
	private VDiskAPI<VDiskAuthSession> mApi;
	
    public ShowFilesAsyncTask(Context context,VDiskAPI<VDiskAuthSession> mApi,String url) {
		this.context = context;
		this.url = url;
		this.mApi = mApi;
	}

	@Override  
    protected void onPreExecute()  
    {  
		String name = "正在加载...";
		dialog = new LoadingDialog(context,name);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show(); 
    }  

    @Override  
    protected Void doInBackground(Void... params)  
    {  

        
        try  
        {  
        	VDiskFragment.contents.clear();
        	Entry data = mApi.metadata(url, null, true, false);
        	VDiskFragment.setContents(data.contents);
            
        } catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
         

        return null;  
    }  

    @Override  
    protected void onProgressUpdate(Integer... values)  
    {  
    	
    }  

    @Override  
    protected void onPostExecute(Void result)  
    {  
        // 进行数据加载完成后的UI操作  
    	dialog.dismiss();
    	VDiskFragment.adapter.notifyDataSetChanged();
    }  
}  
