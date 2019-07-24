package com.mintforpeople.robobo.framework.remotecontrol.ros2.subscribers;

import com.mytechia.robobo.framework.remote_control.remotemodule.Command;

import org.ros2.rcljava.consumers.Consumer;
import org.ros2.rcljava.node.BaseComposableNode;
import org.ros2.rcljava.subscription.Subscription;

import java.util.HashMap;

import robobo_msgs.msg.SetSensorFrequencyTopic;

public class SetFrequencySub {
    private SubNode subNode;
    private BaseComposableNode baseComposableNode;
    private Subscription<SetSensorFrequencyTopic> subscriber;
    private String topicName;

    public SetFrequencySub(SubNode subNode, String topicName) {
        this.subNode = subNode;
        this.topicName = topicName;
        this.baseComposableNode = new BaseComposableNode("SetFrequencySub");
    }

    public BaseComposableNode getBaseComposableNode() {
        return this.baseComposableNode;
    }

    public void start(){
        this.subscriber = this.baseComposableNode.getNode().createSubscription(SetSensorFrequencyTopic.class, topicName, new CallBack());
    }

    public class CallBack implements Consumer<SetSensorFrequencyTopic> {

        @Override
        public void accept(SetSensorFrequencyTopic msg) {
            String freq = "FAST";
            switch(msg.getFrequency().getData()) {
                case 0:
                    freq = "LOW";
                    break;
                case 1:
                    freq = "NORMAL";
                    break;
                case 2:
                    freq = "FAST";
                    break;
                case 3:
                    freq = "MAX";
                    break;
                default:
                    freq = "FAST";
            }

            HashMap<String, String> parameters = new HashMap();
            parameters.put("frequency", freq);
            Command command = new Command("SET-SENSOR-FREQUENCY", 0, parameters);
            SetFrequencySub.this.subNode.getRemoteControlModule().queueCommand(command);
            /*
            Int8 r = response.getError();
            r.setData((byte)0);
            response.setError(r);
            */

        }
    }
}
