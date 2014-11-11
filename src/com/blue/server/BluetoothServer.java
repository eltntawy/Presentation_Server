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

    // start server
    private void startServer() throws IOException {

	// Create a UUID for SPP
	UUID uuid = new UUID("1101", true);

	// print uuid
	System.out.println("UUID : " + uuid.toString());

	// Create the servicve url
	String connectionString = "btspp://localhost:" + uuid
		+ ";name=Sample SPP Server";

	// open server url
	StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier) Connector
		.open(connectionString);

	// Wait for client connection
	System.out
		.println("\nServer Started. Waiting for clients to connect...");
	StreamConnection connection = streamConnNotifier.acceptAndOpen();
	RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
	System.out.println("Remote device address: "
		+ dev.getBluetoothAddress());
	System.out.println("Remote device name: " + dev.getFriendlyName(true));

	// read string from spp client
	InputStream inStream = connection.openInputStream();

	String lineRead = "";
	// EOF signal to terminal/end connection
	while (!"EOF".equals(lineRead)) {

	    BufferedReader bReader = new BufferedReader(new InputStreamReader(
		    inStream));
	    lineRead = bReader.readLine();

	    if (lineRead != null) {
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

    public static void main(String[] args) throws IOException {

	// display local device address and name
	LocalDevice localDevice = LocalDevice.getLocalDevice();
	System.out.println("Address: " + localDevice.getBluetoothAddress());
	System.out.println("Name: " + localDevice.getFriendlyName());

	BluetoothServer sampleSPPServer = new BluetoothServer();
	sampleSPPServer.startServer();

    }
}