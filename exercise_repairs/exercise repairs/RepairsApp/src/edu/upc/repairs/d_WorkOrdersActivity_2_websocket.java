package edu.upc.repairs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.glassfish.tyrus.client.ClientManager;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import edu.upc.repairs.adapter.MyAdapter_WorkOrders;
import edu.upc.repairs.comms.RPC;

import static edu.upc.repairs.comms.Comms.ENDPOINT;
import static edu.upc.repairs.comms.Comms.gson;
import entity.Operator;
import entity.WorkOrder;

public class d_WorkOrdersActivity_2_websocket extends Activity implements ListView.OnItemClickListener {

  _GlobalState globalState;
  MyAdapter_WorkOrders adapter;
  ListView listView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    globalState = (_GlobalState) getApplication();
    setContentView(R.layout.d_workorders);
    new Thread(new Runnable() {
      public void run() {
        connectToServer();
      }
    }).start();
  }

  @Override
  protected void onResume() {
    super.onResume();
    new DownloadWorkOrders_Task().execute(globalState.my_operator);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    new Thread(new Runnable() {
      public void run() {
        disconnectFromServer();
      }
    }).start();
  }

  @Override
  public void onItemClick(AdapterView<?> l, View v, int position, long id) {
    globalState.workOrder = ((MyAdapter_WorkOrders) l.getAdapter()).workOrders.get(position);
    startActivity(new Intent(this, e_DetailsActivity.class));
  }

  Session session;

  private void connectToServer(){
    try {
      ClientManager client = ClientManager.createClient();

      client.connectToServer(new MyEndPoint(),
          ClientEndpointConfig.Builder.create().build(),
          URI.create(ENDPOINT));
    }
    catch (Exception e) {
      e.printStackTrace();
      sendMessageToHandler("error","connectToServer error");
      session = null;
    }
  }
  private void disconnectFromServer(){
    if(session!=null){
      try {
        session.close();
      } catch (IOException e) {
        e.printStackTrace();
        sendMessageToHandler("error","disconnectFromServer error");
      }
    }
  }
  //this is executed by an independent thread:
  public class MyEndPoint extends Endpoint {

    @Override
    public void onOpen(Session session, EndpointConfig EndpointConfig) {
      try {
        String json = gson.toJson(globalState.my_operator);
        session.getBasicRemote().sendText(json);
        d_WorkOrdersActivity_2_websocket.this.session = session;
        sendMessageToHandler("open","connection opened");

        session.addMessageHandler(new MessageHandler.Whole<String>() {
          @Override
          public void onMessage(String workOrder) {
            sendMessageToHandler("workOrder", workOrder);
          }
        });
      }
      catch (Exception e) {
        e.printStackTrace();
        sendMessageToHandler("error","onOpen error: "+e.getMessage());
      }
    }

    @Override
    public void onError(Session session, Throwable t) {
      t.printStackTrace();
      sendMessageToHandler("error","onError error: "+t.getMessage());
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
      sendMessageToHandler("close","connection closed");
      d_WorkOrdersActivity_2_websocket.this.session = null;
    }
  }

  private void sendMessageToHandler(String type, String content){
    android.os.Message msg = handler.obtainMessage();
    Bundle bundle = new Bundle();
    bundle.putCharSequence("type", type);
    bundle.putCharSequence("content", content);
    msg.setData(bundle);
    handler.sendMessage(msg);
  }

  Handler handler = new Handler() {
    @Override
    public void handleMessage(android.os.Message msg) {
      String type = msg.getData().getCharSequence("type").toString();
      String content = msg.getData().getCharSequence("content").toString();
      if(type.equals("workOrder")){

        WorkOrder new_workOrder = gson.fromJson(content, WorkOrder.class);

        if(new_workOrder.getOperator().getId().intValue()==globalState.my_operator.getId()){
          globalState.append_a_workOrder(new_workOrder);
          adapter.workOrders.add(new_workOrder);
          adapter.notifyDataSetChanged();
          listView.setSelection(listView.getCount() - 1);
          String name  = new_workOrder.getClient().getName();
          String title = new_workOrder.getTitle();
          toastShow("New WorkOrder!!!\n"+name+"\n"+title);
        }
        else{
          for(WorkOrder workOrder : adapter.workOrders){
            if(workOrder.getId().intValue()==new_workOrder.getId().intValue()){
              globalState.remove_a_workOrder(new_workOrder);
              adapter.workOrders.remove(workOrder);
              adapter.notifyDataSetChanged();
              listView.setSelection(listView.getCount() - 1);
              String name  = new_workOrder.getClient().getName();
              String title = new_workOrder.getTitle();
              toastShow("WorkOrder removed!!!\n"+name+"\n"+title);
              break;
            }
          }
        }
      }
      else{
        toastShow(content);
      }
    }
  };

  private class DownloadWorkOrders_Task extends AsyncTask<Operator, Void, List<WorkOrder>> {

    ProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {
      progressDialog = ProgressDialog.show(d_WorkOrdersActivity_2_websocket.this,
          "WorkOrdersActivity", "downloading your workOrders...");
    }

    @Override
    protected List<WorkOrder> doInBackground(Operator... operators) {
      return RPC.getWorkOrders(operators[0]);
    }

    @Override
    protected void onPostExecute(List<WorkOrder> workorders) {
      progressDialog.dismiss();
      if (workorders == null) {
        toastShow("There's been an error downloading your workOrders");
      } else {
        globalState.save_my_workOrders(workorders);
        adapter = new MyAdapter_WorkOrders(d_WorkOrdersActivity_2_websocket.this, workorders);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(d_WorkOrdersActivity_2_websocket.this);
      }
    }
  }

  static final int MENU_REFRESH = 0;
  static final int MENU_VIEW_MAP = 1;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    menu.add(0, MENU_REFRESH, 0, "Refresh").setIcon(android.R.drawable.ic_menu_rotate);
    menu.add(0, MENU_VIEW_MAP, 0, "View Map").setIcon(android.R.drawable.ic_menu_mylocation);
    return true;
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case MENU_REFRESH:
        new DownloadWorkOrders_Task().execute(globalState.my_operator);
        return true;
      case MENU_VIEW_MAP:
        startActivity(new Intent(this, f_GoogleMapsActivity.class));
        return true;
    }
    return false;
  }

  private void toastShow(String text) {
    Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
    toast.setGravity(0, 0, 200);
    toast.show();
  }

}
