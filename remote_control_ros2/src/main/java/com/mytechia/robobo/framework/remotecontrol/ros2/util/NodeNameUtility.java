/*******************************************************************************
 *
 *   Copyright 2016 Mytech Ingenieria Aplicada <http://www.mytechia.com>
 *   Copyright 2016 Julio Alberto Gomez Fernandez <julio.gomez@mytechia.com>
 *
 *   This file is part of Robobo Remote Control Module.
 *
 *   Robobo Remote Control Module is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Robobo Remote Control Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Robobo Remote Control Module.  If not, see <http://www.gnu.org/licenses/>.
 *
 ******************************************************************************/

//package com.mytechia.robobo.framework.remotecontrol.ros.util;
package com.mytechia.robobo.framework.remotecontrol.ros2.util;


/**
 * //TODO Rellename!
 * @author Julio Alberto Gomez Fernandez
 */
public class NodeNameUtility {

    private static String prefix = "robot";

    public static String createNodeName(String nameRobobo, String nodeName) {

        if ((nameRobobo == null) || (nameRobobo.isEmpty())) {
            return String.format("%s/%s", prefix, nodeName);
        } else {
            return String.format("%s/%s/%s", prefix, nameRobobo, nodeName);
        }

    }

    public static String createNodeAction(String nameRobobo, String nameAction) {

        if ((nameRobobo == null) || (nameRobobo.isEmpty())) {
            return String.format("%s/%s", prefix, nameAction);
        } else {
            return String.format("%s/%s/%s", prefix, nameRobobo, nameAction);
        }

    }

}
