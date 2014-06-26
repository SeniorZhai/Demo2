package com.gkong.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.gkong.app.R;
import com.gkong.app.data.RequestManager;
import com.polites.android.GestureImageView;

public class ShowWebImageActivity extends Activity {
	private String imagePath = null;
	private GestureImageView imageView = null;
	private ImageView imgGoHome;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_webimage);
		imgGoHome = (ImageView) findViewById(R.id.show_title_gohome);
		imgGoHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		this.imagePath = getIntent().getStringExtra("image");
		imageView = (GestureImageView) findViewById(R.id.web_image);
		imageView.setMinScale(0.5f);
		imageView.setMaxScale(10.0f);
		ImageLoader loader = RequestManager.getImageLoader();
		ImageListener listener = ImageLoader.getImageListener(imageView, 0, 0);
		loader.get(imagePath, listener);
	}
}
