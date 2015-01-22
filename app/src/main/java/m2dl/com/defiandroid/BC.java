package m2dl.com.defiandroid;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * Created by Guillaume on 22/01/2015.
 */
class BluetoothClient extends Thread {
    private final BluetoothSocket blueSocket;
    private final BluetoothDevice blueDevice;
    private BluetoothAdapter blueAdapter;

    public BluetoothClient(BluetoothDevice device, BluetoothAdapter blueAdapter_) {
        // On utilise un objet temporaire car blueSocket et blueDevice sont "final"
        BluetoothSocket tmp = null;
        blueDevice = device;
        this.blueAdapter=blueAdapter_;

        // On récupère un objet BluetoothSocket grâce à l'objet BluetoothDevice
        try {
            // MON_UUID est l'UUID (comprenez identifiant serveur) de l'application. Cette valeur est nécessaire côté serveur également !
            UUID res = UUID.fromString("5a2d0751-b375-43e1-8438-a2e248dc9fae");
            tmp = blueDevice.createRfcommSocketToServiceRecord(res);
        } catch (IOException e) { }
        blueSocket = tmp;
    }

    public void run() {
        // On annule la découverte des périphériques (inutile puisqu'on est en train d'essayer de se connecter)
        blueAdapter.cancelDiscovery();

        try {
            // On se connecte. Cet appel est bloquant jusqu'à la réussite ou la levée d'une erreur
            blueSocket.connect();
        } catch (IOException connectException) {
            // Impossible de se connecter, on ferme la socket et on tue le thread
            try {
                blueSocket.close();
            } catch (IOException closeException) { }
            return;
        }

        // Utilisez la connexion (dans un thread séparé) pour faire ce que vous voulez
        manageConnectedSocket(blueSocket);
    }

    // Annule toute connexion en cours et tue le thread
    public void cancel() {
        try {
            blueSocket.close();
        } catch (IOException e) { }
    }

    public void manageConnectedSocket(BluetoothSocket blueSocket) {
        Log.i("BTClient", "ManageConnectedSocket");
        ConnectedThread t = new ConnectedThread(blueSocket);
        t.run();
    }


    /**
     *
     *
     *
     */
    private class ConnectedThread extends Thread {
        public final int MESSAGE_READ = 100;
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        private final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String address = null;
                switch (msg.what) {
                    case MESSAGE_READ:
                        //byte[] readBuf = (byte[]) msg.obj;
                        // construct a string from the valid bytes in the buffer
                        //String readMessage = new String(readBuf, 0, msg.arg1);
                        //mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                        String s = (String)msg.obj;
                        Log.d("Receive thaaaaaaaaaaaaaaaaaat : ","Trop cool");
                        break;

                }
            }
        };

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }
}