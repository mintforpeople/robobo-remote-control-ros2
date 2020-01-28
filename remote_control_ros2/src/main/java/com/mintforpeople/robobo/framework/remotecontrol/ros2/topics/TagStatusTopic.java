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

/* Author: Luis F. Llamas Luaces luis.llamas@mintforpeople.com */


package com.mintforpeople.robobo.framework.remotecontrol.ros2.topics;

import com.mytechia.robobo.framework.remote_control.remotemodule.Status;

import org.ros2.rcljava.node.BaseComposableNode;
import org.ros2.rcljava.publisher.Publisher;

import robobo_msgs.msg.Fling;
import robobo_msgs.msg.Tag;

/**
 * Status Topic for tags detected by the robot.
 *
 * The topic is robot/tag
 *
 */
public class TagStatusTopic extends AStatusTopic {

    private static final String TOPIC = "tag";
    public static final String STATUS = "TAG";
    private BaseComposableNode baseComposableNode = null;


    private Publisher<Tag> topic;


    public BaseComposableNode getBaseComposableNode(){
        return this.baseComposableNode;
    }

    public TagStatusTopic(StatusNode node) {
        super(node, TOPIC, STATUS, null);
        this.baseComposableNode = new BaseComposableNode("TagStatusTopic");
    }

    public void start() {
        this.topic= this.baseComposableNode.getNode().<Tag>createPublisher(Tag.class, this.getTopicName());
    }


    @Override
    public void publishStatus(Status status) {

        if (status.getName().equals(this.getSupportedStatus())) {

            Tag msg = new Tag();
            String id = status.getValue().get("id");
            String cor1x = status.getValue().get("cor1x");
            String cor1y = status.getValue().get("cor1y");
            String cor2x = status.getValue().get("cor2x");
            String cor2y = status.getValue().get("cor2y");
            String cor3x = status.getValue().get("cor3x");
            String cor3y = status.getValue().get("cor3y");
            String cor4x = status.getValue().get("cor4x");
            String cor4y = status.getValue().get("cor4y");
            String rvec_0 = status.getValue().get("rvec_0");
            String rvec_1 = status.getValue().get("rvec_1");
            String rvec_2 = status.getValue().get("rvec_2");
            String tvec_0 = status.getValue().get("tvec_0");
            String tvec_1 = status.getValue().get("tvec_1");
            String tvec_2 = status.getValue().get("tvec_2");


            if (id !=null
                    && cor1x != null
                    && cor1y != null
                    && cor2x != null
                    && cor2y != null
                    && cor3x != null
                    && cor3y != null
                    && cor4x != null
                    && cor4y != null
                    && rvec_0 != null
                    && rvec_1 != null
                    && rvec_2 != null
                    && tvec_0 != null
                    && tvec_1 != null
                    && tvec_2 != null ) {


                msg.getId().setData(id);
                msg.getCor1x().setData(Integer.parseInt(cor1x));
                msg.getCor1y().setData(Integer.parseInt(cor1y));
                msg.getCor2x().setData(Integer.parseInt(cor2x));
                msg.getCor2y().setData(Integer.parseInt(cor2y));
                msg.getCor3x().setData(Integer.parseInt(cor3x));
                msg.getCor3y().setData(Integer.parseInt(cor3y));
                msg.getCor4x().setData(Integer.parseInt(cor4x));
                msg.getCor4y().setData(Integer.parseInt(cor4y));
                msg.getRvec0().setData(Double.parseDouble(rvec_0));
                msg.getRvec1().setData(Double.parseDouble(rvec_1));
                msg.getRvec2().setData(Double.parseDouble(rvec_2));
                msg.getTvec0().setData(Double.parseDouble(tvec_0));
                msg.getTvec1().setData(Double.parseDouble(tvec_1));
                msg.getTvec2().setData(Double.parseDouble(tvec_2));


                this.topic.publish(msg);

            }

        }

    }
}
