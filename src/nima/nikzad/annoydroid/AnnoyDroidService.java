package nima.nikzad.annoydroid;

import nima.nikzad.annoydroid.logcat.LogcatWatcher;
import nima.nikzad.annoydroid.tracktarget.FacebookLaunchTarget;
import nima.nikzad.annoydroid.tracktarget.TrackTarget;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AnnoyDroidService extends Service {
	private static final String TAG = "AnnoyDroidService";
	private final IBinder m_binder = new LocalBinder();
	private NotificationManager m_notificationManager;
	
	private TrackTarget m_facebookLaunchTarget;
	
	private LogcatWatcher m_logcatWatcher;

	///////////////////////////
	// LIFE CYCLE
	///////////////////////////
	public class LocalBinder extends Binder {
		public AnnoyDroidService getService() {
			return AnnoyDroidService.this;
		}
	}
	
	@Override
	public void onCreate() {
		Log.d(TAG, "AnnoyDroidService created!");
		m_notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		showNotification();
		m_logcatWatcher = new LogcatWatcher();
		
		// Create each track target
		m_facebookLaunchTarget = new FacebookLaunchTarget();
		
		// Subscribe each tracker of interest
		m_logcatWatcher.addTarget(m_facebookLaunchTarget);
		
		m_logcatWatcher.start();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {
		Log.d(TAG, "AnnoyDroidService started!");
		
		// START_STICKY says not to kill the service unless explicitly requested
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		Log.d(TAG, "AnnoyDroidService destroyed!");
		// Get rid of notification in status bar
		m_notificationManager.cancel(TAG.hashCode());
		m_logcatWatcher.stop();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return m_binder;
	}
	
	///////////////////////////
	// PUBLIC METHODS
	///////////////////////////
	
	public void killService() {
		this.stopSelf();
	}
	
	///////////////////////////
	// NOTIFICATION
	///////////////////////////
	// Manages the status bar notification
	private void showNotification() {
		// Text to show
		CharSequence message = getString(R.string.notificationMessage);
		CharSequence label = getString(R.string.notificationName);
		// Icon, text, and time to display
		Notification notification = new Notification(R.drawable.app_icon, 
				message, System.currentTimeMillis());
		// If selected, start activity
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, 
				new Intent(this, AnnoyDroidActivity.class), 0);
		
		notification.setLatestEventInfo(this, label, message, contentIntent);
		
		// Send notification
		m_notificationManager.notify(TAG.hashCode(), notification);
	}

}
