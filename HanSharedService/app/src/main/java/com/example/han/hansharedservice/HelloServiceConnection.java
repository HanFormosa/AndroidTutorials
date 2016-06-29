package com.example.han.hansharedservice;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Han on 6/29/16.
 */
public class HelloServiceConnection implements ServiceConnection {
    private static final String tag = "SharedService";

    // This is the object that receives interactions from clients.
    private IPostMonitor _service = null;

    // This is the object that receives interactions from service.
    private IPostListener _listener = null;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service)
    {
        Log.d(tag, "Service is connected.");
        _service = (IPostMonitor) service;
        _service.setListener(_listener);
    }

    @Override
    public void onServiceDisconnected(ComponentName name)
    {
        _service = null;
        Log.d(tag, "Service is disconnected.");
    }

    /**
     * Set the callback listener that should be used to communicate with the
     * activity. This will be forwarded to the Service and *must* be called
     * before onBind() in this case.
     *
     * @param listener
     */
    public void setServiceListener(IPostListener listener)
    {
        _listener = listener;
    }
}
