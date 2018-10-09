<%@page import="webSocketService.WebSocketServer"%>
<%@page import="entity.WorkOrder"%>
<%@page import="entity.Operator"%>
<%@ page import="util.Global" %>
<jsp:useBean id="global" scope="application" class="util.Global" />

<%
  int id_workorder = Integer.parseInt(request.getParameter("id_workorder"));
  int id_operator = Integer.parseInt(request.getParameter("id_operator"));

  WorkOrder workorder_to_assign = null;
  for (WorkOrder workorder : global.workorders) {
    if (workorder.getId() == id_workorder) {
      workorder_to_assign = workorder;
      break;
    }
  }

  if (workorder_to_assign.getOperator().getId() != id_operator) {

    Operator operator_to_assign = null;
    for (Operator operator : global.operators) {
      if (operator.getId() == id_operator) {
        operator_to_assign = operator;
        break;
      }
    }

    if (workorder_to_assign != null && operator_to_assign != null) {
      Operator old_ope = workorder_to_assign.getOperator();
      workorder_to_assign.setOperator(operator_to_assign);
      if (old_ope.getId() != 0) {
        WebSocketServer.push_remove_WorkOrder(workorder_to_assign, old_ope);
      }
      WebSocketServer.push_new_WorkOrder(workorder_to_assign);
    }

  }

%>

<jsp:forward page="list.jsp"></jsp:forward>
