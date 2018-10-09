package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Client;
import entity.Operator;
import entity.WorkOrder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author juanluis
 */
public class Global {

  public List<Client> clients;
  public List<Operator> operators;
  public List<WorkOrder> workorders;

  public Global() {

    clients = new ArrayList<Client>();
    operators = new ArrayList<Operator>();
    workorders = new ArrayList<WorkOrder>();

    File clients_file = new File("clients.txt");

    if (clients_file.exists()) {
      System.out.println("files exist: reading files");
      loadData();
    } else {
      System.out.println("files do not exist: using initialData");
      initialData();
    }
  }

  private void initialData() {
    Client client0 = new Client(0);
    client0.setName("UPC");
    client0.setAddress("c/Jordi Girona 1-3");
    client0.setLatitude(41.4298773824209);
    client0.setLongitude(2.20201349545703);

    Client client1 = new Client(1);
    client1.setName("La Caixa");
    client1.setAddress("Avg. Diagonal 576");
    client1.setLatitude(41.4187659173581);
    client1.setLongitude(2.19525217321957);

    Client client2 = new Client(2);
    client2.setName("Fundacio UPC");
    client2.setAddress("c/Badajoz 47");
    client2.setLatitude(41.4228952217698);
    client2.setLongitude(2.17888292315955);

    clients.add(client0);
    clients.add(client1);
    clients.add(client2);
    
    Operator operator0 = new Operator(0);
    operator0.setLogin("no ope");
    operator0.setPassword("none");
    operator0.setName("no ope");
    operator0.setSurname("no ope");

    Operator operator1 = new Operator(1);
    operator1.setLogin("john");
    operator1.setPassword("1234");
    operator1.setName("john");
    operator1.setSurname("smith");

    Operator operator2 = new Operator(2);
    operator2.setLogin("peter");
    operator2.setPassword("1234");
    operator2.setName("peter");
    operator2.setSurname("brown");

    operators.add(operator0);
    operators.add(operator1);
    operators.add(operator2);

    WorkOrder workOrder0 = new WorkOrder(0);
    workOrder0.setTitle("lift out of order");
    workOrder0.setClient(client0);
    workOrder0.setDate(new Date());
    workOrder0.setOperator(operator1);

    WorkOrder workOrder1 = new WorkOrder(1);
    workOrder1.setTitle("broken printer");
    workOrder1.setClient(client1);
    workOrder1.setDate(new Date());
    workOrder1.setOperator(operator1);

    WorkOrder workOrder2 = new WorkOrder(2);
    workOrder2.setTitle("electricity cut-off");
    workOrder2.setClient(client2);
    workOrder2.setDate(new Date());
    workOrder2.setOperator(operator2);

    workorders.add(workOrder0);
    workorders.add(workOrder1);
    workorders.add(workOrder2);
  }
  private void loadData() {

    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializerDeserializer()).create();

    try {
      FileReader fr_clients = new FileReader("clients.txt");
      BufferedReader br_clients = new BufferedReader(fr_clients);
      String json_client;
      while ((json_client = br_clients.readLine()) != null) {
        clients.add(gson.fromJson(json_client, Client.class));
      }
      fr_clients.close();

      FileReader fr_operators = new FileReader("operators.txt");
      BufferedReader br_operators = new BufferedReader(fr_operators);
      String json_operator;
      while ((json_operator = br_operators.readLine()) != null) {
        operators.add(gson.fromJson(json_operator, Operator.class));
      }
      fr_operators.close();

      FileReader fr_workorders = new FileReader("workorders.txt");
      BufferedReader br_workorders = new BufferedReader(fr_workorders);
      String json_workorder;
      while ((json_workorder = br_workorders.readLine()) != null) {
        workorders.add(gson.fromJson(json_workorder, WorkOrder.class));
      }
      fr_workorders.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public void storeData() {

    System.out.println("store data: saving files");

    File clients_file = new File("clients.txt");
    clients_file.delete();
    File operators_file = new File("operators.txt");
    operators_file.delete();
    File workorders_file = new File("workorders.txt");
    workorders_file.delete();

    try {

      clients_file.createNewFile();
      clients_file.setWritable(true);
      clients_file.setReadable(true);
      
      System.out.println("file location: " + clients_file.getAbsolutePath());
      
      operators_file.createNewFile();
      operators_file.setWritable(true);
      operators_file.setReadable(true);
      
      workorders_file.createNewFile();
      workorders_file.setWritable(true);
      workorders_file.setReadable(true);

      Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializerDeserializer()).create();

      PrintWriter pw_clients = new PrintWriter(clients_file);
      for (Client client : clients) {
        String json_client = gson.toJson(client);
        pw_clients.println(json_client);
        pw_clients.flush();
      }
      pw_clients.close();

      PrintWriter pw_operators = new PrintWriter(operators_file);
      for (Operator operator : operators) {
        String json_operator = gson.toJson(operator);
        pw_operators.println(json_operator);
        pw_operators.flush();
      }
      pw_operators.close();

      PrintWriter pw_workorders = new PrintWriter(workorders_file);
      for (WorkOrder workorder : workorders) {
        String json_workorder = gson.toJson(workorder);
        pw_workorders.println(json_workorder);
        pw_workorders.flush();
      }
      pw_workorders.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
