package nam.com.rausach.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import nam.com.rausach.R;
import nam.com.rausach.utils.Constant;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgAvatarPro;
    private TextView tvNamePro;
    private TextView tvBirtdayPro;
    private TextView tvSexPro;
    private TextView tvEmailPro;
    private TextView tvPhonePro;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initComponent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInfoFacebook();
    }

    private void getInfoFacebook() {
        String name = getIntent().getStringExtra(Constant.NAME);
        String email = getIntent().getStringExtra(Constant.EMAIL);
        String birthday = getIntent().getStringExtra(Constant.BIRTHDAY);
        String id = getIntent().getStringExtra(Constant.ID_USER);
        String loginFacebook = getIntent().getStringExtra(Constant.LOGINFACEBOOK);

        tvNamePro.setText(name);
        tvBirtdayPro.setText(birthday);
        tvEmailPro.setText(email);
        Picasso.with(ProfileActivity.this).load("https://graph.facebook.com/" + id + "/picture?type=large").into(imgAvatarPro);
    }

    private void initComponent() {
        imgAvatarPro = findViewById(R.id.imgAvatarPro);
        tvNamePro = findViewById(R.id.tvNamePro);
        tvSexPro = findViewById(R.id.tvSexPro);
        tvBirtdayPro = findViewById(R.id.tvDateOfBirthPro);
        tvEmailPro = findViewById(R.id.tvEmailPro);
        tvPhonePro = findViewById(R.id.tvPhonePro);
        imgBack = findViewById(R.id.btnBack);
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnEdit:
                Toast.makeText(this, "Edit profile", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
