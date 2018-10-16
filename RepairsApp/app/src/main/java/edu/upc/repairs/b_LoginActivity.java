package edu.upc.repairs;

import edu.upc.repairs.comms.RPC;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.upc.repairs.service.LocationService;
import edu.upc.repairs.service.PushService;
import entity.Operator;

public class b_LoginActivity extends Activity implements View.OnClickListener {

  _GlobalState globalState;
  ProgressDialog progressDialog;
  Operator operator_to_login;
  OperationPerformer operationPerformer;

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    globalState = (_GlobalState) getApplication();
    setContentView(R.layout.b_login);
    ((Button) findViewById(R.id.editloginButton)).setOnClickListener(this);
  }

  public void onClick(View arg0) {
    if (arg0 == findViewById(R.id.editloginButton)) {
      operator_to_login = new Operator();
      operator_to_login.setLogin(((EditText) findViewById(R.id.editloginLoginEditText)).getText().toString());
      operator_to_login.setPassword(((EditText) findViewById(R.id.editloginPasswordEditText)).getText().toString());
      progressDialog = ProgressDialog.show(this, "LoginActivity", "Logging into the server...");
      // if there's still a running thread doing something, we don't create a new one
      if (operationPerformer == null) {
        operationPerformer = new OperationPerformer();
        operationPerformer.start();
      }
    }
  }

  private class OperationPerformer extends Thread {

    @Override
    public void run() {
      Message msg = handler.obtainMessage();
      Bundle b = new Bundle();

      Operator operator_reply = RPC.login(operator_to_login);
      b.putSerializable("operator_reply", operator_reply);

      msg.setData(b);
      handler.sendMessage(msg);
    }
  }

  Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {

      operationPerformer = null;
      progressDialog.dismiss();

      Operator operator_reply = (Operator) msg.getData().getSerializable("operator_reply");

      if (operator_reply.getId() >= 0) {
        toastShow("Login successful");
        globalState.my_operator = operator_reply;
//        globalState.save_my_operator();
//        startService(new Intent(b_LoginActivity.this, PushService.class));
//        startService(new Intent(b_LoginActivity.this, LocationService.class));
        startActivity(new Intent(b_LoginActivity.this, d_WorkOrdersActivity_1.class));
        finish();
      } else if (operator_reply.getId() == -1) {
        toastShow("Login unsuccessful, try again please.");
      } else if (operator_reply.getId() == -2) {
        toastShow("Not logged in, connection problem due to: " + operator_reply.getName());
      }

    }
  };

  private void toastShow(String text) {
    Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
    toast.setGravity(0, 0, 200);
    toast.show();
  }
}
