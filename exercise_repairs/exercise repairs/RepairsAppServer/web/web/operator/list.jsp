<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.List"%>
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
        <h2>Operators list:</h2>
        
    <body>
        <table width="50%" border="1" bordercolordark="#000000" bordercolorlight="#FFFFFF" cellpadding="3" cellspacing="0">
        <tr > <font size="3" face="Verdana">
        <td>Login</td>
        <td>Password</td>
        <td>Name</td>
        <td>Surname</td>
        <td>Latitude</td>
        <td>Longitude</td>
        <td>Update</td>
        <td>Delete</td>

             </font>
        </tr> 

<% for(int i=1; i<operators.size(); i++){ %>

        <tr> <font size="1" face="Verdana">
            <td><%=operators.get(i).getLogin()%></td>
            <td><%=operators.get(i).getPassword()%></td>
            <td><%=operators.get(i).getName()%></td>
            <td><%=operators.get(i).getSurname()%></td>
            <td><%=String.format("%.2f", operators.get(i).getLatitude())%></td>
            <td><%=String.format("%.2f", operators.get(i).getLongitude())%></td>
            <td><A href=edit.jsp?id_operator=<%=operators.get(i).getId()%> >Edit</A> </td>
            <td><A href=delete.jsp?id_operator=<%=operators.get(i).getId()%> >Delete</A> </td>
            </font>
        </tr>
<% } %>

    </table>
    <br/><br/>
    <A href="../../init.jsp" >Back</A>
</html>