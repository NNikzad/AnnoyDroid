package nima.nikzad.annoydroid;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AnnoyDroidActivity extends Activity {
	private static final String TAG = "AnnoyDroidActivity";
	private AnnoyDroidService annoyService;
	private Intent serviceIntent;
	private Button serviceToggleButton;
	//private MediaPlayer mediaPlayer;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //mediaPlayer = MediaPlayer.create(this, R.raw.did_i_do);
        serviceIntent = new Intent(this, AnnoyDroidService.class);
        serviceToggleButton = (Button)findViewById(R.id.annoy_button);
        serviceToggleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(annoyService == null) {
					doStartService();
				}
				else {
					doStopService();
				}
			}
        });
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	doStartService();
    }
    
    @Override
    public void onStop() {
    	super.onStop();
    	// Leave service running, but unbind from it
    	if(annoyService != null) {
    		this.unbindService(serviceConnection);
    	}
    }
    
    ///////////////////////////
	// SERVICE CONNECTION
	///////////////////////////
    
    // Handles binding to the service
    private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			annoyService = ((AnnoyDroidService.LocalBinder)service).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			annoyService = null;
		}
    };
    
    private void doStartService() {
    	Log.d(TAG, "Attempting to start service...");
    	startService(serviceIntent);
    	bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    	//mediaPlayer.start();
    }
    
    private void doStopService() {
    	if(annoyService != null) {
    		Log.d(TAG, "Attempting to stop service...");
    		unbindService(serviceConnection);
    		annoyService.killService();
    		annoyService = null;
    	}
    }
}