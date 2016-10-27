/**
 * Copyright (c) 2016 Indy Star
 * This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0
 * This program has been modified by GitHub user id: indystar1
 *
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * FYI, http://kacangbawang.com/openhab2-binding-architecture/
 * OpenHAB will look for a ThingHandlerFactory in our binding bundle.
 * This is what it will use to instantiate ThingHandler’s.
 * Only the ThingHandlerFactory needs to be exposed to OpenHAB.
 */
package org.openhab.binding.twcomm;

import static org.openhab.binding.twcomm.TWcommBindingConstants.THING_TYPE_TW_COMM;

import java.util.Collections;
import java.util.Set;

import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.events.EventFilter;
import org.eclipse.smarthome.core.events.EventPublisher;
import org.eclipse.smarthome.core.events.EventSubscriber;
import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.items.ItemNotFoundException;
import org.eclipse.smarthome.core.items.ItemRegistry;
import org.eclipse.smarthome.core.items.events.ItemEventFactory;
import org.eclipse.smarthome.core.items.events.ItemStateEvent;
import org.eclipse.smarthome.core.library.items.RollershutterItem;
import org.eclipse.smarthome.core.library.items.SwitchItem;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.UpDownType;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.TypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link TWcommHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author indystar1 - Initial contribution
 */
public class TWcommHandlerFactory extends BaseThingHandlerFactory implements EventSubscriber {

    private final static Logger LOG = LoggerFactory.getLogger(TWcommHandlerFactory.class);
    protected static ItemRegistry itemRegistry = null;
    protected static EventPublisher eventPublisher = null;
    private boolean update_flag;
    static double homeTemperature = 0;
    static double old_homeTemperature = 0;
    static double homeHumidity = 0;
    static double old_homeHumidity = 0;
    static double baseTemperature = 0;
    static double old_baseTemperature = 0;
    static double baseHumidity = 0;
    static double old_baseHumidity = 0;
    static boolean yunAlive = false;
    static boolean old_yunAlive = false;
    static boolean dehumidState = false;
    static boolean old_dehumidState = false;
    static boolean irriStatus = false;
    static boolean old_irriStatus = false;
    static boolean frontMotionState = false;
    static boolean old_frontMotionState = false;
    static boolean cam1OnlineState = false;
    static boolean old_cam1OnlineState = false;
    static boolean cam2OnlineState = false;
    static boolean old_cam2OnlineState = false;
    static String old_equipStatus = "";
    static int hvacOnOffInteger = 0;
    static int waterSoftenerState = 0;
    static int old_waterSoftenerState = 0;
    static int WS_BrineDepth = 0;
    static int old_WS_BrineDepth = 0;

    private final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections.singleton(THING_TYPE_TW_COMM);
    // private static ThingworxRestInterface twri = new ThingworxRestInterface();

    static {
        new BaseEdgeServer();
    }

    /*
     * @see
     * org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory#supportsThingType(org.eclipse.smarthome.core.thing.
     * ThingTypeUID)
     *
     * Returns a list of supported types. Used for checking whether this factory
     * is able to instantiate a particular Thing type
     */
    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    // This is the main factory method which returns new instances of ThingHandler
    @Override
    protected ThingHandler createHandler(Thing thing) {

        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(THING_TYPE_TW_COMM)) {
            LOG.debug("A New ThingWorx handler Created!");
            return new TWcommHandler(thing);
        }

