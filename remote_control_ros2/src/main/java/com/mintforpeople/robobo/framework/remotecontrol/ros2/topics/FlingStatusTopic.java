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

import com.mytechia.robobo.framework.remote_control.remotemodule.Status;

import org.ros2.rcljava.publisher.Publisher;
import org.ros2.rcljava.node.BaseComposableNode;

import robobo_msgs.msg.Fling;

/**
 * Status Topic for the robot base battery level.
 *
 * The topic is robot/battery/base
 *
 */
public class FlingStatusTopic extends AStatusTopic {

    private static final String TOPIC = "fling";
    public static final String STATUS = "FLING";
    private BaseComposableNode baseComposableNode = null;


    private Publisher<Fling> topic;


    public BaseComposableNode getBaseComposableNode(){
        return this.baseComposableNode;
    }

    public FlingStatusTopic(StatusNode node) {
        super(node, TOPIC, STATUS, null);
        this.baseComposableNode = new BaseComposableNode("FlingStatusTopic");
    }

    public void start() {
        this.topic= this.baseComposableNode.getNode().<Fling>createPublisher(Fling.class, this.getTopicName());
    }


    @Override
    public void publishStatus(Status status) {

        if (status.getName().equals(this.getSupportedStatus())) {

            Fling msg = new Fling();

            String angle = status.getValue().get("angle");
            String time = status.getValue().get("time");
            String distance = status.getValue().get("distance");

            if (angle!=null && time!=null && distance!= null) {

                msg.getAngle().setData((short)Float.parseFloat(angle));
                msg.getTime().setData(Integer.parseInt(time));
                msg.getDistance().setData((short) Float.parseFloat(distance));

                this.topic.publish(msg);

            }

        }

    }
}
