package com.hyuan.diy.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.hyuan.diy.R;

public class LoadingDialog extends Dialog {
	private TextView tv;
	private String name;

	public LoadingDialog(Context context,String name) {
		super(context, R.style.loadingDialogStyle);
		this.name = name;
	}

	private LoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loading);
		tv = (TextView)this.findViewById(R.id.tv);
		tv.setText(name);
	}
}
