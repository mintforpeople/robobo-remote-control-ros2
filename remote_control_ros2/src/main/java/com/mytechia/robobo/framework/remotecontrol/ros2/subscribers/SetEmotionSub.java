package com.mytechia.robobo.framework.remotecontrol.ros2.subscribers;

import com.mytechia.robobo.framework.remote_control.remotemodule.Command;

import org.ros2.rcljava.consumers.Consumer;
import org.ros2.rcljava.node.BaseComposableNode;
import org.ros2.rcljava.subscription.Subscription;

import java.util.HashMap;

import robobo_msgs.msg.SetEmotionTopic;
import std_msgs.msg.Int8;

public class SetEmotionSub {
    private SubNode subNode;
    private BaseComposableNode baseComposableNode;
    private Subscription<SetEmotionTopic> subscriber;
    private String topicName;

    public SetEmotionSub(SubNode subNode, String topicName) {
        this.subNode = subNode;
        this.topicName = topicName;
        this.baseComposableNode = new BaseComposableNode("SetEmotionSub");
    }

    public BaseComposableNode getBaseComposableNode() {
        return this.baseComposableNode;
    }

    public void start(){
        this.subscriber = this.baseComposableNode.getNode().createSubscription(SetEmotionTopic.class, topicName, new CallBack());
    }

    public class CallBack implements Consumer<SetEmotionTopic> {

        @Override
        public void accept(SetEmotionTopic msg) {
            HashMap<String, String> parameters = new HashMap();
            parameters.put("emotion", msg.getEmotion().getData());
            Command command = new Command("SET-EMOTION", 0, parameters);
            SetEmotionSub.this.subNode.getRemoteControlModule().queueCommand(command);
            /*
            Int8 r = response.getError();
            r.setData((byte)0);
            response.setError(r);
            */
        }
    }
}
