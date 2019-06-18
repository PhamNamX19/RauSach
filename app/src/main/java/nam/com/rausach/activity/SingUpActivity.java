package nam.com.rausach.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import nam.com.rausach.R;
import nam.com.rausach.utils.Constant;
import nam.com.rausach.utils.Server;

public class SingUpActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    EditText edConfirmPassword;
    EditText edAccount;
    EditText edPassword;
    Button btnSingup;
    TextView tvLoginLink;

    ProgressDialog pDialog;
    Intent intent;

    int success;
    ConnectivityManager conMgr;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        edConfirmPassword = findViewById(R.id.input_confirm_password);
        edAccount = findViewById(R.id.input_email);
        btnSingup = findViewById(R.id.btn_signup);
        edPassword = findViewById(R.id.input_password);
        tvLoginLink = findViewById(R.id.link_login);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        tvLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edAccount.getText().toString();
                String password = edPassword.getText().toString();
                String confirm_password = edConfirmPassword.getText().toString();

                if (validate()) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        checkRegister(username, password, confirm_password);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void checkRegister(final String username, final String password, final String confirm_password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Register ...");
        showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest strReq = new StringRequest(Request.Method.POST, Server.pathRegister, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.e("Successfully Register!", jObj.toString());

                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        edAccount.setText("");
                        edPassword.setText("");
                        edConfirmPassword.setText("");

                        intent = new Intent(SingUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USERNAME_LOCAL, username);
                params.put(Constant.PASSWORD_LOCAL, password);
                params.put("confirm_password", confirm_password);

                return params;
            }
        };

        // Adding request to request queue
        requestQueue.add(strReq);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(SingUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

//    public void signup() {
//        Log.d(TAG, "Signup");
//
//        if (!validate()) {
//            onSignupFailed();
//            return;
//        }
//
//        btnSingup.setEnabled(false);
//
//        final ProgressDialog progressDialog = new ProgressDialog(SingUpActivity.this);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Creating Account...");
//        progressDialog.show();
//
//        String name = edConfirmPassword.getText().toString();
//        String email = edAccount.getText().toString();
//        String password = edPassword.getText().toString();
//
//        // TODO: Implement your own signup logic here.
//
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onSignupSuccess or onSignupFailed
//                        // depending on success
//                        onSignupSuccess();
//                        // onSignupFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
//    }


    public void onSignupSuccess() {
        btnSingup.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        btnSingup.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String confirmPassword = edConfirmPassword.getText().toString();
        String account = edAccount.getText().toString();
        String password = edPassword.getText().toString();

        if (account.isEmpty() || account.length() < 4 || account.length() > 10) {
            edAccount.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            edAccount.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            edPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            edPassword.setError(null);
        }

        if (confirmPassword.isEmpty() || !password.equals(confirmPassword)) {
            edConfirmPassword.setError("password not found");
            valid = false;
        } else {
            edConfirmPassword.setError(null);
        }

        return valid;
    }
}
