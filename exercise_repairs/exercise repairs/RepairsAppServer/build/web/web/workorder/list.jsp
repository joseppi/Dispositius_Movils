<%@page import="java.util.List"%>
<%@page import="entity.WorkOrder"%>
<%@ page import="util.Global" %>
<jsp:useBean id="global" scope="application" class="util.Global" />

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
     
<%
    List<WorkOrder> workOrders = global.workorders;
%> 
    
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>JSP Page</title>
    </head>

        <h2>WorkOrders list:</h2>
        
    <body>
        <table width="80%" border="1" bordercolordark="#000000" bordercolorlight="#FFFFFF" cellpadding="3" cellspacing="0">
        <tr > <font size="3" face="Verdana">
            
        <td>Title</td>
        <td>Client</td>
        <td>Address</td>
        <td>Details</td>
        <td>Operator</td>
        <td>State</td>
        <td>Comments</td>

        <td>Update</td>
        <td>Delete</td>
        <td>Assign</td>
             </font>
        </tr> 

<% for(int i=0; i<workOrders.size(); i++){ %>

        <tr> <font size="1" face="Verdana">
            <td><%=workOrders.get(i).getTitle()%></td>
            <td><%=workOrders.get(i).getClient().getName()%></td>
            <td><%=workOrders.get(i).getClient().getAddress()%></td>
            <td><%=workOrders.get(i).getDetails()%></td>
            <td><%=workOrders.get(i).getOperator().getName()%></td>

            <% switch (workOrders.get(i).getState()){
                case 0:
            %>
                    <td>New</td>
            <%      break;
                case 1:
            %>
                    <td>No intervention</td>
            <%      break;
                case 2:
            %>
                    <td>Pending</td>
            <%      break;
                case 3:
            %>
                    <td>Solved</td>
            <%      break;
                }
            %>

            <td><%=workOrders.get(i).getComments()%></td>
            
            <td><A href=edit.jsp?id_workorder=<%=workOrders.get(i).getId()%> >Edit</A> </td>
            <td><A href=delete.jsp?id_workorder=<%=workOrders.get(i).getId()%> >Delete</A> </td>
            <td><A href=select.jsp?id_workorder=<%=workOrders.get(i).getId()%> >Assign</A> </td>
            </font>
        </tr>
<% } %>

    </table>
    <br/><br/>
    <A href="../../init.jsp" >Back</A>
    </body>
</html>
