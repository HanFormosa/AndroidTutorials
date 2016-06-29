package com.example.han.hansharedservice;

/**
 * Allow the Service to communicate with the Activity via a callback listener..
 */
public interface IPostListener {
    /**
     * The state message from the service.
     * @param msg
     */
    void stateUpdate(String msg);
}
