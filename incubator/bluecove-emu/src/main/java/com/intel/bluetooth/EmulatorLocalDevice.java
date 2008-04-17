/**
 *  BlueCove - Java library for Bluetooth
 *  Copyright (C) 2008 Michael Lifshits
 *  Copyright (C) 2008 Vlad Skarzhevskyy
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  @version $Id$
 */
package com.intel.bluetooth;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.bluetooth.BluetoothConnectionException;
import javax.bluetooth.BluetoothStateException;

import com.intel.bluetooth.BluetoothConsts.DeviceClassConsts;
import com.intel.bluetooth.emu.DeviceDescriptor;
import com.intel.bluetooth.emu.DeviceManagerService;
import com.intel.bluetooth.emu.EmulatorConfiguration;
import com.intel.bluetooth.emu.EmulatorUtils;

/**
 * @author vlads
 * 
 */
class EmulatorLocalDevice {

	private DeviceManagerService service;

	private DeviceDescriptor deviceDescriptor;

	private int bluetooth_sd_attr_retrievable_max = 0;

	private int bluetooth_l2cap_receiveMTU_max = 0;

	private int bluetooth_connected_devices_max = 0;

	private EmulatorConfiguration configuration;

	private Map<String, String> propertiesMap;

	private Vector<Long> channels = new Vector<Long>();

	private Vector<Long> pcms = new Vector<Long>();

	private long connectionCount = 0;

	private Map<Long, EmulatorConnection> connections = new Hashtable<Long, EmulatorConnection>();

	EmulatorLocalDevice(DeviceManagerService service, DeviceDescriptor deviceDescriptor) throws BluetoothStateException {
		this.service = service;
		this.deviceDescriptor = deviceDescriptor;

		propertiesMap = new Hashtable<String, String>();
		propertiesMap.put("bluecove.radio.version", BlueCoveImpl.version);
		propertiesMap.put("bluecove.radio.manufacturer", "pyx4j.com");
		propertiesMap.put("bluecove.stack.version", BlueCoveImpl.version);

		updateConfiguration();
	}

	void destroy() {
		service = null;
		deviceDescriptor = null;
	}

	DeviceManagerService getDeviceManagerService() {
		return service;
	}

	void updateConfiguration() throws BluetoothStateException {
		configuration = service.getEmulatorConfiguration(deviceDescriptor.getAddress());
		bluetooth_sd_attr_retrievable_max = configuration.getIntProperty("bluetooth.sd.attr.retrievable.max");
		bluetooth_l2cap_receiveMTU_max = configuration.getIntProperty("bluetooth.l2cap.receiveMTU.max");
		bluetooth_connected_devices_max = configuration.getIntProperty("bluetooth.connected.devices.max");

		if (bluetooth_l2cap_receiveMTU_max + 2 > configuration.getConnectionBufferSize()) {
			throw new BluetoothStateException("l2cap.receiveMTU.max larger then connection buffer");
		}

		String[] property = { "bluetooth.master.switch", "bluetooth.sd.attr.retrievable.max",
				"bluetooth.connected.devices.max", "bluetooth.l2cap.receiveMTU.max", "bluetooth.sd.trans.max",
				"bluetooth.connected.inquiry.scan", "bluetooth.connected.page.scan", "bluetooth.connected.inquiry",
				"bluetooth.connected.page" };
		for (int i = 0; i < property.length; i++) {
			propertiesMap.put(property[i], configuration.getProperty(property[i]));
		}
	}

	void updateLocalDeviceProperties() {
		this.deviceDescriptor = service.getDeviceDescriptor(deviceDescriptor.getAddress());
		try {
			updateConfiguration();
		} catch (BluetoothStateException ignore) {
		}
	}

	long getAddress() {
		return deviceDescriptor.getAddress();
	}

	String getName() {
		return deviceDescriptor.getName();
	}

	int getDeviceClass() {
		return deviceDescriptor.getDeviceClass();
	}

	void setLocalDeviceServiceClasses(int classOfDevice) {
		int c = deviceDescriptor.getDeviceClass();
		c &= DeviceClassConsts.MAJOR_MASK | DeviceClassConsts.MINOR_MASK;
		c |= classOfDevice;
		deviceDescriptor.setDeviceClass(c);
		service.setLocalDeviceServiceClasses(deviceDescriptor.getAddress(), c);
	}

