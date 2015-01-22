package m2dl.com.defiandroid;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Point;
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


public class MainActivity extends Activity implements View.OnTouchListener{

    private int screenWidth;
    private int screenHeight;
    private int _xDelta;
    private int _yDelta;
    private ImageView image;
    private Handler mHandler ;

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
}
