package enzo.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by enzoe on 7/28/2016.
 */
public class ConnectThread extends Thread{
    public BluetoothSocket getMmSocket() {
        return mmSocket;
    }

    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;

    public ConnectThread(BluetoothDevice device) {
        System.out.println("In the ConnectThread");
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        // Default UUID
        UUID DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        try {
            // Use the UUID of the device that discovered // TODO Maybe need extra device object
            if (mmDevice != null)
            {
                System.out.println("Device Name: " + mmDevice.getName());
                System.out.println("Device UUID: " + mmDevice.getUuids()[0].getUuid());
                tmp = device.createRfcommSocketToServiceRecord(mmDevice.getUuids()[0].getUuid());

            }
            else System.out.println("Device is null.");
        }
        catch (NullPointerException e)
        {
            System.out.println("UUID from device is null, Using Default UUID, Device name: " + device.getName());
            try {
                tmp = device.createRfcommSocketToServiceRecord(DEFAULT_UUID);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        catch (IOException e) {
            System.out.println(e.toString());
        }
        mmSocket = tmp;
    }

    public void run() {
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            System.out.println(String.valueOf(mmSocket.isConnected()));
            mmSocket.connect();
            if (mmSocket.isConnected()) {
                System.out.println("Socket is connected");
            }

        } catch (IOException connectException) {
            System.out.println(connectException.toString());
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException /*closeException*/e) {
                System.out.println(e.toString());
            }
            return;
        }

        // Do work to manage the connection (in a separate thread)
        //manageConnectedSocket(mmSocket);
    }

    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}
