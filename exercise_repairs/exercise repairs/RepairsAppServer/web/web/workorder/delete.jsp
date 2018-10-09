<%@page import="entity.Operator"%>
<%@page import="webSocketService.WebSocketServer"%>
<%@page import="entity.WorkOrder"%>
<%@ page import="util.Global" %>
<jsp:useBean id="global" scope="application" class="util.Global" />

<%
  int id_workorder = Integer.parseInt(request.getParameter("id_workorder"));

  WorkOrder workorder_to_remove = null;
  for (WorkOrder workorder : global.workorders) {
    if (workorder.getId() == id_workorder) {
      workorder_to_remove = workorder;
      break;
    }
  }
  
  if(workorder_to_remove!=null){
    global.workorders.remove(workorder_to_remove);
    Operator old_ope = workorder_to_remove.getOperator();
    workorder_to_remove.setOperator(global.operators.get(0));
    WebSocketServer.push_remove_WorkOrder(workorder_to_remove, old_ope);
  }
%>

<jsp:forward page="list.jsp"></jsp:forward>