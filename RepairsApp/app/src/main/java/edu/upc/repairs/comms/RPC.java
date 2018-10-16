package edu.upc.repairs.comms;

import entity.Operator;
import entity.WorkOrder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RPC {
  public static final int TIMEOUT = 5000;

  public static Operator registration(Operator operator) {
    try {
      URL url = new URL(Comms.url_rpc + "/1_registration.jsp");
      HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
      ucon.setRequestMethod("POST");
      ucon.setDoInput(true);
      ucon.setDoOutput(true);
      ucon.setConnectTimeout(TIMEOUT);
      ucon.setRequestProperty("Content-Type", "application/json; charset=utf-8");
      ucon.setRequestProperty("Accept", "application/json; charset=utf-8");

      PrintWriter out = new PrintWriter(ucon.getOutputStream(), true);
      out.println(Comms.gson.toJson(operator));

      ucon.connect();

      BufferedReader in = new BufferedReader(new InputStreamReader(ucon.getInputStream()));
      Operator operator_reply = Comms.gson.fromJson(in, Operator.class);

      out.close();
      in.close();
      ucon.getInputStream().close();

      return operator_reply;

    } catch (Exception e) {
      e.printStackTrace();
      Operator operator_exception = new Operator(-2);
      operator_exception.setName(e.getMessage());
      return operator_exception;
    }
  }

  public static Operator login(Operator operator) {
    try {
      URL url = new URL(Comms.url_rpc + "/2_login.jsp");
      HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
      ucon.setRequestMethod("POST");
      ucon.setDoInput(true);
      ucon.setDoOutput(true);
      ucon.setConnectTimeout(TIMEOUT);
      ucon.setRequestProperty("Content-Type", "application/json; charset=utf-8");
      ucon.setRequestProperty("Accept", "application/json; charset=utf-8");

      PrintWriter out = new PrintWriter(ucon.getOutputStream(), true);
      out.println(Comms.gson.toJson(operator));

      ucon.connect();

      BufferedReader in = new BufferedReader(new InputStreamReader(ucon.getInputStream()));
      Operator operator_reply = Comms.gson.fromJson(in, Operator.class);

      in.close();
      out.close();
      ucon.getInputStream().close();

      return operator_reply;

    } catch (Exception e) {
      e.printStackTrace();
      Operator operator_exception = new Operator(-2);
      operator_exception.setName(e.getMessage());
      return operator_exception;
    }
  }

  public static List<WorkOrder> getWorkOrders(Operator operator) {
    try {
      URL url = new URL(Comms.url_rpc + "/3_getWorkOrders.jsp");
      HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
      ucon.setRequestMethod("POST");
      ucon.setDoInput(true);
      ucon.setDoOutput(true);
      ucon.setConnectTimeout(TIMEOUT);
      ucon.setRequestProperty("Content-Type", "application/json; charset=utf-8");
      ucon.setRequestProperty("Accept", "application/json; charset=utf-8");

      PrintWriter out = new PrintWriter(ucon.getOutputStream(), true);
      out.println(Comms.gson.toJson(operator));

      ucon.connect();

      BufferedReader in = new BufferedReader(new InputStreamReader(ucon.getInputStream()));
      WorkOrder[] workOrdersArray = Comms.gson.fromJson(in, WorkOrder[].class);
      List<WorkOrder> workorders = new ArrayList<WorkOrder>();
      workorders.addAll(Arrays.asList(workOrdersArray));

      in.close();
      out.close();
      ucon.getInputStream().close();

      return workorders;

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Operator setOperatorLocation(Operator operator) {
    try {
      URL url = new URL(Comms.url_rpc + "/4_setOperatorLocation.jsp");
      HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
      ucon.setRequestMethod("POST");
      ucon.setDoInput(true);
      ucon.setDoOutput(true);
      ucon.setConnectTimeout(TIMEOUT);
      ucon.setRequestProperty("Content-Type", "application/json; charset=utf-8");
      ucon.setRequestProperty("Accept", "application/json; charset=utf-8");

      PrintWriter out = new PrintWriter(ucon.getOutputStream(), true);
      out.println(Comms.gson.toJson(operator));

      ucon.connect();

      BufferedReader in = new BufferedReader(new InputStreamReader(ucon.getInputStream()));
      Operator operator_updated = Comms.gson.fromJson(in, Operator.class);

      in.close();
      out.close();
      ucon.getInputStream().close();

      return operator_updated;

    } catch (Exception e) {
      e.printStackTrace();
      Operator operator_exception = new Operator(-2);
      operator_exception.setName(e.getMessage());
      return operator_exception;
    }
  }

  public static WorkOrder updateWorkOrder(WorkOrder workOrder) {
    try {
      URL url = new URL(Comms.url_rpc + "/5_updateWorkOrder.jsp");
      HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
      ucon.setRequestMethod("POST");
      ucon.setDoInput(true);
      ucon.setDoOutput(true);
      ucon.setConnectTimeout(TIMEOUT);
      ucon.setRequestProperty("Content-Type", "application/json; charset=utf-8");
      ucon.setRequestProperty("Accept", "application/json; charset=utf-8");

      PrintWriter out = new PrintWriter(ucon.getOutputStream(), true);
      out.println(Comms.gson.toJson(workOrder));

      ucon.connect();

      BufferedReader in = new BufferedReader(new InputStreamReader(ucon.getInputStream()));
      WorkOrder workOrder_updated = Comms.gson.fromJson(in, WorkOrder.class);

      in.close();
      out.close();
      ucon.getInputStream().close();

      return workOrder_updated;

    } catch (Exception e) {
      e.printStackTrace();
      WorkOrder workorder_exception = new WorkOrder(-2);
      workorder_exception.setTitle(e.getMessage());
      return workorder_exception;
    }

  }

}
