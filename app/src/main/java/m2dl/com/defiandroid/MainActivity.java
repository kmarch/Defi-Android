package m2dl.com.defiandroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.util.Random;


public class MainActivity extends Activity implements View.OnClickListener, SensorEventListener {

    private int screenWidth;
    private int screenHeight;
    private ImageView image;
    private SensorManager mSensorMgr;
    private ImageView raquet;
    private Sensor mLight;
    private float coord_x;
    private Handler mHandler ;
    private int position[] = new int[2];
    private int [] destination = new int[2];
    private int [] originalPosition = new int[2];
    private Double depX;
    private Double depY;
    private int depXtest;
    private int depYtest;
    private Random random;

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
        image.setOnClickListener(this);
        raquet = (ImageView) findViewById (R.id.raquet);
        mSensorMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorMgr.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        random =  new Random();
    }

    public void onClick(View v) {
        image.getLocationOnScreen(position);
        launch(500,2000, 3000);
        raquet.setY(screenHeight);
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
        image.getLocationOnScreen(position);

        TranslateAnimation anim = new TranslateAnimation( 0, toX - position[0],
                0, toY - position[1]);
        originalPosition[0] = position[0];
        originalPosition[1] = position[1];
        position[0] = toX;
        position[1] = toY;

        anim.setDuration(speed);
        anim.setFillAfter( true );
        image.startAnimation(anim);
        mHandler = new Handler();
        anim.setAnimationListener(
                new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationRepeat(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        int [] raquetPosition = new int[2];
                        raquet.getLocationOnScreen(raquetPosition);
                        int  leftBorder = raquetPosition[0] - raquet.getWidth()/2;
                        int rightBorder = raquetPosition[0] + raquet.getWidth()/2;

                        //  barre
                        if(position[0] <= rightBorder && position[0] >= leftBorder) {
                            Log.d("touch", "touchéé");

                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                            r.play();

                            depX = 3000*Math.cos(random.nextInt()%90);
                            depXtest = depX.intValue();
                            depY = -3000*Math.sin(random.nextInt()%90);
                            depYtest = depY.intValue();
                            Log.d("la", position[0] + depXtest + "");
                            image.setX(position[0]);
                            image.setY(position[1]);
                            launch(originalPosition[0] + depXtest,originalPosition[1] + depYtest,3000);
                        }  else  if (position[0] >= screenWidth - image.getWidth()){//mur droite
                            Log.d("lol" , position[0] + " ici " + position[1] );
                            depX = -3000*Math.cos(45);
                            depXtest = depX.intValue();
                            depY = 3000*Math.sin(45);
                            depYtest = depY.intValue();
                            image.setX(position[0]);
                            image.setY(position[1]);
                            launch(originalPosition[0] + depXtest,originalPosition[1] + depYtest, 3000);
                        } else if (position[0] <=   0) {//mur gauche
                            Log.d("la" , position[0] + " ici " + position[1] );
                            depX = 3000*Math.cos(45);
                            depXtest = depX.intValue();
                            depY = 3000*Math.sin(45);
                            depYtest = depY.intValue();
                            image.setX(position[0]);
                            image.setY(position[1]);
                            launch(originalPosition[0] + depXtest,originalPosition[1] + depYtest, 3000);
                        } else if(position[1] <= 0) {// haut

                            Log.d("merde" , position[0] + " ici " + position[1] );
                            depX = 3000*Math.cos(45);
                            depXtest = depX.intValue();
                            depY = 3000*Math.sin(45);
                            depYtest = depY.intValue();
                            image.setX(position[0]);
                            image.setY(position[1]);
                            launch(originalPosition[0] + depXtest,originalPosition[1] + depYtest, 3000);
                        }
                        else{
                            ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(500);
                        }

                    }
                }
        );
//        mHandler.postDelayed(mUpdateTimeTask, speed);

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
        float lat = event.values[1];
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
