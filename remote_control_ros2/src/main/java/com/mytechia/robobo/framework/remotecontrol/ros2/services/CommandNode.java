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

package com.mytechia.robobo.framework.remotecontrol.ros2.services;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;
import org.ros2.rcljava.executors.Executor;


/**
 * The main functionality of CommandNode is to instantiate the services nodes and add to the ros executor.
 */

public class CommandNode{

    private IRemoteControlModule remoteControlModule;

    private String roboboName ="";

    private MoveWheelsService moveWheelsService;
    private MovePanTiltService movePanTiltService;
    private PlaySoundService playSoundService;
    private ResetWheelsService resetWheelsService;
    private SetCameraService setCameraService;
    private SetEmotionService setEmotionService;
    private SetFrequencyService setFrequencyService;
    private SetLedService setLedService;
    private TalkService talkService;


    public CommandNode(IRemoteControlModule remoteControlModule, String roboboName) throws InternalErrorException {

        if(roboboName !=null){
            this.roboboName = roboboName;
        }

        if(remoteControlModule==null){
            throw new InternalErrorException("The parameter remoteControlModule is required");
        }

        this.remoteControlModule= remoteControlModule;

    }


    String getRoboboName() {
        return this.roboboName;
    }


    IRemoteControlModule getRemoteControlModule() {
        return this.remoteControlModule;
    }


    public void onStart(Executor executor) throws NoSuchFieldException, IllegalAccessException {

        this.moveWheelsService = new MoveWheelsService(this);
        executor.addNode(this.moveWheelsService.getBaseComposableNode());
        this.moveWheelsService.start();

        this.movePanTiltService = new MovePanTiltService(this);
        executor.addNode(this.movePanTiltService.getBaseComposableNode());
        this.movePanTiltService.start();

        this.playSoundService = new PlaySoundService(this);
        executor.addNode(this.playSoundService.getBaseComposableNode());
        this.playSoundService.start();

        this.resetWheelsService = new ResetWheelsService(this);
        executor.addNode(this.resetWheelsService.getBaseComposableNode());
        this.resetWheelsService.start();

        this.setCameraService = new SetCameraService(this);
        executor.addNode(this.setCameraService.getBaseComposableNode());
        this.setCameraService.start();

        this.setEmotionService = new SetEmotionService(this);
        executor.addNode(this.setEmotionService.getBaseComposableNode());
        this.setEmotionService.start();

        this.setFrequencyService = new SetFrequencyService(this);
        executor.addNode(this.setFrequencyService.getBaseComposableNode());
        this.setFrequencyService.start();

        this.setLedService = new SetLedService(this);
        executor.addNode(this.setLedService.getBaseComposableNode());
        this.setLedService.start();

        this.talkService = new TalkService(this);
        executor.addNode(this.talkService.getBaseComposableNode());
        this.talkService.start();

    }

}
