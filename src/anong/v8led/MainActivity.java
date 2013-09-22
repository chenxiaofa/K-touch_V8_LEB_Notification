package anong.v8led;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity{
	private LEDControler controler = null;
	private String[] activitys = {"基本设置","应用白名单","保存设置并退出"};
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controler = new LEDControler();
        if(!controler.isable())
        	if(!controler.getPermit()){
        		Log.e("can't get permit","");
        		//System.exit(0);
        	}
        controler = null;
        
        ListView listView = new ListView(this);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,activitys));
        setContentView(listView);
        
        listView.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch(position){
				case 0:
					MainActivity.this.startActivity(new Intent(MainActivity.this,CommonConfActivity.class));
					break;
				case 1:
					MainActivity.this.startActivity(new Intent(MainActivity.this,AppWhiteList.class));
					break;
				case 2:
					MainActivity.this.finish();
				}
			}
        	
        });
        
    }
    public void onDestroy(){
    	MyAccessibilityService.UpdateConfiguration(this);
    	super.onDestroy();
    }
}
