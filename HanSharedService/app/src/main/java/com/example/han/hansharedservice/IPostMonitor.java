package com.example.han.hansharedservice;

/**
 * Allow the Service to communicate with the Activity via a callback listener..
 */
public interface IPostMonitor {

    /**
     * Set the callback listener.
     * @param callback
     */
    public void setListener(IPostListener callback);
}
