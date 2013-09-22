package anong.v8led;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;

public class CommonConfActivity extends Activity{
	private Configure config = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commonconfactivity);
        config = Configure.getConfigure(this);
        EditText txtbox_delay = (EditText)this.findViewById(R.id.delay);
        txtbox_delay.setText(String.valueOf(config.delay));
        Button bottun_save = (Button)this.findViewById(R.id.button_save);
        Switch switch_allwaynotic = (Switch)this.findViewById(R.id.switch1);
        switch_allwaynotic.setTextOff("应用程序定义");
        switch_allwaynotic.setTextOn("白名单模式");
        switch_allwaynotic.setChecked(config.isalwaynotic);
        bottun_save.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				long delay = Long.valueOf(((EditText)CommonConfActivity.this.findViewById(R.id.delay)).getText().toString());
				if(delay>0){
					config.delay = delay;
					Configure.writeConfigure(CommonConfActivity.this, config);
					CommonConfActivity.this.finish();
				}
			}
		});
        switch_allwaynotic.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				config.isalwaynotic = isChecked;
			}
        	
        });
        Switch switch_onScreenOn = (Switch)this.findViewById(R.id.switch2);
        switch_onScreenOn.setChecked(config.isNoticWhenScreenOn);
        switch_onScreenOn.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				config.isNoticWhenScreenOn = isChecked;
			}
        });
        
    }
    public void onDestroy(){
    	
    	super.onDestroy();
    }
}
