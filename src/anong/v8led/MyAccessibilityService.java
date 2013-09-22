package anong.v8led;

import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
public class MyAccessibilityService extends AccessibilityService implements ScreenActionReceiver.CallBack {

	private static Configure config = null;
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		if(event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED){
			if(config==null)
				MyAccessibilityService.UpdateConfiguration(this);
			if(isScreenOn()&&!config.isNoticWhenScreenOn&&!event.getPackageName().toString().equals("com.android.phone"))
				return;
			
			Notification n = (Notification)event.getParcelableData();

			if(config.isalwaynotic){			
					if(inArray(config.whitelist,event.getPackageName().toString())){
						if((Notification.FLAG_SHOW_LIGHTS&n.flags)==0&&n.ledOnMS==0&&((Notification.FLAG_AUTO_CANCEL&n.flags)==0&&n.flags!=0)){
							return;
						}else{
							regTouchEvent();
							StartNotic();
						}

					}
					
					
			}else{
				if(n!=null){
					if((Notification.FLAG_SHOW_LIGHTS&n.flags)==0&&n.ledOnMS==0){
						return;
					}else{
						regTouchEvent();
						StartNotic();
					}
				}else{
					return;
				}
			}
		}else if(event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED){
			StopNotic();
			UnregTouchEvent();
		}
			
	}
	public boolean isScreenOn(){
		return !((KeyguardManager)this.getSystemService(Context.KEYGUARD_SERVICE)).inKeyguardRestrictedInputMode();
	}
	private boolean isNoticing = false;
	PendingIntent sender = null;
	private PendingIntent InitPendingIntent(){
		Intent intent =new Intent(this, TimerReceiver.class);
		intent.setAction("anong.action_timer");
		return PendingIntent.getBroadcast(this, 0, intent, 0);
		
	}
	public synchronized boolean StartNotic(){
		if(screenlistener == null)
			screenlistener = new ScreenActionReceiver(this,this);
		if(isNoticing)
			return false;
		if(null==sender)
			sender=InitPendingIntent();
		TimerReceiver.Init();
		((AlarmManager)this.getSystemService(Context.ALARM_SERVICE)).setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), gettimedelay() , sender);
		isNoticing = true;
		Log.i("LED","Started");
		return true;
	}
	private long gettimedelay(){
		return config.delay + config.delay2 + (config.lpBreatheIn.length*100)+(config.lpBreatheOut.length*100);
	}
	public synchronized  boolean StopNotic(){
		if(!isNoticing)
			return false;
		TimerReceiver.Destroy();
		if(null!=sender)
			((AlarmManager)this.getSystemService(Context.ALARM_SERVICE)).cancel(sender);
		isNoticing = false;
		Log.i("LED","Canceled");
		return true;

	}
	private void regTouchEvent(){
    	AccessibilityServiceInfo info = null;
    	if(android.os.Build.VERSION.SDK_INT == 16)
    		info = this.getServiceInfo();
    	else{
    		info = new AccessibilityServiceInfo();
    		info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
    		info.flags = AccessibilityServiceInfo.DEFAULT;
    		info.notificationTimeout = 100;
    	}
    	
    	info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED|AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
    	this.setServiceInfo(info);
    }
	public static Configure getconfig(){
		return config;
	}
	private void UnregTouchEvent(){
    	AccessibilityServiceInfo info = null;
    	if(android.os.Build.VERSION.SDK_INT == 16)
    		info = this.getServiceInfo();
    	else{
    		info = new AccessibilityServiceInfo();
    		info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
    		info.flags = AccessibilityServiceInfo.DEFAULT;
    		info.notificationTimeout = 100;
    	}
    	
    	info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
    	this.setServiceInfo(info);
    }

	private ScreenActionReceiver screenlistener = null;
	protected void onServiceConnected (){
		if(((KeyguardManager)this.getSystemService(Context.KEYGUARD_SERVICE)).inKeyguardRestrictedInputMode()){//ÊÇ·ñËøÆÁ
			return;
		}
		
		
		
		
		Intent i = new Intent(this,MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(i);
		/**/
		
	}
	private boolean inArray(List<String> whitelist,String value){
		for(String str:whitelist){
			if(str.equals(value))
				return true;
		}
		return false;
	}
	public static void UpdateConfiguration(Context context){
		config = null;
		config = Configure.getConfigure(context);
	}
	@Override
	public void onInterrupt() {
		
	}
	public void onScreenUnlock() {
		this.StopNotic();
	}
	public void onDestroy(){
		if(screenlistener!=null)
			screenlistener.Destroy();
		super.onDestroy();
	}
}
