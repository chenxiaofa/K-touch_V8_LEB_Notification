package anong.v8led;


import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

public class AppWhiteList extends Activity {

	private Configure config = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        config = Configure.getConfigure(this);
        TableLayout layout = (TableLayout)this.findViewById(R.id.Layout);
        PackageManager mpackmgr = this.getPackageManager();
        List<PackageInfo> packs = mpackmgr.getInstalledPackages(0);  
        for(PackageInfo pinfo:packs){
        	TableRow pkg = new TableRow(this);
        	LinearLayout div = new LinearLayout(this);
        	ImageView img = new ImageView(this);
        	img.setImageDrawable(zoomDrawable(pinfo.applicationInfo.loadIcon(mpackmgr),100,100));
        	CheckBox ckbox = new CheckBox(this);
        	ckbox.setText(pinfo.packageName);
        	ckbox.setChecked(inArray(config.whitelist,pinfo.packageName));
        	ckbox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					int index = searchIndex(config.whitelist,buttonView.getText().toString());
					if(isChecked){
						if(index==-1)
							config.whitelist.add(buttonView.getText().toString());
					}else{
						if(index!=-1)
							config.whitelist.remove(index);
					}
				}});
        	div.addView(img);
        	div.addView(ckbox);
        	pkg.addView(div);
        	layout.addView(pkg);
        }

    }

    private int searchIndex(List<String> whitelist,String value){
		if(whitelist!=null)
			for(int i = 0;i < whitelist.size();i++){
				if(whitelist.get(i).equals(value))
					return i;
			}
    	return -1;
    }
	private boolean inArray(List<String> whitelist,String value){
		if(whitelist!=null)
			for(String str:whitelist){
				if(str.equals(value))
					return true;
			}
		return false;
	}
    
    private Drawable zoomDrawable(Drawable drawable, int w, int h) {  
        int width = drawable.getIntrinsicWidth();  
        int height = drawable.getIntrinsicHeight();  
        Bitmap oldbmp = drawableToBitmap(drawable);  
        Matrix matrix = new Matrix();  
        float scaleWidth = ((float) w / width);  
        float scaleHeight = ((float) h / height);  
        matrix.postScale(scaleWidth, scaleHeight);  
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,  
                matrix, true);  
        return new BitmapDrawable(null, newbmp);  
    }  
      
    private Bitmap drawableToBitmap(Drawable drawable) {  
        int width = drawable.getIntrinsicWidth();  
        int height = drawable.getIntrinsicHeight();  
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                : Bitmap.Config.RGB_565;  
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);  
        Canvas canvas = new Canvas(bitmap);  
        drawable.setBounds(0, 0, width, height);  
        drawable.draw(canvas);  
        return bitmap;  
    }  
    

	public void onPause(){
		super.onPause();
		//this.finish();
	}
	public void onDestroy(){
		Configure.writeConfigure(this,config);
		//MyAccessibilityService.UpdateConfiguration(this);
		//this.startActivity(new Intent(this,commonconfactivity.class));
		super.onDestroy();
	}
}
