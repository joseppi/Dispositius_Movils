<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="webSocketService.WebSocketServer"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="util.DateSerializerDeserializer"%>
<%@page import="java.util.Date"%>
<%@page import="entity.Message"%>
<%@page import="entity.UserInfo"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page pageEncoding="UTF-8"%>

<jsp:useBean id="global" scope="application" class="util.Global" />

<%

  Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializerDeserializer()).create();
  
  int id_1 = Integer.parseInt(request.getParameter("id_1"));
  int id_2 = Integer.parseInt(request.getParameter("id_2"));
  
  System.out.println("retrieving messages of: ("+id_1+","+id_2+")");

  List<Message> messages = new ArrayList<Message>();
  
  for(Message message : global.messages){
    if(message.getUserSender().getId()==id_1 && message.getUserReceiver().getId()==id_2){
      messages.add(message);
    }
    if(message.getUserSender().getId()==id_2 && message.getUserReceiver().getId()==id_1){
      messages.add(message);
    }
  }
  
  out.clear();
  out.println(gson.toJson(messages));

%>