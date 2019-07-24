package com.mintforpeople.robobo.framework.remotecontrol.ros2.subscribers;


import android.util.Log;

import com.mytechia.robobo.framework.remote_control.remotemodule.Command;

import org.ros2.rcljava.node.BaseComposableNode;
import org.ros2.rcljava.subscription.Subscription;

import org.ros2.rcljava.consumers.Consumer;

import java.util.HashMap;

import robobo_msgs.msg.MovePanTiltTopic;

public class MovePanTiltSub {

    private SubNode subNode;
    private BaseComposableNode baseComposableNode;
    private Subscription<MovePanTiltTopic> subscriber;
    private String topicName;


    public MovePanTiltSub(SubNode subNode, String topicName) {
        this.subNode = subNode;
        this.topicName = topicName;
        this.baseComposableNode = new BaseComposableNode("MovePanTiltSub");
    }

    public BaseComposableNode getBaseComposableNode() {
        return this.baseComposableNode;
    }

    public void start(){
        this.subscriber = this.baseComposableNode.getNode().createSubscription(MovePanTiltTopic.class, topicName, new CallBack());
    }

    public class CallBack implements Consumer<MovePanTiltTopic> {

        @Override
        public void accept(MovePanTiltTopic msg) {
            HashMap<String, String> panParams = new HashMap();
            panParams.put("pos", String.valueOf(msg.getPanpos().getData()));
            panParams.put("speed", String.valueOf(msg.getPanspeed().getData()));
            int panId = msg.getPanunlockid().getData();
            panParams.put("blockid", String.valueOf(panId));
            Log.i("MOVE-PT", "MovePanMsg: " + (String)panParams.get("pos") + " - " + (String)panParams.get("speed"));
            Command panCommand = new Command("MOVEPAN-BLOCKING", panId, panParams);
            HashMap<String, String> tiltParams = new HashMap();
            tiltParams.put("pos", String.valueOf(msg.getTiltpos().getData()));
            tiltParams.put("speed", String.valueOf(msg.getTiltspeed().getData()));
            int tiltId = msg.getTiltunlockid().getData();
            tiltParams.put("blockid", String.valueOf(tiltId));
            Log.i("MOVE-PT", "MovePanMsg: " + (String)tiltParams.get("pos") + " - " + (String)tiltParams.get("speed"));
            Command tiltCommand = new Command("MOVETILT-BLOCKING", tiltId, tiltParams);
            if (panId > 0) {
                MovePanTiltSub.this.subNode.getRemoteControlModule().queueCommand(panCommand);
            }

            if (tiltId > 0) {
                MovePanTiltSub.this.subNode.getRemoteControlModule().queueCommand(tiltCommand);
            }

            /*
            Int8 r = response.getError();
            r.setData((byte)0);
            response.setError(r);
            */

        }
    }
}
