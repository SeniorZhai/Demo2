package com.gkong.app.view.emtion;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gkong.app.view.emtion.model.FaceText;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

public class EmoticonsEditText extends EditText {
	public static List<FaceText> faceTexts = new ArrayList<FaceText>();
	static {
		faceTexts.add(new FaceText("[qq00]"));
		faceTexts.add(new FaceText("[qq01]"));
		faceTexts.add(new FaceText("[qq02]"));
		faceTexts.add(new FaceText("[qq03]"));
		faceTexts.add(new FaceText("[qq04]"));
		faceTexts.add(new FaceText("[qq05]"));
		faceTexts.add(new FaceText("[qq06]"));
		faceTexts.add(new FaceText("[qq07]"));
		faceTexts.add(new FaceText("[qq08]"));
		faceTexts.add(new FaceText("[qq09]"));
		faceTexts.add(new FaceText("[qq10]"));
		faceTexts.add(new FaceText("[qq11]"));
		faceTexts.add(new FaceText("[qq12]"));
		faceTexts.add(new FaceText("[qq13]"));
		faceTexts.add(new FaceText("[qq14]"));
		faceTexts.add(new FaceText("[qq15]"));
		faceTexts.add(new FaceText("[qq16]"));
		faceTexts.add(new FaceText("[qq17]"));
		faceTexts.add(new FaceText("[qq18]"));
		faceTexts.add(new FaceText("[qq19]"));
		faceTexts.add(new FaceText("[qq20]"));
		faceTexts.add(new FaceText("[qq21]"));
		faceTexts.add(new FaceText("[qq22]"));
		faceTexts.add(new FaceText("[qq23]"));
		faceTexts.add(new FaceText("[qq24]"));
		faceTexts.add(new FaceText("[qq25]"));
		faceTexts.add(new FaceText("[qq26]"));
		faceTexts.add(new FaceText("[qq27]"));
		faceTexts.add(new FaceText("[qq28]"));
		faceTexts.add(new FaceText("[qq29]"));
		faceTexts.add(new FaceText("[qq30]"));
		faceTexts.add(new FaceText("[qq31]"));
		faceTexts.add(new FaceText("[qq32]"));
		faceTexts.add(new FaceText("[qq33]"));
		faceTexts.add(new FaceText("[qq34]"));
		faceTexts.add(new FaceText("[qq35]"));
		faceTexts.add(new FaceText("[qq36]"));
		faceTexts.add(new FaceText("[qq37]"));
		faceTexts.add(new FaceText("[qq38]"));
		faceTexts.add(new FaceText("[qq39]"));
		faceTexts.add(new FaceText("[qq40]"));
		faceTexts.add(new FaceText("[qq41]"));
		faceTexts.add(new FaceText("[qq42]"));
		faceTexts.add(new FaceText("[qq43]"));
		faceTexts.add(new FaceText("[qq44]"));
		faceTexts.add(new FaceText("[qq45]"));
		faceTexts.add(new FaceText("[qq46]"));
		faceTexts.add(new FaceText("[qq47]"));
		faceTexts.add(new FaceText("[qq48]"));
		faceTexts.add(new FaceText("[qq49]"));
		faceTexts.add(new FaceText("[qq50]"));
		faceTexts.add(new FaceText("[qq51]"));
		faceTexts.add(new FaceText("[qq52]"));
		faceTexts.add(new FaceText("[qq53]"));
		faceTexts.add(new FaceText("[qq54]"));
		faceTexts.add(new FaceText("[qq55]"));
		faceTexts.add(new FaceText("[qq56]"));
		faceTexts.add(new FaceText("[qq57]"));
		faceTexts.add(new FaceText("[qq58]"));
		faceTexts.add(new FaceText("[qq59]"));
		faceTexts.add(new FaceText("[qq60]"));
		faceTexts.add(new FaceText("[qq61]"));
		faceTexts.add(new FaceText("[qq62]"));
		faceTexts.add(new FaceText("[qq63]"));
		faceTexts.add(new FaceText("[qq64]"));
		faceTexts.add(new FaceText("[qq65]"));
		faceTexts.add(new FaceText("[qq66]"));
		faceTexts.add(new FaceText("[qq67]"));
		faceTexts.add(new FaceText("[qq68]"));
		faceTexts.add(new FaceText("[qq69]"));
		faceTexts.add(new FaceText("[qq70]"));
		faceTexts.add(new FaceText("[qq71]"));
		faceTexts.add(new FaceText("[qq72]"));
		faceTexts.add(new FaceText("[qq73]"));
		faceTexts.add(new FaceText("[qq74]"));
		faceTexts.add(new FaceText("[qq75]"));
		faceTexts.add(new FaceText("[qq76]"));
		faceTexts.add(new FaceText("[qq77]"));
		faceTexts.add(new FaceText("[qq78]"));
		faceTexts.add(new FaceText("[qq79]"));
		faceTexts.add(new FaceText("[qq80]"));
		faceTexts.add(new FaceText("[qq81]"));
		faceTexts.add(new FaceText("[qq82]"));
		faceTexts.add(new FaceText("[qq83]"));
		faceTexts.add(new FaceText("[qq84]"));
		faceTexts.add(new FaceText("[qq85]"));
		faceTexts.add(new FaceText("[qq86]"));
		faceTexts.add(new FaceText("[qq87]"));
		faceTexts.add(new FaceText("[qq88]"));
		faceTexts.add(new FaceText("[qq89]"));
	}
	public EmoticonsEditText(Context context) {
		super(context);
	}

	public EmoticonsEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public EmoticonsEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		if (!TextUtils.isEmpty(text)) {
			super.setText(replace(text.toString()), type);
		} else {
			super.setText(text, type);
		}
	}
	
	private Pattern buildPattern() {
		// 匹配 \\uea09 这类的正则表达式
		return Pattern.compile("\\[qq[0-9]{2}\\]", Pattern.CASE_INSENSITIVE);
	}
	
	private CharSequence replace(String text) {
		try {
			SpannableString spannableString = new SpannableString(text);
			int start = 0;
			Pattern pattern = buildPattern();
			Matcher matcher = pattern.matcher(text);
			while (matcher.find()) {
				// 获取匹配的字符
				String faceText = matcher.group();
				String key = faceText.substring(1,5);
				BitmapFactory.Options options = new BitmapFactory.Options();
				// 根据字符找到图片资源
				Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
						getContext().getResources().getIdentifier(key, "drawable", getContext().getPackageName()), options);
				ImageSpan imageSpan = new ImageSpan(getContext(), bitmap);
				// 找到当前匹配字符串起始位置
				int startIndex = text.indexOf(faceText, start);
				// 找到当前匹配字符串起始位置
				int endIndex = startIndex + faceText.length();
				// 使用图案替换
				if (startIndex >= 0)
					spannableString.setSpan(imageSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				start = (endIndex - 1);
			}
			return spannableString;
		} catch (Exception e) {
			return text;
		}
	}
}
