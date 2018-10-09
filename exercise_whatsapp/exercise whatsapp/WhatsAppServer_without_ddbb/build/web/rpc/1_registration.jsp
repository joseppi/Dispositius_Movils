<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="util.DateSerializerDeserializer"%>
<%@page import="java.util.Date"%>
<%@page import="entity.UserInfo"%>
<%@page import="entity.User"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page pageEncoding="UTF-8"%>

<jsp:useBean id="global" scope="application" class="util.Global" />

<%

  BufferedReader rd = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
  Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializerDeserializer()).create();
  User new_user = gson.fromJson(rd, User.class);
  UserInfo new_userinfo = new_user.getUserInfo();

  System.out.println("registering of: "+new_user.getUserInfo().getName());

  for(User user : global.users){
    if(user.getLogin().equals(new_user.getLogin())){
      UserInfo userinfo = new UserInfo(-1);
      out.clear();
      out.println(gson.toJson(userinfo));
      return;
    }
  }
  
  if (global.users.isEmpty()) {
    new_userinfo.setId(0);
    new_user.setId(0);
    }
  else{
    User last_user = global.users.get(global.users.size() - 1);
    new_userinfo.setId(last_user.getId() + 1);
    new_user.setId(last_user.getId() + 1);
  }
  global.users.add(new_user);
  
  out.clear();
  out.println(gson.toJson(new_userinfo));

%>