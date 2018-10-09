<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="util.DateSerializerDeserializer"%>
<%@page import="entity.Operator"%>
<%@page import="org.json.JSONObject"%>
<%@page import="entity.WorkOrder"%>
<%@page import="com.google.gson.Gson"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page trimDirectiveWhitespaces="true" %>

<jsp:useBean id="global" scope="application" class="util.Global" />

<%

  BufferedReader rd = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
  Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializerDeserializer()).create();
  WorkOrder updated_workorder = gson.fromJson(rd, WorkOrder.class);

  System.out.println("updating WorkOrder: ("+updated_workorder.getClient().getName()+
    ", "+updated_workorder.getTitle()+ ", state: "+updated_workorder.getState()+
    ", comments: "+updated_workorder.getComments()+")");
  
  for(WorkOrder workOrder : global.workorders){
    if(workOrder.getId()==updated_workorder.getId()){
      workOrder.setState(updated_workorder.getState());
      workOrder.setComments(updated_workorder.getComments());
    }
  }
    
  out.clear();
  out.println(gson.toJson(updated_workorder));

%>