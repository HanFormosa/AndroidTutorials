package com.example.han.hansharedservice;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity {
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
    // accessed from the Application as a scoped global.
    private ServiceManager serviceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        // Create our service connection so we can bind the activity to the service...
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
        Log.i(tag, "unbinding service on Pause");
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
        Log.i(tag,"Binding service on resume");
    }

    public void onClickButton(View v)
    {
        // Start the second activity.
        this.startActivity(new Intent(this, SecondActivity.class));
    }
}
