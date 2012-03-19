package nima.nikzad.annoydroid.tracktarget;

import nima.nikzad.annoydroid.R;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class FoursquareNoiseTarget extends TrackTarget {

	private Context m_context;
	private MediaPlayer m_mediaPlayer;
	
	public FoursquareNoiseTarget (Context context) {
		super("InputDispatcher", "I", "com.joelapenna.foursquared.MainActivity");
		m_handler = new FoursquareNoiseHandler();
		m_context = context;
		m_mediaPlayer = MediaPlayer.create(m_context, R.raw.did_i_do);
	}
	
	private class FoursquareNoiseHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Log.d("FoursqareNoiseTarget", (String)msg.obj);
			m_mediaPlayer.start();
		}
	}
}
