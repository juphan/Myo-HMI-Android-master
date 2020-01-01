package example.ASPIRE.MyoHMI_Android;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class SendToHACKBerry extends Service {

    private static final String TAG = "HACKberryConnect";

    private static final UUID BLUETOOTH_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mBluetoothSocket; // Bluetooth connection
    private OutputStream os; // Output Stream for Bluetooth connection
    private String deviceHack = null;

    private static String gesture;

    // When service is started
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SendToHACKberry", "Started");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        deviceHack = intent.getStringExtra("KEY");
        Log.d(TAG, "Incoming: " + deviceHack);

        if(deviceHack != null) {
            Log.d(TAG, "Incoming: " + deviceHack);

            // Connect to the selected Bluetooth device
            BluetoothDevice mBluetoothHack = mBluetoothAdapter.getRemoteDevice(deviceHack);
            try {
                mBluetoothSocket = mBluetoothHack.createRfcommSocketToServiceRecord(BLUETOOTH_UUID);
                mBluetoothSocket.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Create an Output Stream
            try {
                os = mBluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Send out a test string
            byte[] bytes;
            for(int i=0; i<5; i++){
                bytes = (String.valueOf(i)).getBytes(Charset.defaultCharset());
                try {
                    os.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void getPrediction(String s){
        gesture = s;
    }

}
