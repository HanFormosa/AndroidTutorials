package com.example.han.hansharedservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


/**
 * The second of two activities that share a single service. This activity will
 * bind itself to the service and, when the service is started by the user, will
 * receive messages (the message is the run time in units of seconds) from the
 * service. The service can be stopped and started by the user in the menu bar.
 * Note that the service will not completely stop until all of the activities
 * are unbound from the service and the unbind occurs in onPause(). Once the
 * service will stop completely once the user has stopped the service and paused
 * the application, but other configurations can be implemented.
 *
 * @author Kaleb
 *
 */
public class SecondActivity extends AppCompatActivity {
    private static final String tag = "SharedService";

    // Keep track of the services state.
    private boolean serviceStarted = false;

    // The button used to change activities.
    private Button button;

    // Our service connection used to bind the this activity to the service.
    private HelloServiceConnection helloServiceConnection = null;
    // Our listener callback from the service will deliver the messages.
    private IPostListener helloCallback;
    // Our service manager will manage the service for all activities and is
    // accessed from the Application as a scoped global
    private ServiceManager serviceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Get the service manager from the Application.
        serviceManager = ((HelloServiceApplication) this
                .getApplicationContext()).getServiceManager();

        // Find out if the service is already started...
        serviceStarted = serviceManager.isServiceStarted();
        // And update our menu with the appropriate state.
        invalidateOptionsMenu();

        // Setup our service listener callback.
        helloCallback = new IPostListener()
        {
            public void stateUpdate(String msg)
            {
                // The service is running within a thread that is not the main
                // UI thread, so you would need Handlers to touch the views...
                // We just send the message out to LogCat.
                Log.d(tag, msg);
            }
        };

        // Create our service connection so we can bind the activity to the
        // service...
        helloServiceConnection = new HelloServiceConnection();
        // And connect our service listener callback.
        helloServiceConnection.setServiceListener(helloCallback);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onDestroy()
    {
        super.onDestroy();

        // Stop the service is the activity is destroyed. This may or may not be
        // the behavior you want.
        serviceManager.stopService();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {

            case R.id.action_service:

                // Manage the stopping and starting of the service with the menu
                // button.

                if (serviceStarted)
                {
                    // Stop the service if it is already started.
                    serviceStarted = false;
                    serviceManager.stopService();
                }
                else
                {
                    // Start the service if it is stopped.
                    serviceStarted = true;
                    serviceManager.startService();
                }

                // Update our menu button. This will call the onPrepareOptionsMenu()
                // method that is overridden to update the menu button with the
                // correct state.
                invalidateOptionsMenu();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onPause()
    {
        super.onPause();

        // Unbind the service when the activity is paused.
        serviceManager.unbindService(helloServiceConnection);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);

        // Update the menu button.

        if (serviceStarted)
        {
            menu.getItem(0).setTitle(
                    getResources().getString(R.string.action_stop_service));
        }
        else
        {
            menu.getItem(0).setTitle(
                    getResources().getString(R.string.action_start_service));
        }

        return true;
    }

    public void onResume()
    {
        super.onResume();

        // Bind the service when the activity resumes.
        serviceManager.bindService(helloServiceConnection);
    }


    public void onClick(View v)
    {
        // Start the first activity.
        this.startActivity(new Intent(this, FirstActivity.class));
    }


}
