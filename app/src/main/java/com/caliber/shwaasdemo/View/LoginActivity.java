package com.caliber.shwaasdemo.View;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.caliber.shwaasdemo.R;
import com.caliber.shwaasdemo.Utils.SessionManager;
import com.caliber.shwaasdemo.Utils.URLs;
import com.caliber.shwaasdemo.Utils.Util;
import com.caliber.shwaasdemo.Utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static int REQUEST_LOGIN_COUNTER = 0;
    private EditText _emailText;
    private EditText _passwordText;
    private TextView _forgetPassword;
    private Button _loginButton;
    private LinearLayout linearLayout;
    // Session Manager Class
    private SessionManager session;
    private Context context;
    private boolean comingFromForgetPassword = false;

    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //  ButterKnife.inject(this);
   /*@InjectView(R.id.input_email)*/
        linearLayout = findViewById(R.id.linear);
        _forgetPassword = findViewById(R.id.forget_password);
        _emailText = findViewById(R.id.input_email);
   /* @InjectView(R.id.input_password)*/
        _passwordText = findViewById(R.id.input_password_login);

   /* @InjectView(R.id.btn_login)*/
        _loginButton = findViewById(R.id.btn_login);
   /* @InjectView(R.id.link_signup)
    TextView _signupLink;*/
        context = LoginActivity.this;
        // Session Manager
        session = new SessionManager(getApplicationContext());

        Intent in = getIntent();
        if (in != null){
            comingFromForgetPassword = in.getBooleanExtra("forgotPassord",false);
            if (comingFromForgetPassword){
                _forgetPassword.setVisibility(View.INVISIBLE);
            }

        }

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Check if no view has focus:
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (Util.checkInternetConnection(LoginActivity.this)) {
                    login();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(linearLayout, "Check Your Internet Connection!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
        _forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkInternetConnection(LoginActivity.this)) {
                    Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                    startActivity(intent);
                } else {
                    Snackbar snackbar = Snackbar
                            .make(linearLayout, "Check Your Internet Connection!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

    }


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }
        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        //If every thing is fine
        StringRequest loginRequest = new StringRequest(Request.Method.GET, URLs.URL_LOGIN + email + "/password/" + password + "/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //   Doc_Details docDetails = new Doc_Details();
                            //  docDetails.setDocName(jsonObject.optString("name"));
                            final String name = jsonObject.optString("name");
                            // docDetails.setDocName(name);
                            //  docDetails.setDocEmail(jsonObject.optString("email"));
                            final String mail = jsonObject.optString("email");
                            final String password = jsonObject.optString("password");
                            //   docDetails.setDocEmail(mail);
                            //   docDetails.setDocHospital(jsonObject.optString("hospitalName"));
                            //    Log.d("a12",""+docDetails.getDocHospital());
                            //   docDetails.setDocFirstLogin(jsonObject.optBoolean("firstLogin",false));
                            JSONObject json = jsonObject.optJSONObject("hospital");
                            String hospital = null;
                            if(json !=null) {
                               hospital = json.optString("hospitalName", null);
                            }
                            //    docDetails.setDocHospital(hospital);
                            session.createLoginSession(false, name, mail, hospital, password);

                            boolean isFirstLogin = jsonObject.optBoolean("firstLogin");
                            Log.d("aaaa", "" + isFirstLogin);
                            //    docDetails.setDocFirstLogin(isFirstLogin);
                            progressDialog.dismiss();
                            if (isFirstLogin) {
                              /*  Log.d("a12",""+docDetails.getDocEmail());
                                Log.d("a12",""+docDetails.getDocName());*/
                                REQUEST_LOGIN_COUNTER = 0;
                                Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                                // intent.putExtra("sampleObject", docDetails);
                                startActivity(intent);
                            } else {
                                final String finalHospital = hospital;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        REQUEST_LOGIN_COUNTER = 0;
                                        session.createLoginSession(true, name, mail, finalHospital, password);
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        // intent.putExtra("sampleObject", docDetails);
                                        startActivity(intent);
                                        finish();
                                    }
                                },4000);

                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
                                View mView = getLayoutInflater().inflate(R.layout.dialog_welcome, null);
                                mBuilder.setView(mView);
                                final AlertDialog dialog = mBuilder.create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                _loginButton.setEnabled(true);
                REQUEST_LOGIN_COUNTER++;
                if (REQUEST_LOGIN_COUNTER == 3) {
                    REQUEST_LOGIN_COUNTER = 0;
                    Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                    // intent.putExtra("sampleObject", docDetails);
                    startActivity(intent);
                }
                    else Toast.makeText(getApplicationContext(), "Login Failed. Try again!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", email);
                params.put("password", password);
                return params;
            }
        };
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(loginRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the HomeActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    public void onLoginFailed() {
        //  Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        Snackbar snackbar = Snackbar
                .make(linearLayout, "Login failed!", Snackbar.LENGTH_LONG);
        snackbar.show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.requestFocus();
            _emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.requestFocus();
            _passwordText.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
