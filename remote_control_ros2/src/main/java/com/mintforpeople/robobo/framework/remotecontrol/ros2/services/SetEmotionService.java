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

package com.mintforpeople.robobo.framework.remotecontrol.ros2.services;


import org.ros2.rcljava.consumers.TriConsumer;
import org.ros2.rcljava.node.BaseComposableNode;
import org.ros2.rcljava.service.RMWRequestId;

import java.util.HashMap;

import robobo_msgs.srv.SetEmotion;
import robobo_msgs.srv.SetEmotion_Request;
import robobo_msgs.srv.SetEmotion_Response;
import std_msgs.msg.Int8;


/**
 * ROS2 service for changing the emotion of the robot.
 *
 * It sends a SET-EMOTION command to the robobo remote control module.
 *
 */
public class SetEmotionService {

    private CommandNode commandNode;
    private BaseComposableNode baseComposableNode;


    public SetEmotionService(CommandNode commandNode) {
        this.commandNode = commandNode;
        this.baseComposableNode = new BaseComposableNode("SetEmotionService");
    }


    public BaseComposableNode getBaseComposableNode(){
        return this.baseComposableNode;
    }


    public void start() throws NoSuchFieldException, IllegalAccessException {

        this.baseComposableNode.getNode().createService(SetEmotion.class,"set_emotion", new CallBack());
    }

    public class CallBack implements TriConsumer<RMWRequestId, SetEmotion_Request, SetEmotion_Response> {

        @Override
        public void accept(RMWRequestId rmwRequestId, SetEmotion_Request request, SetEmotion_Response response) {

            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("emotion", request.getEmotion().getData());

            com.mytechia.robobo.framework.remote_control.remotemodule.Command command=
                    new com.mytechia.robobo.framework.remote_control.remotemodule.Command("SET-EMOTION", 0, parameters);

            commandNode.getRemoteControlModule().queueCommand(command);

            Int8 r = response.getError();
            r.setData((byte)0);
            response.setError(r);

        }
    }
}
