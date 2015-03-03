package com.dwij.syc.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import android.content.Context;
import android.graphics.BitmapFactory;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwij.syc.models.PublicProgram;
import com.dwij.syc.utils.BitmapManager;
import com.syc.R;

public class PublicProgramsAdapter extends BaseAdapter {


	String TAG = "SYCenters";
	
	private Context context;
	private ArrayList<PublicProgram> programs;
	private static LayoutInflater inflater = null;

	public PublicProgramsAdapter(Context context, ArrayList<PublicProgram> programs) {
		Log.d(TAG, "Public Programs ListAdapter() start");
		this.context = context;
		this.programs = programs;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		BitmapManager.INSTANCE.setPlaceholder(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
	}

	public int getCount() {
		return programs.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			view = inflater.inflate(R.layout.row_notif, null);
		} else {
			view = convertView;
		}
		// title
		TextView brandTitle = (TextView) view.findViewById(R.id.brandTitle);
		// tagLine
		TextView tagLine = (TextView) view.findViewById(R.id.tagLine);
		// updatedTime
		TextView updatedTime = (TextView) view.findViewById(R.id.updatedTime);
		// thumb image
		ImageView thumb_image = (ImageView) view.findViewById(R.id.list_image);

		// Setting all values in listview
		brandTitle.setText(programs.get(position).getPname());
		tagLine.setText(programs.get(position).getPaddress());
/*		brandTitle.setText("Hello");
		tagLine.setText("This is an Event");*/
		
		
		
		// imageLoader.DisplayImage(brand.getBrandLogoUrl(), thumb_image);
		// thumb_image.setBackgroundResource(R.drawable.ic_launcher);
		BitmapManager.INSTANCE.loadBitmap(programs.get(position).getThumbUrl(), thumb_image, 32, 32);
		return view;
	}


}
