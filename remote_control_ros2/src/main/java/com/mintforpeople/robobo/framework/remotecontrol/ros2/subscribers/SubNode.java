package com.mintforpeople.robobo.framework.remotecontrol.ros2.subscribers;

import com.mytechia.commons.framework.exception.InternalErrorException;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;

import org.ros2.rcljava.executors.Executor;

public class SubNode {

    private IRemoteControlModule remoteControlModule;

    private String roboboName ="";

    private MoveWheelsSub moveWheelsSub;
    private MovePanTiltSub movePanTiltSub;
    private PlaySoundSub playSoundSub;
    private ResetWheelsSub resetWheelsSub;
    private SetCameraSub setCameraSub;
    private SetEmotionSub setEmotionSub;
    private SetFrequencySub setFrequencySub;
    private SetLedSub setLedSub;
    private TalkSub talkSub;

    public SubNode(IRemoteControlModule remoteControlModule, String roboboName) throws InternalErrorException {
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

    public void onStart(Executor executor){

        this.moveWheelsSub = new MoveWheelsSub(this,"move_wheels" );
        executor.addNode(this.moveWheelsSub.getBaseComposableNode());
        this.moveWheelsSub.start();

        this.movePanTiltSub = new MovePanTiltSub(this, "move_pan_tilt");
        executor.addNode(this.movePanTiltSub.getBaseComposableNode());
        this.movePanTiltSub.start();

        this.playSoundSub = new PlaySoundSub(this, "play_sound");
        executor.addNode(this.playSoundSub.getBaseComposableNode());
        this.playSoundSub.start();

        this.resetWheelsSub = new ResetWheelsSub(this, "reset_wheels");
        executor.addNode(this.resetWheelsSub.getBaseComposableNode());
        this.resetWheelsSub.start();

        this.setCameraSub = new SetCameraSub(this, "set_camera");
        executor.addNode(this.setCameraSub.getBaseComposableNode());
        this.setCameraSub.start();

        this.setEmotionSub = new SetEmotionSub(this, "set_emotion");
        executor.addNode(this.setEmotionSub.getBaseComposableNode());
        this.setEmotionSub.start();

        this.setFrequencySub = new SetFrequencySub(this, "set_frequency");
        executor.addNode(this.setFrequencySub.getBaseComposableNode());
        this.setFrequencySub.start();

        this.setLedSub = new SetLedSub(this, "set_led");
        executor.addNode(this.setLedSub.getBaseComposableNode());
        this.setLedSub.start();

        this.talkSub = new TalkSub(this, "talk");
        executor.addNode(this.talkSub.getBaseComposableNode());
        this.talkSub.start();
    }
}
