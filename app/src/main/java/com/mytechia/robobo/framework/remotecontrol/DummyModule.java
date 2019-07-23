package com.mytechia.robobo.framework.remotecontrol;

import com.mytechia.robobo.framework.remote_control.remotemodule.Command;
import com.mytechia.robobo.framework.remote_control.remotemodule.ICommandExecutor;
import com.mytechia.robobo.framework.remote_control.remotemodule.IRemoteControlModule;
import com.mytechia.robobo.framework.remote_control.remotemodule.Response;

/*******************************************************************************
 * Copyright 2016 Mytech Ingenieria Aplicada <http://www.mytechia.com>
 * Copyright 2016 Luis Llamas <luis.llamas@mytechia.com>
 * <p>
 * This file is part of Robobo Remote Control Module.
 * <p>
 * Robobo Remote Control Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Robobo Remote Control Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with Robobo Remote Control Module.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
