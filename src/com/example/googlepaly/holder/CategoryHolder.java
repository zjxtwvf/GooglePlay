package com.example.googlepaly.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.googleplay.R;
import com.example.googleplay.http.HttpHelper;
import com.example.googleplay.utils.BitmapCacheUtils;
import com.example.googleplay.utils.UIUtils;
import com.example.goolepaly.domain.CatogoryInfo;

public class CategoryHolder extends BaseHolder<CatogoryInfo>{
	
	private TextView textView1;
	private TextView textView2;
	private TextView textView3;
	
	private ImageView imageView1;
	private ImageView imageView2;
	private ImageView imageView3;
	
	@Override
	public View initView() {
        View view = UIUtils.inflate(R.layout.list_item_category);
        
        textView1 = (TextView) view.findViewById(R.id.tv_category1);
        textView2 = (TextView) view.findViewById(R.id.tv_category2);
        textView3 = (TextView) view.findViewById(R.id.tv_category3);
        
        imageView1 = (ImageView) view.findViewById(R.id.iv_category1);
        imageView2 = (ImageView) view.findViewById(R.id.iv_category2);
        imageView3 = (ImageView) view.findViewById(R.id.iv_category3);
        
		return view;
	}

	@Override
	public void refreshView() {
		textView1.setText(data.name1);
		textView2.setText(data.name2);
		textView3.setText(data.name3);
		
		BitmapCacheUtils.getInstance().display(imageView1,HttpHelper.URL + "image?name="
				+ data.url1);
		BitmapCacheUtils.getInstance().display(imageView2,HttpHelper.URL + "image?name="
				+ data.url2);
		BitmapCacheUtils.getInstance().display(imageView3,HttpHelper.URL + "image?name="
				+ data.url3);
	}
}
