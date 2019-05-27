package nam.com.rausach.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import nam.com.rausach.R;

public class SingUpActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    EditText edName;
    EditText edEmail;
    EditText edPassword;
    Button btnSingup;
    TextView tvLoginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        edName = findViewById(R.id.input_name);
        edEmail = findViewById(R.id.input_email);
        btnSingup = findViewById(R.id.btn_signup);
        edPassword = findViewById(R.id.input_password);
        tvLoginLink = findViewById(R.id.link_login);



        btnSingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        tvLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SingUpActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        btnSingup.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SingUpActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = edName.getText().toString();
        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


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

        String name = edName.getText().toString();
        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            edName.setError("at least 3 characters");
            valid = false;
        } else {
            edName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmail.setError("enter a valid email address");
            valid = false;
        } else {
            edEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            edPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            edPassword.setError(null);
        }

        return valid;
    }
}
