package edu.upc.repairs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.upc.repairs.service.LocationService;
import edu.upc.repairs.service.PushService;

public class a_WelcomeActivity_persistence extends Activity implements View.OnClickListener {

  _GlobalState globalState;
  Button loginButton, registerButton;

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    globalState = (_GlobalState) getApplication();
    setContentView(R.layout.a_welcome);
    loginButton = (Button) findViewById(R.id.welcomeLoginButton);
    registerButton = (Button) findViewById(R.id.welcomeRegisterButton);
    loginButton.setOnClickListener(this);
    registerButton.setOnClickListener(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    if (globalState.isThere_my_operator()) {
      (loginButton).setText("NEXT");
      (registerButton).setText("LOGOUT");
    }
  }

  public void onClick(View arg0) {

    if (arg0 == loginButton && loginButton.getText().equals("LOGIN")) {
      startActivity(new Intent(this, b_LoginActivity.class));
    }
    if (arg0 == loginButton && loginButton.getText().equals("NEXT")) {
      startActivity(new Intent(this, d_WorkOrdersActivity_3_broadcast_receiver.class));
    }
    if (arg0 == registerButton && registerButton.getText().equals("REGISTER")) {
      startActivity(new Intent(this, c_RegistrationActivity.class));
    }
    if (arg0 == registerButton && registerButton.getText().equals("LOGOUT")) {
      globalState.remove_my_operator();
      stopService(new Intent(this, PushService.class));
      stopService(new Intent(this, LocationService.class));
      startActivity(new Intent(this, a_WelcomeActivity_persistence.class));
      finish();
    }
  }

}
