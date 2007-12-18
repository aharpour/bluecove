/**
 *  BlueCove - Java library for Bluetooth
 *  Copyright (C) 2007 Vlad Skarzhevskyy
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

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRegistrationException;
import javax.bluetooth.UUID;

/**
 * This is empty stub to be implemented in bluecove-gpl project
 *
 * This class is not distributed with BlueCove binary distribution: bluecove.jar
 *
 * @author vlads
 *
 */
class BluetoothStackBlueZ implements BluetoothStack {

	// Used mainly in Unit Tests
	static {
		NativeLibLoader.isAvailable(BlueCoveImpl.NATIVE_LIB_BLUEZ);
	}

	BluetoothStackBlueZ() {

	}

	// ---------------------- Library initialization ----------------------

	public String getStackID() {
		return BlueCoveImpl.STACK_BLUEZ;
	}

	public int getLibraryVersion() {
		// empty stub to be implemented in bluecove-gpl
		return 0;
	}

	public int detectBluetoothStack() {
		// empty stub to be implemented in bluecove-gpl
		return 0;
	}

	public void initialize() {
		// empty stub to be implemented in bluecove-gpl
	}

	public void destroy() {
		// empty stub to be implemented in bluecove-gpl

	}

	public void enableNativeDebug(Class nativeDebugCallback, boolean on) {
		// empty stub to be implemented in bluecove-gpl

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.intel.bluetooth.BluetoothStack#isCurrentThreadInterruptedCallback()
	 */
	public boolean isCurrentThreadInterruptedCallback() {
		return Thread.interrupted();
	}

	// ---------------------- LocalDevice ----------------------

	public String getLocalDeviceBluetoothAddress() throws BluetoothStateException {
		// empty stub to be implemented in bluecove-gpl
		return null;
	}

	public DeviceClass getLocalDeviceClass() {
		// empty stub to be implemented in bluecove-gpl
		return null;
	}

	public String getLocalDeviceName() {
		// empty stub to be implemented in bluecove-gpl
		return null;
	}

	public boolean isLocalDevicePowerOn() {
		// empty stub to be implemented in bluecove-gpl
		return false;
	}

	public String getLocalDeviceProperty(String property) {
		// empty stub to be implemented in bluecove-gpl
		return null;
	}

	public int getLocalDeviceDiscoverable() {
		// empty stub to be implemented in bluecove-gpl
		return 0;
	}

	public boolean setLocalDeviceDiscoverable(int mode) throws BluetoothStateException {
		// empty stub to be implemented in bluecove-gpl
		return false;
	}

	public String getRemoteDeviceFriendlyName(long address) throws IOException {
		// empty stub to be implemented in bluecove-gpl
		return null;
	}

	// ---------------------- Device Inquiry ----------------------

	public boolean startInquiry(int accessCode, DiscoveryListener listener) throws BluetoothStateException {
		return DeviceInquiryThread.startInquiry(this, accessCode, listener);
	}

	public int runDeviceInquiry(DeviceInquiryThread startedNotify, int accessCode, DiscoveryListener listener)
			throws BluetoothStateException {
		// empty stub to be implemented in bluecove-gpl
		return 0;
	}

	public void deviceDiscoveredCallback(DiscoveryListener listener, long deviceAddr, int deviceClass,
			String deviceName, boolean paired) {
		// empty stub to be implemented in bluecove-gpl
	}

	public boolean cancelInquiry(DiscoveryListener listener) {
		// empty stub to be implemented in bluecove-gpl
		return false;
	}

	// ---------------------- Service search ----------------------

	public int runSearchServices(SearchServicesThread startedNotify, int[] attrSet, UUID[] uuidSet,
			RemoteDevice device, DiscoveryListener listener) throws BluetoothStateException {
		return SearchServicesThread.startSearchServices(this, attrSet, uuidSet, device, listener);
	}

	public int searchServices(int[] attrSet, UUID[] uuidSet, RemoteDevice device, DiscoveryListener listener)
			throws BluetoothStateException {
		// empty stub to be implemented in bluecove-gpl
		return 0;
	}

	public boolean cancelServiceSearch(int transID) {
		// empty stub to be implemented in bluecove-gpl
		return false;
	}

	public boolean populateServicesRecordAttributeValues(ServiceRecordImpl serviceRecord, int[] attrIDs)
			throws IOException {
		// empty stub to be implemented in bluecove-gpl
		return false;
	}

	// ---------------------- Client RFCOMM connections ----------------------

	public long connectionRfOpenClientConnection(BluetoothConnectionParams params) throws IOException {
		// empty stub to be implemented in bluecove-gpl
		return 0;
	}

	public void connectionRfCloseClientConnection(long handle) throws IOException {
		// empty stub to be implemented in bluecove-gpl

	}

	public int getSecurityOpt(long handle, int expected) throws IOException {
		return expected;
	}

	// ---------------------- Server RFCOMM connections ----------------------

	public long rfServerOpen(BluetoothConnectionNotifierParams params, ServiceRecordImpl serviceRecord)
			throws IOException {
		// empty stub to be implemented in bluecove-gpl
		return 0;
	}

	public void rfServerClose(long handle, ServiceRecordImpl serviceRecord) throws IOException {
		// empty stub to be implemented in bluecove-gpl

	}

	public void rfServerUpdateServiceRecord(long handle, ServiceRecordImpl serviceRecord, boolean acceptAndOpen)
			throws ServiceRegistrationException {
		// empty stub to be implemented in bluecove-gpl

	}

	public long rfServerAcceptAndOpenRfServerConnection(long handle) throws IOException {
		// empty stub to be implemented in bluecove-gpl
		return 0;
	}

	public void connectionRfCloseServerConnection(long handle) throws IOException {
		// empty stub to be implemented in bluecove-gpl
	}

	// ---------------------- Shared Client and Server RFCOMM connections
	// ----------------------

	public void connectionRfFlush(long handle) throws IOException {
		// empty stub to be implemented in bluecove-gpl

	}

	public int connectionRfRead(long handle) throws IOException {
		// empty stub to be implemented in bluecove-gpl
		return 0;
	}

	public int connectionRfRead(long handle, byte[] b, int off, int len) throws IOException {
		// empty stub to be implemented in bluecove-gpl
		return 0;
	}

	public int connectionRfReadAvailable(long handle) throws IOException {
		// empty stub to be implemented in bluecove-gpl
		return 0;
	}

	public void connectionRfWrite(long handle, int b) throws IOException {
		// empty stub to be implemented in bluecove-gpl

	}

	public void connectionRfWrite(long handle, byte[] b, int off, int len) throws IOException {
		// empty stub to be implemented in bluecove-gpl

	}

	public long getConnectionRfRemoteAddress(long handle) throws IOException {
		// empty stub to be implemented in bluecove-gpl
		return 0;
	}

	// ---------------------- Client and Server L2CAP connections
	// ----------------------

	/*
	 * (non-Javadoc)
	 *
	 * @see com.intel.bluetooth.BluetoothStack#l2OpenClientConnection(com.intel.bluetooth.BluetoothConnectionParams,
	 *      int, int)
	 */
	public long l2OpenClientConnection(BluetoothConnectionParams params, int receiveMTU, int transmitMTU)
			throws IOException {
		// empty stub to be implemented in bluecove-gpl
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.intel.bluetooth.BluetoothStack#l2CloseClientConnection(long)
	 */
	public void l2CloseClientConnection(long handle) throws IOException {
		// empty stub to be implemented in bluecove-gpl
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.intel.bluetooth.BluetoothStack#l2ServerOpen(com.intel.bluetooth.BluetoothConnectionNotifierParams,
	 *      int, int, com.intel.bluetooth.ServiceRecordImpl)
	 */
	public long l2ServerOpen(BluetoothConnectionNotifierParams params, int receiveMTU, int transmitMTU,
			ServiceRecordImpl serviceRecord) throws IOException {
		// empty stub to be implemented in bluecove-gpl
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.intel.bluetooth.BluetoothStack#l2ServerUpdateServiceRecord(long,
	 *      com.intel.bluetooth.ServiceRecordImpl, boolean)
	 */
	public void l2ServerUpdateServiceRecord(long handle, ServiceRecordImpl serviceRecord, boolean acceptAndOpen)
			throws ServiceRegistrationException {
		// empty stub to be implemented in bluecove-gpl
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.intel.bluetooth.BluetoothStack#l2ServerAcceptAndOpenServerConnection(long)
	 */
	public long l2ServerAcceptAndOpenServerConnection(long handle) throws IOException {
		// empty stub to be implemented in bluecove-gpl
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.intel.bluetooth.BluetoothStack#l2CloseServerConnection(long)
	 */
	public void l2CloseServerConnection(long handle) throws IOException {
		// empty stub to be implemented in bluecove-gpl

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.intel.bluetooth.BluetoothStack#l2ServerClose(long,
	 *      com.intel.bluetooth.ServiceRecordImpl)
	 */
	public void l2ServerClose(long handle, ServiceRecordImpl serviceRecord) throws IOException {
		// empty stub to be implemented in bluecove-gpl

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.intel.bluetooth.BluetoothStack#l2Ready(long)
	 */
	public boolean l2Ready(long handle) throws IOException {
		// empty stub to be implemented in bluecove-gpl
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.intel.bluetooth.BluetoothStack#l2receive(long, byte[])
	 */
	public int l2Receive(long handle, byte[] inBuf) throws IOException {
		// empty stub to be implemented in bluecove-gpl
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.intel.bluetooth.BluetoothStack#l2send(long, byte[])
	 */
	public void l2Send(long handle, byte[] data) throws IOException {
		// empty stub to be implemented in bluecove-gpl
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.intel.bluetooth.BluetoothStack#l2GetReceiveMTU(long)
	 */
	public int l2GetReceiveMTU(long handle) throws IOException {
		// empty stub to be implemented in bluecove-gpl
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.intel.bluetooth.BluetoothStack#l2GetTransmitMTU(long)
	 */
	public int l2GetTransmitMTU(long handle) throws IOException {
		// empty stub to be implemented in bluecove-gpl
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.intel.bluetooth.BluetoothStack#l2RemoteAddress(long)
	 */
	public long l2RemoteAddress(long handle) throws IOException {
		// empty stub to be implemented in bluecove-gpl
		return 0;
	}

}