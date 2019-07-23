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


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.mytechia.robobo.framework.remotecontrol.R;
import com.mytechia.robobo.framework.remote_control.remotemodule.ros.RosRemoteControlModule;


public class ConfigurationRosActivity extends Activity {

    private static final String TAG="ConfigurationRosActivity";

    public static final String ROS_MODULE_PREFERENCES="com.mytehia.ros.rosmodule.preferences";

    public static final String MASTER_URI_PREFERENCE="com.mytehia.ros.rosmodule.master.uri.prefecences";

    public static final String NODE_MASTER_PREFERENCE="com.mytehia.ros.rosmodule.this.master.prefecences";

    public static final String ROBOT_NAME_PREFERENCE="com.mytehia.ros.rosmodule.robot.name.prefecences";

    public static final String USE_ROBOT_NAME_PREFERENCE="com.mytehia.ros.rosmodule.use.robot.name.prefecences";

    private EditText txtUrlMaster;

    private CheckBox checkUseAsMaster;

    private EditText txtRobotName;

    private CheckBox checkUseMultiRobot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button btnNext= (Button) findViewById(R.id.btnNext);

        checkUseAsMaster= (CheckBox) findViewById(R.id.checkUseAsMaster);

        txtUrlMaster= (EditText)findViewById(R.id.txtUrlMaster);

        checkUseAsMaster.setOnClickListener(new CheckOnUseAsMasterOnClickListener());

        txtRobotName= (EditText)findViewById(R.id.txtRobotName);

        checkUseMultiRobot= (CheckBox) findViewById(R.id.checkUseMultiRobot);

        checkUseMultiRobot.setOnClickListener(new CheckUseMultiRobotOnClickListener());


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPref= getApplicationContext().getSharedPreferences(ROS_MODULE_PREFERENCES, Context.MODE_PRIVATE);

                SharedPreferences.Editor editorSharePreferences = sharedPref.edit();

                Editable editableUrlMaster= txtUrlMaster.getText();
                String urlMaster = editableUrlMaster.toString().trim();

                editorSharePreferences.putString(MASTER_URI_PREFERENCE, urlMaster);
                editorSharePreferences.putBoolean(NODE_MASTER_PREFERENCE, checkUseAsMaster.isChecked());
                editorSharePreferences.commit();

                Editable editableRobotName = txtRobotName.getText();
                String roboName = editableRobotName.toString().trim();

                boolean useRoboboName= ((!roboName.isEmpty()) && (checkUseMultiRobot.isChecked()));

                editorSharePreferences.putString(ROBOT_NAME_PREFERENCE, roboName);
                editorSharePreferences.putBoolean(USE_ROBOT_NAME_PREFERENCE, useRoboboName);
                editorSharePreferences.commit();

                Intent mainActivityRosIntent=new Intent(ConfigurationRosActivity.this, MainActivityRos.class);

                mainActivityRosIntent.putExtra(RosRemoteControlModule.MASTER_URI, urlMaster);
                if (useRoboboName) {
                    mainActivityRosIntent.putExtra(RosRemoteControlModule.ROBOBO_NAME, roboName);
                }

                startActivity(mainActivityRosIntent);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPref= this.getApplicationContext().getSharedPreferences(ROS_MODULE_PREFERENCES, Context.MODE_PRIVATE);

        boolean useHowMaster=sharedPref.getBoolean(NODE_MASTER_PREFERENCE, true);
        checkUseAsMaster.setChecked(useHowMaster);

        String url=sharedPref.getString(MASTER_URI_PREFERENCE, RosRemoteControlModule.DEFAULT_MASTER_URI);
        txtUrlMaster.setText(url);
        txtUrlMaster.setEnabled(!useHowMaster);

        String robotName=sharedPref.getString(ROBOT_NAME_PREFERENCE, "");
        txtRobotName.setText(robotName);


    }



    private class CheckOnUseAsMasterOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if(checkUseAsMaster.isChecked()){
                txtUrlMaster.getText().clear();
                txtUrlMaster.getText().append(RosRemoteControlModule.DEFAULT_MASTER_URI);
            }

            txtUrlMaster.setEnabled(!checkUseAsMaster.isChecked());

        }
    }

    private class CheckUseMultiRobotOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            txtRobotName.setEnabled(checkUseMultiRobot.isChecked());
        }
    }
}
