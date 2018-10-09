<%@page import="entity.Client"%>
<%@page import="entity.Operator"%>
<%@page import="webSocketService.WebSocketServer"%>
<%@page import="java.util.Date"%>
<%@page import="entity.WorkOrder"%>
<%@ page import="util.Global" %>
<jsp:useBean id="global" scope="application" class="util.Global" />

<%
  java.lang.String arg0 = request.getParameter("title");
  java.lang.String arg1 = request.getParameter("details");
  
  int id;
  if(global.clients.isEmpty()){
    id = 0;
  }
  else{
    Client last_client = global.clients.get(global.clients.size()-1);
    id = last_client.getId()+1;
  }
  Client new_client = new Client(id);
  new_client.setName("Client "+(int)(Math.random()*1000));
  new_client.setAddress("Paseo Verdun nº "+(int)(Math.random()*100));
  new_client.setLatitude(41.4364059271734+(Math.random()*0.01));
  new_client.setLongitude(2.18192019283132+(Math.random()*0.01));

  if(global.workorders.isEmpty()){
    id = 0;
  }
  else{
    WorkOrder last_workOrder = global.workorders.get(global.workorders.size() - 1);
    id = last_workOrder.getId() + 1;
  }
  WorkOrder new_workOrder = new WorkOrder(id);
  new_workOrder.setTitle(arg0);
  new_workOrder.setDetails(arg1);
  new_workOrder.setDate(new Date());
  new_workOrder.setClient(new_client);
  new_workOrder.setOperator(global.operators.get(0));

  global.clients.add(new_client);
  global.workorders.add(new_workOrder);
%>

<jsp:forward page="list.jsp"></jsp:forward>