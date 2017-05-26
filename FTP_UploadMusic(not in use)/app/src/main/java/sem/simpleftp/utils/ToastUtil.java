package sem.simpleftp.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtil {

	private static Toast mToast;

	public static void showShortToast(Context context, String message) {
		if (mToast != null) {
			mToast.setText(message);
			mToast.setDuration(Toast.LENGTH_SHORT);
		} else {
			mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	public static void showShortToast(Context context, String message,
			boolean inChildThread) {
		if (inChildThread) {
			Looper.prepare();
			mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			mToast.show();
			Looper.loop();
		} else {
			showShortToast(context, message);
		}
	}

	public static void showLongToast(Context context, String message) {
		if (mToast != null) {
			mToast.setText(message);
			mToast.setDuration(Toast.LENGTH_LONG);
		} else {
			mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		}
		mToast.show();
	}

	public static void cancel() {
		if (mToast != null) {
			mToast.cancel();
		}
	}
}
