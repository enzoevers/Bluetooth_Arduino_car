package enzo.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;


public class Controller extends Activity {

    ConnectThread connectThread;
    ConnectedThread connectedThread;
    BluetoothDevice BTDevice;

    Button bForward, bBackward, bRight, bLeft;
    boolean bForwardDown, bBackwardDown, bRightDown, bLeftDown;

    boolean[] boolArray;
    boolean[] forwardArray;
    boolean[] backwardArray;
    boolean[] rightForwardArray;
    boolean[] rightBackwardArray;
    boolean[] leftForwardArray;
    boolean[] leftBackwardArray;
    boolean[] stopArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        boolArray = new boolean[]{bForwardDown, bBackwardDown, bRightDown, bLeftDown};
        forwardArray = new boolean[]{true, false, false, false};
        backwardArray = new boolean[]{false, true, false, false};

        rightForwardArray = new boolean[]{true, false, true, false};
        rightBackwardArray = new boolean[]{false, true, true, false};

        leftForwardArray = new boolean[]{true, false, false, true};
        leftBackwardArray = new boolean[]{false, true, false, true};

        stopArray = new boolean[]{false, false, false, false};

        bForward = (Button) findViewById(R.id.btnForward);
        bBackward = (Button) findViewById(R.id.btnBackward);
        bRight = (Button) findViewById(R.id.btnRight);
        bLeft = (Button) findViewById(R.id.btnLeft);

        setTouchListeners();

        try{
            BTDevice = getIntent().getExtras().getParcelable("Device");
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

        connectToDevice(BTDevice);
    }

    void connectToDevice(BluetoothDevice device) {
        connectThread = new ConnectThread(device);
        connectThread.start();

        while(!connectThread.getMmSocket().isConnected()){}

        if(connectThread.getMmSocket().isConnected()) {
            Toast.makeText(getApplicationContext(), "Connected to:" + device.getName(), Toast.LENGTH_SHORT).show();
            connectedThread = new ConnectedThread(connectThread.getMmSocket());
        }
    }

    void setTouchListeners(){
        bForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    boolArray[0] = true;
                    sendControllerCommand();
                    return true;
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    boolArray[0] = false;
                    sendControllerCommand();
                    return true;
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_CANCEL){
                    boolArray[0] = false;
                    sendControllerCommand();
                    return true;
                }

                return false;
            }
        });

        bBackward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    boolArray[1] = true;
                    sendControllerCommand();
                    return true;
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    boolArray[1] = false;
                    sendControllerCommand();
                    return true;
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_CANCEL){
                    boolArray[1] = false;
                    sendControllerCommand();
                    return true;
                }
                return false;
            }
        });

        bRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    boolArray[2] = true;
                    sendControllerCommand();
                    return true;
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    boolArray[2] = false;
                    sendControllerCommand();
                    return true;
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_CANCEL){
                    boolArray[2] = false;
                    sendControllerCommand();
                    return true;
                }
                return false;
            }
        });

        bLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    boolArray[3] = true;
                    sendControllerCommand();
                    return true;
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    boolArray[3] = false;
                    sendControllerCommand();
                    return true;
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_CANCEL){
                    boolArray[3] = false;
                    sendControllerCommand();
                    return true;
                }
                return false;
            }
        });
    }

    void sendControllerCommand(){
        if(Arrays.equals(boolArray, forwardArray)){sendForward();}
        else if(Arrays.equals(boolArray, backwardArray)){sendBackward();}

        else if(Arrays.equals(boolArray, rightForwardArray)){sendRightForward();}
        else if(Arrays.equals(boolArray, rightBackwardArray)){sendRightBackward();}

        else if(Arrays.equals(boolArray, leftForwardArray)){sendLeftForward();}
        else if(Arrays.equals(boolArray, leftBackwardArray)){sendLeftBackward();}

        else if(Arrays.equals(boolArray, stopArray)){sendStop();}
    }



    void sendForward(){connectedThread.write("#f$".getBytes());}
    void sendBackward(){connectedThread.write("#b$".getBytes());}

    void sendRightForward(){connectedThread.write("#rf$".getBytes());}
    void sendRightBackward(){connectedThread.write("#rb$".getBytes());}

    void sendLeftForward(){connectedThread.write("#lf$".getBytes());}
    void sendLeftBackward(){connectedThread.write("#lb$".getBytes());}

    void sendStop(){connectedThread.write("#s$".getBytes());}



   @Override
    public void onBackPressed(){
       disconnect();
       finish();
       return;
    }

    public void disconnect(){
        connectedThread.write("#s$".getBytes());

        String returnedErrors = connectedThread.resetConnection();
        String toastToShow = null;

        if(returnedErrors != null){
            toastToShow = returnedErrors;
        }
        else{
            toastToShow = "Disconnected";
        }

        Toast.makeText(Controller.this, toastToShow, Toast.LENGTH_SHORT).show();
    }
}
