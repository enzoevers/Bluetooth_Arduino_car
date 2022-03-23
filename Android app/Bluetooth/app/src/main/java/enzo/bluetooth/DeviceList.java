package enzo.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class DeviceList extends Activity {

    Button bGetPairedDevices, bGetNewDevices, bOff;
    private BluetoothAdapter BA = BluetoothAdapter.getDefaultAdapter();
    private Set<BluetoothDevice> pairedDevices;
    ArrayList<String> listDevices = new ArrayList();
    TextView TxtViewDeviceListHeader;

    Resources res;

    String reason;
    String reasonPaired;
    String reasonNew;

    //int REQUEST_BLUETOOTH_PERMISSION = 1;
    int TURN_ON_BLUETOOTH_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        bGetPairedDevices = (Button) findViewById(R.id.btnGetPairedDevices);
        bGetNewDevices = (Button) findViewById(R.id.btnGetNewDevices);
        bOff = (Button) findViewById(R.id.btnOff);

        res = getResources();
        TxtViewDeviceListHeader = (TextView) findViewById(R.id.tvDeviceListHeader);
        setHeaderText("");

        reasonPaired = reason = String.format(res.getString(R.string.getDeviceButtonReason), "Paired");
        reasonNew = reason = String.format(res.getString(R.string.getDeviceButtonReason), "New");

        registerClickCallback();

        // Register the BroadcastReceiver
        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(BCR, filter1); // Don't forget to unregister during onDestroy
    }




    /*
     * Bluetooth functionality's
     */

    private final BroadcastReceiver BCR = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //int i = device.getBondState();
                // Add the name and address to an array adapter to show in a ListView
                listDevices.add(device.getName() + "\n" + device.getAddress());
            }
            showDiscoveredDevices();
        }
    };

    public void visible() {
        if(!BA.isDiscovering()) {
            Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            getVisible.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
            startActivityForResult(getVisible, 0);
        }
    }

    public void on() {
        if(!BA.isEnabled()) {
            if (BA != null) {
                Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnOn, TURN_ON_BLUETOOTH_REQUEST);
            } else {
                Toast.makeText(this, "Device doesn't have bluetooth", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        else{
            checkNewOrPaired();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == TURN_ON_BLUETOOTH_REQUEST){
            if(resultCode == RESULT_OK){
                Toast.makeText(getApplicationContext(), "Turned on", Toast.LENGTH_SHORT).show();
                checkNewOrPaired();
            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Bluetooth has been canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void checkNewOrPaired(){
        if(reason == reasonPaired){
            listPaired();
        }
        else if(reason == reasonNew){
            visible();
            listDiscover();
        }

        reason = null;
    }

    /*void grandBluetoothPermission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED){
            on();
        }
        else{
            if(shouldShowRequestPermissionRationale(android.Manifest.permission.BLUETOOTH)){
                Toast.makeText(getApplicationContext(), "To use the app bluetooth is needed", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[] {android.Manifest.permission.BLUETOOTH}, REQUEST_BLUETOOTH_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResult){
        if(requestCode == REQUEST_BLUETOOTH_PERMISSION){
            if(grantResult[0] == PackageManager.PERMISSION_GRANTED){
                System.out.println("Request was accepted");
                on();
            }
            else{
                Toast.makeText(getApplicationContext(), "Bluetooth permission was denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else{
            super.onRequestPermissionsResult(requestCode, permission, grantResult);
        }
    }*/

    /* Fill lists to be displayed */
    public void listDiscover() {
        setHeaderText("New");

        listDevices.clear();
        Toast.makeText(getApplicationContext(), "Showing New Devices", Toast.LENGTH_SHORT).show();
        BA.startDiscovery();
    }

    public void showDiscoveredDevices() {
        populateListView(listDevices);
    }

    public void listPaired() {
        setHeaderText("Paired");

        listDevices.clear();

        Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();

        pairedDevices = BA.getBondedDevices();
        for (BluetoothDevice bt : pairedDevices)
            listDevices.add(bt.getName() + "\n" + bt.getAddress());

        populateListView(listDevices);
    }




    /*
     * ListView methods
     */

    void setHeaderText(String headerType){
        String header = String.format(res.getString(R.string.deviceListHeader), headerType);
        TxtViewDeviceListHeader.setText(header);
    }

    private void populateListView(List listDevices) {
        // Build Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,                 // Context for the activity
                R.layout.devices,     // Layout to use (create)
                listDevices);   // Items to be displayed

        // Configure the list view
        ListView list = (ListView) findViewById(R.id.lvDevices);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.lvDevices);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long ID) {
                BA.cancelDiscovery();

                TextView textView = (TextView) viewClicked;
                String text = textView.getText().toString();
                String address = text.substring(text.length() - 17);
                BluetoothDevice device = BA.getRemoteDevice(address);

                Intent intent = new Intent(DeviceList.this, Controller.class);
                intent.putExtra("Device", device);
                startActivity(intent);
            }
        });
    }




    /*
     * Bluetooth function buttons
     */
    public void off(View v) {
        BA.disable();
        Toast.makeText(getApplicationContext(), "Turned off", Toast.LENGTH_LONG).show();
        setHeaderText("");
        listDevices.clear();
        populateListView(listDevices);
    }

    public void showPairedDevices(View v){
        reason = reasonPaired;
        on();
    }

    public void showNewDevices(View v){
        reason = reasonNew;
        on();
    }
}







