<%@page import="entity.Operator"%>
<%@page import="entity.WorkOrder"%>
<%@ page import="util.Global" %>
<jsp:useBean id="global" scope="application" class="util.Global" />

<%
  int id = Integer.parseInt(request.getParameter("id_operator"));
  Operator operator_to_remove = null;
  for (Operator operator : global.operators) {
    if (operator.getId() == id) {
      operator_to_remove = operator;
      break;
    }
  }

  if (operator_to_remove != null && id!=0) {
    global.operators.remove(operator_to_remove);
    for (WorkOrder workorder : global.workorders) {
      if (workorder.getOperator().getId() == id) {
        workorder.setOperator(global.operators.get(0));
      }
    }
  }
%>

<jsp:forward page="list.jsp"></jsp:forward>