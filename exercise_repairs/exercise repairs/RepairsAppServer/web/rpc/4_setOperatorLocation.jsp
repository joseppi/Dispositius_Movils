<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="util.DateSerializerDeserializer"%>
<%@page import="java.util.Date"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.google.gson.Gson"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="entity.Operator"%>
<%@page import="java.io.*"%>

<jsp:useBean id="global" scope="application" class="util.Global" />

<%

  BufferedReader rd = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
  Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializerDeserializer()).create();
  Operator operator = gson.fromJson(rd, Operator.class);

  System.out.println("setting Operator Location of: ("+operator.getName()+" "+operator.getSurname()+")");
  System.out.println("latitude: "+operator.getLatitude()+" longitude: "+operator.getLongitude());

  for(Operator ope : global.operators){
    if(ope.getId().intValue()==operator.getId().intValue()){
      ope.setLatitude(operator.getLatitude());
      ope.setLongitude(operator.getLongitude());
      ope.setDate(operator.getDate());
      break;
    }
  }
  
  out.clear();
  out.println(gson.toJson(operator));
  
%>