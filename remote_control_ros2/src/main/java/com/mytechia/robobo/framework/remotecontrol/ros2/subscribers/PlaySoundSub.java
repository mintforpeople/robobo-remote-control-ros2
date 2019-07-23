package com.mytechia.robobo.framework.remotecontrol.ros2.subscribers;

import com.mytechia.robobo.framework.remote_control.remotemodule.Command;

import org.ros2.rcljava.consumers.Consumer;
import org.ros2.rcljava.node.BaseComposableNode;
import org.ros2.rcljava.subscription.Subscription;

import java.util.HashMap;

import robobo_msgs.msg.PlaySoundTopic;


public class PlaySoundSub {
    private SubNode subNode;
    private BaseComposableNode baseComposableNode;
    private Subscription<PlaySoundTopic> subscriber;
    private String topicName;

    public PlaySoundSub(SubNode subNode, String topicName) {
        this.subNode = subNode;
        this.topicName = topicName;
        this.baseComposableNode = new BaseComposableNode("PlaySoundSub");
    }

    public BaseComposableNode getBaseComposableNode() {
        return this.baseComposableNode;
    }

    public void start() {
        this.subscriber = this.baseComposableNode.getNode().createSubscription(PlaySoundTopic.class, topicName, new CallBack());
    }

    public class CallBack implements Consumer<PlaySoundTopic> {

        @Override
        public void accept(PlaySoundTopic msg) {
            HashMap<String, String> parameters = new HashMap();
            parameters.put("sound", msg.getSound().getData());
            Command command = new Command("PLAY-SOUND", 0, parameters);
            PlaySoundSub.this.subNode.getRemoteControlModule().queueCommand(command);
            /*
            Int8 r = response.getError();
            r.setData((byte)0);
            response.setError(r);
            */

        }
    }
}
