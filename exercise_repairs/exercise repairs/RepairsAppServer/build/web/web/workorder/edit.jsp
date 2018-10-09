<%@page import="entity.WorkOrder"%>
<%@page import="entity.Client"%>
<%@ page import="util.Global" %>
<jsp:useBean id="global" scope="application" class="util.Global" />

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
  int id_workorder = Integer.parseInt(request.getParameter("id_workorder"));
  WorkOrder workorder_to_edit = null;
  for (WorkOrder workorder : global.workorders) {
    if (workorder.getId() == id_workorder) {
      workorder_to_edit = workorder;
      break;
    }
  }
  
  if(workorder_to_edit!=null) {
%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>Update Work Order</title>
    </head>
    <body>

        <h2>Update Work Order:</h2>

        <form action="update.jsp" method=POST>
            <table width="50%" border="0" bordercolordark="#000000" bordercolorlight="#FFFFFF" cellpadding="3" cellspacing="0">
                <tr>
                    <td>Title:</td>
                    <td><input type=text name=title value="<%=workorder_to_edit.getTitle()%>" size=10></td>
                </tr>

                <tr>
                    <td>Client:</td>
                    <td><%=workorder_to_edit.getClient().getName()%></td>
                </tr>

                <tr>
                    <td>Address:</td>
                    <td><%=workorder_to_edit.getClient().getAddress()%></td>
                </tr>

                <tr>
                    <td>Details:</td>
                    <td><input type=text name=details value="<%=workorder_to_edit.getDetails()%>" size=10></td>
                </tr>
            </table>

            <input type="hidden" name=id_workorder value="<%=workorder_to_edit.getId()%>" >
            <input type=submit name=send value=Send>
        </form>

    </body>
</html>

<% }

else {
%>
<jsp:forward page="list.jsp"></jsp:forward>
<%
}
%>


