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


package com.mintforpeople.robobo.framework.remotecontrol.ros2.topics;

import org.ros2.rcljava.executors.Executor;

/**
 * The main functionality of StatusNode is to instantiate the publishers nodes and add to the ros executor.
 */
public class StatusNode {

    private static final String STATUS_BATBASE = "BAT-BASE";
    private static final String TOPIC_BATBASE = "battery/base";
    private static final String KEY_BATTERY = "level";

    private static final String STATUS_PHONEBASE = "BAT-PHONE";
    private static final String TOPIC_PHONEBASE = "battery/phone";

    private static final String STATUS_PAN = "PAN";
    private static final String TOPIC_PAN = "pan";
    private static final String STATUS_TILT = "TILT";
    private static final String TOPIC_TILT = "tilt";

    private static final String STATUS_ALIGHT = "AMBIENTLIGHT";
    private static final String TOPIC_ALIGHT = "ambientlight";

    private static final String STATUS_EMOTION = "EMOTION";
    private static final String TOPIC_EMOTION = "emotion";


    private static final String TAG = "Robobo Status";

    private static final String NAME_NODE_ROB_STATUS ="robobo_status";


    private String roboboName ="";


    private Int8StatusTopic baseBatteryStatusTopic;
    private Int8StatusTopic phoneBatteryStatusTopic;
    private Int16StatusTopic panStatusTopic;
    private Int16StatusTopic tiltStatusTopic;
    private Int32StatusTopic ambientLightStatusTopic;
    private StringStatusTopic emotionStatusTopic;
    private AccelerationStatusTopic accelStatusTopic;
    private OrientationStatusTopic orientationStatusTopic;
    private UnlockMoveStatusTopic unlockMoveStatusTopic;
    private UnlockTalkStatusTopic unlockTalkStatusTopic;
    private WheelsStatusTopic wheelsStatusTopic;
    private LedStatusTopic ledStatusTopic;
    private TapStatusTopic tapStatusTopic;
    private FlingStatusTopic flingStatusTopic;
    private IRsStatusTopic irsStatusTopic;


    private boolean started = false;



    public StatusNode(String roboboName){

        if(roboboName !=null){
            this.roboboName = roboboName;
        }
    }


    String getRoboboName() {
        return this.roboboName;
    }


    public void onStart(Executor executor) {

        this.baseBatteryStatusTopic = new Int8StatusTopic(this, "BaseBatteryStatusTopic", TOPIC_BATBASE, STATUS_BATBASE, KEY_BATTERY);
        this.baseBatteryStatusTopic.start();
        executor.addNode(this.baseBatteryStatusTopic.getBaseComposableNode());

        this.phoneBatteryStatusTopic = new Int8StatusTopic(this,"PhoneBatteryStatusTopic", TOPIC_PHONEBASE, STATUS_PHONEBASE, KEY_BATTERY);
        this.phoneBatteryStatusTopic.start();
        executor.addNode(this.phoneBatteryStatusTopic.getBaseComposableNode());

        this.panStatusTopic = new Int16StatusTopic(this,"PanStatusTopic", TOPIC_PAN, STATUS_PAN, "panPos");
        this.panStatusTopic.start();
        executor.addNode(this.panStatusTopic.getBaseComposableNode());

        this.tiltStatusTopic = new Int16StatusTopic(this,"TiltStatusTopic", TOPIC_TILT, STATUS_TILT, "tiltPos");
        this.tiltStatusTopic.start();
        executor.addNode(this.tiltStatusTopic.getBaseComposableNode());

        this.ambientLightStatusTopic = new Int32StatusTopic(this,"AmbientLightStatusTopic", TOPIC_ALIGHT, STATUS_ALIGHT, "level");
        this.ambientLightStatusTopic.start();
        executor.addNode(this.ambientLightStatusTopic.getBaseComposableNode());

        this.emotionStatusTopic = new StringStatusTopic(this, TOPIC_EMOTION, STATUS_EMOTION, "emotion");
        this.emotionStatusTopic.start();
        executor.addNode(this.emotionStatusTopic.getBaseComposableNode());

        this.accelStatusTopic = new AccelerationStatusTopic(this);
        this.accelStatusTopic.start();
        executor.addNode(this.accelStatusTopic.getBaseComposableNode());

        this.orientationStatusTopic = new OrientationStatusTopic(this);
        this.orientationStatusTopic.start();
        executor.addNode(this.orientationStatusTopic.getBaseComposableNode());

        this.unlockMoveStatusTopic = new UnlockMoveStatusTopic(this);
        this.unlockMoveStatusTopic.start();
        executor.addNode(this.unlockMoveStatusTopic.getBaseComposableNode());

        this.unlockTalkStatusTopic = new UnlockTalkStatusTopic(this);
        this.unlockTalkStatusTopic.start();
        executor.addNode(this.unlockTalkStatusTopic.getBaseComposableNode());

        this.wheelsStatusTopic = new WheelsStatusTopic(this);
        this.wheelsStatusTopic.start();
        executor.addNode(this.wheelsStatusTopic.getBaseComposableNode());

        this.ledStatusTopic = new LedStatusTopic(this);
        this.ledStatusTopic.start();
        executor.addNode(this.ledStatusTopic.getBaseComposableNode());

        this.tapStatusTopic = new TapStatusTopic(this);
        this.tapStatusTopic.start();
        executor.addNode(this.tapStatusTopic.getBaseComposableNode());

        this.flingStatusTopic = new FlingStatusTopic(this);
        this.flingStatusTopic.start();
        executor.addNode(this.flingStatusTopic.getBaseComposableNode());

        this.irsStatusTopic = new IRsStatusTopic(this);
        this.irsStatusTopic.start();
        executor.addNode(this.irsStatusTopic.getBaseComposableNode());

        this.started = true;

    }



    public void publishStatusMessage(com.mytechia.robobo.framework.remote_control.remotemodule.Status status) {

        if (started) {

            switch (status.getName()) {

                case STATUS_BATBASE:
                    this.baseBatteryStatusTopic.publishStatus(status);
                    break;

                case STATUS_PHONEBASE:
                    this.phoneBatteryStatusTopic.publishStatus(status);
                    break;

                case STATUS_PAN:
                    this.panStatusTopic.publishStatus(status);
                    break;

                case STATUS_TILT:
                    this.tiltStatusTopic.publishStatus(status);
                    break;

                case STATUS_ALIGHT:
                    this.ambientLightStatusTopic.publishStatus(status);
                    break;

                case STATUS_EMOTION:
                    this.emotionStatusTopic.publishStatus(status);
                    break;

                case AccelerationStatusTopic.STATUS:
                    this.accelStatusTopic.publishStatus(status);
                    break;

                case OrientationStatusTopic.STATUS:
                    this.orientationStatusTopic.publishStatus(status);
                    break;

                case UnlockTalkStatusTopic.STATUS_UNLOCK_TALK:
                    this.unlockTalkStatusTopic.publishStatus(status);
                    break;

                case WheelsStatusTopic.STATUS:
                    this.wheelsStatusTopic.publishStatus(status);
                    break;

                case LedStatusTopic.STATUS:
                    this.ledStatusTopic.publishStatus(status);
                    break;

                case TapStatusTopic.STATUS:
                    this.tapStatusTopic.publishStatus(status);
                    break;

                case FlingStatusTopic.STATUS:
                    this.flingStatusTopic.publishStatus(status);
                    break;

                case IRsStatusTopic.STATUS:
                    this.irsStatusTopic.publishStatus(status);
                    break;

                default:
                    this.unlockMoveStatusTopic.publishStatus(status);
                    break;

            }

        }

    }


}
