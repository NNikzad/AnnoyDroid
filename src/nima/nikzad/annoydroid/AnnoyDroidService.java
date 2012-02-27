package nima.nikzad.annoydroid;

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
	private NotificationManager notificationManager;

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
		notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		showNotification();
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
		notificationManager.cancel(TAG.hashCode());
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return m_binder;
	}
	
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
		notificationManager.notify(TAG.hashCode(), notification);
	}

}
