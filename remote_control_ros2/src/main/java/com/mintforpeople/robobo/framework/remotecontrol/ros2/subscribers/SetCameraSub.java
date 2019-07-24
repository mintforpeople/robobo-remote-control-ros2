package com.mintforpeople.robobo.framework.remotecontrol.ros2.subscribers;

import com.mytechia.robobo.framework.remote_control.remotemodule.Command;

import org.ros2.rcljava.consumers.Consumer;
import org.ros2.rcljava.node.BaseComposableNode;
import org.ros2.rcljava.subscription.Subscription;

import java.util.HashMap;

import robobo_msgs.msg.SetCameraTopic;


public class SetCameraSub {
    private SubNode subNode;
    private BaseComposableNode baseComposableNode;
    private Subscription<SetCameraTopic> subscriber;
    private String topicName;

    public SetCameraSub(SubNode subNode, String topicName) {
        this.subNode = subNode;
        this.topicName = topicName;
        this.baseComposableNode = new BaseComposableNode("SetCameraSub");
    }

    public BaseComposableNode getBaseComposableNode() {
        return this.baseComposableNode;
    }

    public void start() {
        this.subscriber = this.baseComposableNode.getNode().createSubscription(SetCameraTopic.class, topicName, new CallBack());
    }

    public class CallBack implements Consumer<SetCameraTopic> {

        @Override
        public void accept(SetCameraTopic msg) {
            String camera = "front";
            if (msg.getCamera().getData() == 1) {
                camera = "back";
            }

            HashMap<String, String> parameters = new HashMap();
            parameters.put("camera", camera);
            Command command = new Command("SET-CAMERA", 0, parameters);
            SetCameraSub.this.subNode.getRemoteControlModule().queueCommand(command);

            /*
            Int8 r = response.getError();
            r.setData((byte)0);
            response.setError(r);
            */

        }
    }
}
