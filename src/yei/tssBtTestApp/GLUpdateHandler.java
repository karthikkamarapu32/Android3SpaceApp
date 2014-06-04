package yei.tssBtTestApp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GLUpdateHandler extends Handler {
	public GLTransformNode orient_node;
	private boolean keep_going = false;
	private float xx;
	private float yy;
	private float zz;
	private float ww;
	private float xy;
	private float xz;
	private float yz;
	private float wx;
	private float wy;
	private float wz;
	File root = null;
	File dir = null;
	File file = null;
	static private int count = 0;
	static private String TAG = "MainActivity";
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Calendar cal;
	float[] accVal;
	float[] gyroVal;
	float[] magVal;
	
	
	@Override
	public void handleMessage(Message msg) {

		//Check if we are supposed to keep going or not
		if (msg.what == -1)
		{
			keep_going = false;
		}
		else if (msg.what == 287 && keep_going == false)
		{
			keep_going = true;
			//Call yourself again in a bit
			Message tmp_message = new Message();
			tmp_message.what = 1;
			sendMessageDelayed(tmp_message, 250);
		}
		else if(msg.what == 1)
		{
			if (keep_going){
				//Update the GL scene based on the sensor's orientation

				cal = Calendar.getInstance();
				System.out.println("Time is"+dateFormat.format(cal.getTime())+ " ");

				try {
					accVal = TSSBTSensor.getInstance().getRawAccelerometer();
					//gyroVal = TSSBTSensor.getInstance().getRawGyro();
					//magVal =TSSBTSensor.getInstance().getRawCompass();

				
					/*try 
					{
						if(count == 0)
						{	
							root = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); 

							dir = new File (root.getAbsolutePath() + "/../Download");
							Log.i(TAG,"File Path"+dir);
							dir.mkdirs();

							file = new File(dir, "myData2.txt");
							Log.i(TAG,"file"+file.exists());
							count++;
						}
						           
						if(!file.exists())
						{
							file.createNewFile();
							Log.i(TAG,"file does not exist");
						}
						

						FileWriter fw = new FileWriter(file,true);
						BufferedWriter buf = new BufferedWriter(fw);

						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						Calendar cal = Calendar.getInstance();
						buf.append(dateFormat.format(cal.getTime())+ " ");
						
						buf.append(" "+accVal[0]+" "+accVal[1]+" "+accVal[2]);
						//buf.append(" "+gyroVal[0]+" "+gyroVal[1]+" "+gyroVal[2]);
						//buf.append(" "+magVal[0]+" "+magVal[1]+" "+magVal[2]);
						
						buf.newLine();
						buf.close();
						fw.close();

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
*/


				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

 				}
 				

				/*float[] orient;
				try {
					orient = TSSBTSensor.getInstance().getFiltTaredOrientQuat();
					xx = orient[0] * orient[0];
					yy = orient[1] * orient[1];
					zz = orient[2] * orient[2];
					ww = orient[3] * orient[3];
					xy = orient[0] * orient[1] * 2.0f;
					xz = orient[0] * orient[2] * 2.0f;
					yz = orient[1] * orient[2] * 2.0f;
					wx = orient[3] * orient[0] * 2.0f;
					wy = orient[3] * orient[1] * 2.0f;
					wz = orient[3] * orient[2] * 2.0f;
					orient_node.resource_lock.lock();
					orient_node.matrix[0] = ww + xx - yy - zz;
					orient_node.matrix[4] = xy - wz;
					orient_node.matrix[8] = xz + wy;
					orient_node.matrix[1] = xy + wz;
					orient_node.matrix[5] = ww - xx + yy - zz;
					orient_node.matrix[9] = yz - wx;
					orient_node.matrix[2] = xz - wy;
					orient_node.matrix[6] = yz + wx;
					orient_node.matrix[10] = ww - xx - yy + zz;
					orient_node.resource_lock.unlock();
					//Call yourself again in a bit

				} catch (Exception e) {
					return;
				}
*/
				sendMessageDelayed(obtainMessage(1,0,0), 1);
			}
		}
	}

}
