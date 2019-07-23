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

import robobo_msgs.msg.IRs;

/**
 * Status Topic for the robot base battery level.
 *
 * The topic is robot/battery/base
 *
 */
public class IRsStatusTopic extends AStatusTopic {

    private static final String TOPIC = "irs";
    public static final String STATUS = "IRS";

    private Publisher<IRs> topic;
    private BaseComposableNode baseComposableNode = null;


    public BaseComposableNode getBaseComposableNode(){
        return this.baseComposableNode;
    }

    public IRsStatusTopic(StatusNode node) {
        super(node, TOPIC, STATUS, null);
        this.baseComposableNode = new BaseComposableNode("IRsStatusTopic");
    }

    public void start() {
        this.topic= this.baseComposableNode.getNode().<IRs>createPublisher(IRs.class, this.getTopicName());
    }


    @Override
    public void publishStatus(Status status) {

        if (status.getName().equals(this.getSupportedStatus())) {

            IRs msg = new IRs();

            String BackC = status.getValue().get("Back-C");
            String BackR = status.getValue().get("Back-R");
            String BackL = status.getValue().get("Back-L");
            String FrontC = status.getValue().get("Front-C");
            String FrontR = status.getValue().get("Front-R");
            String FrontRR = status.getValue().get("Front-RR");
            String FrontL = status.getValue().get("Front-L");
            String FrontLL = status.getValue().get("Front-LL");

            if (BackC!=null && BackR!=null && BackL!= null && FrontC!= null
                    && FrontR!= null && FrontRR!= null && FrontL!= null && FrontLL!= null) {

                msg.getBackc().setRange(Float.parseFloat(BackC));
                msg.getBackc().setMinRange(0);
                msg.getBackc().setMaxRange(1.0f);
                msg.getBackc().setRadiationType((byte)1);
                msg.getBackc().setFieldOfView(0);

                msg.getBackr().setRange(Float.parseFloat(BackR));
                msg.getBackr().setMinRange(0);
                msg.getBackc().setMaxRange(1.0f);
                msg.getBackr().setRadiationType((byte)1);
                msg.getBackr().setFieldOfView(0);

                msg.getBackl().setRange(Float.parseFloat(BackL));
                msg.getBackl().setMinRange(0);
                msg.getBackl().setMaxRange(1.0f);
                msg.getBackl().setRadiationType((byte)1);
                msg.getBackl().setFieldOfView(0);

                msg.getFrontc().setRange(Float.parseFloat(FrontC));
                msg.getFrontc().setMinRange(0);
                msg.getFrontc().setMaxRange(1.0f);
                msg.getFrontc().setRadiationType((byte)1);
                msg.getFrontc().setFieldOfView(0);

                msg.getFrontr().setRange(Float.parseFloat(FrontR));
                msg.getFrontr().setMinRange(0);
                msg.getFrontr().setMaxRange(1.0f);
                msg.getFrontr().setRadiationType((byte)1);
                msg.getFrontr().setFieldOfView(0);

                msg.getFrontrr().setRange(Float.parseFloat(FrontRR));
                msg.getFrontrr().setMinRange(0);
                msg.getFrontrr().setMaxRange(1.0f);
                msg.getFrontrr().setRadiationType((byte)1);
                msg.getFrontrr().setFieldOfView(0);

                msg.getFrontl().setRange(Float.parseFloat(FrontL));
                msg.getFrontl().setMinRange(0);
                msg.getFrontl().setMaxRange(1.0f);
                msg.getFrontl().setRadiationType((byte)1);
                msg.getFrontl().setFieldOfView(0);

                msg.getFrontll().setRange(Float.parseFloat(FrontLL));
                msg.getFrontll().setMinRange(0);
                msg.getFrontll().setMaxRange(1.0f);
                msg.getFrontll().setRadiationType((byte)1);
                msg.getFrontll().setFieldOfView(0);

                this.topic.publish(msg);

            }

        }

    }
}
