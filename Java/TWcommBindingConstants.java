/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.twcomm;

import java.util.Collections;
import java.util.Set;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

//import com.google.common.collect.ImmutableSet;

/**
 * The {@link TWcommBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author indystar1 - Initial contribution
 */
public class TWcommBindingConstants {

    public static final String BINDING_ID = "twcomm";

    // List of all Thing Type UIDs
    public final static ThingTypeUID THING_TYPE_TW_COMM = new ThingTypeUID(BINDING_ID, "tw_comm");

    // List of all Channel ids
    public final static String CHANNEL_1 = "switch1";
    public final static String CHANNEL_2 = "switch2";

    // public final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = ImmutableSet.of(THING_TYPE_TW_COMM);
    public final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections.singleton(THING_TYPE_TW_COMM);
}
