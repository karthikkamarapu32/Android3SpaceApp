package yei.tssBtTestApp;

import yei.tssBtTestApp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

public class SetAxisDirectionsActivity extends Activity {
	private Spinner axis_order_spinner;
	private CheckBox is_neg_x;
	private CheckBox is_neg_y;
	private CheckBox is_neg_z;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	//Setup the activity
    	super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // (NEW)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN); // (NEW)
        setContentView(R.layout.axis_direction_menu);
        
        //Create GUI Buttons
        is_neg_x = (CheckBox) findViewById(R.id.neg_x_box);
        is_neg_y = (CheckBox) findViewById(R.id.neg_y_box);
        is_neg_z = (CheckBox) findViewById(R.id.neg_z_box);
        
        axis_order_spinner = (Spinner) findViewById(R.id.axis_direction_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.axis_direction_array, android.R.layout.simple_spinner_item);
        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        axis_order_spinner.setAdapter(adapter);
        Button axis_dir_to_3d_but = (Button)findViewById(R.id.axis_direction_to_3d_but);
        axis_dir_to_3d_but.setOnClickListener(new OnClickListener() {
		  // @Override
		   public void onClick(View v)
		    {
		    	finish();
		    }
		  });
        
        Button set_axis_dir_but = (Button)findViewById(R.id.set_axis_dir_but);
        set_axis_dir_but.setOnClickListener(new OnClickListener() {
 		  // @Override
 		   public void onClick(View v)
 		    {
 			   //Get the axis directions to set
 			   String axis_order = (String)axis_order_spinner.getSelectedItem();
 			   boolean do_neg_x = is_neg_x.isChecked();
 			   boolean do_neg_y = is_neg_y.isChecked();
 			   boolean do_neg_z = is_neg_z.isChecked();
 			   //Call mSensor's setAxisDirections function
 			   try {
				TSSBTSensor.getInstance().setAxisDirections(axis_order, do_neg_x, do_neg_y, do_neg_z);
 				//TSSBTSensor.getInstance().setLEDColor(0.0f, 0.0f, 1.0f);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 		    }
 		  });
        
        //Set GUI elements' values based on current axis directions
        AxisDirectionStruct cur_dir;
		try {
			cur_dir = TSSBTSensor.getInstance().getAxisDirections();
			int id = adapter.getPosition(cur_dir.axis_order);
	        axis_order_spinner.setSelection(id);
	        is_neg_x.setChecked(cur_dir.neg_x);
	        is_neg_y.setChecked(cur_dir.neg_y);
	        is_neg_z.setChecked(cur_dir.neg_z);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

        }
		
    }

}
