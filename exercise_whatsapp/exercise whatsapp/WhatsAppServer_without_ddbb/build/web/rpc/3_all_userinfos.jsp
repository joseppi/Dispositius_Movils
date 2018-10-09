<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="webSocketService.WebSocketServer"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="util.DateSerializerDeserializer"%>
<%@page import="java.util.Date"%>
<%@page import="entity.User"%>
<%@page import="entity.UserInfo"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page pageEncoding="UTF-8"%>

<jsp:useBean id="global" scope="application" class="util.Global" />

<%

  Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializerDeserializer()).create();
   
  System.out.println("requesting all_userinfos");

  List<UserInfo> userinfos = new ArrayList<UserInfo>();
  
  for(User user : global.users){
    userinfos.add(user.getUserInfo());
  }
  
  out.clear();
  out.println(gson.toJson(userinfos));

%>