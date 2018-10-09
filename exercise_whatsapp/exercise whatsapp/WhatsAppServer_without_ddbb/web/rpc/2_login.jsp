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

  BufferedReader rd = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
  Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializerDeserializer()).create();
  User user_to_check = gson.fromJson(rd, User.class);

  System.out.println("login of: ("+user_to_check.getLogin()+","+user_to_check.getPassword()+")");

  for(User user : global.users){
    if(user.getLogin().equals(user_to_check.getLogin()) && 
       user.getPassword().equals(user_to_check.getPassword())){
      out.clear();
      out.println(gson.toJson(user.getUserInfo()));
      return;
    }
  }
  
  UserInfo userinfo = new UserInfo(-1);
  out.clear();
  out.println(gson.toJson(userinfo));

%>