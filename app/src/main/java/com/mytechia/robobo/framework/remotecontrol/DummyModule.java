/*******************************************************************************
 *
 *   Copyright 2019, Manufactura de Ingenios Tecnológicos S.L. 
 *   <http://www.mintforpeople.com>
 *
 *   Redistribution, modification and use of this software are permitted under
 *   terms of the Apache 2.0 License.
 *
 *   This software is distributed in the hope that it will be useful,
 *   but WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND; without even the implied
 *   warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   Apache 2.0 License for more details.
 *
 *   You should have received a copy of the Apache 2.0 License along with    
 *   this software. If not, see <http://www.apache.org/licenses/>.
 *
 ******************************************************************************/

 package com.mytechia.robobo.framework.remotecontrol;

import com.mytechia.robobo.framework.remote_control.remotemodule.Command;
import com.mytechia.robobo.framework.remote_control.remotemodule.ICommandExecutor;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;
import com.mytechia.robobo.framework.remote_control.remotemodule.Response;

public class DummyModule implements ICommandExecutor{
    private ITestListener listener;
    @Override
    public void executeCommand(Command c, IRemoteControlModule rcmodule) {
        listener.onThingsHappen(c.getParameters().toString());
        Response r = (c.createResponse());


//        Response r = new Response(1);
        if (c.getName().equals("C1")){
            r.putContents("content","C1");
        }else{
            r.putContents("content","Other");
        }

        rcmodule.postResponse(r);
    }

    public void subscribe(ITestListener listener){
        this.listener =listener;
    }
}
