package edu.upc.repairs.service;

import android.app.Service;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Date;

import edu.upc.repairs._GlobalState;
import edu.upc.repairs.comms.RPC;
import entity.Operator;

public class LocationService extends Service implements LocationListener {

  private _GlobalState globalState;
  LocationManager locationManager;

  @Override
  public void onCreate() {
    super.onCreate();
    globalState = (_GlobalState) getApplication();
    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    int time = 60000; //milliseconds
    int distance = 100; //meters
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, time, distance, this);
    toastShow("LocationService created");
  }

  @Override
  public int onStartCommand(Intent intent, int flag, int startId) {
    super.onStartCommand(intent, flag, startId);
	  //here we do not have to do anything,
    return Service.START_STICKY;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    locationManager.removeUpdates(this);
    toastShow("LocationService destroyed");
  }

  private final IBinder myBinder = new MyBinder();
  public class MyBinder extends Binder {
    LocationService getService() {
      return LocationService.this;
    }
  }
  @Override
  public IBinder onBind(Intent intent) {
    toastShow("LocationService bound");
    return myBinder;
  }

  public void onLocationChanged(android.location.Location loc) {
    globalState.my_operator.setLatitude(loc.getLatitude());
    globalState.my_operator.setLongitude(loc.getLongitude());
    globalState.my_operator.setDate(new Date());
    new SendOperatorLocation_Task().execute(globalState.my_operator);
  }
  public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
  }
  public void onProviderEnabled(String arg0) {
  }
  public void onProviderDisabled(String arg0) {
  }

  private class SendOperatorLocation_Task extends AsyncTask<Operator, Void, Operator> {

    @Override
    protected Operator doInBackground(Operator... operators) {
      return RPC.setOperatorLocation(operators[0]);
    }

    @Override
    protected void onPostExecute(Operator operator) {
      if (operator.getId()!=-2) {
        toastShow("New operator location sent");
      } else {
        toastShow("New operator location NOT sent due to: "+operator.getName());
      }
    }

  }

  private void toastShow(String text) {
    Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
    toast.setGravity(0, 0, 200);
    toast.show();
  }
 
}
