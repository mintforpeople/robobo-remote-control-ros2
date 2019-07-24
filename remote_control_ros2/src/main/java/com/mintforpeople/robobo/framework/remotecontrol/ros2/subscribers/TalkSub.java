package com.mintforpeople.robobo.framework.remotecontrol.ros2.subscribers;

import com.mytechia.robobo.framework.remote_control.remotemodule.Command;

import org.ros2.rcljava.consumers.Consumer;
import org.ros2.rcljava.node.BaseComposableNode;
import org.ros2.rcljava.subscription.Subscription;

import java.util.HashMap;

import robobo_msgs.msg.TalkTopic;

public class TalkSub {
    private SubNode subNode;
    private BaseComposableNode baseComposableNode;
    private Subscription<TalkTopic> subscriber;
    private String topicName;

    public TalkSub(SubNode subNode, String topicName) {
        this.subNode = subNode;
        this.topicName = topicName;
        this.baseComposableNode = new BaseComposableNode("TalkSub");
    }

    public BaseComposableNode getBaseComposableNode() {
        return this.baseComposableNode;
    }

    public void start(){
        this.baseComposableNode.getNode().createSubscription(TalkTopic.class, topicName, new CallBack());
    }

    public class CallBack implements Consumer<TalkTopic> {

        @Override
        public void accept(TalkTopic msg) {
            HashMap<String, String> parameters = new HashMap();
            parameters.put("text", msg.getText().getData());
            Command command = new Command("TALK", 0, parameters);
            TalkSub.this.subNode.getRemoteControlModule().queueCommand(command);

            /*
            Int8 r = response.getError();
            r.setData((byte)0);
            response.setError(r);
            */

        }
    }
}
