package com.kircherelectronics.sharedservice;

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

import android.app.Application;

import com.kircherelectronics.sharedservice.service.ServiceManager;

/**
 * Base class for maintaining global application state. To share the service
 * between activities, we use a ServiceManager that is responsible for managing
 * the service life-cycle and the components that bind to it. Any Activity can
 * access the ServiceManager as a scoped global.
 * 
 * @author Kaleb
 * 
 */
public class HelloServiceApplication extends Application
{
	// Our service manager.
	private ServiceManager serviceManager;

	@Override
	public void onCreate()
	{
		super.onCreate();

		// Create our service manager and pass it the application context.
		serviceManager = new ServiceManager(this);
	}

	/**
	 * Return the ServiceManager.
	 * 
	 * @return the ServiceManager.
	 */
	public ServiceManager getServiceManager()
	{
		return serviceManager;
	}
}
