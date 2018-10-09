<%@page import="entity.WorkOrder"%>
<%@page import="entity.Operator"%>
<%@ page import="util.Global" %>
<jsp:useBean id="global" scope="application" class="util.Global" />

<%
  int id_operator = Integer.parseInt(request.getParameter("id_operator"));
  java.lang.String arg1 = request.getParameter("login");
  java.lang.String arg2 = request.getParameter("password");
  java.lang.String arg3 = request.getParameter("name");
  java.lang.String arg4 = request.getParameter("surname");
  Operator operator_to_update = null;
  for (Operator operator : global.operators) {
    if (operator.getId() == id_operator) {
      operator_to_update = operator;
      break;
    }
  }
  if (operator_to_update != null) {
    operator_to_update.setLogin(arg1);
    operator_to_update.setPassword(arg2);
    operator_to_update.setName(arg3);
    operator_to_update.setSurname(arg4);
    for (WorkOrder workorder : global.workorders) {
      if (workorder.getOperator().getId() == id_operator) {
        workorder.setOperator(operator_to_update);
      }
    }
  }
%>

<jsp:forward page="list.jsp"></jsp:forward>