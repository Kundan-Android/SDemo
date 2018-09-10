package com.caliber.shwaasdemo.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.caliber.shwaasdemo.R;
import com.caliber.shwaasdemo.Utils.URLs;
import com.caliber.shwaasdemo.Utils.Util;
import com.caliber.shwaasdemo.Utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordActivity extends AppCompatActivity {
    Button _btnNext;
    EditText _textRegisteredMail;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        _btnNext = findViewById(R.id.btn_Next);
        linearLayout = findViewById(R.id.linear4);
        _textRegisteredMail = findViewById(R.id.input_RegisteredEmail);
        _btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if no view has focus:
                view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (Util.checkInternetConnection(ForgetPasswordActivity.this)) {
                    if (!validate()) {
                        onLoginFailed();
                        return;
                    }
                    _btnNext.setEnabled(false);
                    sendLinkToCloud();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(linearLayout, "Check Your Internet Connection!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    private void sendLinkToCloud() {
        final ProgressDialog progressDialog = new ProgressDialog(ForgetPasswordActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sending...");
        progressDialog.show();
        final String email = _textRegisteredMail.getText().toString();
        //If every thing is fine
        StringRequest loginRequest = new StringRequest(Request.Method.GET, URLs.URL_FORGOTPASSWORD + email,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.equals("Email not found")){
                                progressDialog.dismiss();
                                Toast.makeText(ForgetPasswordActivity.this, "This email is not registered.", Toast.LENGTH_SHORT).show();
                                _btnNext.setEnabled(true);
                            } else {
                                JSONObject jsonObject = new JSONObject(response);
                                progressDialog.dismiss();
                                Toast.makeText(ForgetPasswordActivity.this, "Password has been sent to your registered email.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                                intent.putExtra("forgotPassord", true);
                                finish();
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("909",error.toString());
                progressDialog.dismiss();
                _btnNext.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Login Failed. Try again!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", email);
                return params;
            }
        };
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(loginRequest);
    }

    public void onLoginFailed() {
        //  Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        Snackbar snackbar = Snackbar
                .make(linearLayout, "Please enter valid e-mail ID.", Snackbar.LENGTH_LONG);
        snackbar.show();
        _btnNext.setEnabled(true);
    }


    public boolean validate() {
        boolean valid = true;

        String email = _textRegisteredMail.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _textRegisteredMail.setError("Enter a valid email address");
            valid = false;
        } else {
            _textRegisteredMail.setError(null);
        }
        return valid;
    }
}
