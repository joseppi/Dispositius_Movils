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
  Operator operator = gson.fromJson(rd, Operator.class);

  System.out.println("getting WorkOrders of: ("+operator.getName()+" "+operator.getSurname()+")");

  List<WorkOrder> workOrders = new ArrayList<WorkOrder>();
  
  for(WorkOrder workOrder : global.workorders){
    if(workOrder.getOperator().getId()==operator.getId()){
      workOrders.add(workOrder);
    }
  }
    
  out.clear();
  out.println(gson.toJson(workOrders));

%>