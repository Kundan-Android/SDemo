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

public class ChangePasswordActivity extends AppCompatActivity {
    private Button btn_save;
    private EditText passwordBox1, passwordBox2;
    LinearLayout linearLayout;
    String password1 = "";
    String email = "";
    String ResultStatusMessage = "";

    ProgressDialog progressDialog;
    // Session Manager Class
    SessionManager session;
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Hospital address (make variable public to access from outside)
    public static final String KEY_HOSPITAL = "hospital";

    public static final String KEY_PASSWORD = "password_CALIBER";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        btn_save = findViewById(R.id.btn_save);
        linearLayout = findViewById(R.id.linear2);
        passwordBox1 = findViewById(R.id.input_change_password1);
        passwordBox2 = findViewById(R.id.input_change_password2);

        progressDialog = new ProgressDialog(ChangePasswordActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        // Session Manager
        session = new SessionManager(getApplicationContext());

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }
                if (Util.checkInternetConnection(ChangePasswordActivity.this)) {
                    if (!validate()) {
                        onSavingFailed();
                        return;
                    }
                    btn_save.setEnabled(false);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Saving...");
                    progressDialog.show();
                    //  Toast.makeText(ChangePasswordActivity.this, "Send the password to server", Toast.LENGTH_SHORT).show();
                    makeServiceCall();
                    progressDialog.dismiss();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(linearLayout, "Check Your Internet Connection!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    private void makeServiceCall() {
        final String changedPassword = passwordBox1.getText().toString();
        HashMap<String, String> user = new HashMap<String, String>();
        user = session.getUserDetails();
        final String name = user.get(KEY_NAME);
        email = user.get(KEY_EMAIL);
        final String hospital = user.get(KEY_HOSPITAL);
        final String password = user.get(KEY_PASSWORD);

        Log.d("a456", String.valueOf(user.get(KEY_NAME)));
        Log.d("a456", String.valueOf(user.get(KEY_EMAIL)));
        Log.d("a456", String.valueOf(user.get(KEY_HOSPITAL)));
        Log.d("a456", String.valueOf(user.get(KEY_PASSWORD)));


        //If every thing is fine
        StringRequest loginRequest = new StringRequest(Request.Method.PUT, URLs.URL_LOGIN + email + "/password/" + changedPassword + "/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //   Doc_Details docDetails = new Doc_Details();
                            //  docDetails.setDocName(jsonObject.optString("name"));
                           /* String name = jsonObject.optString("name");
                            // docDetails.setDocName(name);
                            //  docDetails.setDocEmail(jsonObject.optString("email"));
                            String mail = jsonObject.optString("email");
                            //   docDetails.setDocEmail(mail);
                            //   docDetails.setDocHospital(jsonObject.optString("hospitalName"));
                            //    Log.d("a12",""+docDetails.getDocHospital());
                            //   docDetails.setDocFirstLogin(jsonObject.optBoolean("firstLogin",false));
                            JSONObject json = jsonObject.optJSONObject("hospital");
                            String hospital = json.optString("hospitalName");*/
                            //    docDetails.setDocHospital(hospital);
                            //      session.createLoginSession(name,mail,hospital);
                            // String ResultStatusMessage = jsonObject.optString("");
                            session.createLoginSession(true, name, email, hospital, password);
                            progressDialog.dismiss();
                            Snackbar snackbar = Snackbar
                                    .make(linearLayout, "password changed successfully!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            Intent intent = new Intent(ChangePasswordActivity.this, HomeActivity.class);
                            // intent.putExtra("sampleObject", docDetails);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                btn_save.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Saving Failed. Try again!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KEY_EMAIL, email);
                params.put(KEY_PASSWORD, password1);
                return params;
            }
        };
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(loginRequest);
    }

    public void onSavingFailed() {
        //  Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        Snackbar snackbar = Snackbar
                .make(linearLayout, "Login failed!", Snackbar.LENGTH_LONG);
        snackbar.show();
        btn_save.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        password1 = passwordBox1.getText().toString();
        String password2 = passwordBox2.getText().toString();

        if (password1.isEmpty() || password1.length() < 4 || password1.length() > 10) {
            passwordBox1.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordBox1.setError(null);
        }
        Log.d("val1", "validate: " + password1);
        Log.d("val1", "validate: " + password2);
        if (!password1.equals(password2)) {

            // Toast.makeText(this, "Password is not matched !", Toast.LENGTH_SHORT).show();
            passwordBox2.setError("confirm password not matched!");
            valid = false;
        }

        return valid;
    }

}
