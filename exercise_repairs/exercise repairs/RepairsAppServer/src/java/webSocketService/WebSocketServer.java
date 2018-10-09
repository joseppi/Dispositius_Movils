package webSocketService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Operator;
import entity.WorkOrder;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import util.DateSerializerDeserializer;

@ServerEndpoint("/push")
public class WebSocketServer {
  
  private static Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializerDeserializer()).create();

  //alive connections with remote operators:
  private static Map<Session, Integer> sessions = new HashMap<Session, Integer>();
  private static Map<Integer, Session> operator_ids = new HashMap<Integer, Session>();

  @OnMessage
  public void onMessage(String message, Session session)
    throws IOException, SQLException {
    System.out.println("WebSocketServer - new connection from Operator: " + message);
    Operator operator = gson.fromJson(message, Operator.class);
    sessions.put(session, operator.getId());
    operator_ids.put(operator.getId(), session);
  }

  @OnOpen
  public void onOpen(Session session) {
    System.out.println("WebSocketServer - new session: " + session.getId());
  }

  @OnClose
  public void onClose(Session session) {
    System.out.println("WebSocketServer - closed session: " + session.getId());
    Integer operator_id = sessions.get(session);
    sessions.remove(session);
    operator_ids.remove(operator_id);
  }
  
  @OnError
  public void onError(Session session, Throwable t) {
    System.err.println("WebSocketServer - Error on session: " + session.getId());
  }

  public static void push_new_WorkOrder(WorkOrder workOrder) {
    Operator push_ope = workOrder.getOperator();
    Session session = operator_ids.get(push_ope.getId());
    if (session != null) {
      if (!session.isOpen()) {
        sessions.remove(session);
        operator_ids.remove(push_ope.getId());
      } else {
        try {
          String json_workorder = gson.toJson(workOrder);
          session.getBasicRemote().sendText(json_workorder);
          System.out.println("Sending workOrder: " + json_workorder
            + " to: " + push_ope.getName() + ", " + push_ope.getSurname());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static void push_remove_WorkOrder(WorkOrder workOrder, Operator old_ope) {
    Session session = operator_ids.get(old_ope.getId());
    if (session != null) {
      if (!session.isOpen()) {
        sessions.remove(session);
        operator_ids.remove(old_ope.getId());
      } else {
        try {
          String json_workorder = gson.toJson(workOrder);
          session.getBasicRemote().sendText(json_workorder);
          System.out.println("Removing workOrder: " + json_workorder
            + " to: " + old_ope.getName() + ", " + old_ope.getSurname());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
}
