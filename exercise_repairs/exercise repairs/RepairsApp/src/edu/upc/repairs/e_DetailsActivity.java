package edu.upc.repairs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import edu.upc.repairs.comms.RPC;
import entity.WorkOrder;

public class e_DetailsActivity extends Activity implements View.OnClickListener {

  _GlobalState globalState;
  Spinner spinner;

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    globalState = (_GlobalState)getApplication();
    setContentView(R.layout.e_details);

    spinner = (Spinner) findViewById(R.id.workorderSpinner);
    ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.states,
        android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);

    ((Button) findViewById(R.id.workorderSendButton)).setOnClickListener(this);

    // we don't save any temporary change on state and comments of the workOrder unless sent the
    // update, otherwise these sentences would have to be executed at onResume(),
    // and saved the updates at onPause():
    ((TextView)findViewById(R.id.workorderTitle)).setText(globalState.workOrder.getTitle());
    ((TextView)findViewById(R.id.workorderClient)).setText(globalState.workOrder.getClient().getName());
    ((TextView)findViewById(R.id.workorderAddress)).setText(globalState.workOrder.getClient().getAddress());
    ((TextView)findViewById(R.id.workorderDetails)).setText(globalState.workOrder.getDetails());
    spinner.setSelection(globalState.workOrder.getState(), true);
    ((EditText)findViewById(R.id.workorderComments)).setText(globalState.workOrder.getComments());
  }

  public void onClick(View arg0) {
    if (arg0 == findViewById(R.id.workorderSendButton)) {
      WorkOrder workOrder_to_update = globalState.workOrder.hardCopy();
      workOrder_to_update.setState(spinner.getSelectedItemPosition());
      workOrder_to_update.setComments(((EditText)findViewById(R.id.workorderComments)).getText().toString());
      new UpdateWorkOrder_Task().execute(workOrder_to_update);
    }
  }

  private class UpdateWorkOrder_Task extends AsyncTask<WorkOrder, Void, WorkOrder> {

    ProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {
      progressDialog = ProgressDialog.show(e_DetailsActivity.this,
          "DetailsActivity", "updating the workOrder...");
    }

    @Override
    protected WorkOrder doInBackground(WorkOrder... workorders) {
      return RPC.updateWorkOrder(workorders[0]);
    }

    @Override
    protected void onPostExecute(WorkOrder workorder_updated) {
      progressDialog.dismiss();
      if (workorder_updated == null) {
        toastShow("Error updating the workOrder due to: "+
            workorder_updated.getTitle());
      } else {
        toastShow("workOrder updated!!!");
      }
    }
  }

  private void toastShow(String text) {
    Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
    toast.setGravity(0, 0, 200);
    toast.show();
  }
}
