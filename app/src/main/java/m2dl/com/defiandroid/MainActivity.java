package m2dl.com.defiandroid;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class MainActivity extends Activity implements View.OnTouchListener, SensorEventListener {

    private int screenWidth;
    private int screenHeight;
    private ImageView image;
    private SensorManager mSensorMgr;
    private ImageView raquet;
    private Sensor mLight;
    private float coord_x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        image = (ImageView) findViewById (R.id.ball);
        screenWidth = (size.x);
        screenHeight = size.y - 500;
        image.setOnTouchListener(this);
        raquet = (ImageView) findViewById (R.id.raquet);
        mSensorMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorMgr.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {


    }

    public void move(float x){
        coord_x = raquet.getX() + (x*200);
        if(coord_x <= 0 ) {
            coord_x = 0;
        } else if (coord_x >= screenWidth) {
            coord_x = screenWidth - raquet.getWidth();
        }
        raquet.setX(coord_x);


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
