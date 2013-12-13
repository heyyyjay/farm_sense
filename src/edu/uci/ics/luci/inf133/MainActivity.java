package edu.uci.ics.luci.inf133;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView xView;
    private TextView yView;
    private TextView zView;
    private TextView scalarView;
    private TextView currentState;
    
    private SensorManager mSensorManager;
    private Sensor mRotationVectorSensor;
    private SensorEventListener mEventListenerRotation;
    
    private float rotationLogX;
    private float rotationLogY;
    private float rotationLogZ;
    private float rotationLogScalar;
    
    private static MediaPlayer mp;
    private AssetFileDescriptor afdUp;
    private AssetFileDescriptor afdDwn;
    private AssetFileDescriptor afdLft;
    private AssetFileDescriptor afdRght;
    private AssetFileDescriptor afdUpsideDown;
    
    synchronized void playAudio(AssetFileDescriptor afd) {
    	if(mp.isPlaying()){
    		return;
    	}
    	mp.reset();
    	try {
    		mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
    		mp.prepare();
    	}
    	catch(Exception e){
    		Log.d("playAudio","Exception:"+e.getStackTrace()[0].toString()+" afd: "+afd.toString());
    	}
    	mp.start();
    }
    
   // private float[] myArray;
	
    private void updateUI(final CharSequence newText) {
    	runOnUiThread(new Runnable(){

			@Override
			public void run() {
				
				currentState.setText(newText);
				
				
				xView.setText("X ROTATION: " + rotationLogX);
				yView.setText("Y ROTATION: " + rotationLogY);
				zView.setText("Z ROTATION: " + rotationLogZ);
				//scalarView.setText("SCALAR ROTATION: " + rotationLogScalar);
			}
    		
    	});
    }
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // CUSTOM CODE WRITTEN BELOW
        xView = (TextView) findViewById(R.id.xAxis);
        yView = (TextView) findViewById(R.id.yAxis);
        zView = (TextView) findViewById(R.id.zAxis);
        //scalarView = (TextView) findViewById(R.id.scalar);
        currentState = (TextView) findViewById(R.id.currentState);
        
        // Get instance of SensorManager
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
    	mp = new MediaPlayer();

    	afdUp = getApplicationContext().getResources().openRawResourceFd(R.raw.cow);
    	afdDwn = getApplicationContext().getResources().openRawResourceFd(R.raw.goat);
    	afdLft = getApplicationContext().getResources().openRawResourceFd(R.raw.dog);
    	afdRght = getApplicationContext().getResources().openRawResourceFd(R.raw.duck);
    	afdUpsideDown = getApplicationContext().getResources().openRawResourceFd(R.raw.rooster);
    	
        
        
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

		        if (	(rotationLogX < -0.7  || rotationLogX > .7)		&&
		        		(rotationLogY < -0.56 || rotationLogY > 0.56)	&&
		        		(rotationLogZ < -0.15 || rotationLogZ > 0.15)
		        	) {
		        	updateUI("UPSIDE-DOWN");
		        	playAudio(afdUpsideDown);
		        }
		        else if (	rotationLogX < -0.32 && rotationLogX > -0.63 &&
		        			rotationLogY < -0.2 && rotationLogY > -0.27 &&
		        			rotationLogZ > -0.7
		        		) {
		        	updateUI("LEFT");
		        	playAudio(afdLft);
		        }
		        else if (	rotationLogX > 0.31 &&
		        			rotationLogY > 0.22 &&
		        			rotationLogZ > -0.7
		        		){
		        	updateUI("RIGHT");
		        	playAudio(afdRght);
		        }
		        else if (	rotationLogX > 0.34 &&
		        			rotationLogY < -0.48 &&
		        			rotationLogZ > -0.65
		        		){
		        	updateUI("DOWN");
		        	playAudio(afdDwn);
		        }
		        else if (	rotationLogX < -0.32 &&
		        			rotationLogY > 0.5 &&
		        			rotationLogZ > -0.68
		        		){
		        	updateUI("UP");
		        	playAudio(afdUp);
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
				SensorManager.SENSOR_DELAY_UI);
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