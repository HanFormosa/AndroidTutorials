package com.example.han.hansharedservice;

import android.app.Application;

/**
 * Base class for maintaining global application state. To share the service
 * between activities, we use a ServiceManager that is responsible for managing
 * the service life-cycle and the components that bind to it. Any Activity can
 * access the ServiceManager as a scoped global.
 *
 * @author Kaleb
 *
 */
public class HelloServiceApplication extends Application {

    // Our service manager.
    private ServiceManager serviceManager;

    @Override
    public void onCreate() {
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
