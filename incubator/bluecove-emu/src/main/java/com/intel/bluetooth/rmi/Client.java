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
package com.intel.bluetooth.rmi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Vector;

public class Client {

	private static String rmiRegistryHost = "localhost";

	private static int rmiRegistryPort = Server.rmiRegistryPort;

	private static RemoteService remoteService;

	private static class ServiceProxy implements InvocationHandler {

		public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
			ServiceRequest request = new ServiceRequest(m.getDeclaringClass().getCanonicalName(), m.getName(), m
					.getParameterTypes(), args);
			ServiceResponse response = execute(request, m);
			if (response.getException() == null) {
				return response.getReturnValue();
			} else {
				Throwable t = response.getException();
				// Build combined StackTrace
				StackTraceElement[] remote = t.getStackTrace();
				StackTraceElement[] curreent = Thread.currentThread().getStackTrace();
				List<StackTraceElement> combined = new Vector<StackTraceElement>();
				for (int i = 0; i < remote.length; i++) {
					combined.add(remote[i]);
					if ((remote[i].getMethodName().equals(m.getName()))
							&& (remote[i].getClassName().startsWith(m.getDeclaringClass().getCanonicalName()))) {
						break;
					}
				}
				int startClient = curreent.length;
				for (int i = 0; i < curreent.length; i++) {
					if (curreent[i].getClassName().equals(this.getClass().getName())) {
						startClient = i + 1;
						break;
					}
				}
				for (int i = startClient; i < curreent.length; i++) {
					combined.add(curreent[i]);
				}
				t.setStackTrace(combined.toArray(new StackTraceElement[combined.size()]));
				throw t;
			}
		}
	}

	private static String getRemoteExceptionMessage(RemoteException e) {
		String message = e.getMessage();
		int idx = message.indexOf("; nested exception is:");
		if (idx != -1) {
			return message.substring(0, idx);
		}
		return message;
	}

	public synchronized static Object getService(Class<?> interfaceClass, String host, String port)
			throws RuntimeException {
		if (remoteService == null) {
			try {
				remoteService = getRemoteService(host, port);
				remoteService.verify(interfaceClass.getCanonicalName());
			} catch (RemoteException e) {
				Throwable t = (e.getCause() != null) ? e.getCause() : e;
				throw new RuntimeException(getRemoteExceptionMessage(e), t);
			} catch (NotBoundException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		Class<?>[] allInterfaces = new Class[interfaceClass.getInterfaces().length + 1];
		allInterfaces[0] = interfaceClass;
		System.arraycopy(interfaceClass.getInterfaces(), 0, allInterfaces, 1, interfaceClass.getInterfaces().length);
		return Proxy.newProxyInstance(interfaceClass.getClassLoader(), allInterfaces, new ServiceProxy());
	}

	private static ServiceResponse execute(ServiceRequest request, Method method) throws RuntimeException {
		try {
			return remoteService.execute(request);
		} catch (RemoteException e) {
			Throwable t = (e.getCause() != null) ? e.getCause() : e;
			throw new RuntimeException(getRemoteExceptionMessage(e), t);
		}
	}

	private static RemoteService getRemoteService(String host, String port) throws RemoteException, NotBoundException {
		String rmiHost = rmiRegistryHost;
		if ((host != null) && (host.length() > 0)) {
			rmiHost = host;
		}
		int rmiPort = rmiRegistryPort;
		if ((port != null) && (port.length() > 0)) {
			rmiPort = Integer.parseInt(port);
		}
		if (rmiPort == 0) {
			// in process server
			return new RemoteServiceImpl();
		} else {
			Registry registry = LocateRegistry.getRegistry(rmiHost, rmiPort);
			return (RemoteService) registry.lookup(RemoteService.SERVICE_NAME);
		}
	}
}
