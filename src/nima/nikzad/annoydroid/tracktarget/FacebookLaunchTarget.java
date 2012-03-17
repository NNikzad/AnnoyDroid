package nima.nikzad.annoydroid.tracktarget;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class FacebookLaunchTarget extends TrackTarget {
	
	private class FacebookLaunchHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Log.d("FacebookLaunchHandler", (String)msg.obj);
		}
	};
	
	public FacebookLaunchTarget() {
		super("ActivityManager", "I", "com.facebook.katana");
		m_handler = new FacebookLaunchHandler();
	}

}