        return null;
    }

    public void setItemRegistry(ItemRegistry itemRegistry) {
        TWcommHandlerFactory.itemRegistry = itemRegistry;
    }

    public void unsetItemRegistry(ItemRegistry itemRegistry) {
        TWcommHandlerFactory.itemRegistry = null;
    }

    public void setEventPublisher(EventPublisher eventPublisher) {
        TWcommHandlerFactory.eventPublisher = eventPublisher;
    }

    public void unsetEventPublisher(EventPublisher eventPublisher) {
        TWcommHandlerFactory.eventPublisher = null;
    }

    @Override
    public Set<String> getSubscribedEventTypes() {
        return Collections.singleton(ItemStateEvent.TYPE);
    }

    @Override
    public EventFilter getEventFilter() {
        return null;
    }

    public static void sendCommand(String itemName, String commandString) {
        try {
            if (itemRegistry != null) {
                Item item = itemRegistry.getItem(itemName);
                Command command = null;
                if (item != null) {
                    if (eventPublisher != null) {
                        if ("toggle".equalsIgnoreCase(commandString)
                                && (item instanceof SwitchItem || item instanceof RollershutterItem)) {
                            if (OnOffType.ON.equals(item.getStateAs(OnOffType.class))) {
                                command = OnOffType.OFF;
                            }
                            if (OnOffType.OFF.equals(item.getStateAs(OnOffType.class))) {
                                command = OnOffType.ON;
                            }
                            if (UpDownType.UP.equals(item.getStateAs(UpDownType.class))) {
                                command = UpDownType.DOWN;
                            }
                            if (UpDownType.DOWN.equals(item.getStateAs(UpDownType.class))) {
                                command = UpDownType.UP;
                            }
                        } else {
                            command = TypeParser.parseCommand(item.getAcceptedCommandTypes(), commandString);
                        }
                        if (command != null) {
                            LOG.debug("Received command '{}' for item '{}'", commandString, itemName);
                            eventPublisher.post(ItemEventFactory.createCommandEvent(itemName, command));
                        } else {
                            LOG.warn("Received invalid command '{}' for item '{}'", commandString, itemName);
                        }
                    }
                } else {
                    LOG.warn("Received command '{}' for non-existent item '{}'", commandString, itemName);
                }
            } else {
                return;
            }
        } catch (ItemNotFoundException e) {
            LOG.warn("Received TW command for a non-existent item '{}'", itemName);
        }
    }

    @Override
    public void receive(Event event) {
        ItemStateEvent ise = (ItemStateEvent) event;
        String itemName = ise.getItemName();
        String itemState = ise.getItemState().toString();
        update_flag = false;
        if (itemName.equals("HomeTemperature")) {
            homeTemperature = Double.parseDouble(itemState);
            if (homeTemperature != old_homeTemperature) {
                update_flag = true;
                old_homeTemperature = homeTemperature;
            }
        } else if (itemName.equals("HomeHumidity")) {
            homeHumidity = Double.parseDouble(itemState);
            if (homeHumidity != old_homeHumidity) {
                update_flag = true;
                old_homeHumidity = homeHumidity;
            }
        }
        if (itemName.equals("BaseTemperature")) {
            baseTemperature = Double.parseDouble(itemState);
            if (baseTemperature != old_baseTemperature) {
                update_flag = true;
                old_baseTemperature = baseTemperature;
            }
        } else if (itemName.equals("BaseHumidity")) {
            baseHumidity = Double.parseDouble(itemState);
            if (baseHumidity != old_baseHumidity) {
                update_flag = true;
                old_baseHumidity = baseHumidity;
            }
        } else if (itemName.equals("YunAlive")) {
            yunAlive = (Integer.parseInt(itemState) == 1) ? true : false;
            if (yunAlive != old_yunAlive) {
                update_flag = true;
                old_yunAlive = yunAlive;
            }
        } else if (itemName.equals("Dehumidifier")) {
            dehumidState = (Integer.parseInt(itemState) == 1) ? true : false;
            if (dehumidState != old_dehumidState) {
                update_flag = true;
                old_dehumidState = dehumidState;
            }
        } else if (itemName.equals("IrriStatus")) {
            irriStatus = (Integer.parseInt(itemState) == 1) ? true : false;
            if (irriStatus != old_irriStatus) {
                update_flag = true;
                old_irriStatus = irriStatus;
            }
        } else if (itemName.equals("FrontMotion")) {
            frontMotionState = (Integer.parseInt(itemState) == 1) ? true : false;
            if (frontMotionState != old_frontMotionState) {
                update_flag = true;
                old_frontMotionState = frontMotionState;
            }
        } else if (itemName.equals("Cam1Status")) {
            cam1OnlineState = (Integer.parseInt(itemState) == 1) ? true : false;
            if (cam1OnlineState != old_cam1OnlineState) {
                update_flag = true;
                old_cam1OnlineState = cam1OnlineState;
            }
        } else if (itemName.equals("Cam2Status")) {
            cam2OnlineState = (Integer.parseInt(itemState) == 1) ? true : false;
            if (cam2OnlineState != old_cam2OnlineState) {
                update_flag = true;
                old_cam2OnlineState = cam2OnlineState;
            }
        } else if (itemName.equals("equipmentStatus")) {
            if (!old_equipStatus.equals(itemState)) {
                update_flag = true;
                old_equipStatus = new String(itemState);
                if (old_equipStatus.equals("")) {
                    System.err.println("No HVAC equipment is running!");
                    hvacOnOffInteger = 0;
                } else {
                    System.err.println("The following HVAC equipment is currently running: " + old_equipStatus);
                    hvacOnOffInteger = 1;
                }
            }
        } else if (itemName.equals("WaterSoftenerState")) {
            waterSoftenerState = Integer.parseInt(itemState);
            if (waterSoftenerState != old_waterSoftenerState) {
                update_flag = true;
                old_waterSoftenerState = waterSoftenerState;
            }
        } else if (itemName.equals("WS_brineDepth")) {
            WS_BrineDepth = Integer.parseInt(itemState);
            if (WS_BrineDepth != old_WS_BrineDepth) {
                update_flag = true;
                old_WS_BrineDepth = WS_BrineDepth;
            }
        }

        if (update_flag == true) {
            // LOG.debug("Sending to ThingWorx update '{}' for item '{}'", itemState, itemName);
            try {
                BaseEdgeServer.manualSending();
                // twri.RestRequest("JLeeOHThings");
            } catch (Exception e) {
                System.err.println("Sending to ThingWorx failed: " + e.getMessage());
            }
        }
    }

    public static double getHomeTemp() {
        return homeTemperature;
    }

    public static double getHomeHumid() {
        return homeHumidity;
    }

    public static double getBaseTemp() {
        return baseTemperature;
    }

    public static double getBaseHumid() {
        return baseHumidity;
    }

    public static boolean getYunAlive() {
        return yunAlive;
    }

    public static boolean getBaseDehumid() {
        return dehumidState;
    }

    public static boolean getFrontMotionState() {
        return frontMotionState;
    }

    public static boolean getCam1OnlineState() {
        return cam1OnlineState;
    }

    public static boolean getCam2OnlineState() {
        return cam2OnlineState;
    }

    public static String getHvacState() {
        return old_equipStatus;
    }

    public static int getHvacOnOffState() {
        return hvacOnOffInteger;
    }

    public static boolean getSprinkState() {
        return irriStatus;
    }

    public static int getWS_State() {
        return waterSoftenerState;
    }

    public static int getWS_BrineDepth() {
        return WS_BrineDepth;
    }
}
