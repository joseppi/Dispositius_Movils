<%@page import="entity.Operator"%>
<%@ page import="util.Global" %>
<jsp:useBean id="global" scope="application" class="util.Global" />

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
  int id = Integer.parseInt(request.getParameter("id_operator"));
  Operator operator_to_edit = null;
  for (Operator operator : global.operators) {
    if (operator.getId() == id) {
      operator_to_edit = operator;
      break;
    }
  }
  if(operator_to_edit==null || operator_to_edit.getId()==0){
%>    
    <jsp:forward page="list.jsp"></jsp:forward> 
<%}
  else{
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>JSP Page</title>
    </head>
    <body>
        <h2>Update Operator:</h2>

        <form action="update.jsp" method=POST>
            <table width="25%" border="0" bordercolordark="#000000" bordercolorlight="#FFFFFF" cellpadding="3" cellspacing="0">
                <tr>
                    <td>Login:</td>		
                    <td><input type=text name=login value="<%=operator_to_edit.getLogin()%>" size=10></td>
                </tr>

                <tr>
                    <td>Password:</td>		
                    <td><input type=text name=password value="<%=operator_to_edit.getPassword()%>" size=10></td>
                </tr>

                <tr>
                    <td>Name:</td>
                    <td><input type=text name=name value="<%=operator_to_edit.getName()%>" size=10></td>
                </tr>

                <tr>
                    <td>Surname:</td>
                    <td><input type=text name=surname value="<%=operator_to_edit.getSurname()%>" size=10></td>
                </tr>
            </table>

            <input type="hidden" name=id_operator value="<%=operator_to_edit.getId()%>" >
            <input type=submit name=send value=Send>
        </form>

    </body>
</html>

<%  }
%>
