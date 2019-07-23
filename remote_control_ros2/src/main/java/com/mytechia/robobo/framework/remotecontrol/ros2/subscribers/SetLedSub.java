package com.mytechia.robobo.framework.remotecontrol.ros2.subscribers;

import com.mytechia.robobo.framework.remote_control.remotemodule.Command;

import org.ros2.rcljava.consumers.Consumer;
import org.ros2.rcljava.node.BaseComposableNode;
import org.ros2.rcljava.subscription.Subscription;

import java.util.HashMap;
import robobo_msgs.msg.SetLedTopic;
import std_msgs.msg.Int8;

public class SetLedSub {
    private SubNode subNode;
    private BaseComposableNode baseComposableNode;
    private Subscription<SetLedTopic> subscriber;
    private String topicName;

    public SetLedSub(SubNode subNode, String topicName) {
        this.subNode = subNode;
        this.topicName = topicName;
        this.baseComposableNode = new BaseComposableNode("SetLedSub");
    }

    public BaseComposableNode getBaseComposableNode() {
        return this.baseComposableNode;
    }

    public void start() {
        this.subscriber = this.baseComposableNode.getNode().createSubscription(SetLedTopic.class, topicName, new CallBack());
    }

    public class CallBack implements Consumer<SetLedTopic> {

        @Override
        public void accept(SetLedTopic msg) {
            HashMap<String, String> parameters = new HashMap();
            parameters.put("led", msg.getId().getData());
            parameters.put("color", msg.getColor().getData());
            Command command = new Command("SET-LEDCOLOR", 0, parameters);
            SetLedSub.this.subNode.getRemoteControlModule().queueCommand(command);
            /*
            Int8 r = response.getError();
            r.setData((byte)0);
            response.setError(r);
            */

        }
    }
}
