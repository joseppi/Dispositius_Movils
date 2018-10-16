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

public class c_RegistrationActivity extends Activity implements View.OnClickListener {

  _GlobalState globalState;
  ProgressDialog progressDialog;
  Operator new_operator;
  OperationPerformer operationPerformer;

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    globalState = (_GlobalState) getApplication();
    setContentView(R.layout.c_registration);
    ((Button) findViewById(R.id.editregistrationButton)).setOnClickListener(this);
  }

  public void onClick(View arg0) {
    if (arg0 == findViewById(R.id.editregistrationButton)) {
      new_operator = new Operator();
      new_operator.setLogin(((EditText) findViewById(R.id.editregistrationLoginEditText)).getText().toString());
      new_operator.setPassword(((EditText) findViewById(R.id.editregistrationPasswordEditText)).getText().toString());
      new_operator.setName(((EditText) findViewById(R.id.editregistrationNameEditText)).getText().toString());
      new_operator.setSurname(((EditText) findViewById(R.id.editregistrationSurnameEditText)).getText().toString());
      progressDialog = ProgressDialog.show(this, "RegistrationActivity", "Registering to service...");
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

      Operator operator_reply = RPC.registration(new_operator);
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
        toastShow("Registration successful");
        globalState.my_operator = operator_reply;
//        globalState.save_my_operator();
//        startService(new Intent(c_RegistrationActivity.this, PushService.class));
//        startService(new Intent(c_RegistrationActivity.this, LocationService.class));
        startActivity(new Intent(c_RegistrationActivity.this, d_WorkOrdersActivity_1.class));
        finish();
      } else if (operator_reply.getId() == -1) {
        toastShow("Registration unsuccessful,\nlogin already used by another operator");
      } else if (operator_reply.getId() == -2) {
        toastShow("Not registered, connection problem due to: " + operator_reply.getName());
        System.out.println("--------------------------------------------------");
        System.out.println("error!!!");
        System.out.println(operator_reply.getName());
        System.out.println("--------------------------------------------------");
      }
    }
  };

  private void toastShow(String text) {
    Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
    toast.setGravity(0, 0, 200);
    toast.show();
  }
}
