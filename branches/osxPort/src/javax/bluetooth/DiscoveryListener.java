/*
 Copyright 2004 Intel Corporation

 This file is part of Blue Cove.

 Blue Cove is free software; you can redistribute it and/or modify
 it under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation; either version 2.1 of the License, or
 (at your option) any later version.

 Blue Cove is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with Blue Cove; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package javax.bluetooth;

/**
 * The DiscoveryListener interface allows an application to receive device 
 * discovery and service discovery events. This interface provides four methods,
 * two for discovering devices and two for discovering services.
 *
 */
public interface DiscoveryListener {
	/**
	 * Indicates the normal completion of device discovery. Used with the
	 * {@link #inquiryCompleted(int)} method. 
	 * <p>
	 * The value of INQUIRY_COMPLETED is 0x00 (0).
	 * 
	 * @see #inquiryCompleted(int)
	 * @see DiscoveryAgent#startInquiry(int, javax.bluetooth.DiscoveryListener)
	 */

	public static final int INQUIRY_COMPLETED = 0;

	/**
	 * Indicates device discovery has been canceled by the application and did
	 * not complete. Used with the {@link #inquiryCompleted(int)} method. 
	 * <p>
	 * The value of INQUIRY_TERMINATED is 0x05 (5).
	 * 
	 * @see  #inquiryCompleted(int)
	 * @see DiscoveryAgent#startInquiry(int, javax.bluetooth.DiscoveryListener)
	 * @see DiscoveryAgent#cancelInquiry(javax.bluetooth.DiscoveryListener)
	 */

	public static final int INQUIRY_TERMINATED = 5;

	/**
	 * Indicates that the inquiry request failed to complete normally, but was
	 * not cancelled. 
	 * <p>
	 * The value of INQUIRY_ERROR is 0x07 (7).
	 * 
	 * @see  #inquiryCompleted(int)
	 * @see DiscoveryAgent#startInquiry(int, javax.bluetooth.DiscoveryListener)
	 */

	public static final int INQUIRY_ERROR = 7;

	/**
	 * Indicates the normal completion of service discovery. Used with the
	 * {@link #serviceSearchCompleted(int, int)} method. 
	 * <p>
	 * The value of SERVICE_SEARCH_COMPLETED is 0x01 (1).
	 * 
	 * @see #serviceSearchCompleted(int, int)
	 * @see DiscoveryAgent#searchServices(int[], javax.bluetooth.UUID[],
	 * javax.bluetooth.RemoteDevice, javax.bluetooth.DiscoveryListener)
	 */

	public static final int SERVICE_SEARCH_COMPLETED = 1;

	/**
	 * Indicates the service search has been canceled by the application and did
	 * not complete. Used with the {@link #serviceSearchCompleted(int, int)} method. 
	 * <p>
	 * The value of SERVICE_SEARCH_TERMINATED is 0x02 (2).
	 * 
	 * @see  #serviceSearchCompleted(int, int)
	 * @see DiscoveryAgent#searchServices(int[], javax.bluetooth.UUID[],
	 * javax.bluetooth.RemoteDevice, javax.bluetooth.DiscoveryListener)
	 * @see DiscoveryAgent#cancelServiceSearch(int)
	 */

	public static final int SERVICE_SEARCH_TERMINATED = 2;

	/**
	 * Indicates the service search terminated with an error. Used with the
	 * {@link #serviceSearchCompleted(int, int)} method. 
	 * <p>
	 * The value of SERVICE_SEARCH_ERROR is 0x03 (3).
	 * 
	 * @see #serviceSearchCompleted(int, int)
	 * @see DiscoveryAgent#searchServices(int[], javax.bluetooth.UUID[],
	 * javax.bluetooth.RemoteDevice, javax.bluetooth.DiscoveryListener)
	 */

	public static final int SERVICE_SEARCH_ERROR = 3;

	/**
	 * Indicates the service search has completed with no service records found
	 * on the device. Used with the {@link #serviceSearchCompleted(int, int)} method. 
	 * <p>
	 * The value of SERVICE_SEARCH_NO_RECORDS is 0x04 (4).
	 * 
	 * @see #serviceSearchCompleted(int, int)
	 * @see DiscoveryAgent#searchServices(int[], javax.bluetooth.UUID[],
	 * javax.bluetooth.RemoteDevice, javax.bluetooth.DiscoveryListener)
	 */

	public static final int SERVICE_SEARCH_NO_RECORDS = 4;

