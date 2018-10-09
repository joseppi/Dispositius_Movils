package edu.upc.repairs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import edu.upc.repairs.comms.RPC;
import edu.upc.repairs.adapter.MyAdapter_WorkOrders;
import entity.WorkOrder;
import entity.Operator;

import java.util.List;

public class d_WorkOrdersActivity_1 extends Activity implements ListView.OnItemClickListener {

  _GlobalState globalState;
  MyAdapter_WorkOrders adapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    globalState = (_GlobalState) getApplication();
    setContentView(R.layout.d_workorders);
  }

  @Override
  protected void onResume() {
    super.onResume();
    new DownloadWorkOrders_Task().execute(globalState.my_operator);
  }

  @Override
  public void onItemClick(AdapterView<?> l, View v, int position, long id) {
    globalState.workOrder = ((MyAdapter_WorkOrders) l.getAdapter()).workOrders.get(position);
    startActivity(new Intent(this, e_DetailsActivity.class));
  }

  private class DownloadWorkOrders_Task extends AsyncTask<Operator, Void, List<WorkOrder>> {

    ProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {
      progressDialog = ProgressDialog.show(d_WorkOrdersActivity_1.this,
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
        adapter = new MyAdapter_WorkOrders(d_WorkOrdersActivity_1.this, workorders);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(d_WorkOrdersActivity_1.this);
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
