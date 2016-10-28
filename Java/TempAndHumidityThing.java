/**
 * Copyright (c) 2016 Indy Star
 * This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * This program has been modified by GitHub user id: indystar1
 */
package org.openhab.binding.twcomm;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.metadata.FieldDefinition;
import com.thingworx.metadata.annotations.ThingworxEventDefinition;
import com.thingworx.metadata.annotations.ThingworxEventDefinitions;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinition;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinitions;
import com.thingworx.metadata.annotations.ThingworxServiceDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceParameter;
import com.thingworx.metadata.annotations.ThingworxServiceResult;
import com.thingworx.metadata.collections.FieldDefinitionCollection;
import com.thingworx.types.BaseTypes;
import com.thingworx.types.collections.ValueCollection;
import com.thingworx.types.constants.CommonPropertyNames;
import com.thingworx.types.primitives.StringPrimitive;

/**
 * Copyright (c) 2015 PTC Inc.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

// Event Definitions
@ThingworxEventDefinitions(events = {
        @ThingworxEventDefinition(name = "SpecialEvent", description = "Special Test Event", dataShape = "OHDataShape.Event", category = "Faults", isInvocable = true, isPropertyEvent = false) })

@ThingworxPropertyDefinitions(properties = {
        @ThingworxPropertyDefinition(name = "EventStatus", description = "Event status", baseType = "BOOLEAN", category = "Faults", aspects = {
                "isReadOnly:true" }),
        @ThingworxPropertyDefinition(name = "EventLimit", description = "Some fault limit", baseType = "INTEGER", category = "Faults", aspects = {
                "isReadOnly:false" }),

        @ThingworxPropertyDefinition(name = "Front_Motion", description = "Front Motion detected from OpenHAB.", baseType = "BOOLEAN", aspects = {
                "dataChangeType:ALWAYS", "dataChangeThreshold:0", "cacheTime:0", "isPersistent:FALSE",
                "isReadOnly:TRUE", "pushType:VALUE", "defaultValue:0" }),
        @ThingworxPropertyDefinition(name = "Cam1_Online", description = "Camera 1 is online from OpenHAB.", baseType = "BOOLEAN", aspects = {
                "dataChangeType:ALWAYS", "dataChangeThreshold:0", "cacheTime:0", "isPersistent:TURE", "isReadOnly:TRUE",
                "pushType:VALUE", "defaultValue:0" }),
        @ThingworxPropertyDefinition(name = "Cam2_Online", description = "Camera 2 is online from OpenHAB.", baseType = "BOOLEAN", aspects = {
                "dataChangeType:ALWAYS", "dataChangeThreshold:0", "cacheTime:0", "isPersistent:TRUE", "isReadOnly:TRUE",
                "pushType:VALUE", "defaultValue:0" }),
        @ThingworxPropertyDefinition(name = "WS_BrineDepth", description = "The Water Softener Brine Depth from OpenHAB.", baseType = "INTEGER", aspects = {
                "dataChangeType:ALWAYS", "dataChangeThreshold:0", "cacheTime:0", "isPersistent:FALSE",
                "isReadOnly:TRUE", "pushType:VALUE", "defaultValue:0" }),
        @ThingworxPropertyDefinition(name = "WaterSoftener_State", description = "The Water Softener State from OpenHAB.", baseType = "INTEGER", aspects = {
                "dataChangeType:ALWAYS", "dataChangeThreshold:0", "cacheTime:0", "isPersistent:FALSE",
                "isReadOnly:TRUE", "pushType:VALUE", "defaultValue:0" }),
        @ThingworxPropertyDefinition(name = "HVAC_OnOff_State", description = "The HVAC On/Off State from OpenHAB.", baseType = "INTEGER", aspects = {
                "dataChangeType:ALWAYS", "dataChangeThreshold:0", "cacheTime:0", "isPersistent:FALSE",
                "isReadOnly:TRUE", "pushType:VALUE", "defaultValue:0" }),
        @ThingworxPropertyDefinition(name = "Sprinkler_State", description = "The sprinkler state from OpenHAB.", baseType = "BOOLEAN", aspects = {
                "dataChangeType:ALWAYS", "dataChangeThreshold:0", "cacheTime:0", "isPersistent:FALSE",
                "isReadOnly:TRUE", "pushType:VALUE", "defaultValue:0" }),
        @ThingworxPropertyDefinition(name = "HVAC_State", description = "The HVAC state from OpenHAB.", baseType = "STRING", aspects = {
                "dataChangeType:ALWAYS", "dataChangeThreshold:0", "cacheTime:0", "isPersistent:FALSE",
                "isReadOnly:TRUE", "pushType:VALUE", "defaultValue:0" }),
        @ThingworxPropertyDefinition(name = "Base_Dehumid", description = "The dehumidifier state from OpenHAB.", baseType = "BOOLEAN", aspects = {
                "dataChangeType:ALWAYS", "dataChangeThreshold:0", "cacheTime:0", "isPersistent:TRUE", "isReadOnly:TRUE",
                "pushType:VALUE", "defaultValue:0" }),
        @ThingworxPropertyDefinition(name = "Prop_YunAlive", description = "The YunAlive state from OpenHAB.", baseType = "BOOLEAN", aspects = {
                "dataChangeType:ALWAYS", "dataChangeThreshold:0", "cacheTime:0", "isPersistent:FALSE",
                "isReadOnly:TRUE", "pushType:VALUE", "defaultValue:0" }),
        @ThingworxPropertyDefinition(name = "Base_Humidity", description = "The basement humidity value from OpenHAB.", baseType = "NUMBER", aspects = {
                "dataChangeType:ALWAYS", "dataChangeThreshold:0", "cacheTime:0", "isPersistent:FALSE",
                "isReadOnly:TRUE", "pushType:VALUE", "defaultValue:0" }),
        @ThingworxPropertyDefinition(name = "Base_Temperature", description = "The basement temperature value from OpenHAB.", baseType = "NUMBER", aspects = {
                "dataChangeType:ALWAYS", "dataChangeThreshold:0", "cacheTime:0", "isPersistent:FALSE",
                "isReadOnly:TRUE", "pushType:VALUE", "defaultValue:0" }),
        @ThingworxPropertyDefinition(name = "Prop_Humidity", description = "The home humidity value from OpenHAB.", baseType = "NUMBER", aspects = {
                "dataChangeType:ALWAYS", "dataChangeThreshold:0", "cacheTime:0", "isPersistent:FALSE",
                "isReadOnly:TRUE", "pushType:VALUE", "defaultValue:0" }),
        @ThingworxPropertyDefinition(name = "Prop_Temperature", description = "The home temperature value from OpenHAB.", baseType = "NUMBER", aspects = {
                "dataChangeType:ALWAYS", "dataChangeThreshold:0", "cacheTime:0", "isPersistent:FALSE",
                "isReadOnly:TRUE", "pushType:VALUE", "defaultValue:0" }), })

/**
 * This remote (Virtual) thing is responsible for obtaining values for the properties described in the annotations
 * above. This particular class interfaces with the AM2302 Humidity and Temperature Sensor
 *
 * There are many different ways to collect data as well as deliver it to Thingworx. This is intended as a simple,
 * flexible example than should work in almost any environment.
 *
 * This example can operate without the presence of the AM2302 hardware if the simulated parameter is provided
 * when the class is constructed.
 */
