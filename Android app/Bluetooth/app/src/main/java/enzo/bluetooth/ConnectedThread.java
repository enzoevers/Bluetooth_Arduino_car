package enzo.bluetooth;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by enzoe on 7/28/2016.
 */
public class ConnectedThread extends Thread {
    //private static final long serialVersionUID = 654897646;

    private BluetoothSocket mmSocket;
    private InputStream mmInStream;
    private OutputStream mmOutStream;

    public ConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        }
        catch (IOException e) { }

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
                 /*mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                 .sendToTarget();*/
            } catch (IOException e) {
                break;
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) { }
    }


    public String resetConnection() {
        String error = null;

        if (mmInStream != null) {
            try {mmInStream.close();} catch (Exception e) {
                error += " InSteam ";
            }
            mmInStream = null;
        }

        if (mmOutStream != null) {
            try {mmOutStream.close();} catch (Exception e) {
                error += " OutStream ";
            }
            mmOutStream = null;
        }

        if (mmSocket != null) {
            try {mmSocket.close();} catch (Exception e) {
                error += " Socket ";
            }
            mmSocket = null;
        }


        if(error != null){
            return  "Problems with: " + error;
        }

        return null;
    }
}
