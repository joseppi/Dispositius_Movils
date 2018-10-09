<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="util.DateSerializerDeserializer"%>
<%@page import="java.util.Date"%>
<%@page import="entity.Operator"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page pageEncoding="UTF-8"%>

<jsp:useBean id="global" scope="application" class="util.Global" />

<%

  BufferedReader rd = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
  Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializerDeserializer()).create();
  Operator new_operator = gson.fromJson(rd, Operator.class);

  System.out.println("registering of: "+new_operator.getName());

  for(Operator operator : global.operators){
    if(operator.getLogin().equals(new_operator.getLogin())){
      operator = new Operator(-1);
      out.clear();
      out.println(gson.toJson(operator));
      return;
    }
  }
  
  if (global.operators.isEmpty()) {
    new_operator.setId(0);
    }
  else{
    Operator last_operator = global.operators.get(global.operators.size() - 1);
    new_operator.setId(last_operator.getId() + 1);
  }
  global.operators.add(new_operator);
  
  out.clear();
  out.println(gson.toJson(new_operator));

%>