public class TempAndHumidityThing extends VirtualThing {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(TempAndHumidityThing.class);

    // private final String name;
    // private final String description;
    // private final String simulated;
    // private final ConnectedThingClient client;

    // This is a constructor called by BaseEdgeServer(), which is called by TWcommHandler upon initialization

    public TempAndHumidityThing(String name, String description, ConnectedThingClient client, String simulated) {
        super(name, description, client);
        // this.name = name;
        // this.description = description;
        // this.client = client;
        // this.simulated = simulated;

        // Data Shape definition that is used by the steam sensor fault event
        // The event only has one field, the message
        FieldDefinitionCollection faultFields = new FieldDefinitionCollection();
        faultFields.addFieldDefinition(new FieldDefinition(CommonPropertyNames.PROP_MESSAGE, BaseTypes.STRING));
        defineDataShapeDefinition("OHDataShape.Event", faultFields);

        // Populate the thing shape with the properties, services, and events that are annotated in this code
        initializeFromAnnotations();
        try {
            setDefaultPropertyValue("Prop_Temperature");
            setDefaultPropertyValue("Prop_Humidity");
            setDefaultPropertyValue("Prop_YunAlive");
            setDefaultPropertyValue("Base_Temperature");
            setDefaultPropertyValue("Base_Humidity");
            setDefaultPropertyValue("Base_Dehumid");
            setDefaultPropertyValue("HVAC_State");
            setDefaultPropertyValue("HVAC_OnOff_State");
            setDefaultPropertyValue("Sprinkler_State");
            setDefaultPropertyValue("WaterSoftener_State");
            setDefaultPropertyValue("WS_BrineDepth");
            setDefaultPropertyValue("Front_Motion");
            setDefaultPropertyValue("Cam1_Online");
            setDefaultPropertyValue("Cam2_Online");
        } catch (Exception localException) {
            LOG.error("Failed to set default value.", localException);
        }
    }

