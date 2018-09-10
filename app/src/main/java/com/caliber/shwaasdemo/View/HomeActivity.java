package com.caliber.shwaasdemo.View;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.caliber.shwaasdemo.Model.ClickListener;
import com.caliber.shwaasdemo.Model.Doc_Details;
import com.caliber.shwaasdemo.Model.Patient;
import com.caliber.shwaasdemo.Model.PatientAdapter;
import com.caliber.shwaasdemo.Model.TestDate;
import com.caliber.shwaasdemo.R;
import com.caliber.shwaasdemo.Utils.CircleTransform;
import com.caliber.shwaasdemo.Utils.SessionManager;
import com.caliber.shwaasdemo.Utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
                                                                SearchView.OnQueryTextListener, ClickListener {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    EditText _emailText;
    EditText _passwordText;
    TextView _signupLink;
    Button _loginButton;
    DrawerLayout drawer;
    NavigationView navigationView;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private View navHeader;
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    // copy from home activity
    private List<Patient> patientList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PatientAdapter mPatientAdapter;
    private SharedPreferences prefs = null;
    String url = "http://shwaascloudsolution-env.us-east-2.elasticbeanstalk.com/patient";
    String reportUrl = "http://shwaascloudsolution-env.us-east-2.elasticbeanstalk.com/report/patient/";
    //  String url = "http://default-environment.ynnpj6c276.us-east-2.elasticbeanstalk.com/patient";
    ProgressDialog progressDialog;
    private LinearLayout linearLayout;
    ConnectivityManager connec;
    Context context;
    private TextView mNameView, mGenderView, mDoBView;
    int number = 0;
    Patient patient;
    int file_size = 0;
    private final int ALL_PERMISSIONS_GRANTED = 1;
    MenuItem hospitalName;
    Doc_Details doc_details;
    // Session Manager Class
    SessionManager session;
    int mReportNumber = 0;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    private InputMethodManager imm;

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Hospital address (make variable public to access from outside)
    public static final String KEY_HOSPITAL = "hospital";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.header_textName);
        txtWebsite = (TextView) navHeader.findViewById(R.id.header_textWebsite);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.header_imageView);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // get menu from navigationView
        Menu menu = navigationView.getMenu();
        //    hospitalName = menu.findItem(R.id.nav_hospital);
        // Session Manager
        session = new SessionManager(getApplicationContext());

        arrayAdapter = new ArrayAdapter<String>(HomeActivity.this,android.R.layout.select_dialog_singlechoice);

        // copy from home activity
        linearLayout = (LinearLayout) findViewById(R.id
                .linearlayout);
        connec = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        recyclerView = findViewById(R.id.recycler_view);
        progressDialog = new ProgressDialog(HomeActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        context = HomeActivity.this;
        listView = (ListView) findViewById(R.id.date_list);
        mNameView = findViewById(R.id.nameId);
        mGenderView = findViewById(R.id.genderId);
        mDoBView = findViewById(R.id.dobId);
        if (Util.checkInternetConnection(this)) new HomeActivity.DownloadTask().execute(url);
        else {
            mNameView.setVisibility(View.INVISIBLE);
            mGenderView.setVisibility(View.INVISIBLE);
            mDoBView.setVisibility(View.INVISIBLE);
            progressDialog.dismiss();
            Snackbar snackbar = Snackbar
                    .make(linearLayout, "Check Your Internet Connection", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }
        checkStorageLocationPermission();

        mPatientAdapter = new PatientAdapter(context,patientList,this);
        prefs = getSharedPreferences("com.caliber.shwaasdemo", MODE_PRIVATE);
        prefs.edit().putBoolean("firstRun", false).commit();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mPatientAdapter);


        // load nav menu header data
        if (Util.checkInternetConnection(this)) loadNavHeader();
        else {
            Snackbar snackbar = Snackbar
                    .make(linearLayout, "Check Your Internet Connection!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }


    }


 /*Commented due search view issue*/
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Patient> filteredModelList = filter(patientList, newText);
        mPatientAdapter.setFilter(filteredModelList);
        return true;
    }
    private List<Patient> filter(List<Patient> models, String query) {
        query = query.toLowerCase();
        final List<Patient> filteredModelList = new ArrayList<>();
        for (Patient model : models) {
            final String text = model.getName().toLowerCase();
            final String age = model.getBirthYear().toLowerCase();
            if (text.contains(query) || age.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onClick(Patient patient) {
        if (Util.checkInternetConnection(HomeActivity.this)) {
       // Toast.makeText(context, ""+patient.getPatientId(), Toast.LENGTH_SHORT).show();
        number = patient.getPatientId();
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        new NumberOfReport().execute(reportUrl + "" + number);
       /* AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomeActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_demo, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        Button monoButton = mView.findViewById(R.id.btn_mono);
        Button multiButton = mView.findViewById(R.id.btn_multi);
        monoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
               // Toast.makeText(HomeActivity.this, "mono", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, ReportActivity.class);
                intent.putExtra("123", number);
                intent.putExtra("mode", "mono");
                progressDialog.dismiss();
                startActivity(intent);
            }
        });
        multiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
             //   Toast.makeText(HomeActivity.this, "multi", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, ReportActivity.class);
                intent.putExtra("123", number);
                Log.d("zxc",""+number);
                intent.putExtra("mode", "multi");
                progressDialog.dismiss();
                startActivity(intent);
            }
        });

        dialog.show();*/
        }else {
            Snackbar snackbar = Snackbar
                    .make(linearLayout, "Check Your Internet Connection!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    public void onLongClick(Patient patient) {
        //no Use
    }

    public class DownloadTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Integer doInBackground(String... strings) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();
                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    parseResult(response.toString());
                    result = 1;
                } else {

                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {
                Log.d("ABC", e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {

            progressDialog.dismiss();
            if (result == 1) {
                recyclerView.setAdapter(mPatientAdapter);
            } else {
                Toast.makeText(HomeActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }

        private void parseResult(String result) {
            try {
                JSONArray response = new JSONArray(result);
                // patientList = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    JSONObject resul = response.getJSONObject(i);
                    // JSONObject post = resul.optJSONObject();
                    Patient item = new Patient();
                    String fname = resul.optString("firstName");
                    String lname = resul.optString("lastName");
                    String name = fname + " " + lname;
                    if (name != null && name.length() > 12) item.setName(fname);
                        // item.setName(resul.optString("firstName")+" "+ resul.optString("lastName"));
                    else item.setName(name);
                    String gender = resul.optString("gender");
                    if (gender != null) {
                        switch (gender) {
                            case "0":
                                gender = "male";
                                break;
                            case "1":
                                gender = "female";
                                break;
                            case "2":
                                gender = "other";
                                break;
                            case "3":
                                gender = "N/A";
                                break;
                            default:
                                gender = "N/A";

                        }
                    }
                    item.setGender(gender);
                    item.setBirthYear(resul.optString("dob"));
                    item.setPatientId(resul.optInt("patientId"));
                    item.setCreatedOn(resul.optString("createdDate"));
                    patientList.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public class NumberOfReport extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();
                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    parseResult(response.toString());
                    result = 1;
                } else {

                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {
                Log.d("ABC", e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressDialog.dismiss();
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(HomeActivity.this);
            //builderSingle.setIcon(R.drawable.ic_launcher);
            builderSingle.setTitle("Select Date:-");

            builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    arrayAdapter.clear();
                }
            });

            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    String strName = arrayAdapter.getItem(which);
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomeActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_demo, null);
                    mBuilder.setView(mView);
                    arrayAdapter.clear();
                    final AlertDialog dialog1 = mBuilder.create();
                    Button monoButton = mView.findViewById(R.id.btn_mono);
                    Button multiButton = mView.findViewById(R.id.btn_multi);
                    monoButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog1.dismiss();
                            // Toast.makeText(HomeActivity.this, "mono", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HomeActivity.this, ReportActivity.class);
                            intent.putExtra("123", number);
                            intent.putExtra("mode", "mono");
                            intent.putExtra("numberTest", which);
                            progressDialog.dismiss();
                            startActivity(intent);
                        }
                    });
                    multiButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog1.dismiss();
                            //   Toast.makeText(HomeActivity.this, "multi", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HomeActivity.this, ReportActivity.class);
                            intent.putExtra("123", number);
                            Log.d("zxc",""+number);
                            intent.putExtra("mode", "multi");
                            intent.putExtra("numberTest", which);
                            progressDialog.dismiss();
                            startActivity(intent);
                        }
                    });

                    dialog1.show();
                   /* AlertDialog.Builder builderInner = new AlertDialog.Builder(HomeActivity.this);
                    builderInner.setMessage(strName);
                    builderInner.setTitle("Your Selected Item is");
                    builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int which) {
                            dialog.dismiss();
                            arrayAdapter.clear();
                        }
                    });
                    builderInner.show();*/

                }
            });
            builderSingle.setCancelable(false);
            builderSingle.show();


         /*   AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomeActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.report_list, null);
            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            Button monoButton = mView.findViewById(R.id.btn_mono);
            Button multiButton = mView.findViewById(R.id.btn_multi);
            monoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    // Toast.makeText(HomeActivity.this, "mono", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, ReportActivity.class);
                    intent.putExtra("123", number);
                    intent.putExtra("mode", "mono");
                    progressDialog.dismiss();
                    startActivity(intent);
                }
            });
            multiButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    //   Toast.makeText(HomeActivity.this, "multi", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, ReportActivity.class);
                    intent.putExtra("123", number);
                    Log.d("zxc",""+number);
                    intent.putExtra("mode", "multi");
                    progressDialog.dismiss();
                    startActivity(intent);
                }
            });

            dialog.show(); */
        }
    }
    private void parseResult(String result) {
        try {
            JSONArray response = new JSONArray(result);
            String tempReportID = "";
            String tempReportDate = "";
            TestDate testDate = new TestDate();
            arrayAdapter.clear();
            for (int i = 0; i < response.length(); i++) {
                JSONObject resul = response.getJSONObject(i);
                tempReportID = resul.optString("reportId");
                tempReportDate = resul.optString("testDate");
                testDate.setmDate(tempReportDate);
                arrayAdapter.add(tempReportDate);

             //   ek array list bnao fir usme "reportId" and "date created" ko  parse kr k add kro fir then usse listview me show kro.

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void loadNavHeader() {
        HashMap<String, String> user = new HashMap<String, String>();
        user = session.getUserDetails();
        String name = user.get(KEY_NAME);
        String email = user.get(KEY_EMAIL);
        String hospital = user.get(KEY_HOSPITAL);
        txtName.setText(name);
        txtWebsite.setText(email + "\n" + hospital + " Hospital, Bangalore");

        Glide.with(HomeActivity.this).load(urlProfileImg)
                .override(100, 100)
                .centerCrop()
                .crossFade().thumbnail(0.5f).bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        arrayAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
          /*Commented due to searchview issue */ final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
         searchView.setOnQueryTextListener(this);


        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                    // Do something when collapsed
                        mPatientAdapter.setFilter(patientList);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                    // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.signOut) {
            // Handle the camera action
            Toast.makeText(this, "Signing out", Toast.LENGTH_SHORT).show();
            session.logoutUser();
        } else if (id == R.id.nav_password_change) {
            Intent intent = new Intent(this, Nav_ChangePasswordActivity.class);
            startActivity(intent);
            Toast.makeText(this, "password change", Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void checkStorageLocationPermission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, ALL_PERMISSIONS_GRANTED);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            // case ALL_PERMISSIONS_GRANTED : Toast.makeText(context, "permission "+requestCode, Toast.LENGTH_SHORT).show();

        }
    }
}
