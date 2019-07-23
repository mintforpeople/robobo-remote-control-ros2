package com.mytechia.robobo.framework.remotecontrol.ros2.subscribers;

import com.mytechia.robobo.framework.remote_control.remotemodule.Command;

import org.ros2.rcljava.consumers.Consumer;
import org.ros2.rcljava.node.BaseComposableNode;
import org.ros2.rcljava.subscription.Subscription;

import java.util.HashMap;

import robobo_msgs.msg.MoveWheelsTopic;


public class MoveWheelsSub {

    private SubNode subNode;
    private BaseComposableNode baseComposableNode;
    private Subscription<MoveWheelsTopic> subscriber;
    private String topicName;

    public MoveWheelsSub(SubNode subNode, String topicName) {
        this.subNode = subNode;
        this.topicName = topicName;
        this.baseComposableNode = new BaseComposableNode("MoveWheelsSub");
    }

    public BaseComposableNode getBaseComposableNode() {
        return this.baseComposableNode;
    }

    public void start(){
        this.subscriber = this.baseComposableNode.getNode().createSubscription(MoveWheelsTopic.class, topicName, new CallBack());
    }

    public class CallBack implements Consumer<MoveWheelsTopic> {

        @Override
        public void accept(MoveWheelsTopic msg) {
            HashMap<String, String> parameters = new HashMap();
            parameters.put("lspeed", String.valueOf(msg.getLspeed().getData()));
            parameters.put("rspeed", String.valueOf(msg.getRspeed().getData()));
            int time = msg.getTime().getData();
            parameters.put("time", String.valueOf((double)time / 1000.0D));
            int id = msg.getUnlockid().getData();
            parameters.put("blockid", String.valueOf(id));
            Command command = new Command("MOVE-BLOCKING", id, parameters);
            MoveWheelsSub.this.subNode.getRemoteControlModule().queueCommand(command);
            /*
            Int8 r = response.getError();
            r.setData((byte)0);
            response.setError(r);
            */

        }
    }
}
