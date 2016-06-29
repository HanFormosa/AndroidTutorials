package com.kircherelectronics.sharedservice.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

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
 * A Service is an application component that can perform long-running
 * operations in the background and does not provide a user interface. This
 * service runs a thread that counts the seconds that the service has been
 * running and forwards the count to service listeners via the IPostListener
 * object.
 * 
 * @author Kaleb
 * 
 */
public class HelloService extends Service
{
	private static final String tag = HelloService.class.getSimpleName();

	// Keep track of the services run state.
	private boolean run = false;

	private int count = 0;

	// Our custom, but very simple, Binder. It is only responsible for setting
	// the IPostListener once it has been set by the ServiceConnection which
	// manages the state of the application service and allows us to bind
	// components to the service.
	private final Binder _binder = new LocalBinder();

	// The callback listener is responsible for forwarding information from the
	// service (in this case the service run time) to the bound components (an
	// activity in this case).
	private IPostListener _callback = null;

	// Our thread that counts the run time.
	private Thread thread;

	@Override
	public void onCreate()
	{

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

		// Start the thread when the service is started.
		startThread();

		// If we get killed, after returning from here, restart
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return _binder;
	}

	@Override
	public void onDestroy()
	{
		Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();

		// Stop the thread when the service is destroyed.
		stopThread();
	}

	/**
	 * Start the thread.
	 */
	private void startThread()
	{
		if (!run)
		{
			run = true;

			// Start up the thread running the service.
			thread = new Thread(new WorkThread());
			thread.start();
		}
	}

	/**
	 * Stop the thread.
	 */
	private void stopThread()
	{
		run = false;

		if (thread != null)
		{
			thread.interrupt();
			thread = null;
		}
	}

	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC. This
	 * class is what allows clients to communicate with the service.
	 */
	public class LocalBinder extends Binder implements IPostMonitor
	{
		/**
		 * Set the callback listener for the client.
		 * 
		 * @param callback
		 *            Our listener callback which is owned by the listening
		 *            component (our activity).
		 */
		public void setListener(IPostListener callback)
		{
			_callback = callback;
		}
	}

	/**
	 * The Runnable class for our thread that counts the run time.
	 * 
	 * @author Kaleb
	 * 
	 */
	private class WorkThread implements Runnable
	{
		@Override
		public void run()
		{
			while (run && !Thread.currentThread().isInterrupted())
			{
				synchronized (this)
				{
					// Make sure we have a listener to callback...
					if (_callback != null)
					{
						_callback.stateUpdate("Seconds: " + count++);
					}

					try
					{
						// Sleep for 1 second.
						Thread.sleep(1000);
					}
					catch (InterruptedException e)
					{
						Thread.currentThread().interrupt();
					}
				}
			}

			Thread.currentThread().interrupt();
		}
	}
}