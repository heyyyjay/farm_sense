package edu.uci.ics.luci.inf133;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView xView;
    private TextView yView;
    private TextView zView;
    private TextView scalarView;
    
    private SensorManager mSensorManager;
    private Sensor mRotationVectorSensor;
    private SensorEventListener mEventListenerRotation;
    
    private float rotationLogX;
    private float rotationLogY;
    private float rotationLogZ;
    private float rotationLogScalar;
    
   // private float[] myArray;
	
    private void updateUI(final CharSequence newText) {
    	runOnUiThread(new Runnable(){

			@Override
			public void run() {
				
				xView.setText(newText);
				
				
				// xView.setText("X ROTATION: " + rotationLogX);
				// yView.setText("Y ROTATION: " + rotationLogY);
				// zView.setText("Z ROTATION: " + rotationLogZ);
				// scalarView.setText("SCALAR ROTATION: " + rotationLogScalar);
			}
    		
    	});
    }
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // CUSTOM CODE WRITTEN BELOW
        
        /* Code for button click
         * ===================== 
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateUI();
			}
		});
		*/
		

        xView = (TextView) findViewById(R.id.xAxis);
        yView = (TextView) findViewById(R.id.yAxis);
        zView = (TextView) findViewById(R.id.zAxis);
        //scalarView = (TextView) findViewById(R.id.scalar);
        
        // Get instance of SensorManager
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        
        // myArray = new float[16];
        
        mEventListenerRotation = new SensorEventListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onSensorChanged(SensorEvent event) {
				//float[] values = event.values;
				//rotationValue = values[]

		        //SensorManager.getRotationMatrixFromVector(myArray, event.values);
		        rotationLogX = event.values[0];
		        rotationLogY = event.values[1];
		        rotationLogZ = event.values[2];
		        //rotationLogScalar = event.values[3];
		        
		        if (rotationLogX < 0 && rotationLogY < 0 && rotationLogZ < 0) {
		        	updateUI("LEFT");
		        }
		        else if (rotationLogX >= 0 && rotationLogY < 0 && rotationLogZ < 0){
		        	updateUI("DOWN");
		        }
		        else if (rotationLogX < 0 && rotationLogY >= 0 && rotationLogZ < 0){
		        	updateUI("UP");
		        }
		        else if (rotationLogZ > 0) {
		        	updateUI("UPSIDE-DOWN");
		        }
		        else {
		        	updateUI("at rest");
		        }
			}
			
			@Override
			public void onAccuracyChanged(Sensor arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		};
    }

	
	@Override
	public void onResume() {
		super.onResume();
		mSensorManager.registerListener(mEventListenerRotation,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
				SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	public void onStop() {
		mSensorManager.unregisterListener(mEventListenerRotation);
		super.onStop();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}