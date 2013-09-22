package anong.v8led;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TimerReceiver extends BroadcastReceiver{
	private static LEDControler controler = null;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i("","Timer Received");
		if(controler!=null){
			if(!controler.Breath())
				TimerReceiver.Destroy();
		}
	}
	public static void Init(){
		if(controler==null){
			controler = new LEDControler();
			controler.open();
		}
	}
	public static void Destroy(){
		if(controler!=null){
			controler.stop();
			controler = null;
		}
	}
}
