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


package com.mytechia.robobo.framework.remotecontrol.ros2.topics;

import com.mytechia.robobo.framework.remote_control.remotemodule.Status;

import org.ros2.rcljava.publisher.Publisher;
import org.ros2.rcljava.node.BaseComposableNode;

import robobo_msgs.msg.Wheels;

/**
 * Status Topic for the robot base battery level.
 *
 * The topic is robot/battery/base
 *
 */
public class WheelsStatusTopic extends AStatusTopic {

    private static final String TOPIC = "wheels";
    public static final String STATUS = "WHEELS";


    private Publisher<Wheels> topic;
    private BaseComposableNode baseComposableNode = null;


    public BaseComposableNode getBaseComposableNode(){
        return this.baseComposableNode;
    }


    public WheelsStatusTopic(StatusNode node) {
        super(node, TOPIC, STATUS, null);
        this.baseComposableNode = new BaseComposableNode("WheelsStatusTopic");
    }


    public void start() {
        this.topic = this.baseComposableNode.getNode().<Wheels>createPublisher(Wheels.class, this.getTopicName());
    }


    @Override
    public void publishStatus(Status status) {

        if (status.getName().equals(this.getSupportedStatus())) {

            Wheels msg = new Wheels();

            String wheelPosR = status.getValue().get("wheelPosR");
            String wheelPosL = status.getValue().get("wheelPosR");
            String wheelSpeedR = status.getValue().get("wheelSpeedR");
            String wheelSpeedL = status.getValue().get("wheelSpeedL");

            if (wheelPosR!=null && wheelPosL!=null && wheelSpeedR!= null && wheelSpeedL!= null) {

                msg.getWheelposr().setData(Long.parseLong(wheelPosR));
                msg.getWheelposl().setData(Long.parseLong(wheelPosL));
                msg.getWheelspeedr().setData(Long.parseLong(wheelSpeedR));
                msg.getWheelspeedl().setData(Long.parseLong(wheelSpeedL));


                this.topic.publish(msg);

            }

        }

    }
}
