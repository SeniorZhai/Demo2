package com.gkong.app.ui.base;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.gkong.app.R;
import com.gkong.app.utils.LogUtil;

public class BaseActivity extends Activity{
	private static final String TAG = "BaseActivity";

	protected AlertDialog mAlertDialog;
	protected AsyncTask mRunningTask;

	/******************************** ��Activity LifeCycle For Debug�� *******************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogUtil.d(TAG, this.getClass().getSimpleName()
				+ " onCreate() invoked!!");
		super.onCreate(savedInstanceState);
		/**
		 * requestWindowFeature�������õ�ֵ�У�
 		 * 1.DEFAULT_FEATURES��ϵͳĬ��״̬��һ�㲻��Ҫָ��
         * 2.FEATURE_CONTEXT_MENU������ContextMenu��Ĭ�ϸ��������ã�һ������ָ��
         * 3.FEATURE_CUSTOM_TITLE���Զ�����⡣����Ҫ�Զ������ʱ����ָ�����磺������һ����ťʱ
         * 4.FEATURE_INDETERMINATE_PROGRESS����ȷ���Ľ���
         * 5.FEATURE_LEFT_ICON������������ͼ��
         * 6.FEATURE_NO_TITLE���ޱ���
         * 7.FEATURE_OPTIONS_PANEL�����á�ѡ����塱���ܣ�Ĭ�������á�
         * 8.FEATURE_PROGRESS������ָʾ������
         * 9.FEATURE_RIGHT_ICON:�������Ҳ��ͼ��
		 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		

	}

	@Override
	protected void onStart() {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onStart() invoked!!");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		LogUtil.d(TAG, this.getClass().getSimpleName()
				+ " onRestart() invoked!!");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		LogUtil.d(TAG, this.getClass().getSimpleName()
				+ " onResume() invoked!!");
		super.onResume();
		
	}

	@Override
	protected void onPause() {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onPause() invoked!!");
		super.onPause();
	}

	@Override
	protected void onStop() {
		LogUtil.d(TAG, this.getClass().getSimpleName() + " onStop() invoked!!");
		super.onStop();
	}

	@Override
	public void onDestroy() {
		LogUtil.d(TAG, this.getClass().getSimpleName()
				+ " onDestroy() invoked!!");
		super.onDestroy();
//
//		if (mRunningTask != null && mRunningTask.isCancelled() == false) {
//			mRunningTask.cancel(false);
//			mRunningTask = null;
//		}
		if (mAlertDialog != null) {
			mAlertDialog.dismiss();
			mAlertDialog = null;
		}
	}

	public void recommandToYourFriend(String url, String shareTitle) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, shareTitle + "   " + url);

		Intent itn = Intent.createChooser(intent, "����");
		startActivity(itn);
	}

	/******************************** ��Activity LifeCycle For Debug�� *******************************************/

	protected void showShortToast(int pResId) {
		showShortToast(getString(pResId));
	}

	protected void showLongToast(String pMsg) {
		Toast.makeText(this, pMsg, Toast.LENGTH_LONG).show();
	}

	protected void showShortToast(String pMsg) {
		Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
	}

