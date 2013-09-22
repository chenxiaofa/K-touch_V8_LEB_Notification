package anong.v8led;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

public class Configure implements Serializable{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 7599234039569130669L;
	public boolean enableMissedCall = true;
	public List<String> whitelist = new ArrayList<String>();
	public long delay = 2000;
	public long delay2 = 1000;
	public boolean isNoticWhenScreenOn = false;
	public boolean isalwaynotic = false;
	public String[] lpBreatheIn  = {
									   "0","50","100","150","200","255"
									  };
	public String[] lpBreatheOut = {
										"220",
										"190","160",
										"130","100",
										"70","40",
										"10","0"
									  };
	
    /*
     * 
     * 	Get the configuration from file
     * 	config.cfg
     * 
     * 
     * 
     * 
     * 
     * */
	public static Configure getConfigure(Context context){
        Configure conf = null;
    	File configFile = new File(context.getFilesDir().getPath()+"config.cfg");
    	if(!configFile.exists()){
	        return new Configure();
    	}else{
    		try {
				ObjectInputStream objinput = new ObjectInputStream(new FileInputStream(configFile));
				conf = (Configure)objinput.readObject();
				objinput.close();
    		} catch (Exception e) {
    			Log.e("Error",e.getMessage());
				e.printStackTrace();
			} 
    	}
    	return conf;
	}
    /*
     * 
     * 	write the configuration to file
     * 	config.cfg
     * 
     * 
     * 
     * 
     * 
     * */
	public static void writeConfigure(Context context,Configure conf){
    	File configFile = new File(context.getFilesDir().getPath()+"config.cfg");
    	if(configFile.exists())
    		configFile.delete();
    	try {
			configFile.createNewFile();
	    	ObjectOutputStream  objoutput = new ObjectOutputStream (new FileOutputStream(configFile));
	    	objoutput.writeObject(conf);
	    	objoutput.flush();
	    	objoutput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
