/*******************************************************************************
 *
 *   Copyright 2016 Mytech Ingenieria Aplicada <http://www.mytechia.com>
 *   Copyright 2016 Gervasio Varela <gervasio.varela@mytechia.com>
 *   Copyright 2016 Julio Gomez <julio.gomez@mytechia.com>
 *
 *   This file is part of Robobo Ros Module.
 *
 *   Robobo Ros Module is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Robobo Ros Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Robobo Ros Module.  If not, see <http://www.gnu.org/licenses/>.
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
