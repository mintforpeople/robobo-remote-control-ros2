/*********************************************************************
 * Software License Agreement (BSD License)
 *
 *  Copyright (c) 2019, Intelligent Robotics Core S.L.
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided
 *     with the distribution.
 *   * Neither the name of Intelligent Robotics Core nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 *  FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 *  COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *  LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *  ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 **********************************************************************/

/* Author: Lorena Bajo Rebollo - lbajo9@gmail.com */

package com.mintforpeople.robobo.framework.remotecontrol.ros2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.mintforpeople.robobo.framework.remotecontrol.ros2.services.CommandNode;
import com.mintforpeople.robobo.framework.remotecontrol.ros2.topics.StatusNode;
import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.frequency.FrequencyMode;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlProxy;
import com.mytechia.robobo.framework.remote_control.remotemodule.Response;
import com.mytechia.robobo.framework.remote_control.remotemodule.Status;

import android.net.wifi.WifiManager;
import android.os.PowerManager;

import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.executors.Executor;
import org.ros2.rcljava.executors.SingleThreadedExecutor;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.POWER_SERVICE;
import static android.content.Context.WIFI_SERVICE;

public class Ros2RemoteControlModule implements IRos2RemoteControlModule, IRemoteControlProxy {

    private static final String MODULE_INFO = "ROS2 RC Module";

    private static final String TAG = MODULE_INFO;

    private static final String MODULE_VERSION = "1.0.0";

    public static final String ROBOBO_NAME="robobo.name";

    private Context context;

    private StatusNode statusNode;

    private CommandNode commandNode;

    private String roboName;

    private IRemoteControlModule remoteControlModule;

    private PowerManager.WakeLock wakeLock;

    private WifiManager.WifiLock wifiLock;

    private Executor rosExecutor;

    private Handler handler;

    private Timer timer;



    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void startup(RoboboManager roboboManager) throws InternalErrorException {
        Log.d(TAG, "Starting ROS2 Remote Control Module");

        this.remoteControlModule = roboboManager.getModuleInstance(IRemoteControlModule.class);

        if (this.remoteControlModule == null) {
            throw new InternalErrorException("No instance IRemoteControlModule found.");
        }

        this.context = roboboManager.getApplicationContext();

        PowerManager powerManager = (PowerManager) context.getApplicationContext().getSystemService(POWER_SERVICE);

        this.wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);

        this.wakeLock.acquire();

        int wifiLockType = WifiManager.WIFI_MODE_FULL;

        try {
            wifiLockType = WifiManager.class.getField("WIFI_MODE_FULL_HIGH_PERF").getInt(null);
        } catch (Exception e) {
            this.shutdown();
            // We must be running on a pre-Honeycomb device.
            Log.w(TAG, "Unable to acquire high performance wifi lock.", e);
        }

        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);

        this.wifiLock = wifiManager.createWifiLock(wifiLockType, TAG);

        wifiLock.acquire();

        Bundle roboboBundleOptions = roboboManager.getOptions();

        Bundle roboboOptions = roboboManager.getOptions();

        this.roboName = roboboOptions.getString(Ros2RemoteControlModule.ROBOBO_NAME, "");

        this.handler = new Handler();

        RCLJava.rclJavaInit();

        this.rosExecutor = createExecutor();

        run();

        initRoboboRos2Nodes(this.remoteControlModule, this.roboName);

        this.remoteControlModule.registerRemoteControlProxy(this);

        roboboManager.changeFrequencyModeTo(FrequencyMode.MAX);

    }

    @Override
    public void shutdown() throws InternalErrorException {

        RCLJava.shutdown();

        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
        if (wifiLock.isHeld()) {
            wifiLock.release();
        }

    }

    @Override
    public String getModuleInfo() {
        return MODULE_INFO;
    }

    @Override
    public String getModuleVersion() {
        return MODULE_VERSION;
    }

    @Override
    public void notifyStatus(Status status) {

        statusNode.publishStatusMessage(status);

    }

    @Override
    public void notifyReponse(Response response) {

    }

    @Override
    public Executor getExecutor() {
        return this.rosExecutor;
    }

    protected Executor createExecutor() {
        return new SingleThreadedExecutor();
    }

    public void run() {
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Runnable runnable = new Runnable() {
                    public void run() {
                        Ros2RemoteControlModule.this.getExecutor().spinSome();
                    }
                };
                Ros2RemoteControlModule.this.handler.post(runnable);
            }
        }, 0L, 200L);
    }

    @Override
    public StatusNode getStatusNode() {
        return this.statusNode;
    }

    @Override
    public CommandNode getCommandNode() {
        return this.commandNode;
    }

    @Override
    public String getRoboboName() {
        return this.roboName;
    }

    @Override
    public void initRoboboRos2Nodes(IRemoteControlModule remoteControlModule, String roboboName) throws InternalErrorException {

        this.statusNode = new StatusNode(roboboName);
        this.statusNode.onStart(getExecutor());

        this.commandNode = new CommandNode(remoteControlModule, roboboName);
        try {
            this.commandNode.onStart(getExecutor());

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}
