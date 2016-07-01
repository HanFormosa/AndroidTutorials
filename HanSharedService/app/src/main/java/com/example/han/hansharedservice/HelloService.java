package com.example.han.hansharedservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class HelloService extends Service {
    private static final String TAG = "SharedService";

    public HelloService() {
    }

    String SERVERIP = "192.168.2.108";
    public static final int SERVERPORT = 5004;

//    private static final String tag = HelloService.class.getSimpleName();

    PrintWriter out;
    Socket socket;
    InetAddress serverAddr;
    DataInputStream is;

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
    private Thread connect;
//    private Runnable connect;

    @Override
    public IBinder onBind(Intent intent) {
        return _binder;
    }

    public void sendMessage(String message){
        if (out != null && !out.checkError()) {
            Log.i(TAG,"in sendMessage: "+message);
//            setCmd(message);
//            Log.i(TAG,"this is result from callback func : " + serviceCallbacks.processDataReceivedfromDevice());
            out.println(message);
            out.flush();
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // Start the thread when the service is started.
//        startThread();


        startThread();
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        try {
            socket.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        socket = null;

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
            connect = new Thread(new connectSocket());
            connect.start();
//            new Thread(connect).start();
        }
    }

    /**
     * Stop the thread.
     */
    private void stopThread()
    {
        run = false;

        if (connect != null)
        {
            connect.interrupt();
            connect = null;
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

    class connectSocket implements Runnable {
//        String result = null;
//        byte[] data = new byte[64];
        @Override
        public void run() {
            Log.i(TAG,"i am in run of connectSocket started");
//            mRun = true;
            try {
                //here you must put your computer's IP address.
                serverAddr = InetAddress.getByName(SERVERIP);
                Log.i(TAG, "C: Connecting... at address :" + SERVERIP + " " + serverAddr);
                //create a socket to make the connection with the server

                socket = new Socket(serverAddr, SERVERPORT);
//                Log.i(TAG, "mRun stat is " + mRun);
                try {

                    //send the message to the server
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    Log.e("TCP Client", "C: Sent.");
                    Log.e("TCP Client", "C: Done.");
                }
                catch (Exception e) {
                    Log.e("TCP", "S: Error", e);
                }
            } catch (Exception e) {
                Log.e("TCP", "C: Error", e);
            }
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
