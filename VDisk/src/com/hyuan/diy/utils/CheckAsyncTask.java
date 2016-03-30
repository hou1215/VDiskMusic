package com.hyuan.diy.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


public class CheckAsyncTask extends AsyncTask<Void, Integer, Void>  
{  

	private static final String TAG = "MyAsyncTask";
	private LoadingDialog dialog;
	private Context context;
	
    public CheckAsyncTask(Context context) {
		
		this.context = context;
	}

	@Override  
    protected void onPreExecute()  
    {  
		String name = "���ڼ��...";
		dialog = new LoadingDialog(context,name);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show(); 
        Log.e(TAG, Thread.currentThread().getName() + " onPreExecute ");  
    }  

    @Override  
    protected Void doInBackground(Void... params)  
    {  

        
        try  
        {  
        	Thread.sleep(1000);
        	
            
        } catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
         

        Log.e(TAG, Thread.currentThread().getName() + " doInBackground ");  
        return null;  
    }  

    @Override  
    protected void onProgressUpdate(Integer... values)  
    {  
    	
        Log.e(TAG, Thread.currentThread().getName() + " onProgressUpdate ");  
    }  

    @Override  
    protected void onPostExecute(Void result)  
    {  
        // �������ݼ�����ɺ��UI����  
    	dialog.dismiss();
    	Toast.makeText(context, "��ǰ�汾������߰汾", 0).show();
    	
    }  
}  
