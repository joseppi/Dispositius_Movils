package edu.upc.repairs.comms;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;

public interface Comms {
  String Repairs_server = "10.0.2.2:8080/RepairsAppServer";
  String url_rpc = "http://"+Repairs_server+"/rpc";
  String ENDPOINT = "ws://"+Repairs_server+"/push";
  //  Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss+01:00").create();
  Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializerDeserializer()).create();
}
