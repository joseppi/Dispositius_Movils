<%@page import="entity.WorkOrder"%>
<%@ page import="util.Global" %>
<jsp:useBean id="global" scope="application" class="util.Global" />

<%
  java.lang.String arg0 = request.getParameter("id_workorder");
  int id_workorder = Integer.parseInt(arg0);
  java.lang.String arg1 = request.getParameter("title");
  java.lang.String arg2 = request.getParameter("details");

  WorkOrder workorder_to_update = null;
  for (WorkOrder workorder : global.workorders) {
    if (workorder.getId() == id_workorder) {
      workorder_to_update = workorder;
      break;
    }
  }

  if (workorder_to_update != null) {
    workorder_to_update.setTitle(arg1);
    workorder_to_update.setDetails(arg2);
  }
%>

<jsp:forward page="list.jsp"></jsp:forward>