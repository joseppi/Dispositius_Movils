package edu.upc.repairs;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.upc.repairs.comms.RPC;
import entity.Operator;
import entity.WorkOrder;

public class f_GoogleMapsActivity extends FragmentActivity
  implements LocationListener, OnMarkerDragListener, OnMarkerClickListener, OnMapClickListener {

  _GlobalState globalState;

  LocationManager locationManager;
  LatLng LatLng_operator;
  List<LatLng> history_LatLng_operator;

  GoogleMap googleMap;
  Circle circle;
  Marker circle_marker;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    globalState = (_GlobalState) getApplication();
    setContentView(R.layout.f_googlemaps);
    setUpMapIfNeeded();

    history_LatLng_operator = new ArrayList<LatLng>();

    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    UiSettings uiSettings = googleMap.getUiSettings();
    uiSettings.setZoomControlsEnabled(true);
    uiSettings.setMyLocationButtonEnabled(true);
    googleMap.setOnMapClickListener(this);
    googleMap.setOnMarkerDragListener(this);
    googleMap.setOnMarkerClickListener(this);

    //just to get started from a known location:
    LatLng_operator = new LatLng(41.40, 2.17);
    addingMarkers();
    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng_operator, 13.0f));

    history_LatLng_operator.add(LatLng_operator);
  }
  
  @Override
  public void onResume() {
    super.onResume();
    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    int time = 60000; //milliseconds
    int distance = 100; //meters
    
    //only 3 providers are defined:
    // LocationManager.GPS_PROVIDER
    // LocationManager.NETWORK_PROVIDER
    // LocationManager.PASSIVE_PROVIDER
    
    //last known location:
    android.location.Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    if(loc!=null){
      LatLng_operator = new LatLng(loc.getLatitude(), loc.getLongitude());
    }
    
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, time, distance, this);

  }
  @Override
  public void onPause() {
    super.onPause();
    locationManager.removeUpdates(this);
  }

  public void onLocationChanged(android.location.Location loc) {
    toastShow("location changed");
    LatLng_operator = new LatLng(loc.getLatitude(), loc.getLongitude());
    history_LatLng_operator.add(LatLng_operator);
    googleMap.clear();
    addingMarkers();
    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng_operator, 13.0f));

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

  public void addingMarkers() {

    //draws operator location:
    googleMap.addMarker(new MarkerOptions()
      .position(LatLng_operator)
      .anchor(0.5f, 0.5f)
      .title("Operator location")
      .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));

    //draws work orders locations:
    if(globalState.isThere_my_workOrders()) {
      for (WorkOrder wo : globalState.load_my_workOrders()) {
        LatLng LatLng_point = new LatLng(wo.getClient().getLatitude(), wo.getClient().getLongitude());
        googleMap.addMarker(new MarkerOptions()
            .position(LatLng_point)
            .anchor(0.5f, 0.5f)
            .title(wo.getTitle())
            .snippet(wo.getClient().getName() + ": " + wo.getClient().getAddress())
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag)));
      }
    }

    //draws operator's route:
    if (!history_LatLng_operator.isEmpty()) {
      PolylineOptions polylineOptions = new PolylineOptions();
      polylineOptions.color(Color.argb(255, 255, 102, 0));
      polylineOptions.width(4);
      for (LatLng LatLng_point : history_LatLng_operator) {
        polylineOptions.add(LatLng_point);
      }
      googleMap.addPolyline(polylineOptions);
    }

    //draws a circle around the operator location:
    CircleOptions circleOptions = new CircleOptions()
      .center(LatLng_operator)
      .radius(0); // In meters
    circleOptions.strokeColor(Color.argb(255, 255, 102, 0));
    circleOptions.fillColor(Color.argb(50, 255, 102, 0));
    circleOptions.strokeWidth(4);
    circle = googleMap.addCircle(circleOptions);

    //draws marker to enlarge the circle:
    LatLng latlng_marker = new LatLng(LatLng_operator.latitude + 0.005, LatLng_operator.longitude + 0.005);
    circle_marker = googleMap.addMarker(new MarkerOptions()
      .position(latlng_marker)
      .draggable(true)
      .title("set a circle radius around the operator"));
  }
  
  public void onMapClick(LatLng latlng) {

    Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
    try {
      List<Address> addresses = geoCoder.getFromLocation(latlng.latitude, latlng.longitude, 1);

      String add = "";
      if (addresses.size() > 0) {
        for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
          add += addresses.get(0).getAddressLine(i) + "\n";
        }
      }

      toastShow(add);

    } catch (IOException e) {
      e.printStackTrace();
      toastShow("error at onMapClick: "+e.getMessage());
    }
  }
  public void onMarkerDragStart(Marker marker) {}
  public void onMarkerDrag(Marker marker) {
    int radius = (int) distance(LatLng_operator.latitude, marker.getPosition().latitude,
      LatLng_operator.longitude, marker.getPosition().longitude, 0, 0);
    circle.setRadius(radius);
  }
  public void onMarkerDragEnd(Marker marker) {}
  public boolean onMarkerClick(Marker marker) {
    if (marker.getTitle().equals("Operator location")) {
      circle.setRadius(0);
    }
    return false;
  }

  public void reverseGeocoding(String street) {
    Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
    try {
      List<Address> addresses = geoCoder.getFromLocationName(street, 1);
      if (addresses.size() > 0) {
        LatLng latlng_point = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng_point, 14.0f));
        toastShow(street + " located at: " + addresses.get(0).getLatitude() + ","
          + addresses.get(0).getLongitude());
      }
    } catch (IOException e) {
      toastShow("error at reverseGeocoding: " + e.getMessage());
    }
  }

  private void toastShow(String text) {
    Toast toast = Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG);
    toast.setGravity(0, 0, 200);
    toast.show();
  }

  private class SendOperatorLocation_Task extends AsyncTask<Operator, Void, Operator> {

    ProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {
      progressDialog = ProgressDialog.show(f_GoogleMapsActivity.this,
          "GoogleMapsActivity", "sending operator location...");
    }

    @Override
    protected Operator doInBackground(Operator... operators) {
      return RPC.setOperatorLocation(operators[0]);
    }

    @Override
    protected void onPostExecute(Operator operator) {
      progressDialog.dismiss();
      if (operator.getId()!=-2) {
        toastShow("New operator location sent");
      } else {
        toastShow("New operator location NOT sent due to: "+operator.getName());
      }
    }
  }

  private void setUpMapIfNeeded() {

    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
    switch (resultCode) {
      case ConnectionResult.SUCCESS: // proceed
        break;
      case ConnectionResult.SERVICE_MISSING:
      case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
      case ConnectionResult.SERVICE_DISABLED:
        toastShow("error with GooglePlayServices");
    }

    // Do a null check to confirm that we have not already instantiated the map.
    if (googleMap == null) {
      // Try to obtain the map from the SupportMapFragment.
      googleMap = ((SupportMapFragment) getSupportFragmentManager().
          findFragmentById(R.id.mapFragment)).getMap();
      // Check if we were successful in obtaining the map.
      if (googleMap != null) {
        setUpMap();
      }
    }
  }

  private void setUpMap() {
    googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
  }

  private double distance(double lat1, double lat2, double lon1, double lon2, double el1, double el2) {

    final int R = 6371; // Radius of the earth in Km

    Double latDistance = deg2rad(lat2 - lat1);
    Double lonDistance = deg2rad(lon2 - lon1);
    Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
      + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
      * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double distance = R * c * 1000; // convert to meters

    double height = el1 - el2;
    distance = Math.pow(distance, 2) + Math.pow(height, 2);
    return Math.sqrt(distance);
  }

  private double deg2rad(double deg) {
    return (deg * Math.PI / 180.0);
  }
}