	protected boolean hasExtra(String pExtraKey) {
		if (getIntent() != null) {
			return getIntent().hasExtra(pExtraKey);
		}
		return false;
	}

	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
	}

	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}

	protected void openActivity(String pAction, Bundle pBundle) {
		Intent intent = new Intent(pAction);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	/**
	 * ͨ�����������öԻ����Ƿ�Ҫ�رգ��ڱ�У��ʱ�ܹ��ã� ��Ϊ���û���д����ʱ��ȷ��ʱĬ��Dialog����ʧ�� ���Դﲻ��У���Ч��
	 * ��mShowing�ֶξ������������Ƿ�Ҫ��ʧ�ģ�������Dialog����˽�б����� ����ֻ��ͨ������ȥ���������
	 * 
	 * @param pDialog
	 * @param pIsClose
	 */
	public void setAlertDialogIsClose(DialogInterface pDialog, Boolean pIsClose) {
		try {
			Field field = pDialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(pDialog, pIsClose);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected AlertDialog showAlertDialog(String TitleID, String Message) {
		mAlertDialog = new AlertDialog.Builder(this).setTitle(TitleID)
				.setMessage(Message).show();
		return mAlertDialog;
	}

	protected AlertDialog showAlertDialog(int pTitelResID, String pMessage,
			DialogInterface.OnClickListener pOkClickListener) {
		String title = getResources().getString(pTitelResID);
		return showAlertDialog(title, pMessage, pOkClickListener, null, null);
	}

	protected AlertDialog showAlertDialog(String pTitle, String pMessage,
			DialogInterface.OnClickListener pOkClickListener,
			DialogInterface.OnClickListener pCancelClickListener,
			DialogInterface.OnDismissListener pDismissListener) {
		mAlertDialog = new AlertDialog.Builder(this)
				.setTitle(pTitle)
				.setMessage(pMessage)
				.setPositiveButton(android.R.string.ok, pOkClickListener)
				.setNegativeButton(android.R.string.cancel,
						pCancelClickListener).show();
		if (pDismissListener != null) {
			mAlertDialog.setOnDismissListener(pDismissListener);
		}
		return mAlertDialog;
	}

	protected AlertDialog showAlertDialog(String pTitle, String pMessage,
			String pPositiveButtonLabel, String pNegativeButtonLabel,
			DialogInterface.OnClickListener pOkClickListener,
			DialogInterface.OnClickListener pCancelClickListener,
			DialogInterface.OnDismissListener pDismissListener) {
		mAlertDialog = new AlertDialog.Builder(this).setTitle(pTitle)
				.setMessage(pMessage)
				.setPositiveButton(pPositiveButtonLabel, pOkClickListener)
				.setNegativeButton(pNegativeButtonLabel, pCancelClickListener)
				.show();
		if (pDismissListener != null) {
			mAlertDialog.setOnDismissListener(pDismissListener);
		}
		return mAlertDialog;
	}

	protected ProgressDialog showProgressDialog(int pTitelResID,
			String pMessage,
			DialogInterface.OnCancelListener pCancelClickListener) {
		String title = getResources().getString(pTitelResID);
		return showProgressDialog(title, pMessage, pCancelClickListener);
	}

	protected ProgressDialog showProgressDialog(String pTitle, String pMessage,
			DialogInterface.OnCancelListener pCancelClickListener) {
		mAlertDialog = ProgressDialog.show(this, pTitle, pMessage, true, true);
		mAlertDialog.setOnCancelListener(pCancelClickListener);
		return (ProgressDialog) mAlertDialog;
	}

	protected void hideKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	protected void handleOutmemoryError() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BaseActivity.this, "�ڴ�ռ䲻�㣡", Toast.LENGTH_SHORT)
						.show();
				// finish();
			}
		});
	}

	private int network_err_count = 0;

	protected void handleNetworkError() {
		network_err_count++;
		// runOnUiThread()����UI
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (network_err_count < 3) {
					Toast.makeText(BaseActivity.this, "���ٺ�����ô��������",
							Toast.LENGTH_SHORT).show();
				} else if (network_err_count < 5) {
					Toast.makeText(BaseActivity.this, "������Ĳ�������",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(BaseActivity.this, "���������������ô��ô���",
							Toast.LENGTH_SHORT).show();
				}
				// finish();
			}
		});
	}

	protected void handleMalformError() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BaseActivity.this, "���ݸ�ʽ����", Toast.LENGTH_SHORT)
						.show();
				// finish();
			}
		});
	}

	protected void handleFatalError() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BaseActivity.this, "������һ�����⣬������ֹ��",
						Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}

	public void finish()
	{
		super.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	public void defaultFinish()
	{
		super.finish();
	}

}
