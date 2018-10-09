<%@page import="java.util.List"%>
<%@page import="entity.WorkOrder"%>
<%@page import="entity.Operator"%>
<%@ page import="util.Global" %>
<jsp:useBean id="global" scope="application" class="util.Global" />

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
    List<Operator> operators = global.operators;
%>
        
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>JSP Page</title>
    </head>
    
     <h2>Assign Operator to WorkOrder:</h2>
     
    <body>

        <table width="25%" border="1" bordercolordark="#000000" bordercolorlight="#FFFFFF" cellpadding="3" cellspacing="0">
        <tr > <font size="3" face="Verdana">
            <td>Name</td>
            <td>Surname</td>
            <td>Assign</td>
        </font>
        </tr> 

<% for(int i=0; i<operators.size(); i++){ %>

        <tr> <font size="1" face="Verdana">
            <td><%=operators.get(i).getName()%></td>
            <td><%=operators.get(i).getSurname()%></td>
            <td><A href=assign.jsp?id_workorder=<%=request.getParameter("id_workorder")%>&id_operator=<%=operators.get(i).getId()%> >Assign</A> </td>
            </font>
        </tr>
<% } %>

    </table>
    <br/><br/>
<A href="../workorder/list.jsp" >Back</A>
   

    </body>
</html>