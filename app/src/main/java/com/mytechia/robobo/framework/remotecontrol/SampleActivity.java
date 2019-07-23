package com.mytechia.robobo.framework.remotecontrol;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import android.widget.TextView;

import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;

import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;
import com.mytechia.robobo.framework.remote_control.remotemodule.Status;
import com.mytechia.robobo.framework.service.RoboboServiceHelper;
import com.mytechia.robobo.rob.BluetoothRobInterfaceModule;
import com.mytechia.robobo.rob.util.RoboboDeviceSelectionDialog;


import org.java_websocket.client.WebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;


public class SampleActivity extends AppCompatActivity implements ITestListener  {
    private static final String TAG="RemoteControlActivity";

    public SampleActivity() throws URISyntaxException {
    }
    private RoboboServiceHelper roboboHelper;
    private RoboboManager roboboManager;

    private TextView tv;
    URI uri = new URI("ws://localhost:40404");
    Integer i = 1;
    private ProgressDialog waitDialog;



        private IRemoteControlModule remoteModule;


    private WebSocketClient ws ;


    public boolean onTouchEvent(MotionEvent event){

        Status s = new Status("TapNumber");
        s.putContents("Taps",i.toString());
        i = i+1;
        Log.d(TAG,s.toString());
        remoteModule.postStatus(s);

        return true;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ws = new WebSocketClient(uri) {
//            @Override
//            public void onOpen(ServerHandshake handshakedata) {
//                Log.d(TAG,"on open client");
//            }
//
//            @Override
//            public void onMessage(String message) {
//                Log.d(TAG,"on message client");
//            }
//
//            @Override
//            public void onClose(int code, String reason, boolean remote) {
//                Log.d(TAG,"on close client");
//            }
//
//            @Override
//            public void onError(Exception ex) {
//
//            }
//        };
//
//        ws.connect();

        setContentView(R.layout.activity_sample);
        this.tv = (TextView) findViewById(R.id.textView) ;

//        roboboHelper = new RoboboServiceHelper(this, new RoboboServiceHelper.Listener() {
//            @Override
//            public void onRoboboManagerStarted(RoboboManager robobo) {
//
//                //the robobo service and manager have been started up
//                roboboManager = robobo;
//
//
//                //dismiss the wait dialog
//
//
//                //start the "custom" robobo application
//                startRoboboApplication();
//
//            }
//
//            @Override
//            public void onError(String errorMsg) {
//
//                final String error = errorMsg;
//
//
//            }
//
//        });
//
//        //start & bind the Robobo service
//        Bundle options = new Bundle();
//        roboboHelper.bindRoboboService(options);
        showRoboboDeviceSelectionDialog();
    }
    private void startRoboboApplication() {

        try {

            this.remoteModule = this.roboboManager.getModuleInstance(IRemoteControlModule.class);


        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }







    }
    /** Shows a Robobo device selection dialog, suscribes to device selection
     * events to "wait" until the user selects a device, and then starts
     * the Robobo Framework using the RoboboHelper inside an AsyncTask to
     * not freeze the UI code.
     */
    private void showRoboboDeviceSelectionDialog() {

        RoboboDeviceSelectionDialog dialog = new RoboboDeviceSelectionDialog();
        dialog.setListener(new RoboboDeviceSelectionDialog.Listener() {
            @Override
            public void roboboSelected(String roboboName) {

                final String roboboBluetoothName = roboboName;

                //start the framework in background
                AsyncTask<Void, Void, Void> launchRoboboService =
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                launchAndConnectRoboboService(roboboBluetoothName);
                                return null;
                            }
                        };
                launchRoboboService.execute();

            }

            @Override
            public void selectionCancelled() {

            }

            @Override
            public void bluetoothIsDisabled() {
                finish();
            }

        });
        dialog.show(getFragmentManager(),"BLUETOOTH-DIALOG");

    }


    private void launchAndConnectRoboboService(String roboboBluetoothName) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //wait to dialog shown during the startup of the framework and the bluetooth connection
                waitDialog = ProgressDialog.show(SampleActivity.this,
                        "Conectando","conectando", true);
            }
        });


        //we use the RoboboServiceHelper class to manage the startup and binding
        //of the Robobo Manager service and Robobo modules
        roboboHelper = new RoboboServiceHelper(this, new RoboboServiceHelper.Listener() {
            @Override
            public void onRoboboManagerStarted(RoboboManager robobo) {

                //the robobo service and manager have been started up
                roboboManager = robobo;

                //dismiss the wait dialog
                waitDialog.dismiss();

                //start the "custom" robobo application
                startRoboboApplication();

            }

            @Override
            public void onError(String errorMsg) {

                final String error = errorMsg;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //dismiss the wait dialog
                        waitDialog.dismiss();

                        //show an error dialog


                    }
                });

            }

        });

        //start & bind the Robobo service
        Bundle options = new Bundle();
        options.putString(BluetoothRobInterfaceModule.ROBOBO_BT_NAME_OPTION, roboboBluetoothName);
        roboboHelper.bindRoboboService(options);

    }

    @Override
    public void onThingsHappen(final String things) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

               tv.setText(things);


            }
        });
    }
}