package nam.com.rausach.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import nam.com.rausach.R;
import nam.com.rausach.utils.Constant;
import nam.com.rausach.utils.Server;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private String username;
    private String password;
    private ProgressDialog pDialog;
    private EditText edId;
    private EditText edPassword;
    private Button btnLogin, btnLoginFacbook, btnLoginGmail;
    private LoginButton loginFb;
    private TextView tvSignupLink;
    private CallbackManager callbackManager;

    //lấy thông tin người dùng facebook
    String id, name, birthday, email;

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String idUser;
    int success;
    ConnectivityManager conMgr;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponent();
        loginFacebook();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        sharedpreferences = getSharedPreferences(Constant.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(Constant.SESSION_STATUS, false);
        id = sharedpreferences.getString(Constant.ID_LOCAL, null);
        username = sharedpreferences.getString(Constant.USERNAME_LOCAL, null);
    }

    private void checkLogin(final String username, final String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in ...");
        showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest strReq = new StringRequest(Request.Method.POST, Server.pathLogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(Constant.TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        String username = jObj.getString(Constant.USERNAME_LOCAL);
                        String idUser = jObj.getString(Constant.ID_LOCAL);
                        Log.e("Successfully Login!", jObj.toString());
                        Toast.makeText(getApplicationContext(), jObj.getString(Constant.TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        // lưu thông tin đăng nhập
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(Constant.SESSION_STATUS, true);
                        editor.putString(Constant.ID_LOCAL, idUser);
                        editor.putString(Constant.USERNAME_LOCAL, username);
                        editor.putString(Constant.LOGINFACEBOOK, "false");
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(Constant.TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        Log.d(TAG, "checkLoginstr: " + strReq.toString());
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

    private void initComponent() {
        edId = findViewById(R.id.input_email_login);
        edPassword = findViewById(R.id.input_password_login);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        tvSignupLink = findViewById(R.id.link_signup);
        tvSignupLink.setOnClickListener(this);
        btnLoginFacbook = findViewById(R.id.btnFacebook);
        btnLoginFacbook.setOnClickListener(this);
        btnLoginGmail = findViewById(R.id.btnEmail);
        btnLoginGmail.setOnClickListener(this);
        loginFb = findViewById(R.id.login_facebook);
        loginFb.setOnClickListener(this);
    }

    private void loginFacebook() {
        loginFb.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();
        loginFb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d("Use ID", "Use ID" + loginResult.getAccessToken().getUserId() + "\n" +
                        "Auth Token: " + loginResult.getAccessToken().getToken());

                loginFb.setVisibility(View.INVISIBLE);
                getFbInfo();
            }

            @Override
            public void onCancel() {
                Log.d("Use ID", "Login canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("Use ID", "Login failed.");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                username = edId.getText().toString().toLowerCase().trim();
                password = edPassword.getText().toString().trim();
                if (validateInputs()) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        Log.d(TAG, "onClick: " + username + password);
                        checkLogin(username, password);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.link_signup:
                Intent intent = new Intent(getApplicationContext(), SingUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                break;
            case R.id.btnFacebook:
                Toast.makeText(this, "đăng nhập bằng fb", Toast.LENGTH_SHORT).show();
                if (view == btnLoginFacbook) {
                    loginFb.performClick();
                }
                break;
            case R.id.btnEmail:
                Toast.makeText(this, "đăng nhập bằng email", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void getFbInfo() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        Log.d("JSON", response.getJSONObject().toString());

                        try {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            id = object.getString("id");
                            name = object.getString("name");
                            email = object.getString("email");
                            // birthday = object.getString("birthday");
                            //image_url = "http://graph.facebook.com/" + id + "/picture?type=large";

//                            intent.putExtra(Constant.ID_USER, id);
//                            intent.putExtra(Constant.NAME, name);
//                            intent.putExtra(Constant.EMAIL, email);
//                            // intent.putExtra(Constant.BIRTHDAY, birthday);
//                            //intent.putExtra("image_url", image_url);
//                            intent.putExtra(Constant.LOGINFACEBOOK, "true");

                            SharedPreferences.Editor editor=sharedpreferences.edit();
                            editor.putString(Constant.ID_USER, id);
                            editor.putString(Constant.NAME, name);
                            editor.putString(Constant.EMAIL, email);
                            editor.putString(Constant.LOGINFACEBOOK, "true");
                            editor.apply();

                            //finish();
                            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Authenticating...");
                            progressDialog.show();
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "JSONException", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture"); // id,first_name,last_name,email,gender,birthday,cover,picture.type(large)
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onStart() {
        LoginManager.getInstance().logOut();
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                //this.finish();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void onLoginSuccess() {
        btnLogin.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        btnLogin.setEnabled(true);
    }

    private boolean validateInputs() {
        if (Constant.KEY_EMPTY.equals(username)) {
            edId.setError("Username cannot be empty");
            edId.requestFocus();
            return false;
        }
        if (Constant.KEY_EMPTY.equals(password)) {
            edPassword.setError("Password cannot be empty");
            edPassword.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case 200:

                boolean fineloction = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean coarselocation = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                boolean readstorage = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                boolean writestorage = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                break;

        }

    }
}