	/**
	 * Indicates the service search could not be completed because the remote
	 * device provided to {@link DiscoveryAgent#searchServices(int[], javax.bluetooth.UUID[],
	 * javax.bluetooth.RemoteDevice, javax.bluetooth.DiscoveryListener) 
	 * DiscoveryAgent.searchServices()} could not be reached.
	 * Used with the {@link #serviceSearchCompleted(int, int)} method. 
	 * <P>
	 * The value of SERVICE_SEARCH_DEVICE_NOT_REACHABLE is 0x06 (6).
	 * 
	 * @see #serviceSearchCompleted(int, int)
	 * @see DiscoveryAgent#searchServices(int[], javax.bluetooth.UUID[],
	 * javax.bluetooth.RemoteDevice, javax.bluetooth.DiscoveryListener)
	 */

	public static final int SERVICE_SEARCH_DEVICE_NOT_REACHABLE = 6;

	/**
	 * Called when a device is found during an inquiry. An inquiry searches for
	 * devices that are discoverable. The same device may be returned multiple
	 * times. 
	 * 
	 * @param btDevice the device that was found during the inquiry 
	 * @param cod - the service classes, major device class, and minor device
	 * class of the remote device 
	 * @see DiscoveryAgent#startInquiry(int, javax.bluetooth.DiscoveryListener)
	 */

	public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod);

	/**
	 * Called when service(s) are found during a service search. 
	 * 
	 * @param transID the transaction ID of the service search that is posting the
	 * result 
	 * @param servRecord  a list of services found during the search request 
	 * @see DiscoveryAgent#searchServices(int[], javax.bluetooth.UUID[],
	 * javax.bluetooth.RemoteDevice, javax.bluetooth.DiscoveryListener)
	 */

	public void servicesDiscovered(int transID, ServiceRecord[] servRecord);

	/**
	 * Called when a service search is completed or was terminated because of an
	 * error. Legal status values in the {@code respCode} argument include
	 * {@link #SERVICE_SEARCH_COMPLETED}, {@link #SERVICE_SEARCH_TERMINATED},
	 * {@link #SERVICE_SEARCH_ERROR}, {@link #SERVICE_SEARCH_NO_RECORDS} and
	 * {@link #SERVICE_SEARCH_DEVICE_NOT_REACHABLE}. The following table describes when
	 * each {@code respCode} will be used: 
	 * <table><tr><th>respCode</th><th>Reason</th></tr>
	 * <tr><td>{@link #SERVICE_SEARCH_COMPLETED}</td>
	 * 			<td>if the service search completed normally</td></tr>
	 * <tr><td>{@link #SERVICE_SEARCH_TERMINATED}</td>
	 * 			<td>if the service search request was cancelled by a call to
	 * 			{@link DiscoveryAgent#cancelServiceSearch(int)}</td></tr>
	 * <tr><td>{@link #SERVICE_SEARCH_ERROR}</td>
	 * 			<td>if an error occurred while processing the request</td></tr>
	 * <tr><td>{@link #SERVICE_SEARCH_NO_RECORDS}</td>
	 * 			<td>if no records were found during the service search</td></tr>
	 * <tr><td>{@link #SERVICE_SEARCH_DEVICE_NOT_REACHABLE}</td>
	 * 			<td>if the device specified in the search request could not be reached or 
	 * 			the local device could not establish a connection to the remote device
	 * </td></tr></table>
	 * 
	 * @param transID the transaction ID identifying the request which
	 * initiated the service search 
	 * @param respCode  the response code that indicates the status of the transaction
	 */

	public void serviceSearchCompleted(int transID, int respCode);

	/**
	 * Called when an inquiry is completed. The {@code discType} will be
	 * {@link #INQUIRY_COMPLETED} if the inquiry ended normally or {@link #INQUIRY_TERMINATED}
	 * if the inquiry was canceled by a call to 
	 * {@link DiscoveryAgent#cancelInquiry(DiscoveryListener)}. The {@code discType} will be 
	 * {@link #INQUIRY_ERROR} if an error occurred while processing the inquiry causing the 
	 * inquiry to end abnormally. 
	 * 
	 * @param discType the type of request that was completed; either 
	 * 				{@link #INQUIRY_COMPLETED}, {@link #INQUIRY_TERMINATED}, 
	 * 				or {@link #INQUIRY_ERROR}
	 * @see #INQUIRY_COMPLETED
	 * @see #INQUIRY_TERMINATED
	 * @see #INQUIRY_ERROR
	 */

	public void inquiryCompleted(int discType);
}