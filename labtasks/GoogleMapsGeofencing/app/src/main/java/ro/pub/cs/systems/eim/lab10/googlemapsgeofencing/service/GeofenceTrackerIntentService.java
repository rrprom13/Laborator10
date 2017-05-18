package ro.pub.cs.systems.eim.lab10.googlemapsgeofencing.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import ro.pub.cs.systems.eim.lab10.R;
import ro.pub.cs.systems.eim.lab10.googlemapsgeofencing.general.Constants;
import ro.pub.cs.systems.eim.lab10.googlemapsgeofencing.view.GoogleMapsGeofenceEventActivity;

public class GeofenceTrackerIntentService extends IntentService {

    public GeofenceTrackerIntentService() {
        super(Constants.TAG);
    }

    @Override
    public void onHandleIntent(Intent intent) {

        Log.i(Constants.TAG, "onHandleIntent() callback method has been invoked");

        // TODO: exercise 9
        // obtain GeofencingEvent from the calling intent, using GeofencingEvent.fromIntent(intent);
        // check whether the GeofencingEvent hasError(), log it and exit the method
        // check the specific error from GeofenceStatusCodes (GEOFENCE_NOT_AVAILABLE, GEOFENCE_TOO_MANY_GEOFENCES, GEOFENCE_TOO_MANY_PENDING_INTENTS)
        // get the geofence transition using getGeofenceTransition() method
        // if transition is only Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT
        // build a detailed message
        // - include the transition type
        // - include the request identifier (getRequestId()) for each  geofence that triggered the event (getTriggeringGeofences())
        // send a notification with the detailed message (sendNotification())
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            int errorCode = geofencingEvent.getErrorCode();
            /*
            if (errorCode == Constants.GEOFENCE_NOT_AVAILABLE_ERROR) {

            }
            if (errorCode == Constants.GEOFENCE_TOO_MANY_GEOFENCES_ERROR) {

            }
            if (errorCode == Constants.GEOFENCE_TOO_MANY_PENDING_INTENTS_ERROR) {

            }*/
            Log.e(Constants.TAG, "Event not found");
            return;
        }
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
        String message = Constants.GEOFENCE_TRANSITION_ENTER;
        for (Geofence geofence : triggeringGeofences) {
            message += " " + geofence.getRequestId();
        }
        sendNotification(message);
    }

    private void sendNotification(String notificationDetails) {
        Intent notificationIntent = new Intent(getApplicationContext(), GoogleMapsGeofenceEventActivity.class);
        notificationIntent.putExtra(Constants.NOTIFICATION_DETAILS, notificationDetails);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(GoogleMapsGeofenceEventActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setColor(Color.RED)
                .setContentTitle(Constants.GEOFENCE_TRANSITION_EVENT)
                .setContentText(notificationDetails)
                .setContentIntent(notificationPendingIntent);
        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
