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
import com.caliber.shwaasdemo.Utils.SessionManager;
import com.caliber.shwaasdemo.Utils.URLs;
import com.caliber.shwaasdemo.Utils.Util;
import com.caliber.shwaasdemo.Utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Nav_ChangePasswordActivity extends AppCompatActivity {
    private EditText _currentPassword, _newPassword, _confirmNewPassword;
    private Button _btnSave;
    ProgressDialog progressDialog;
    // Session Manager Class
    SessionManager session;
    String new_Password1 = "";
    String Current_password = "";
    String currentPasswordformDB = "";
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Hospital address (make variable public to access from outside)
    public static final String KEY_HOSPITAL = "hospital";

    public static final String KEY_PASSWORD = "password_CALIBER";
    public static final String KEY_CURRENT_PASSWORD = "current_password_CALIBER";
    LinearLayout linearLayout;
    HashMap<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav__change_password);
        _currentPassword = findViewById(R.id.Nav_current_password);
        _newPassword = findViewById(R.id.Nav_change_password1);
        _confirmNewPassword = findViewById(R.id.nav_change_password2);
        _btnSave = findViewById(R.id.btn_savePassword);
        progressDialog = new ProgressDialog(Nav_ChangePasswordActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        linearLayout = findViewById(R.id.linear3);
        // Session Manager
        session = new SessionManager(getApplicationContext());
        //If every thing is fine
        user = new HashMap<String, String>();
        user = session.getUserDetails();

        currentPasswordformDB = user.get(KEY_PASSWORD);
        _btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkInternetConnection(Nav_ChangePasswordActivity.this)) {
                    progressDialog.show();
                    View view1 = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                    }
                    if (!validate()) {
                        onSavingFailed();
                        return;
                    }
                    SaveNewPassword();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(linearLayout, "Check Your Internet Connection!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    private void SaveNewPassword() {
        final String name = user.get(KEY_NAME);
        final String email = user.get(KEY_EMAIL);
        final String hospital = user.get(KEY_HOSPITAL);
        StringRequest loginRequest = new StringRequest(Request.Method.PUT, URLs.URL_LOGIN + email + "/password/" + new_Password1 + "/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            progressDialog.dismiss();
                            Toast.makeText(Nav_ChangePasswordActivity.this, "password changed successfully!", Toast.LENGTH_SHORT).show();
                            session.createLoginSession(true, name, email, hospital, new_Password1);
                            Intent intent = new Intent(Nav_ChangePasswordActivity.this, HomeActivity.class);
                            // intent.putExtra("sampleObject", docDetails);
                            finish();
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                _btnSave.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Saving Failed. Try again!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KEY_PASSWORD, new_Password1);
                params.put(KEY_CURRENT_PASSWORD, Current_password);
                return params;
            }
        };
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(loginRequest);
    }

    public boolean validate() {
        boolean valid = true;

        Current_password = _currentPassword.getText().toString();
        new_Password1 = _newPassword.getText().toString();
        String confirm_New_Password2 = _confirmNewPassword.getText().toString();

        if (Current_password.isEmpty() || Current_password.length() < 4 || Current_password.length() > 10) {
            _currentPassword.requestFocus();
            _currentPassword.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _currentPassword.setError(null);
        }
        if (new_Password1.isEmpty() || new_Password1.length() < 4 || new_Password1.length() > 10) {
            _newPassword.requestFocus();
            _newPassword.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _newPassword.setError(null);
        }
        if (confirm_New_Password2.isEmpty() || confirm_New_Password2.length() < 4 || confirm_New_Password2.length() > 10) {
            _confirmNewPassword.requestFocus();
            _confirmNewPassword.setError("confirm password is not matching with new password!");
            valid = false;
        } else {
            _confirmNewPassword.setError(null);
        }
        Log.d("val1", "validate: " + new_Password1);
        Log.d("val1", "validate: " + confirm_New_Password2);
        if (!confirm_New_Password2.equals(new_Password1)) {

            // Toast.makeText(this, "Password is not matched !", Toast.LENGTH_SHORT).show();
            _confirmNewPassword.requestFocus();
            _confirmNewPassword.setError("confirm password not matched!");
            valid = false;
        }
        if (!currentPasswordformDB.equals(Current_password)) {
            _currentPassword.requestFocus();
            _currentPassword.setError("current password is not valid!");
            Snackbar snackbar = Snackbar
                    .make(linearLayout, "current password is not valid!", Snackbar.LENGTH_LONG);
            snackbar.show();
            valid = false;
        }

        return valid;
    }

    public void onSavingFailed() {
        //  Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
        Snackbar snackbar = Snackbar
                .make(linearLayout, "saving failed!", Snackbar.LENGTH_LONG);
        snackbar.show();
        _btnSave.setEnabled(true);
    }

}
