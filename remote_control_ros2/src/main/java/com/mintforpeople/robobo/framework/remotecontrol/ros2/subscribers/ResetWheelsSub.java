package com.mintforpeople.robobo.framework.remotecontrol.ros2.subscribers;

import com.mytechia.robobo.framework.remote_control.remotemodule.Command;

import org.ros2.rcljava.consumers.Consumer;
import org.ros2.rcljava.node.BaseComposableNode;
import org.ros2.rcljava.subscription.Subscription;

import java.util.HashMap;

import robobo_msgs.msg.ResetWheelsTopic;

public class ResetWheelsSub {
    private SubNode subNode;
    private BaseComposableNode baseComposableNode;
    private Subscription<ResetWheelsTopic> subscriber;
    private String topicName;

    public ResetWheelsSub(SubNode subNode, String topicName) {
        this.subNode = subNode;
        this.topicName = topicName;
        this.baseComposableNode = new BaseComposableNode("ResetWheelsSub");
    }

    public BaseComposableNode getBaseComposableNode() {
        return this.baseComposableNode;
    }

    public void start(){
        this.subscriber = this.baseComposableNode.getNode().createSubscription(ResetWheelsTopic.class, topicName, new CallBack());
    }

    public class CallBack implements Consumer<ResetWheelsTopic> {

        @Override
        public void accept(ResetWheelsTopic msg) {
            Command command = new Command("RESET-WHEELS", 0, (HashMap)null);
            ResetWheelsSub.this.subNode.getRemoteControlModule().queueCommand(command);
            /*
            Int8 r = response.getError();
            r.setData((byte)0);
            response.setError(r);
            */

        }
    }
}
