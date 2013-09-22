package anong.v8led;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class LEDControler {
	public boolean runFlag = true;
	private String brightnessPath   ="/sys/devices/platform/tegra-i2c.4/i2c-4/4-004a/kled.0/brightness";
	public static String[] lpBreatheIn    = {
									   "0","50","100","150","200","255"
									  };
	public static String[] lpBreatheOut = {
										"220",
										"190","160",
										"130","100",
										"70","40",
										"10","0"
									  };
	public static long delay = 10000;
	
	public boolean isPause = false;
	FileWriter localFileWriter = null;
	public boolean getPermit(){
        Process process = null; 
        DataOutputStream os = null; 
        try { 
            String cmd="chmod 777 " + brightnessPath; 
            process = Runtime.getRuntime().exec("su"); //ÇÐ»»µ½rootÕÊºÅ 
            os = new DataOutputStream(process.getOutputStream()); 
            os.writeBytes(cmd + "\n"); 
            os.writeBytes("exit\n"); 
            os.flush(); 
            process.waitFor(); 
        } catch (Exception e) { 
        	e.printStackTrace();
        } finally { 
            try { 
                if (os != null) { 
                    os.close(); 
                } 
                process.destroy(); 
            } catch (Exception e) { 
            } 
        } 
        return isable();
	}
	public synchronized boolean Breath(){	
		if(null==localFileWriter || !runFlag){
			return false;
		}
		synchronized(localFileWriter){
		    try
		    {
		      
		      for(String light:lpBreatheIn){
		    	  localFileWriter.write(light.toCharArray(),0,light.toCharArray().length);
		    	  localFileWriter.flush();
			      while(runFlag&&isPause)
			    	  Thread.sleep(200);
			      if(!runFlag){
			    	  localFileWriter.write("0",0,1);
			    	  localFileWriter.close();
			    	  localFileWriter = null;
			    	  break;
			      }
			      Thread.sleep(100);
		      }
		      Thread.sleep(delay);
		      for(String light:lpBreatheOut){
		    	  localFileWriter.write(light.toCharArray(),0,light.toCharArray().length);
		    	  localFileWriter.flush();
			      while(runFlag&&isPause)
			    	  Thread.sleep(200);
			      if(!runFlag){
			    	  localFileWriter.write("0",0,1);
			    	  localFileWriter.close();
			    	  localFileWriter = null;
			    	  break;
			      }
			      Thread.sleep(100);
		      }
		      


		    }
		    catch (Exception e)
		    {
		    	return false;
		    }
		}
		return runFlag;
	}
	public boolean showLED(){
		if(null==localFileWriter || !runFlag){
			return false;
		}
		
		
		return false;
		
	}
	
	public boolean isable(){
        try {
			Process process = Runtime.getRuntime().exec("ls -l " + this.brightnessPath);
			process.waitFor();
			InputStream localInputStream = process.getInputStream();
			DataInputStream input = new DataInputStream(localInputStream);
			@SuppressWarnings("deprecation")
			//String result = input.readLine().substring(1, 10);
			//return result.equals("rwxrwxrwx");
			String result = input.readLine().substring(8, 9);
			return result.equals("w");
        }catch(Exception e){
        	return false;
        }
	}
	public void stop(){
		runFlag = false;
		isPause = false;
	}
	public boolean open(){
		if(!isable())
			if(!getPermit())
				System.exit(0);
		try {
			localFileWriter = new FileWriter(this.brightnessPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		Configure config = MyAccessibilityService.getconfig();
		
		if(null!=config){
			if(null!=config.lpBreatheIn)
				lpBreatheIn = config.lpBreatheIn;
			if(null!=config.lpBreatheOut)
			lpBreatheOut = MyAccessibilityService.getconfig().lpBreatheOut;
			if(config.delay>0)
				delay = config.delay2;
		}
		
		runFlag = true;
		isPause = false;
		return true;
	}
	public void pause(){
		isPause = true;
	}
	public void continu(){
		isPause = false;
	}
}
