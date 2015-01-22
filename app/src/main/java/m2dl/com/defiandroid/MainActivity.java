package m2dl.com.defiandroid;


import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener,View.OnTouchListener {
    private SensorManager mSensorMgr;
    private ImageView image;
    private Sensor mLight;
    private float coord_x;
    private int screenWidth;
    private int screenHeight;
    private int _xDelta;
    private int _yDelta;
    private ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        image = (ImageView) findViewById (R.id.ball);
        screenWidth = (size.x);
        screenHeight = size.y - 500;
        image.setOnTouchListener(this);
        Log.d("Debut",screenWidth + " "  + screenHeight + " "+ image.getWidth());

    }

    public boolean onTouch(View v, MotionEvent event) {
        launch(-6000,-2000, 3000);
        return true;
    }

    public void launch(int toX, int toY, int speed ) {
        if(toX <= 0 ) {
            toX = 0;
        } else if (toX >= screenWidth) {
            toX = screenWidth - image.getWidth();
        }
        if(toY <= 0) {
            toY = 0 + image.getHeight();
        } else if (toY >= screenHeight){
            toY = screenHeight;
        }
        int originalPos[] = new int[2];
        image.getLocationOnScreen( originalPos );

        TranslateAnimation anim = new TranslateAnimation( 0, toX - originalPos[0],
                0, toY - originalPos[1]);
        anim.setDuration(speed);
        anim.setFillAfter( true );
        image.startAnimation(anim);
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