    /**
     * This is called from BaseEdgeServer.manualSending(), which is called from TWcommHandlerFactory.receive().
     *
     * @throws Exception
     */
    @Override
    public void processScanRequest() throws Exception { // called from BaseEdgeServer.manualSending()
        super.processScanRequest();

        double homeTemp = getHomeTemperature();
        double homeHumid = getHomeHumidity();
        boolean currentYunAlive = getYunAlive();
        double baseTemp = getBaseTemperature();
        double baseHumid = getBaseHumidity();
        boolean baseDehumid = getBaseDehumidity();
        boolean frontMotion = getFrontMotion();
        boolean cam1_Online = getCam1_Online();
        boolean cam2_Online = getCam2_Online();
        String hvacState = getHVAC_State();
        int hvacOnOffState = getHVAC_OnOff_State();
        boolean sprinklerState = getSprinklerState();
        int wsState = getWaterSoftener_State();
        int wsBrineDepth = getWS_brineDepth();

        LOG.debug("Prop_Temperature=" + homeTemp);
        LOG.debug("Prop_Humidity=" + homeHumid);
        LOG.debug("Prop_YunAlive=" + currentYunAlive);
        LOG.debug("Base_Temperature=" + baseTemp);
        LOG.debug("Base_Humidity=" + baseHumid);
        LOG.debug("Base_Dehumid=" + baseDehumid);
        LOG.debug("HVAC_State=" + hvacState);
        LOG.debug("HVAC_OnOff_State=" + hvacOnOffState);
        LOG.debug("Sprinkler_State=" + sprinklerState);
        LOG.debug("WaterSoftener_State=" + wsState);
        LOG.debug("WS_BrineDepth=" + wsBrineDepth);
        LOG.debug("Front_Motion=" + frontMotion);
        LOG.debug("Cam1_Online=" + cam1_Online);
        LOG.debug("Cam2_Online=" + cam2_Online);

        setProperty("Prop_Temperature", homeTemp);
        setProperty("Prop_Humidity", homeHumid);
        setProperty("Prop_YunAlive", currentYunAlive);
        setProperty("Base_Temperature", baseTemp);
        setProperty("Base_Humidity", baseHumid);
        setProperty("Base_Dehumid", baseDehumid);
        setProperty("HVAC_State", hvacState);
        setProperty("HVAC_OnOff_State", hvacOnOffState);
        setProperty("Sprinkler_State", sprinklerState);
        setProperty("WaterSoftener_State", wsState);
        setProperty("WS_BrineDepth", wsBrineDepth);
        setProperty("Front_Motion", frontMotion);
        setProperty("Cam1_Online", cam1_Online);
        setProperty("Cam2_Online", cam2_Online);

        // Get the EventLimit property value from memory
        int eventLimit = (Integer) getProperty("EventLimit").getValue().getValue();

        // Set the eventStatus property value if the eventLimit value is exceeded
        // and it is greater than zero
        boolean eventStatus = false;
        if (wsState > eventLimit) {
            eventStatus = true;
        }

        // If the sensor has a fault...
        if (eventStatus) {
            // Get the previous value of the fault from the property
            // This is the current value because it hasn't been set yet
            // This is done because we don't want to send the event every time it enters the fault state,
            // only send the fault on the transition from non-faulted to faulted
            boolean previousEventStatus = (Boolean) getProperty("EventStatus").getValue().getValue();

            // If the current value is not faulted, then create and queue the event
            if (!previousEventStatus) {
                // Set the event information of the defined data shape for the event
                ValueCollection eventInfo = new ValueCollection();
                eventInfo.put(CommonPropertyNames.PROP_MESSAGE,
                        new StringPrimitive("WaterSoftener at " + wsState + " is above threshold of " + eventLimit));
                // Queue the event
                super.queueEvent("SpecialEvent", DateTime.now(), eventInfo);
            }
        }

        // Set the fault status property value
        setProperty("EventStatus", eventStatus);

        // Update the subscribed properties and events to send any updates to Thingworx.
        // Without calling these methods, the property and event updates will not be sent.
        // The numbers are timeouts in milliseconds.
        updateSubscribedProperties(10000);
        updateSubscribedEvents(10000);
    }

