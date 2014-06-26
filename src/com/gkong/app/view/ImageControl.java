package com.gkong.app.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageControl extends ImageView {

	public ImageControl(Context context) {
		super(context);
	}

	public ImageControl(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ImageControl(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	Matrix imgMatrix = null; // 图片变换矩阵

	static final int DOUBLE_CLICK_TIME_SPACE = 300;
	static final int DOUBLE_POINT_DISTACE = 10;
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	private int mode = NONE;

	float bigScale = 3f;
	Boolean isBig = false;
	long lastClickTime = 0;
	float startDistance;
	float endDistance;

	float topHeight;
	Bitmap primaryBitmap = null;

	float contentW;
	float contentH;

	float primaryW;
	float primaryH;

	float scale;
	Boolean isMoveX = true;
	Boolean isMoveY = true;
	float startX;
	float startY;
	float endX;
	float endY;
	float subX;
	float subY;
	float limitX1;
	float limitX2;
	float limitY1;
	float limitY2;
	ICustomMethod mCustomMethod = null;

	/**
	 * @param bitmap
	 *            要显示的图片
	 * @param contentW
	 *            内容区域的宽度
	 * @param contentH
	 *            内容区域的高度
	 * @param topHeight
	 *            状态栏和标题栏高度之和
	 */
	public void imageInit(Bitmap bitmap, int contentW, int contentH,
			int topHeight, ICustomMethod iCustomMethod) {
		this.primaryBitmap = bitmap;
		this.contentW = contentW;
		this.contentH = contentH;
		this.topHeight = topHeight;
		mCustomMethod = iCustomMethod;
		primaryW = primaryBitmap.getWidth();
		primaryH = primaryBitmap.getHeight();
		float scaleX = (float) contentW / primaryW;
		float scaleY = (float) contentH / primaryH;
		scale = scaleX < scaleY ? scaleX : scaleY;
		if (scale < 1 && 1 / scale < bigScale) {
			bigScale = (float) (1 / scale + 0.5);
		}
		imgMatrix = new Matrix();
		subX = (contentW - primaryW *scale)/2;
		
	}

	public interface ICustomMethod {
		public void customMethod(Boolean currentStatus);
	}
}
