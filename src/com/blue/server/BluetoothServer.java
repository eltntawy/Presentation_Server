package com.blue.server;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 * Class that implements an SPP Server which accepts single line of message from
 * an SPP client and sends a single line of response to the client.
 */
public class BluetoothServer {

    // class logger
    public static Logger log = Logger.getLogger("BluetoothServer");
    FileHandler fh;
    
    public static StringBuffer logString = new StringBuffer();

    // Create a UUID for SPP
    private UUID uuid ;

    // Create the service url
    private String connectionString ;

    // open server url
    private StreamConnectionNotifier streamConnNotifier ;

    private StreamConnection connection;

    private boolean stopService = false ;

    public BluetoothServer () throws IOException  {

        // This block configure the logger with handler and formatter
        fh = new FileHandler(System.getProperty("java.io.tmpdir")+"//presentation_log.log", true);
        log.addHandler(fh);
        log.setLevel(Level.ALL);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);

        // display local device address and name
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        logString.append( "Address: " + localDevice.getBluetoothAddress());
        logString.append("\n");
        logString.append("Name: " + localDevice.getFriendlyName());
        logString.append("\n");
    }


    // start server
    public void startServer() throws IOException {

        // Create a UUID for SPP
        uuid = new UUID("1101", true);

        // print uuid
        logString.append("UUID : " + uuid.toString());
        logString.append("\n");
        // Create the service url
        connectionString = "btspp://localhost:" + uuid + ";name=Sample SPP Server";

        // open server url
        streamConnNotifier = (StreamConnectionNotifier) Connector.open(connectionString);

        // Wait for client connection
        logString.append("\nServer Started. Waiting for clients to connect...");
        logString.append("\n");
        connection = streamConnNotifier.acceptAndOpen();

        RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
        logString.append("Remote device address: " + dev.getBluetoothAddress());
        logString.append("\n");
        logString.append("Remote device name: " + dev.getFriendlyName(true));
        logString.append("\n");

        // open input stream for spp client
        InputStream inStream = connection.openInputStream();

        stopService = false;
        String lineRead = "";
        // EOF signal to terminal/end connection
        while (!"EOF".equals(lineRead) && !stopService) {

            BufferedReader bReader = new BufferedReader(new InputStreamReader(
                    inStream));
            lineRead = bReader.readLine();

            if (lineRead != null && !stopService) {
                System.out.println(lineRead);
                Robot r;
                try {
                    r = new Robot();
                    if ("up".equals(lineRead)) {
                        r.keyPress(KeyEvent.VK_UP);
                        r.keyRelease(KeyEvent.VK_UP);
                    } else if ("down".equals(lineRead)) {
                        r.keyPress(KeyEvent.VK_DOWN);
                        r.keyRelease(KeyEvent.VK_DOWN);
                    } else if ("right".equals(lineRead)) {
                        r.keyPress(KeyEvent.VK_RIGHT);
                        r.keyRelease(KeyEvent.VK_RIGHT);
                    } else if ("left".equals(lineRead)) {
                        r.keyPress(KeyEvent.VK_LEFT);
                        r.keyRelease(KeyEvent.VK_LEFT);
                    }

                } catch (AWTException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }

        // send response to spp client
        OutputStream outStream = connection.openOutputStream();
        PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outStream));
        pWriter.write("Response String from SPP Server\r\n");
        pWriter.flush();

        pWriter.close();
        streamConnNotifier.close();

    }

    public void stopService () throws IOException{
        // send response to spp client
        stopService = true;

        logString.append("blueServer : service is stopped");
        logString.append("\n");
    }


    public static void main(String[] args) throws IOException {

        BluetoothServer sampleSPPServer = new BluetoothServer();
        sampleSPPServer.startServer();

        logString.append("class server terminate normally");
        logString.append("\n");

    }
}