    @ThingworxServiceDefinition(name = "AddNumbers", description = "Add Two Numbers")
    @ThingworxServiceResult(name = CommonPropertyNames.PROP_RESULT, description = "Result", baseType = "NUMBER")
    public Double AddNumbers(
            @ThingworxServiceParameter(name = "a", description = "Value 1", baseType = "NUMBER") Double a,
            @ThingworxServiceParameter(name = "b", description = "Value 2", baseType = "NUMBER") Double b)
            throws Exception {
        double c = 0;
        try {
            c = a + b;
        } catch (Exception e) {
            LOG.error("Service execution for AddNumbers caused exception" + e);
        }
        return c;
    }

    @ThingworxServiceDefinition(name = "controlSprinkler", description = "Turn Sprinkler On or Off")
    @ThingworxServiceResult(name = CommonPropertyNames.PROP_RESULT, description = "Result", baseType = "BOOLEAN")
    public boolean controlSprinkler(
            @ThingworxServiceParameter(name = "control", description = "on or off", baseType = "NUMBER") Double on_off)
            throws Exception {
        try {
            if (on_off == 1) {
                TWcommHandlerFactory.sendCommand("IrriCntl", "ON");
                return true;
            } else if (on_off == 2) {
                TWcommHandlerFactory.sendCommand("IrriCntl", "OFF");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            LOG.error("Service execution for controlSprinkler caused exception" + e);
        }
        return false;
    }

    @ThingworxServiceDefinition(name = "controlCam2", description = "Turn IP Camera 2 On or Off")
    @ThingworxServiceResult(name = CommonPropertyNames.PROP_RESULT, description = "Result", baseType = "BOOLEAN")
    public boolean controlCam2(
            @ThingworxServiceParameter(name = "control", description = "on or off", baseType = "NUMBER") Double on_off)
            throws Exception {
        try {
            if (on_off == 1) {
                TWcommHandlerFactory.sendCommand("Cam2OnOff", "ON");
                return true;
            } else if (on_off == 2) {
                TWcommHandlerFactory.sendCommand("Cam2OnOff", "OFF");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            LOG.error("Service execution for controlCam2 caused exception" + e);
        }
        return false;
    }

    private double getHomeTemperature() {
        return TWcommHandlerFactory.getHomeTemp();
    }

    private Double getHomeHumidity() {
        return TWcommHandlerFactory.getHomeHumid();
    }

    private Boolean getYunAlive() {
        return TWcommHandlerFactory.getYunAlive();
    }

    private double getBaseTemperature() {
        return TWcommHandlerFactory.getBaseTemp();
    }

    private double getBaseHumidity() {
        return TWcommHandlerFactory.getBaseHumid();
    }

    private Boolean getBaseDehumidity() {
        return TWcommHandlerFactory.getBaseDehumid();
    }

    private String getHVAC_State() {
        return TWcommHandlerFactory.getHvacState();
    }

    private int getHVAC_OnOff_State() {
        return TWcommHandlerFactory.getHvacOnOffState();
    }

    private Boolean getSprinklerState() {
        return TWcommHandlerFactory.getSprinkState();
    }

    private int getWaterSoftener_State() {
        return TWcommHandlerFactory.getWS_State();
    }

    private int getWS_brineDepth() {
        return TWcommHandlerFactory.getWS_BrineDepth();
    }

    private Boolean getFrontMotion() {
        return TWcommHandlerFactory.getFrontMotionState();
    }

    private Boolean getCam1_Online() {
        return TWcommHandlerFactory.getCam1OnlineState();
    }

    private Boolean getCam2_Online() {
        return TWcommHandlerFactory.getCam2OnlineState();
    }

    /**
     * Sets the current value of a property to the default value provided in its annotation.
     *
     * @param propertyName
     * @throws Exception
     */
    protected void setDefaultPropertyValue(String propertyName) throws Exception {
        setProperty(propertyName, getProperty(propertyName).getPropertyDefinition().getDefaultValue().getValue());
    }

}
