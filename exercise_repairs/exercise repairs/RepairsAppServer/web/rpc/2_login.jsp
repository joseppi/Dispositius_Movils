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
  Operator operator_to_check = gson.fromJson(rd, Operator.class);

  System.out.println("login of: ("+operator_to_check.getLogin()+","+operator_to_check.getPassword()+")");

  for(Operator operator : global.operators){
    if(operator.getLogin().equals(operator_to_check.getLogin()) && 
       operator.getPassword().equals(operator_to_check.getPassword())){
      out.clear();
      out.println(gson.toJson(operator));
      return;
    }
  }
  
  Operator operator = new Operator(-1);
  out.clear();
  out.println(gson.toJson(operator));

%>