package com.kircherelectronics.sharedservice.service;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

/*
 * SharedService
 * Copyright (C) 2014, Kaleb Kircher - Boki Software, Kircher Engineering, LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * The Service Manager is responsible for managing the life-cycle of the Service
 * and the Activities that bind to it.
 * 
 * @author Kaleb
 * 
 */
public class ServiceManager
{
	private static final String tag = ServiceManager.class.getSimpleName();

	// Keep track of the service run state.
	private boolean serviceStarted = false;

	// The context from the application.
	private Context context;
	// The intent we will use to start the service and bind activities.
	private Intent helloServiceIntent = null;

	public ServiceManager(Context context)
	{
		this.context = context;

		// Create our intent.
		helloServiceIntent = new Intent(this.context, HelloService.class);
	}

	/**
	 * Bind a Service Connection from a component (probably an Activity) to the
	 * service so it can receive status updates.
	 * 
	 * @param serviceConnection
	 */
	public void bindService(ServiceConnection serviceConnection)
	{
		this.context.bindService(helloServiceIntent, serviceConnection,
				Context.BIND_AUTO_CREATE);
	}

	/**
	 * Determine if the service is started.
	 * @return The run state of the service.
	 */
	public boolean isServiceStarted()
	{
		return serviceStarted;
	}

	/**
	 * Start the service.
	 */
	public void startService()
	{
		serviceStarted = true;

		this.context.startService(helloServiceIntent);
	}

	/**
	 * Stop the service.
	 */
	public void stopService()
	{
		serviceStarted = false;

		this.context.stopService(helloServiceIntent);
	}

	/**
	 * Unbind a Service Connection from a component (probably an Activity).
	 * @param serviceConnection
	 */
	public void unbindService(ServiceConnection serviceConnection)
	{
		this.context.unbindService(serviceConnection);
	}
}
