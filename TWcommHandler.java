/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.twcomm;

import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link TWcommHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author indystar1 - Initial contribution
 */
public class TWcommHandler extends BaseThingHandler {

    private static final Logger LOG = LoggerFactory.getLogger(TWcommHandler.class);

    public TWcommHandler(Thing thing) {
        super(thing);
    }

    /**
     * This is arguably the most important function of the ThingHandler class. It
     * receives commands from the framework. Commands such as 'ON', 'OFF', etc.
     * Our binding is then responsible to pass these on to the actual device.
     *
     * In yahooweather binding's case, handleCommand() looks for a Refresh command.
     * It arrives, for example, when you click the "reload arrow" in the PaperUI
     * interface. In response, the binding updates its state (more on this a
     * little bit later) - and sends an event that is propagated to the rest of
     * the system (including UI).
     */
    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        /**
         * TODO: handle command
         * Note: if communication with thing fails for some reason,
         * indicate that by setting the status with detail information
         * updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
         * "Could not control device at IP address x.x.x.x");
         */
        // System.out.println("\nCurrent ClassLoader cahin: " + TWcommHandler.getCurrentClassLoaderDetail());
        String channelID = channelUID.getId().toString();
        String thingID = channelUID.getThingUID().toString();
        LOG.debug("In ThingWorx handleCommand! Channel Id= " + channelID + ", Thing Id= " + thingID + ", Command= "
                + command.toString());

        if (channelID.equals("switch1")) {
            System.err.println("Received Command for switch1 to turn " + command);
        }
        if (channelID.equals("switch2")) {
            System.err.println("Received Command for switch2 to turn " + command);
        }
    }

    public void receiveCommand(String itemName, Command command) {
        LOG.debug("receiveCommand: TWOnOff switch1: Received a command!");
    }

    public void sendCommand(String itemName, Command command) {
        LOG.debug("I am now in ThingWorx sendCommand!");
    }

    /**
     * Will get called shortly after this class is created. It means that the thing
     * has been created in the system and now gets a chance to do whatever
     * initialization tasks it needs to do. Note that it doesn't mean that the
     * Thing is online! Things that are defined in config files and Things that
     * have previously been discovered will go through here. It simply means that
     * the Thing is now part of the system and is ready to receive commands
     * and discovery callbacks (despite it being offline).
     *
     * For yahooweather, there are no online/offline states as it is not really
     * a device. Therefore, the binding starts a polling thread that periodically
     * contacts the weather server over HTTP and updates Thing state based on the results.
     */
    @Override
    public void initialize() {
        // TODO: Initialize the thing. If done set status to ONLINE to indicate proper working.
        // Long running initialization should be done asynchronously in background.

        LOG.debug("Initializing ThingWorx handler!");
        super.initialize();
        // Thing thing = this.getThing();
        // this.refreshInterval = config.getPollingInterval();
        // config.getPassword()
        // this.updateOn();

        updateStatus(ThingStatus.ONLINE);

        /**
         * Note: When initialization can NOT be done, set the status with more details for further
         * analysis. See also class ThingStatusDetail for all available status details.
         * Add a description to give user information to understand why thing does not work
         * as expected. E.g. updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
         * "Can not access device as username and/or password are invalid");
         */
    }

    /**
     * Similarly to above, sends out a status announcement to the framework.
     *
     * In the yahooweather, binding is called on every HTTP request.
     * If successful - update to ONLINE, if failed, update to OFFLINE.
     * Status describes things like whether the Thing is ONLINE or not.
     * It does not describe the “contents” of the Thing, such as a temperature reading,
     * or a relay position in device.
     * UNINITIALIZED(0), INITIALIZING(1), ONLINE(2), OFFLINE(3), REMOVING(4), REMOVED(5)
     */
    public void updateStatus() {

    }

    /**
     * This function is a way to issue commands to the underlying Thing
     * (self or other Things). Note that you shouldn't reach into the depths of
     * the ThingHandler implementation and send, say, an OFF command directly to
     * the device. By going through postCommand the rest of the framework will
     * know what we're up to, and the sending code will get triggered in
     * handleCommand().
     */
    public void postCommand() {

    }

    /**
     * The following functions also communicate data /out/ from the Thing. However,
     * I will only mention them in passing, as we have not talked about
     * Configurations and Properties of Things in this article.
     */
    public void updateThing() {

    }

    public void updateConfiguration() {

    }

    public void updateProperties() {

    }

    public void updateProperty() {

    }
}