	boolean isActive() {
		if (deviceDescriptor == null) {
			return false;
		}
		return deviceDescriptor.isPoweredOn();
	}

	boolean isLocalDevicePowerOn() {
		deviceDescriptor.setPoweredOn(service.isLocalDevicePowerOn(deviceDescriptor.getAddress()));
		return deviceDescriptor.isPoweredOn();
	}

	void setLocalDevicePower(boolean on) {
		deviceDescriptor.setPoweredOn(on);
	}

	public boolean isConnectable() {
		return deviceDescriptor.isPoweredOn() && deviceDescriptor.isConnectable();
	}

	String getLocalDeviceProperty(String property) {
		return (String) propertiesMap.get(property);
	}

	int getBluetooth_sd_attr_retrievable_max() {
		return bluetooth_sd_attr_retrievable_max;
	}

	int getBluetooth_l2cap_receiveMTU_max() {
		return this.bluetooth_l2cap_receiveMTU_max;
	}

	int getLocalDeviceDiscoverable() {
		return service.getLocalDeviceDiscoverable(getAddress());
	}

	boolean setLocalDeviceDiscoverable(int mode) throws BluetoothStateException {
		return service.setLocalDeviceDiscoverable(getAddress(), mode);
	}

	EmulatorConfiguration getConfiguration() {
		return configuration;
	}

	EmulatorConnection getConnection(long handle) throws IOException {
		Object c = connections.get(new Long(handle));
		if (c == null) {
			throw new IOException("Invalid connection handle " + handle);
		}
		return (EmulatorConnection) c;
	}

	void removeConnection(EmulatorConnection c) {
		connections.remove(new Long(c.getHandle()));
		if (c instanceof EmulatorRFCOMMService) {
			channels.remove(new Long(((EmulatorRFCOMMService) c).getChannel()));
		} else if (c instanceof EmulatorL2CAPService) {
			pcms.remove(new Long(((EmulatorL2CAPService) c).getPcm()));
		}
	}

	private long nextConnectionId() {
		long id;
		synchronized (connections) {
			connectionCount++;
			id = connectionCount;
		}
		return id;
	}

	EmulatorRFCOMMService createRFCOMMService() {
		EmulatorRFCOMMService s;
		synchronized (connections) {
			long handle = nextConnectionId();
			int channel = (int) EmulatorUtils.getNextAvailable(channels, 1, 1);
			s = new EmulatorRFCOMMService(this, handle, channel);
			connections.put(new Long(handle), s);
			channels.addElement(new Long(channel));
		}
		return s;
	}

	private void validateCanConnect(long remoteAddress) throws IOException {
		if ((RemoteDeviceHelper.connectedDevices() >= bluetooth_connected_devices_max)
				&& RemoteDeviceHelper.openConnections(remoteAddress) == 0) {
			throw new BluetoothConnectionException(BluetoothConnectionException.NO_RESOURCES,
					"Number of connected device exceeded");
		}
	}

	EmulatorRFCOMMClient createRFCOMMClient(long remoteAddress) throws IOException {
		validateCanConnect(remoteAddress);
		EmulatorRFCOMMClient c;
		synchronized (connections) {
			long handle = nextConnectionId();
			c = new EmulatorRFCOMMClient(this, handle);
			connections.put(new Long(handle), c);
		}
		return c;
	}

	EmulatorL2CAPService createL2CAPService(int bluecove_ext_psm) throws IOException {
		EmulatorL2CAPService s;
		synchronized (connections) {
			int pcm;
			if (bluecove_ext_psm != 0) {
				if (pcms.contains(new Long(bluecove_ext_psm))) {
					throw new IOException("Server PCM " + Integer.toHexString(bluecove_ext_psm) + " already reserved");
				}
				pcm = bluecove_ext_psm;
			} else {
				pcm = (int) EmulatorUtils.getNextAvailable(pcms, 0x1001, 2);
			}
			long handle = nextConnectionId();
			s = new EmulatorL2CAPService(this, handle, pcm);
			connections.put(new Long(handle), s);
			pcms.addElement(new Long(pcm));
		}
		return s;
	}

	EmulatorL2CAPClient createL2CAPClient(long remoteAddress) throws IOException {
		validateCanConnect(remoteAddress);
		EmulatorL2CAPClient c;
		synchronized (connections) {
			long handle = nextConnectionId();
			c = new EmulatorL2CAPClient(this, handle);
			connections.put(new Long(handle), c);
		}
		return c;
	}

}
