import java.io.IOException;
import java.util.Vector;

import javax.bluetooth.*;

/**
 * Minimal Device Discovery example.
 */
public class RemoteDeviceDiscovery implements DiscoveryListener{

	public static final Vector/* <RemoteDevice> */devicesDiscovered = new Vector();

	final static Object inquiryCompletedEvent = new Object();
	
	public void init() throws BluetoothStateException, InterruptedException {
		
		devicesDiscovered.clear();

		synchronized (inquiryCompletedEvent) {
			boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, this);
			if (started) {
				System.out.println("wait for device inquiry to complete...");
				
				inquiryCompletedEvent.wait();
				
				System.out.println(devicesDiscovered.size()+ " device(s) found");
			}
		}
	}

	public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
		System.out.println("Device " + btDevice.getBluetoothAddress()+ " found");
		try {
			System.out.println(" - Device name " + btDevice.getFriendlyName(true)+ " - ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		devicesDiscovered.addElement(btDevice);
		try {
			System.out.println("     name "+ btDevice.getFriendlyName(false));
		} catch (IOException cantGetDeviceName) {
		}
	}

	public void inquiryCompleted(int discType) {
		System.out.println("Device Inquiry completed!");
		synchronized (inquiryCompletedEvent) {
			inquiryCompletedEvent.notifyAll();
		}
	}

	public void serviceSearchCompleted(int transID, int respCode) {
	}

	public void servicesDiscovered(int transID,
			ServiceRecord[] servRecord) {
	}

	
	
	public static void main(String[] args) throws IOException,InterruptedException {

		RemoteDeviceDiscovery rdd = new RemoteDeviceDiscovery();
		rdd.init();
		
	}
}