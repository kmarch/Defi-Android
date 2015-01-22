package m2dl.com.defiandroid;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorMgr;
    private ImageView image;
    private Sensor mLight;
    private float coord_x;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById (R.id.imageView);
        mSensorMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorMgr.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {


    }

    public void move(float x){
        coord_x = image.getX() + (x*200);
        image.setX(coord_x);


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float lat = event.values[0];
        move(lat);

    }


@Override
protected void onResume(){
        super.onResume();
        mSensorMgr.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        }

@Override
protected void onPause(){
        super.onPause();
        mSensorMgr.unregisterListener(this);
        }
}