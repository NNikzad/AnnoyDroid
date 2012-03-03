package nima.nikzad.annoydroid.tracktarget;

public abstract class TrackTarget {
	private String m_name;
	private String m_launchActivity;
	
	public TrackTarget(String name, String launchActivity) {
		m_name = name;
		m_launchActivity = launchActivity;
	}
	
	public String getName() {
		return m_name;
	}
	
	public String getLaunchActivity() {
		return m_launchActivity;
	}
}
