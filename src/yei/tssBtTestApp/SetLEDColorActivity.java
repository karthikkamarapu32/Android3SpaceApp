package yei.tssBtTestApp;


import yei.tssBtTestApp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SetLEDColorActivity extends Activity {
	private SeekBar red_slider;
	private SeekBar green_slider;
	private SeekBar blue_slider;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//Setup the activity
    	super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // (NEW)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN); // (NEW)
        setContentView(R.layout.led_color_menu);
        
        //Set up GUI elements
        Button led_to_3d_but = (Button)findViewById(R.id.led_color_to_3d_but);
 		led_to_3d_but.setOnClickListener(new OnClickListener() {
		   //@Override
		   public void onClick(View v)
		    {
		    	finish();
		    }
		  });
 		red_slider = (SeekBar)findViewById(R.id.red_slider);
 		green_slider = (SeekBar)findViewById(R.id.green_slider);
 		blue_slider = (SeekBar)findViewById(R.id.blue_slider);
 		
 		//Set up the slider's default values
 		float[] cur_LED_color;
		try {
			cur_LED_color = TSSBTSensor.getInstance().getLEDColor();
			red_slider.setProgress((int)(cur_LED_color[0] * 100));
			green_slider.setProgress((int)(cur_LED_color[1] * 100));
			blue_slider.setProgress((int)(cur_LED_color[2] * 100));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
 		
 		//Set up the various listeners
 		red_slider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			//@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			//@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			//@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				//Get each seekbar's progress
				float red_val = progress / 100.0f;
				float green_val = green_slider.getProgress() / 100.0f;
				float blue_val = blue_slider.getProgress() / 100.0f;
				try {
					TSSBTSensor.getInstance().setLEDColor(red_val, green_val, blue_val);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
 		
 		green_slider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			//@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			//@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			//@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				//Get each seekbar's progress
				float red_val = red_slider.getProgress() / 100.0f;
				float green_val = progress / 100.0f;
				float blue_val = blue_slider.getProgress() / 100.0f;
				try {
					TSSBTSensor.getInstance().setLEDColor(red_val, green_val, blue_val);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
 		
 		blue_slider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			//@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			//@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			//@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				//Get each seekbar's progress
				float red_val = red_slider.getProgress() / 100.0f;
				float green_val = green_slider.getProgress() / 100.0f;
				float blue_val = progress / 100.0f;
				try {
					TSSBTSensor.getInstance().setLEDColor(red_val, green_val, blue_val);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
    }
}
