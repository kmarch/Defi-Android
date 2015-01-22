package m2dl.com.defiandroid;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * Created by Guillaume on 22/01/2015.
 */
public class BluetoothServer extends Thread {
    private final BluetoothServerSocket blueServerSocket;
    private BluetoothAdapter blueAdapter;
    private static String APPNAME = "DefiAndroid";
    private DataToExchange data;

    public BluetoothServer(BluetoothAdapter blueAdapter_) {
        this.blueAdapter=blueAdapter_;
        // On utilise un objet temporaire qui sera assigné plus tard à blueServerSocket car blueServerSocket est "final"
        BluetoothServerSocket tmp = null;
        try {
            // MON_UUID est l'UUID (comprenez identifiant serveur) de l'application. Cette valeur est nécessaire côté client également !
            tmp = blueAdapter.listenUsingRfcommWithServiceRecord(APPNAME, UUID.fromString("5a2d0751-b375-43e1-8438-a2e248dc9fae"));
        } catch (IOException e) { }
        blueServerSocket = tmp;

        System.out.println("test 40 "+blueServerSocket);
    }

    public void run() {
        BluetoothSocket blueSocket = null;
        // On attend une erreur ou une connexion entrante
        while (true) {
            System.out.println("test 41 ");
            try {
                blueSocket = blueServerSocket.accept();
                System.out.println("test 46 "+blueSocket);
            } catch (IOException e) {
                System.out.println("test 46bis "+blueSocket);
                break;
            }
            // Si une connexion est acceptée
            if (blueSocket != null) {
                // On fait ce qu'on veut de la connexion (dans un thread séparé), à vous de la créer
                manageConnectedSocket(blueSocket);
                try {
                    blueServerSocket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void manageConnectedSocket(BluetoothSocket blueSocket)
    {
        System.out.println("test 12 ");
        Log.i("BTserver", "manageConnectedSocked");
        try {

            InputStream inputStream = blueSocket.getInputStream();
            //ObjectInputStream ois = new ObjectInputStream(inputStream);
            byte[] b = new byte[1000];
            inputStream.read(b);
            String str = new String(b, "UTF-8");
            System.out.println("test 5 "+str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // On stoppe l'écoute des connexions et on tue le thread
    public void cancel() {
        try {
            blueServerSocket.close();
        } catch (IOException e) { }
    }

//    /**
//     *
//     * ConnectedThread
//     *
//     */
//    private class ConnectedThread extends Thread {
//        public final int MESSAGE_READ = 100;
//        private final BluetoothSocket mmSocket;
//        private final InputStream mmInStream;
//        private final OutputStream mmOutStream;
//
//        private final Handler mHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                String address = null;
//                switch (msg.what) {
//                    case MESSAGE_READ:
//                        byte[] readBuf = (byte[]) msg.obj;
//                        // construct a string from the valid bytes in the buffer
//                        String readMessage = new String(readBuf, 0, msg.arg1);
//                        //mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
//                        break;
//
//                }
//            }
//        };
//
//        public ConnectedThread(BluetoothSocket socket) {
//            mmSocket = socket;
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//
//            // Get the input and output streams, using temp objects because
//            // member streams are final
//            try {
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
//            } catch (IOException e) {
//            }
//
//            mmInStream = tmpIn;
//            mmOutStream = tmpOut;
//        }
//
//        public void run() {
//            byte[] buffer = new byte[1024];  // buffer store for the stream
//            int bytes; // bytes returned from read()
//
//
//            // Keep listening to the InputStream until an exception occurs
//            while (true) {
//                Message m = new Message();
//                m.obj="crotte";
//                mHandler.sendMessage(m);
//            }
//        }
//
//        /* Call this from the main activity to send data to the remote device */
//        public void write(byte[] bytes) {
//            try {
//                mmOutStream.write(bytes);
//            } catch (IOException e) {
//            }
//        }
//
//        /* Call this from the main activity to shutdown the connection */
//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) {
//            }
//        }
//    }
}