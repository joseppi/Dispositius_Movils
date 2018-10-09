package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.User;
import entity.Message;
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

  public List<User> users;
  public List<Message> messages;

  public Global() {

    users = new ArrayList<User>();
    messages = new ArrayList<Message>();

    File users_file = new File("users.txt");

    if (users_file.exists()) {
      System.out.println("files exist: reading files");
      loadData();
    } 
  }

  private void loadData() {

    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializerDeserializer()).create();

    try {

      FileReader fr_users = new FileReader("users.txt");
      BufferedReader br_users = new BufferedReader(fr_users);
      String json_user;
      while ((json_user = br_users.readLine()) != null) {
        users.add(gson.fromJson(json_user, User.class));
      }
      fr_users.close();

      FileReader fr_messages = new FileReader("messages.txt");
      BufferedReader br_messages = new BufferedReader(fr_messages);
      String json_message;
      while ((json_message = br_messages.readLine()) != null) {
        messages.add(gson.fromJson(json_message, Message.class));
      }
      fr_messages.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public void storeData() {

    System.out.println("store data: saving files");

    File users_file = new File("users.txt");
    users_file.delete();
    File messages_file = new File("messages.txt");
    messages_file.delete();

    try {
      
      users_file.createNewFile();
      users_file.setWritable(true);
      users_file.setReadable(true);
      
      System.out.println("file location: " + users_file.getAbsolutePath());
      
      messages_file.createNewFile();
      messages_file.setWritable(true);
      messages_file.setReadable(true);

      Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializerDeserializer()).create();

      PrintWriter pw_users = new PrintWriter(users_file);
      for (User user : users) {
        String json_user = gson.toJson(user);
        pw_users.println(json_user);
        pw_users.flush();
      }
      pw_users.close();

      PrintWriter pw_messages = new PrintWriter(messages_file);
      for (Message message : messages) {
        String json_message = gson.toJson(message);
        pw_messages.println(json_message);
        pw_messages.flush();
      }
      pw_messages.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
