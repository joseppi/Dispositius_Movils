<%@page import="entity.Operator"%>
<%@ page import="util.Global" %>
<jsp:useBean id="global" scope="application" class="util.Global" />

<%
  java.lang.String arg0 = request.getParameter("login");
  java.lang.String arg1 = request.getParameter("password");
  java.lang.String arg2 = request.getParameter("name");
  java.lang.String arg3 = request.getParameter("surname");

  Operator new_operator = new Operator();
  new_operator.setLogin(arg0);
  new_operator.setPassword(arg1);
  new_operator.setName(arg2);
  new_operator.setSurname(arg3);

  boolean operator_exists = false;
  for (Operator operator : global.operators) {
    if (operator.getLogin().equals(new_operator.getLogin())
      && operator.getPassword().equals(new_operator.getPassword())) {
        operator_exists = true;
        break;
    }
  }

  if (!operator_exists) {
    if (global.operators.isEmpty()) {
      new_operator.setId(0);
    } else {
      Operator last_operator = global.operators.get(global.operators.size() - 1);
      new_operator.setId(last_operator.getId() + 1);
    }
    global.operators.add(new_operator);
  }
%>

<jsp:forward page="list.jsp"></jsp:forward> 
