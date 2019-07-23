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

package com.mytechia.robobo.framework.remotecontrol.ros2;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.IModule;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;
import com.mytechia.robobo.framework.remote_control.remotemodule.Response;
import com.mytechia.robobo.framework.remote_control.remotemodule.Status;

import com.mytechia.robobo.framework.remotecontrol.ros2.topics.StatusNode;
import com.mytechia.robobo.framework.remotecontrol.ros2.services.CommandNode;

import org.ros2.rcljava.executors.Executor;
import org.ros2.rcljava.node.BaseComposableNode;

/**
 * Public interface of the Robobo ROS2 node that implements a proxy in ROS2 for the Robobo Remote
 * Control Protocol
 *
 * The module allows the execution of arbritray ROS-java nodes.
 *
 */

public interface IRos2RemoteControlModule extends IModule {


    /** Returns the name of the Robobo robot for multi-robot configurations
     *
     * @return the name of the Robobo robot
     */
    String getRoboboName();

    /*
     *  Start the execution of CommandNode and StatusNode
     *  @param remoteControlModule the remote control module initialized
     *  @param roboboName the robobo name
     */
    void initRoboboRos2Nodes(IRemoteControlModule remoteControlModule, String roboboName) throws InternalErrorException;

    /**
     *   Returns the StatusNode initialized, only works if the StatusNode has been initialized
     **/
    StatusNode getStatusNode();

    /**
     *   Returns the CommandNode initialized, only works if the CommandNode has been initialized
     **/
    CommandNode getCommandNode();

    void run();

    /**
     *  Returns the executor in charge of the administration of the ROS2 nodes
     **/
    Executor getExecutor();
}
