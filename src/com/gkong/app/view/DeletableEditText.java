package com.gkong.app.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

public class DeletableEditText extends EditText {
	private Drawable mRightDrawable;
	private boolean isHasFocus;

	public DeletableEditText(Context context) {
		super(context);
		init();
	}

	public DeletableEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DeletableEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		Drawable[] drawables = this.getCompoundDrawables();
		mRightDrawable = drawables[2];
		// 设置焦点变化的监听
		this.setOnFocusChangeListener(new FocusChangeListenerImpl());
		// 设置EditText文字变化的监听
		this.addTextChangedListener(new TextWatcherImpl());
		// 初始化时让右边clean图标不可见
		setClearDrawableVisible(false);
	}
	// 添加删除按钮的事件
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_UP:
             
            boolean isClean =(event.getX() > (getWidth() - getTotalPaddingRight()))&&
                             (event.getX() < (getWidth() - getPaddingRight()));
            if (isClean) {
                setText("");
            }
            break;
 
        default:
            break;
        }
        return super.onTouchEvent(event);
    }
	
	private class FocusChangeListenerImpl implements OnFocusChangeListener{
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
             isHasFocus=hasFocus;
             if (isHasFocus) {
                 boolean isVisible=getText().toString().length()>=1;
                 setClearDrawableVisible(isVisible);
            } else {
                 setClearDrawableVisible(false);
            }
        }
    }
	 //当输入结束后判断是否显示右边clean的图标
    private class TextWatcherImpl implements TextWatcher{
        @Override
        public void afterTextChanged(Editable s) {
             boolean isVisible=getText().toString().length()>=1;
             setClearDrawableVisible(isVisible);
        }
 
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {
        }
 
        @Override
        public void onTextChanged(CharSequence s, int start, int before,int count) {
        }
    }   
    // 显示或隐藏右侧图标
	protected void setClearDrawableVisible(boolean isVisible) {
        Drawable rightDrawable;
        if (isVisible) {
            rightDrawable = mRightDrawable;
        } else {
            rightDrawable = null;
        }
        //使用代码设置该控件left, top, right, and bottom处的图标
        setCompoundDrawables(getCompoundDrawables()[0],getCompoundDrawables()[1], 
                             rightDrawable,getCompoundDrawables()[3]);
    } 
	 // 显示一个动画,以提示用户输入
    public void setShakeAnimation() {
        this.startAnimation(shakeAnimation(5));
        
    }
 
    // 抖动动画
    public Animation shakeAnimation(int CycleTimes) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 10);
        translateAnimation.setInterpolator(new CycleInterpolator(CycleTimes));
        translateAnimation.setDuration(500);
        return translateAnimation;
    }
}
