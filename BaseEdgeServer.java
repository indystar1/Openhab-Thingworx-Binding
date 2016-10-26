package org.openhab.binding.twcomm;

import java.math.BigDecimal;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thingworx.common.RESTAPIConstants;
import com.thingworx.communications.client.ClientConfigurator;
import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;

/**
 Copyright (c) 2015 PTC Inc.
Permission is hereby granted, free of charge, to any person obtaining a copy of this software
and associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
is furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies
or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/**
 * This class simplifies starting an edge server. It manages the command line parameters
 * an the construction of the edge client and monitoring of any Things registered with it.
 */
public class BaseEdgeServer {
    private static final Logger LOG = LoggerFactory.getLogger(BaseEdgeServer.class);
    private static final int POLLING_INTERVAL_SECONDS = 600;
    // protected static SignalHandler oldSigTERM;
    protected static ConnectedThingClient client;
    protected static String address = "wss://iupui.cloud.thingworx.com:443/Thingworx/WS";
    protected static String appKey = "22889823-850b-4bc2-b901-bd12c122970a";
    protected static String simulated = "simulated";
    public static final String THING_NAME = "JLeeOHThings";
    public static final String THING_DESCRIPTION = "OpenHAB Events";

    private static Boolean serverDown = false;
    private BigDecimal refresh = new BigDecimal(POLLING_INTERVAL_SECONDS);
    ScheduledFuture<?> refreshJob;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    // private static ThingworxRestInterface tRinterface = new ThingworxRestInterface();

    public BaseEdgeServer() {
        LOG.debug("In BaseEdgeServer constructor.");
        try {
            // parseArguments(args);
            client = getEdgeClient();
            client.bindThing(new TempAndHumidityThing(THING_NAME, THING_DESCRIPTION, client, simulated));
            System.err.println("Connecting to " + address + " using key " + appKey);
            client.start();
            serverDown = false;
            // manualSending();
            monitorThings();
        } catch (Exception e) {
            serverDown = true;
            LOG.error("TW server unavailable or connection error: ", e);
        }
    }

    /**
     * Pushes all values of all things being monitored up to the server.
     * It will repeat pushing values every POLLING_INTERVAL_SECONDS.
     * But I no longer use this one, as I am sending updates manually to ThingWorx
     *
     * @throws InterruptedException
     */
    private void monitorThings() throws InterruptedException {
        Runnable runnable = new Runnable() {
            // This is called every refresh interval
            @Override
            public void run() {
                System.err.println("Periodic sending updates to ThingWorx server...");
                if (serverDown == true) {
                    new BaseEdgeServer(); // In case disconnected to server, try re-connect
                    return; // don't execute updating now
                }
                if (client.getEndpoint().isConnected()) {
                    for (VirtualThing vt : client.getThings().values()) { // TempAndHumidityThing
                        try {
                            vt.processScanRequest();
                        } catch (Exception eProcessing) {
                            LOG.error("Error Processing Scan Request for [" + vt.getName() + "] ", eProcessing);
                            // return -1;
                        }
                    }
                }
                /*
                 * try {
                 * tRinterface.RestRequest("JLeeOHThings");
                 * } catch (IOException e) {
                 * System.err.println("Error while calling Rest Request" + e.getMessage());
                 * }
                 */
            }
        };
        // Creating a refreshing job for updating to ThingWorx
        refreshJob = scheduler.scheduleAtFixedRate(runnable, 0, refresh.intValue(), TimeUnit.SECONDS);
    }

    public static void manualSending() throws InterruptedException { // called from TWcommHandlerFactory.receive()
        System.err.println("Executing manual sending updates to ThingWorx server...");
        if (serverDown == true) {
            new BaseEdgeServer(); // In case disconnected to server, try re-connect
            return; // don't execute updating now
        }
        if (client.getEndpoint().isConnected()) {
            for (VirtualThing vt : client.getThings().values()) {
                try {
                    vt.processScanRequest();
                } catch (Exception eProcessing) {
                    LOG.error("Error Processing Scan Request for [" + vt.getName() + "] ", eProcessing);
                    // return -1;
                }
            }
        }
    };

    /**
     * Create an edge client configuration that will use the provided API key for authentication.
     *
     * @return a client configurator.
     */
    protected static ClientConfigurator getClientConfigurator() {
        ClientConfigurator config = new ClientConfigurator();
        config.setUri(address);
        config.getSecurityClaims().addClaim(RESTAPIConstants.PARAM_APPKEY, appKey);
        config.ignoreSSLErrors(true);
        return config;
    }

    /**
     * Create a client for the specified server using the provided API key.
     * Attach the shutdown code to the termination signal so that the client will disconnect
     * when the program is stopped with a CTRL-C.
     *
     * @return
     * @throws Exception
     */
    protected static ConnectedThingClient getEdgeClient() throws Exception {
        ConnectedThingClient aClient = new ConnectedThingClient(getClientConfigurator(), null);

        attachClientShutdownToSigTerm();
        return aClient;
    }

    /**
     * Attach client disconnect handlers for when the program gets a signal to stop running.
     */
    protected static void attachClientShutdownToSigTerm() {
        /*
         * oldSigTERM = Signal.handle(new Signal("TERM"), new SignalHandler() {
         *
         * @Override
         * public void handle(Signal signal) {
         * try {
         * LOG.info("Shutting client down...");
         * client.shutdown();
         * LOG.info("Successfully shut down client.");
         * } catch (Exception e) {
         * LOG.error("Failed to properly shutdown client.", e);
         * }
         * if (oldSigTERM != null) {
         * oldSigTERM.handle(signal);
         * }
         * }
         * });
         */

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    LOG.info("Shutting client down...");
                    client.shutdown();
                    LOG.info("Successfully shut down client.");
                } catch (Exception e) {
                    LOG.error("Failed to properly shutdown client.", e);
                }

            }
        });
    }

}
