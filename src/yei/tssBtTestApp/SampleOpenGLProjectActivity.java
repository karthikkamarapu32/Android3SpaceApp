package yei.tssBtTestApp;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.InputStream;

import yei.tssBtTestApp.R;



public class SampleOpenGLProjectActivity extends Activity {
	private GLUpdateHandler orientation_updater = new GLUpdateHandler();
	//private boolean did_initialize = false;
	private boolean is_polling = false;
	private OpenGLRenderer renderer;
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//Setup the activity
    	super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // (NEW)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN); // (NEW)
        setContentView(R.layout.main);
        
        //Setup our OpenGL surface
 		renderer = new OpenGLRenderer();
 		GLSurfaceView glView = (GLSurfaceView)findViewById(R.id.glsurfaceview);
 		glView.setRenderer(renderer);
 		
 		//Setup the tare button
 		Button tare_but = (Button)findViewById(R.id.tare_button);
 		tare_but.setOnClickListener(new OnClickListener() {
 		    //@Override
 		   public void onClick(View v)
 		    {
 		    	try {
					TSSBTSensor.getInstance().setTareCurrentOrient();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 		    }
 		  });
 		
   		//Setting up the scene graph
   		GLTranslateNode camera_pan_back = new GLTranslateNode();
   		camera_pan_back.trans_z = -6.0f;
   		renderer.root.addChild(camera_pan_back);
   		GLTransformNode sensor_orient_node = new GLTransformNode();
   		camera_pan_back.addChild(sensor_orient_node);
   		
   		//Lets load an obj
   		InputStream test_obj_file = getResources().openRawResource(R.raw.tss_sensor_model);
   		GLObj test_obj = new GLObj(test_obj_file, this);
   		sensor_orient_node.addChild(test_obj);
   		
   		//Disable that annoying greed LED blinking everytime a sensor command it sent
   		try {
			TSSBTSensor.getInstance().setLEDMode(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   		
   		//Set up our updater
   		orientation_updater.orient_node = sensor_orient_node;
   		
   		//Start the GL updating
        Message start_again_message = new Message();
    	start_again_message.what = 287;
        orientation_updater.sendMessage(start_again_message);
        is_polling = true;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
   
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent i;
        switch (item.getItemId()) {
            case R.id.axis_direction_but:
            	//Stop our orientation polling thread
            	if(is_polling)
            	{
	            	Message stop_message = new Message();
	            	stop_message.what = -1;
	            	orientation_updater.sendMessage(stop_message);
	            	is_polling = false;
            	}
            	i = new Intent(getApplicationContext(), SetAxisDirectionsActivity.class);
            	startActivity(i);
                break;
            case R.id.led_color_but:     
            	i = new Intent(getApplicationContext(), SetLEDColorActivity.class);
            	startActivity(i);
                break;
            case R.id.sensor_info_but:
            	i = new Intent(getApplicationContext(), SensorStatusActivity.class);
            	startActivity(i);
                break;
            case R.id.quit_but:
            	finish();
            	
        }
        return true;
    }

    @Override
    public void onDestroy(){
    	if(is_polling)
    	{
	    	Message quit_message = new Message();
	    	quit_message.what = -1;
	    	orientation_updater.sendMessage(quit_message);
	    	is_polling = false;
    	}
    	super.onDestroy();
    	int pid = android.os.Process.myPid();
    	android.os.Process.killProcess(pid);
    }
   
    @Override
    public void onStart(){
	    //Start the GL updating
    	if(!is_polling)
    	{
		    Message start_again_message = new Message();
			start_again_message.what = 287;
		    orientation_updater.sendMessage(start_again_message);
		    is_polling = true;
    	}
    	super.onStart();
    }
    
    
    @Override
    public void onStop()
    {
    	//Stop our orientation polling thread
    	if(is_polling)
    	{
	    	Message stop_message = new Message();
	    	stop_message.what = -1;
	    	orientation_updater.sendMessage(stop_message);
	    	is_polling = false;
    	}
    	super.onStop();
    }

}