package m2dl.com.defiandroid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Set;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class MainActivity extends Activity {

    ArrayAdapter mArrayAdapter;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice htcDada;
    BluetoothDevice htcC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.tv);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        if (mBluetoothAdapter == null) {
            // Le terminal ne possède pas le Bluetooth
            tv.setText("Pas de Bluetooth");
        }
        else
        {
            if (!mBluetoothAdapter.isEnabled()) {
                System.out.println("Enabling BTAdapter");
                mBluetoothAdapter.enable();
            }
            System.out.println("Beginning pairing of devices");
        	/*Set<BluetoothDevice> pairedDevices = blueAdapter.getBondedDevices();
        	for (int i=0 ; i< pairedDevices.toArray().length ; i++) {
        		System.out.println(pairedDevices.toArray()[i].toString());
        	}*/

            //On présume que les trucs ont été pairés avant
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            String s = "";
            // If there are paired devices
            if (pairedDevices.size() > 0) {
                // Loop through paired devices
                for (BluetoothDevice device : pairedDevices) {
                    //Affichage des devices pairés
                    s+="NAME:"+device.getName() + "\n" + "adress:"+device.getAddress()+"\n";
                    //Je cherche celui dont j'ai besoin (en fait les 2 tel avec lesquels j'ai testé
                    if (device.getName().equals("HTC Dada")) {
                        System.out.println("HTDADA FOUND");
                        this.htcDada=device;
                    }
                    if (device.getName().equals("HTC Desire C")) {
                        this.htcC=device;
                        System.out.println("HTCC FOUND");
                    }

                }
                tv.setText(s);
            }


        }
    }

    //Lancement du server
    public void serverBT(View view) {
        Log.i("ServerBT","Starting server");
        BluetoothServer btserv = new BluetoothServer(mBluetoothAdapter);
        btserv.start();
    }

    //Lancement du client
    public void clientBT(View view) {
        Log.i("ClientBT", "Starting client");
        if (this.htcDada == null)
        {
            Log.e("ClientBT", "Device is null");
        }
        else
        {
            System.out.println(this.htcDada.toString());
        }
        BluetoothClient btclient = new BluetoothClient(this.htcDada, mBluetoothAdapter); //le client est HTCC
        btclient.start();
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
