package edu.upc.repairs;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static edu.upc.repairs.comms.Comms.gson;
import edu.upc.repairs.service.LocationService;
import edu.upc.repairs.service.PushService;
import entity.Operator;
import entity.WorkOrder;

public class _GlobalState extends Application {

  public Operator my_operator;
  public WorkOrder workOrder;

  @Override
  public void onCreate() {
    super.onCreate();
//    if(isThere_my_operator()){
//      load_my_operator();
//      startService(new Intent(this, PushService.class));
//      startService(new Intent(this, LocationService.class));
//    }
  }

  public void load_my_operator(){
    try{
      FileInputStream fis = openFileInput("my_operator");
      BufferedReader br = new BufferedReader(new InputStreamReader(fis));

      my_operator = gson.fromJson(br, Operator.class);

      br.close();
      fis.close();
    }
    catch(Exception e){
      toastShow("Exception at load_my_operator");
      e.printStackTrace();
    }
  }
  public void save_my_operator(){
    try {
      FileOutputStream fos = openFileOutput("my_operator", MODE_PRIVATE);
      PrintWriter pw = new PrintWriter(fos);

      pw.println(gson.toJson(my_operator));

      pw.flush();
      pw.close();
      fos.close();
    } catch (Exception e) {
      toastShow("Exception at save_my_operator");
      e.printStackTrace();
    }
  }
  public void remove_my_operator(){
    try{
      deleteFile("my_operator");
    }
    catch(Exception e){
      toastShow("Exception at remove_my_operator");
      e.printStackTrace();
    }
  }
  public boolean isThere_my_operator(){
    try{
      FileInputStream fis = openFileInput("my_operator");
      fis.close();
      return true;
    }
    catch(Exception e){
      return false;
    }
  }

  public List<WorkOrder> load_my_workOrders(){
    try{
      FileInputStream fis = openFileInput("my_workOrders_"+my_operator.getId());
      BufferedReader br = new BufferedReader(new InputStreamReader(fis));

      List<WorkOrder> my_workOrders = new ArrayList<>();
      String json_workorder;
      while ((json_workorder = br.readLine()) != null) {
        my_workOrders.add(gson.fromJson(json_workorder, WorkOrder.class));
      }

      br.close();
      fis.close();
      return my_workOrders;
    }
    catch(Exception e){
      toastShow("Exception at load_my_workOrders");
      e.printStackTrace();
      return null;
    }
  }
  public void save_my_workOrders(List<WorkOrder> my_workOrders){
    try {
      FileOutputStream fos = openFileOutput("my_workOrders_"+my_operator.getId(), MODE_PRIVATE);
      PrintWriter pw = new PrintWriter(fos);

      for (WorkOrder workorder : my_workOrders) {
        String json_workorder = gson.toJson(workorder);
        pw.println(json_workorder);
        pw.flush();
      }

      pw.close();
      fos.close();
    } catch (Exception e) {
      toastShow("Exception at save_my_workOrders");
      e.printStackTrace();
    }
  }
  public void append_a_workOrder(WorkOrder workOrder){
    try {
      FileOutputStream fos = openFileOutput("my_workOrders_"+my_operator.getId(), MODE_APPEND);
      PrintWriter pw = new PrintWriter(fos);

      String json_workorder = gson.toJson(workOrder);
      pw.println(json_workorder);
      pw.flush();

      pw.close();
      fos.close();
    } catch (Exception e) {
      toastShow("Exception at add_a_workOrder");
      e.printStackTrace();
    }
  }
  public void remove_a_workOrder(WorkOrder to_remove_workOrder){
    try {
      List<WorkOrder> my_workOrders = load_my_workOrders();
      for(WorkOrder workOrder : my_workOrders) {
        if (workOrder.getId().intValue() == to_remove_workOrder.getId().intValue()) {
          my_workOrders.remove(workOrder);
          break;
        }
      }
      save_my_workOrders(my_workOrders);
    } catch (Exception e) {
      toastShow("Exception at remove_a_workOrder");
      e.printStackTrace();
    }
  }
  public void remove_my_workOrders(){
    try{
      deleteFile("my_workOrders_"+my_operator.getId());
    }
    catch(Exception e){
      toastShow("Exception at remove_my_workOrders");
      e.printStackTrace();
    }
  }
  public boolean isThere_my_workOrders(){
    try{
      FileInputStream fis = openFileInput("my_workOrders_"+my_operator.getId());
      fis.close();
      return true;
    }
    catch(Exception e){
      return false;
    }
  }

  private void toastShow(String text) {
    Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
    toast.setGravity(0, 0, 200);
    toast.show();
  }
}
