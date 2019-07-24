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

import android.util.Log;

import org.ros2.rcljava.consumers.TriConsumer;
import org.ros2.rcljava.node.BaseComposableNode;
import org.ros2.rcljava.service.RMWRequestId;

import java.util.HashMap;


import robobo_msgs.srv.MovePanTilt;
import robobo_msgs.srv.MovePanTilt_Request;
import robobo_msgs.srv.MovePanTilt_Response;
import std_msgs.msg.Int8;


/**
 * ROS2 service for the Move Pan and Tilt command.
 *
 * It sends a MOVEPAN-BLOCKING and/or MOVETILT-BLOCKING commands to the robobo remote control module.
 *
 */
public class MovePanTiltService {

    private CommandNode commandNode;
    private BaseComposableNode baseComposableNode;


    public MovePanTiltService(CommandNode commandNode) {
        this.commandNode = commandNode;
        this.baseComposableNode = new BaseComposableNode("MovePanTiltService");

    }


    public BaseComposableNode getBaseComposableNode(){
        return this.baseComposableNode;
    }


    public void start() throws NoSuchFieldException, IllegalAccessException {

        this.baseComposableNode.getNode().createService(MovePanTilt.class,"move_pan_tilt", new CallBack());
    }

    public class CallBack implements TriConsumer<RMWRequestId, MovePanTilt_Request, MovePanTilt_Response> {

        @Override
        public void accept(RMWRequestId rmwRequestId, MovePanTilt_Request request, MovePanTilt_Response response) {


            HashMap<String, String> panParams = new HashMap<>();
            panParams.put("pos", String.valueOf(request.getPanpos().getData()));
            panParams.put("speed", String.valueOf(request.getPanspeed().getData()));
            int panId= request.getPanunlockid().getData();
            panParams.put("blockid", String.valueOf(panId));


            Log.i("MOVE-PT", "MovePanMsg: " + panParams.get("pos") + " - " + panParams.get("speed"));

            com.mytechia.robobo.framework.remote_control.remotemodule.Command panCommand =
                    new com.mytechia.robobo.framework.remote_control.remotemodule.Command("MOVEPAN-BLOCKING", panId, panParams);

            HashMap<String, String> tiltParams = new HashMap<>();
            tiltParams.put("pos", String.valueOf(request.getTiltpos().getData()));
            tiltParams.put("speed", String.valueOf(request.getTiltspeed().getData()));
            int tiltId = request.getTiltunlockid().getData();
            tiltParams.put("blockid", String.valueOf(tiltId));

            Log.i("MOVE-PT", "MovePanMsg: " + tiltParams.get("pos") + " - " + tiltParams.get("speed"));

            com.mytechia.robobo.framework.remote_control.remotemodule.Command tiltCommand =
                    new com.mytechia.robobo.framework.remote_control.remotemodule.Command("MOVETILT-BLOCKING", tiltId, tiltParams);

            if (panId > 0) {
                commandNode.getRemoteControlModule().queueCommand(panCommand);
            }

            if (tiltId > 0) {
                commandNode.getRemoteControlModule().queueCommand(tiltCommand);
            }

            Int8 r = response.getError();
            r.setData((byte) 0);
            response.setError(r);

        }
    }

}