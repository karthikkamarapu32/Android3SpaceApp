package yei.tssBtTestApp;

import yei.tssBtTestApp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SensorStatusActivity extends Activity {
	private TextView serial_number_hex;
	private TextView software_version_string;
	private TextView hardware_version_string;
	private TextView battery_status_string;
	private TextView battery_charge_string;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	//Setup the activity
    	super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // (NEW)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN); // (NEW)
        setContentView(R.layout.sensor_status_menu);
        Button sens_status_to_3d_but = (Button)findViewById(R.id.sensor_status_to_3d_but);
        
        //Set up GUI
        sens_status_to_3d_but.setOnClickListener(new OnClickListener() {
		   //@Override
		   public void onClick(View v)
		    {
		    	finish();
		    }
		  });
        serial_number_hex = (TextView)findViewById(R.id.serial_number_text);
        software_version_string = (TextView)findViewById(R.id.software_version_text);
        hardware_version_string = (TextView)findViewById(R.id.hardware_version_text);
        battery_status_string = (TextView)findViewById(R.id.battery_status_text);
        battery_charge_string = (TextView)findViewById(R.id.battery_charge_text);
        //Sensor serial number
        try {
			int serial_num = TSSBTSensor.getInstance().getSerialNumber();
			serial_number_hex.setText(java.lang.Integer.toHexString(serial_num));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //Software Version
        try {
        	software_version_string.setText(TSSBTSensor.getInstance().getSoftwareVersion());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //Hardware Version
        try {
        	hardware_version_string.setText(TSSBTSensor.getInstance().getHardwareVersion());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //Battery Status
        try {
        	int battery_status = TSSBTSensor.getInstance().getBatteryStatus();
        	if(battery_status == 1)
        	{
        		battery_status_string.setText("Fully Charged");
        	}
        	else if(battery_status == 2)
        	{
        		battery_status_string.setText("Charging");
        	}
        	else
        	{
        		battery_status_string.setText("Discharging");
        	}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //Battery Charge
        try {
        	int battery_charge = TSSBTSensor.getInstance().getBatteryLife();
        	battery_charge_string.setText(java.lang.Integer.toString(battery_charge) + "%");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
