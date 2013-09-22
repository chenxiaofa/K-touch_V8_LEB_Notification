package anong.v8led;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class ScreenActionReceiver extends BroadcastReceiver{
	private Context context = null;
	public ScreenActionReceiver(Context c,CallBack cb){
		context = c;
		callback = cb;
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_USER_PRESENT);  
		//intentFilter.addAction(Intent.ACTION_SCREEN_ON);  
		//intentFilter.addAction(Intent.ACTION_USER_PRESENT);
		context.registerReceiver(this, intentFilter);  
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(callback!=null){
			//if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
			//	callback.onScreenOFF();
			//else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
			//	callback.onScreenON();
			//}else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
				callback.onScreenUnlock();
			}
	}
	private CallBack callback = null;
	public void addCallBack(CallBack cb){
		callback = cb;
	}
	public interface CallBack{
		//public void onScreenON();
		//public void onScreenOFF();
		public void onScreenUnlock();
	}
	public void Destroy(){
		context.unregisterReceiver(this);
		callback = null;
	}
}
