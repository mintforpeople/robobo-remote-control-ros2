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

import std_msgs.msg.Int16;

/**
 * Status Topic for the robot base battery level.
 *
 * The topic is robot/battery/base
 *
 */
public class UnlockMoveStatusTopic extends AStatusTopic {

    private static final String TOPIC = "unlock/move";

    private static final String UNLOCK = "UNLOCK";
    private static final String UNLOCK_MOVE = "UNLOCK-MOVE";
    private static final String UNLOCK_PAN ="UNLOCK-PAN";
    private static final String UNLOCK_TILT ="UNLOCK-TILT";
    private BaseComposableNode baseComposableNode = null;


    private Publisher<Int16> topic;


    public BaseComposableNode getBaseComposableNode(){
        return this.baseComposableNode;
    }


    public UnlockMoveStatusTopic(StatusNode node) {
        super(node, TOPIC, null, null);
        this.baseComposableNode = new BaseComposableNode("UnlockMoveStatusTopic");
    }


    public void start() {
        this.topic = this.baseComposableNode.getNode().<Int16>createPublisher(Int16.class, this.getTopicName());
    }


    @Override
    public void publishStatus(Status status) {

        if (status.getName().startsWith(UNLOCK)) {

            Int16 msg = new Int16();

            String value = status.getValue().get("blockid");

            if (value != null) {

                msg.setData(Short.parseShort(value));

                this.topic.publish(msg);

            }

        }

    }
}
