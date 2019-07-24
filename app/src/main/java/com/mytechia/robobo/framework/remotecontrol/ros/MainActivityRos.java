/*******************************************************************************
 *
 *   Copyright 2019, Manufactura de Ingenios Tecnológicos S.L. 
 *   <http://www.mintforpeople.com>
 *
 *   Redistribution, modification and use of this software are permitted under
 *   terms of the Apache 2.0 License.
 *
 *   This software is distributed in the hope that it will be useful,
 *   but WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND; without even the implied
 *   warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   Apache 2.0 License for more details.
 *
 *   You should have received a copy of the Apache 2.0 License along with    
 *   this software. If not, see <http://www.apache.org/licenses/>.
 *
 ******************************************************************************/

package com.mytechia.robobo.framework.remotecontrol.ros;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.remote_control.remotemodule.ros.RosRemoteControlModule;
import com.mytechia.robobo.framework.remotecontrol.R;
import com.mytechia.robobo.framework.service.RoboboServiceHelper;
import com.mytechia.robobo.rob.BluetoothRobInterfaceModule;
import com.mytechia.robobo.rob.util.RoboboDeviceSelectionDialog;

import org.ros.address.InetAddressFactory;

public class MainActivityRos extends AppCompatActivity {

    private static final String TAG="MainActivityRos";


    private RoboboServiceHelper roboboHelper;

    private RoboboManager roboboManager;

    private ProgressDialog waitDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.roboboros_main);

        TextView txtMsg= (TextView) findViewById(R.id.txtMsg);

        txtMsg.setText(InetAddressFactory.newNonLoopback().getHostAddress());

    }




    @Override
    protected void onResume() {

        super.onResume();

        if(roboboHelper!=null) {
            roboboHelper.unbindRoboboService();
            roboboHelper=null;
        }

        showRoboboDeviceSelectionDialog();

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

                if((roboboName==null) || (roboboName.isEmpty())){
                    showErrorDialog(getString(R.string.msg_bluetooth_is_disabled));
                    return;
                }

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
            public void bluetoothIsDisabled() {
                showErrorDialog(getString(R.string.msg_bluetooth_is_disabled));
            }

            @Override
            public void selectionCancelled() {
                finish();
            }
        });

        dialog.show(getFragmentManager(),"BLUETOOTH-DIALOG");

    }


    private void launchAndConnectRoboboService(String roboboBluetoothName) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                waitDialog = ProgressDialog.show(MainActivityRos.this,
                        getString(R.string.dialogWaitConnectionTitle),
                        getString(R.string.dialogWaitConnectionMsg), true);
            }
        });

        //we use the RoboboServiceHelper class to manage the startup and binding
        //of the Robobo Manager service and Robobo modules
        roboboHelper = new RoboboServiceHelper(this, new RoboboServiceHelper.Listener() {
            @Override
            public void onRoboboManagerStarted(RoboboManager robobo) {
                //the robobo service and manager have been started up
                roboboManager = robobo;

                waitDialog.dismiss();

                //start the "custom" robobo application
                startRoboboApplication();
            }

            @Override
            public void onError(String errorMsg) {

                waitDialog.dismiss();

                //show an error dialog
                showErrorDialog(errorMsg);
            }

        });

        Intent intent = getIntent();;

        Bundle bundle= new Bundle();

        bundle.putString(RosRemoteControlModule.MASTER_URI, intent.getStringExtra(RosRemoteControlModule.MASTER_URI));

        bundle.putString(RosRemoteControlModule.ROBOBO_NAME, intent.getStringExtra(RosRemoteControlModule.ROBOBO_NAME));

        bundle.putString(BluetoothRobInterfaceModule.ROBOBO_BT_NAME_OPTION, roboboBluetoothName);

        roboboHelper.bindRoboboService(bundle);



    }

    private void startRoboboApplication() {

/*        try {

            RoboboRosModule roboModule = roboboManager.getModuleInstance(RoboboRosModule.class);

            roboModule.startRoboRosNode(new ImuTopic(roboboManager));

            roboModule.startRoboRosNode(new CameraTopic(roboboManager));

            roboModule.startRoboRosNode(new RoboStatusTopic(roboboManager, new ErrorListener()));

            roboModule.startRoboRosNode(new RoboControlService(roboboManager, new ErrorListener()));

            roboModule.startRoboRosNode(new RoboSpeakService(roboboManager));

            roboModule.startRoboRosNode(new RoboShockTopic(roboboManager));

            roboModule.startRoboRosNode(new RoboEmotionService(roboboManager));

            roboboHelper.launchDisplayActivity(WebGLEmotionDisplayActivity.class);

        } catch (ModuleNotFoundException ex) {
            Log.e(TAG, "Error starting robobo application", ex);
            showErrorDialog("Error :"+ex.getMessage());
        } catch (InternalErrorException ex) {
            Log.e(TAG, "Error starting robobo application", ex);
            showErrorDialog("Error :"+ex.getMessage());
        }catch(Throwable th){
            Log.e(TAG, "Error starting robobo application", th);
            showErrorDialog("Error starting RoboboRosModule: "+th.getMessage());
        }*/


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (roboboHelper != null) {
            roboboHelper.unbindRoboboService();
        }
        finish();
    }



    /** Shows an error dialog with the message 'msg'
     *
     * @param msg the message to be shown in the error dialog
     */
    protected void showErrorDialog(final String msg) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityRos.this);

                builder.setTitle(com.mytechia.robobo.framework.R.string.title_error_dialog).setMessage(msg);
                builder.setPositiveButton(com.mytechia.robobo.framework.R.string.ok_msg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false);

                AlertDialog dialog = builder.create();

                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

                dialog.show();

            }
        });


    }

/*    private class ErrorListener implements RoboRosErrorListener {

        @Override
        public void roboRosError(Throwable ex) {

            Throwable cause = ex.getCause();

            if((cause instanceof CommunicationException) || (ex instanceof CommunicationException)){
                showErrorDialog("Error communicating with robot");
            }else{
                showErrorDialog("Error: "+ ex.getMessage());
            }

        }
    }*/

}
