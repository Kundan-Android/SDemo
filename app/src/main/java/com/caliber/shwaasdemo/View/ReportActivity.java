package com.caliber.shwaasdemo.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.caliber.shwaasdemo.Model.PatientDetails;
import com.caliber.shwaasdemo.R;
import com.caliber.shwaasdemo.Utils.ExternalStoragePath;
import com.caliber.shwaasdemo.Utils.HeaderFooterPageEvent;
import com.caliber.shwaasdemo.Utils.VolleySingleton;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.caliber.shwaasdemo.Utils.URLs.URL_MONO_REPORT;
import static com.caliber.shwaasdemo.Utils.URLs.URL_MULTI_REPORT;

public class ReportActivity extends Activity {
    private static String FILE = null;
    private Context context;
    private Button button;
    private com.itextpdf.text.Document document;
    private ArrayList<Entry> entriesR = new ArrayList<>();
    private ArrayList<Entry> entriesRawFlow = new ArrayList<>();
    private ArrayList<Entry> entriesX = new ArrayList<>();
    private ArrayList<Entry> entriesRawMP = new ArrayList<>();
    private ArrayList<String> labelsR = new ArrayList<String>();
    private ArrayList<String> labelsX = new ArrayList<String>();
    private ArrayList<String> labelsRawFLow = new ArrayList<String>();
    private ArrayList<String> labelsRawMP = new ArrayList<String>();
    private LineChart Rchart, Xchart;
    private LineChart RawFlowchart, RawMPchart;
    private TextView chartTitle1, chartTitle2;
    private LineDataSet datasetR;
    private LineDataSet datasetX;
    private LineDataSet datasetRawFLow;
    private LineDataSet datasetRawMP;
    private LineData dataR;
    private LineData dataRawFlow;
    private LineData dataX;
    private LineData dataRawMP;
    private TextView recyclableTextView;
    private TextView commentText;

    private LinearLayout linearLayout;
    private HeaderFooterPageEvent headerFooterPageEvent;
    private String url = "http://shwaascloudsolution-env.us-east-2.elasticbeanstalk.com/report/patient/";
    // String url = "http://default-environment.ynnpj6c276.us-east-2.elasticbeanstalk.com/rx/patient/";
    private ProgressDialog progressDialog;
    private PatientDetails patientDetails;
    private TextView mPatientNameTextView, mPatientIDTextView, mPatientGenderTextView, mPatientAgeTextView, mPatientSmokerTextView;
    private EditText commentEdittext;
    private  LinearLayout linearRgroup,linearXgroup;
    //private double[] act1 = new double[7];
    private int mPatientId, mPatientGender, mPatientAge, mPatientSmoker;
    private String gender, smoker, age;
    private int patientReportID;
    private Font title;
    private int intValue;
    private int testNumber;
    private String frequencyMode = "";
    private int mMonoFrequency = 0;
    private boolean resultStatus = false;
    private PdfWriter pdfWriter;
    private Double action1[] =  {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
    private Double action2[] =  {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
    private Double action3[] =  {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
    private Double action4[] =  {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
    private Double action5[] =  {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
    private Double action6[] =  {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
    private Double action7[] =  {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
    private Double action8[] =  {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
    private Double action9[] =  {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
    private Double action10[] =  {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
    private Double defaultAction[] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};

    private Double imped1[] = new Double[7];
    private Double imped2[] = new Double[7];
    private Double imped3[] = new Double[7];
    private Double imped4[] = new Double[7];
    private Double imped5[] = new Double[7];
    private Double imped6[] = new Double[7];
    private Double imped7[] = new Double[7];
    private Double imped8[] = new Double[7];
    private Double imped9[] = new Double[7];
    private Double imped10[] = new Double[7];
    private List<String> flowData = new ArrayList<>();
    private List<String> MpData = new ArrayList<>();

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Window window = ReportActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= 21)
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.primary));
        context = this;
        mPatientId = 0;
        mPatientGender = 0;
        mPatientAge = 0;
        mPatientSmoker = 0;
        title = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD | Font.UNDERLINE, BaseColor.GRAY);
        Rchart = findViewById(R.id.Rchart);
        RawFlowchart = findViewById(R.id.RawFlowchart);
        chartTitle1 = findViewById(R.id.RgraphTitle1);
        chartTitle2 = findViewById(R.id.XgraphTitle2);
        Rchart.getAxisLeft().setStartAtZero(false);
        Rchart.getAxisRight().setStartAtZero(false);
        YAxis yAxis = Rchart.getAxisLeft();
        yAxis.setAxisMaxValue(0.6f); // the axis maximum is 0.9f
        yAxis.setAxisMinValue(-0.6f); // the axis minimum is -0.9f

        YAxis yRightAxis = Rchart.getAxisRight();
        yRightAxis.setEnabled(false);

        RawFlowchart.getAxisLeft().setStartAtZero(false);
        RawFlowchart.getAxisRight().setStartAtZero(false);
        YAxis yAxisflow = RawFlowchart.getAxisLeft();
        yAxisflow.setAxisMaxValue(0.6f); // the axis maximum is 0.9f
        yAxisflow.setAxisMinValue(-0.6f); // the axis minimum is -0.9f

        YAxis yRightAxisflow = RawFlowchart.getAxisRight();
        yRightAxisflow.setEnabled(false);



        Xchart = findViewById(R.id.Xchart2);
        Xchart.getAxisLeft().setStartAtZero(false);
        Xchart.getAxisRight().setStartAtZero(false);
        YAxis yAxis2 = Xchart.getAxisLeft();
        yAxis2.setAxisMaxValue(1.0f); // the axis maximum is 0.9f
        yAxis2.setAxisMinValue(-6.0f); // the axis minimum is -0.9f
        YAxis yRightAxis2 = Xchart.getAxisRight();
        yRightAxis2.setEnabled(false);

        RawMPchart = findViewById(R.id.RawMpchart);
        RawMPchart.getAxisLeft().setStartAtZero(false);
        RawMPchart.getAxisRight().setStartAtZero(false);
        YAxis yAxisMpFLow = RawMPchart.getAxisLeft();
        yAxisMpFLow.setAxisMaxValue(1.0f); // the axis maximum is 0.9f
        yAxisMpFLow.setAxisMinValue(-6.0f); // the axis minimum is -0.9f
        YAxis yRightAxisMpFlowRT = RawMPchart.getAxisRight();
        yRightAxisMpFlowRT.setEnabled(false);

        RawMPchart.getAxisLeft().setStartAtZero(false);
        RawMPchart.getAxisRight().setStartAtZero(false);
        YAxis yAxisMP = RawMPchart.getAxisLeft();
        yAxisMP.setAxisMaxValue(0.6f); // the axis maximum is 0.9f
        yAxisMP.setAxisMinValue(-0.6f); // the axis minimum is -0.9f
        YAxis yRightAxisMP = RawMPchart.getAxisRight();
        yRightAxisMP.setEnabled(false);


        button = findViewById(R.id.download);
        commentText = findViewById(R.id.commentText);
        commentEdittext = findViewById(R.id.commentEditetxt);
        linearLayout = findViewById(R.id.commentLayout);
        mPatientNameTextView = findViewById(R.id.patientNameText);
        mPatientIDTextView = findViewById(R.id.patientIDText);
        mPatientAgeTextView = findViewById(R.id.ageText);
        mPatientGenderTextView = findViewById(R.id.genderText);
        mPatientSmokerTextView = findViewById(R.id.smokerText);
        linearRgroup = findViewById(R.id.RlinearRgroup);
        linearXgroup = findViewById(R.id.XlinearXgroup);
        progressDialog = new ProgressDialog(ReportActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Intent mIntent = getIntent();
        intValue = mIntent.getIntExtra("123", 0);
        testNumber = mIntent.getIntExtra("numberTest",0);
        frequencyMode = mIntent.getStringExtra("mode");

           /* if (frequencyMode != null && frequencyMode.equals("mono")) {
             //   linearRgroup.setVisibility(View.GONE);
             //   linearXgroup.setVisibility(View.GONE);
                chartTitle1.setText("---------- Flow raw data Graph -------------");
                chartTitle2.setText("---------- Mouth Pressure raw data Graph -------------");
            }*/

        patientDetails = new PatientDetails();
        new DownloadTask().execute(url + "" + intValue);



        headerFooterPageEvent = new HeaderFooterPageEvent();

        FILE = ExternalStoragePath.getExternalCardPath(ReportActivity.this)
                + "/";
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!commentEdittext.getText().toString().isEmpty()) {
                    try {
                        // create blank document
                        document = new com.itextpdf.text.Document(PageSize.A3);

                        // create pdf writer for writing into new created pdf document
                        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(FILE + "" + patientDetails.getmPatientname() + ".pdf"));
                        pdfWriter.setStrictImageSequence(true);
                        pdfWriter.setPageEvent(headerFooterPageEvent);
                        pdfWriter.getPageNumber();
                        document.open();
                        //add meta data
                        addMetaData(document);
                        //   document.add( Chunk.NEWLINE );
                        addTitlePageinPDF(document);
                        //border of page
                        PdfContentByte canvas = pdfWriter.getDirectContent();
                        Rectangle rect = new Rectangle(5, 5, document.getPageSize().getWidth() - 5.0f, document.getPageSize().getHeight() - 5.0f);
                        rect.setBorder(Rectangle.BOX);
                        rect.setBorderWidth(2);
                        canvas.rectangle(rect);

                        // ColumnText.showTextAligned(template,
                        //         Element.ALIGN_CENTER, new Phrase("Hello World"), x, y, rotation);
                        Paragraph paragraph1 = new Paragraph("R and X Value Table:\n", title);
                        document.add(paragraph1);
                        document.add(Chunk.NEWLINE);
                        document.add(createSecondTable());
                   /* Paragraph paragraph2 = new Paragraph("R value Graph:\n", title);
                    document.add(paragraph2);*/


                        canvas.rectangle(rect);
                        if (frequencyMode.equals("multi")) {
                            addDatatochartintopdf();
                        } else{
                            addDatatochartintopdf();
                        }

                        // add a couple of blank lines
                        document.add(Chunk.NEWLINE);
                        document.add(Chunk.NEWLINE);
                        document.add(Chunk.NEWLINE);

                        Paragraph paragraph3 = new Paragraph("X value Graph:\n", title);
                        Paragraph rawData = new Paragraph("Mouth Pressure raw data Graph:\n", title);

                        if (frequencyMode.equals("multi")) {
                            document.add(paragraph3);
                            addSecondDatatochartintopdf();
                        } else{
                            document.add(rawData);
                            addSecondDatatochartintopdf();
                        }
                        canvas.rectangle(rect);
                        // add a couple of blank lines
                        document.add(Chunk.NEWLINE);
                        document.add(Chunk.NEWLINE);
                        document.add(Chunk.NEWLINE);
                        document.add(Chunk.NEWLINE);
                        document.add(Chunk.NEWLINE);
                        String comment = commentEdittext.getText().toString();
                        if (!comment.matches("")) {
                            canvas.rectangle(rect);
                            doctorComment();
                        }
                        // chart.saveToGallery("mychart.jpg", 85); // 85 is the quality of the image
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // Close Document after writting all content
                    document.close();
                    Toast.makeText(ReportActivity.this, "File is saved", Toast.LENGTH_SHORT).show();
                    showPdf();
                }
                else {
                    Toast.makeText(context, "write comment before saving it.", Toast.LENGTH_SHORT).show();
                }

            }

        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentText.setText("Comment");
                commentEdittext.setVisibility(View.VISIBLE);

            }
        });

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
                String gen = patientDetails.getmGender();
                if (gen != null) {
                    switch (gen) {
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

                String smoke = patientDetails.getmSmoker();
                if (smoke != null) {
                    switch (smoke) {
                        case "0":
                            smoker = "yes(Regular)";
                            break;
                        case "1":
                            smoker = "no";
                            break;
                        case "2":
                            smoker = "chain";
                            break;
                        case "3":
                            smoker = "occasional";
                            break;
                        case "4":
                            smoker = "very rare";
                            break;
                        case "5":
                            smoker = "quit(recently)";
                            break;
                        case "6":
                            smoker = "quit(long back)";
                            break;

                    }
                }
                mPatientNameTextView.setText(patientDetails.getmPatientname());
                mPatientIDTextView.setText(patientDetails.getmPatientID());
                mPatientAgeTextView.setText(patientDetails.getmAge());
                mPatientGenderTextView.setText(gender);
                mPatientSmokerTextView.setText(smoker);
                progressDialog.show();
               /* if (frequencyMode != null && frequencyMode.equals("multi")) {
                    Log.d("zxc", ""+frequencyMode);*/
                    fetchRXValuefromCloud();
               /* } else if(frequencyMode != null && frequencyMode.equals("mono")) {*/
                    fetchZValuefromCloud();
               /* }*/
            }
            else if(result==0){
                Toast.makeText(context, "There is no data available for this patient", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(ReportActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void parseResult(String result) {
        try {
            JSONArray response = new JSONArray(result);
            // patientList = new ArrayList<>();
            for (int j = 0; j<response.length();j++){
                JSONObject object = response.getJSONObject(j);
                patientReportID = object.optInt("reportId"); break;
            }

            for (int i = 0; i < response.length(); i++) {
                JSONObject resul = response.getJSONObject(i);
                double freq = resul.optDouble("frequency", 0.0d);
                resultStatus = resul.optBoolean("result", false);
             //   patientReportID = resul.optInt("reportId");
                patientDetails.setFreq(freq);
                patientDetails.setResult(resultStatus);
                patientDetails.setmPatientReportID(patientReportID);
                JSONObject patient = resul.getJSONObject("patients");
                patientDetails.setmPatientID(patient.optString("patientId"));
                patientDetails.setmPatientID(patient.optString("patientId", null));
                patientDetails.setmPatientname(patient.optString("firstName", null) + " " + patient.optString("lastName", null));
                patientDetails.setmGender(patient.optString("gender", "3"));
                patientDetails.setmAge(patient.optString("dob", null));
                patientDetails.setmSmoker(patient.optString("smoker", null));

            }
            patientDetails.setmPatientReportID(patientDetails.getmPatientReportID() + testNumber);
         //   patientDetails.setmPatientReportID(428);
            Log.d("MainActivity",""+patientDetails.getmPatientReportID());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void fetchRXValuefromCloud(){
        if (patientDetails.getmPatientReportID() != 0) {
            String url = URL_MULTI_REPORT + patientDetails.getmPatientReportID();
            Log.d("ReportActivity",url);
        }
    StringRequest multiReport = new StringRequest(Request.Method.GET,
            URL_MULTI_REPORT + patientDetails.getmPatientReportID(),
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("axs", response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        Log.d("nm123",""+jsonArray.length());
                        for (int i = 0; i < jsonArray.length();i++){
                            double ac[] = new double[9];
                            JSONObject obj = jsonArray.optJSONObject(i);
                            String actNo1 = obj.optString("actNo");
                            ac[0] = Double.parseDouble(obj.optString("r6"));
                            ac[1]  = Double.parseDouble(obj.optString("r12"));
                            ac[2] = Double.parseDouble(obj.optString("r20"));
                            //ac[3] = 0.5;//Double.parseDouble(obj.optString("r30",""));
                            ac[3]  = Double.parseDouble(obj.optString("x6"));
                            ac[4]  = Double.parseDouble(obj.optString("x12"));
                            ac[5]  = Double.parseDouble(obj.optString("x20"));
                            //ac[7] = 0.5;//Double.parseDouble(obj.optString("x30",""));
                            ac[6]  = obj.optDouble("frequency", 0.0d);
                            boolean status  = obj.optBoolean("result", false);
                            ac[7] = status ? 1.0 : 0.0;

                            if (i == 0 ){
                                for (int j = 0; j<=7; j++){
                                    action1[j] = ac[j];
                                    defaultAction[j] = ac[j];
                                }
                            }
                            if (i == 1 ){
                                for (int j = 0; j<=7; j++){
                                    action2[j] = ac[j];
                                    defaultAction[j] = ac[j];
                                }
                            }
                            if (i == 2 ){
                                for (int j = 0; j<=7; j++){
                                    action3[j] = ac[j];
                                    defaultAction[j] = ac[j];
                                }
                            }
                            if (i == 3 ){
                                for (int j = 0; j<=7; j++){
                                    action4[j] = ac[j];
                                    defaultAction[j] = ac[j];
                                }
                            }
                            if (i == 4 ){
                                for (int j = 0; j<=7; j++){
                                    action5[j] = ac[j];
                                    defaultAction[j] = ac[j];
                                }
                            }
                            if (i == 5 ){
                                for (int j = 0; j<=7; j++){
                                    action6[j] = ac[j];
                                    defaultAction[j] = ac[j];
                                }
                            }
                            if (i == 6 ){
                                for (int j = 0; j<=7; j++){
                                    action7[j] = ac[j];
                                    defaultAction[j] = ac[j];
                                }
                            }
                            if (i == 7 ){
                                for (int j = 0; j<=7; j++){
                                    action8[j] = ac[j];
                                    defaultAction[j] = ac[j];
                                }
                            }
                            if (i == 8 ){
                                for (int j = 0; j<=7; j++){
                                    action9[j] = ac[j];
                                    defaultAction[j] = ac[j];
                                }
                            }
                            if (i == 9 ){
                                for (int j = 0; j<=7; j++){
                                    action10[j] = ac[j];
                                    defaultAction[j] = ac[j];
                                }
                            }
                        }
                        creatingRXZTableOnScreen();
                        creatingRXTableOnScreen();
                        addRGraphtoScreen();
                        addXGraphtoScreen();

                        progressDialog.dismiss();
                    } catch (JSONException | NumberFormatException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("nm", "onErrorResponse: "+error.toString());
            progressDialog.dismiss();

        }
    });
                multiReport.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
             VolleySingleton.getInstance(this).addToRequestQueue(multiReport);

    }
    private void fetchZValuefromCloud() {
        Log.d("ReportActivity",""+URL_MONO_REPORT + patientDetails.getmPatientReportID()+testNumber);
        int a = (patientDetails.getmPatientReportID());
        StringRequest monoReport = new StringRequest(Request.Method.GET, URL_MONO_REPORT + patientDetails.getmPatientReportID()+testNumber,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("zxc", "onResponse: "+response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i<jsonArray.length();i++){
                                double imped[] = new double[7];
                                JSONObject object = jsonArray.getJSONObject(i);
                                mMonoFrequency = (int) Double.parseDouble(object.optString("frequency"));
                                imped[0] = Double.parseDouble(object.optString("z"));
                                if (i == 0 ){
                                        switch (mMonoFrequency){
                                            case  6:  imped1[0] = imped[0]; break;
                                            case 10:  imped1[1] = imped[0]; break;
                                            case 14:  imped1[2] = imped[0]; break;
                                            case 20:  imped1[3] = imped[0]; break;
                                            case 22:  imped1[4] = imped[0]; break;
                                        }
                                        imped1[5] = Double.valueOf(mMonoFrequency);

                                }
                                if (i == 1 ){
                                    switch (mMonoFrequency){
                                        case  6:  imped2[0] = imped[0]; break;
                                        case 10:  imped2[1] = imped[0]; break;
                                        case 14:  imped2[2] = imped[0]; break;
                                        case 20:  imped2[3] = imped[0]; break;
                                        case 22:  imped2[4] = imped[0]; break;
                                    }
                                        imped2[5] = Double.valueOf(mMonoFrequency);
                                }
                                if (i == 2 ){
                                    switch (mMonoFrequency){
                                        case  6:  imped3[0] = imped[0]; break;
                                        case 10:  imped3[1] = imped[0]; break;
                                        case 14:  imped3[2] = imped[0]; break;
                                        case 20:  imped3[3] = imped[0]; break;
                                        case 22:  imped3[4] = imped[0]; break;
                                    }
                                    imped3[5] = Double.valueOf(mMonoFrequency);
                                }
                                if (i == 3 ){
                                    switch (mMonoFrequency){
                                        case  6:  imped4[0] = imped[0]; break;
                                        case 10:  imped4[1] = imped[0]; break;
                                        case 14:  imped4[2] = imped[0]; break;
                                        case 20:  imped4[3] = imped[0]; break;
                                        case 22:  imped4[4] = imped[0]; break;
                                    }
                                    imped4[5] = Double.valueOf(mMonoFrequency);
                                }
                                if (i == 4 ){
                                    switch (mMonoFrequency){
                                        case  6:  imped5[0] = imped[0]; break;
                                        case 10:  imped5[1] = imped[0]; break;
                                        case 14:  imped5[2] = imped[0]; break;
                                        case 20:  imped5[3] = imped[0]; break;
                                        case 22:  imped5[4] = imped[0]; break;
                                    }
                                    imped5[5] = Double.valueOf(mMonoFrequency);
                                }
                                if (i == 5 ){
                                    switch (mMonoFrequency){
                                        case  6:  imped6[0] = imped[0]; break;
                                        case 10:  imped6[1] = imped[0]; break;
                                        case 14:  imped6[2] = imped[0]; break;
                                        case 20:  imped6[3] = imped[0]; break;
                                        case 22:  imped6[4] = imped[0]; break;
                                    }
                                    imped6[5] = Double.valueOf(mMonoFrequency);
                                }
                                if (i == 6 ){
                                    switch (mMonoFrequency){
                                        case  6:  imped7[0] = imped[0]; break;
                                        case 10:  imped7[1] = imped[0]; break;
                                        case 14:  imped7[2] = imped[0]; break;
                                        case 20:  imped7[3] = imped[0]; break;
                                        case 22:  imped7[4] = imped[0]; break;
                                    }
                                    imped7[5] = Double.valueOf(mMonoFrequency);
                                }
                                if (i == 7 ){
                                    switch (mMonoFrequency){
                                        case  6:  imped8[0] = imped[0]; break;
                                        case 10:  imped8[1] = imped[0]; break;
                                        case 14:  imped8[2] = imped[0]; break;
                                        case 20:  imped8[3] = imped[0]; break;
                                        case 22:  imped8[4] = imped[0]; break;
                                    }
                                    imped8[5] = Double.valueOf(mMonoFrequency);
                                }
                                if (i == 8 ){
                                    switch (mMonoFrequency){
                                        case  6:  imped9[0] = imped[0]; break;
                                        case 10:  imped9[1] = imped[0]; break;
                                        case 14:  imped9[2] = imped[0]; break;
                                        case 20:  imped9[3] = imped[0]; break;
                                        case 22:  imped9[4] = imped[0]; break;
                                    }
                                    imped9[5] = Double.valueOf(mMonoFrequency);
                                }
                                if (i == 9 ){
                                    switch (mMonoFrequency){
                                        case  6:  imped10[0] = imped[0]; break;
                                        case 10:  imped10[1] = imped[0]; break;
                                        case 14:  imped10[2] = imped[0]; break;
                                        case 20:  imped10[3] = imped[0]; break;
                                        case 22:  imped10[4] = imped[0]; break;
                                    }
                                    imped10[5] = Double.valueOf(mMonoFrequency);
                                }

                                String rawFlowobject = object.optString("rawData","");
                                if (!rawFlowobject.equalsIgnoreCase("null") && !rawFlowobject.isEmpty()) {
                                    JSONObject rawFlowDataObject = new JSONObject(rawFlowobject);
                                    String raw_Flow_data = rawFlowDataObject.optString("flow");
                                    String raw_Mp_data = rawFlowDataObject.optString("mouthPressure");
                                    if (raw_Flow_data != null)
                                        flowData = Arrays.asList(raw_Flow_data.split(","));
                                    if (raw_Mp_data != null)
                                        MpData = Arrays.asList(raw_Mp_data.split(","));
                                    Log.d("ReportActivity", flowData.toString());
                                    Log.d("ReportActivity", MpData.toString());
                                 //   addRGraphtoScreen();
                                 //   addXGraphtoScreen();
                                    addFlowRawGraphtoScreen();
                                    addMPRawGraphtoScreen();
                                } else Toast.makeText(context, "Raw data not available.", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                            creatingRXZTableOnScreen();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("zxc", "onResponse: "+error.toString());
                progressDialog.dismiss();
            }
        });
        monoReport.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(monoReport);
    }


    public void showPdf() {
       /* File file = new File(Environment.getDataDirectory().getPath() + File.separator +
            "interview.pdf");*/

        File file = new File(ExternalStoragePath.getExternalCardPath(ReportActivity.this), ""+patientDetails.getmPatientname()+ ".pdf");
        /*Uri path = FileProvider.getUriForFile(context,"com.caliber.shwaasdemo",file);//Uri.fromFile(file);
        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenintent.setDataAndType(path, "application/pdf");*/
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri apkURI = FileProvider.getUriForFile(
                context,
                context.getApplicationContext()
                        .getPackageName() + ".fileprovider", file);
        install.setDataAndType(apkURI, "application/pdf");
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            progressDialog.dismiss();
            ReportActivity.this.startActivity(install);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "pdf is not available", Toast.LENGTH_SHORT).show();
        }
    }
    private void doctorComment() {
        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 25.0f, Font.NORMAL, BaseColor.BLACK);
        Font title = new Font(Font.FontFamily.TIMES_ROMAN, 28, Font.BOLD | Font.UNDERLINE, BaseColor.GRAY);
        Paragraph paragraph = new Paragraph("Doctor's Comment:\n", title);
        Paragraph p = new Paragraph(commentEdittext.getText().toString(), f);
        p.setKeepTogether(true );
       // paragraph.add(commentEdittext.getText().toString());
        try {
            document.add(paragraph);
            //document.add(Chunk.NEWLINE);
            document.add(p);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void creatingRXZTableOnScreen() {
        // for Second table...Start of the code here
        LayoutParams wrapWrapTableRowParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        int[] fixedColumnWidths = new int[]{20, 20, 20, 20, 20};
        int[] scrollableColumnWidths = new int[]{25, 10, 10, 10, 10, 10};
        String[] parameter = new String[]{"R and X", "R6(kPa/L/s)", "R12(kPa/L/s)", "R20(kPa/L/s)"/*,"R30(kPa/L/s)"*/, "X6(cmH20/L/s)", "X12(cmH20/L/s)", "X20(cmH20/L/s)"/*,"X30(kPa/L/s)"*/, "Fres(Hz)","Test Status"};
        String[] impedenceTable = new String[]{"  Parameter", "Z6(kPa/L/s)", "Z10(kPa/L/s)", " Z14(kPa/L/s)", "Z20(kPa/L/s)", "Z22(kPa/L/s)", "Fres(Hz)", " Signal quality"};
        int fixedRowHeight = 90;
        int fixedHeaderHeight = 90;

        TableRow row = new TableRow(this);

        TableLayout header = (TableLayout) findViewById(R.id.RXtable_header);
        row.setLayoutParams(wrapWrapTableRowParams);
        row.setGravity(Gravity.LEFT);

        row.setBackgroundColor(getResources().getColor(R.color.primary));
        //  row.addView(makeTableRowWithText("R and X", 60, 50));
        row.addView(makeTableRowWithText("Act 1", scrollableColumnWidths[0], fixedHeaderHeight));
        row.addView(makeTableRowWithText("Act 2", scrollableColumnWidths[1], fixedHeaderHeight));
        row.addView(makeTableRowWithText("Act 3", scrollableColumnWidths[2], fixedHeaderHeight));
        row.addView(makeTableRowWithText("Act 4", scrollableColumnWidths[3], fixedHeaderHeight));
        row.addView(makeTableRowWithText("Act 5", scrollableColumnWidths[4], fixedHeaderHeight));
        row.addView(makeTableRowWithText("Act 6", scrollableColumnWidths[4], fixedHeaderHeight));
        header.addView(row);

        //header (fixed horizontally)
        TableLayout fixedColumn = (TableLayout) findViewById(R.id.RXfixed_column);
        //rest of the table (within a scroll view)
        TableLayout scrollablePart = (TableLayout) findViewById(R.id.RXscrollable_part);
        for (int i = 0; i < (/*frequencyMode.equals("multi")?*/ 9 /*: 8*/); i++) {
            TextView fixedView = makeTableRowWithText("" +(/*frequencyMode.equals("multi")?*/ parameter[i]/*:impedenceTable[i]*/), scrollableColumnWidths[0], 90);
            fixedView.setTextSize(9.0f);
            GradientDrawable gd = new GradientDrawable();
           // gd.setColor(0x4fa5d5);
            gd.setCornerRadius(2);
            gd.setStroke(1,0xFF000000);
            gd.setColor(getResources().getColor(R.color.primary));

            GradientDrawable redBG = new GradientDrawable();
            redBG.setCornerRadius(2);
            redBG.setColor(getResources().getColor(R.color.red));
            redBG.setStroke(1,0xFF000000);
          //  gd.setColor(getResources().getColor(R.color.red));

            GradientDrawable greenBG = new GradientDrawable();
            greenBG.setCornerRadius(2);
            greenBG.setStroke(1,0xFF000000);
            greenBG.setColor(getResources().getColor(R.color.green));
        //    greenBG.setColor(getResources().getColor(R.color.green));
            // fixedView.setBackgroundColor(getResources().getColor(R.color.primary));

            fixedView.setBackground(gd);
           // fixedView.setBackgroundColor(getResources().getColor(R.color.primary));
            fixedColumn.addView(fixedView);
            row = new TableRow(this);
            row.setLayoutParams(wrapWrapTableRowParams);
            row.setGravity(Gravity.CENTER);
            row.setBackgroundColor(Color.WHITE);
           /* if(frequencyMode.equals("multi")) {*/
                if (i != 9 ) {
                    if (i < 8) {
                        // row.addView(enterValueTableRowWithText("  "+act1[i], scrollableColumnWidths[1], fixedRowHeight));
                        if (action1[6] != 0.0) {
                            if (i == 7){
                                row.addView(enterValueTableRowWithText("" +(action1[i]== 1.0 ? "true":"false"), scrollableColumnWidths[1], fixedRowHeight));
                                row.setBackground((/*action1[i]*/1.0 == 1.0 ? greenBG : redBG));
                            }
                            else {
                                row.addView(enterValueTableRowWithText("" + (action1[i]), scrollableColumnWidths[1], fixedRowHeight));
                            }
                        }
                        if (action2[6] != 0.0) {
                            if (i == 7){
                                row.addView(enterValueTableRowWithText("" +(action2[i]== 1.0 ? "true":"false"), scrollableColumnWidths[1], fixedRowHeight));
                               // row.setBackground((/*action2[i]*/0.0 == 1.0 ? greenBG : redBG));
                            }
                            else {
                                row.addView(enterValueTableRowWithText("" + action2[i], scrollableColumnWidths[2], fixedRowHeight));
                            }
                        }
                        if (action3[6] != 0.0) {
                            if (i == 7){
                                row.addView(enterValueTableRowWithText("" +(action3[i]== 1.0 ? "true":"false"), scrollableColumnWidths[1], fixedRowHeight));
                             //   row.setBackground((action3[i]== 1.0 ? greenBG : redBG));
                            }
                            else{
                                row.addView(enterValueTableRowWithText("" + action3[i], scrollableColumnWidths[3], fixedRowHeight));
                            }
                        }
                        if (action4[6] != 0.0) {
                            if (i == 7){
                                row.addView(enterValueTableRowWithText("" +(action4[i]== 1.0 ? "true":"false"), scrollableColumnWidths[1], fixedRowHeight));
                             //   row.setBackground((action4[i]== 1.0 ? greenBG : redBG));
                            }
                            else {
                                row.addView(enterValueTableRowWithText("" + action4[i], scrollableColumnWidths[4], fixedRowHeight));
                            }
                        }
                        if (action5[6] != 0.0) {
                            if (i == 7) {
                                row.addView(enterValueTableRowWithText("" + (action5[i] == 1.0 ? "true" : "false"), scrollableColumnWidths[1], fixedRowHeight));
                             //   row.setBackground((action5[i]== 1.0 ? greenBG : redBG));
                            }
                            else {
                                row.addView(enterValueTableRowWithText("" + action5[i], scrollableColumnWidths[5], fixedRowHeight));
                            }
                        }
                        if (action6[6] != 0.0) {
                            if (i == 7){
                                row.addView(enterValueTableRowWithText("" +(action6[i]== 1.0 ? "true":"false"), scrollableColumnWidths[1], fixedRowHeight));
                              //  row.setBackground((action6[i]== 1.0 ? greenBG : redBG));
                            }
                            else {
                                row.addView(enterValueTableRowWithText("" + action6[i], scrollableColumnWidths[5], fixedRowHeight));
                            }
                        }
                    }
                    scrollablePart.addView(row);
                }
           /* }
            else {
                if (i !=  7){
                    if ((imped1[i] != null))
                        row.addView(enterValueTableRowWithText(""+ imped1[i], scrollableColumnWidths[1], fixedRowHeight));
                    else
                        row.addView(enterValueTableRowWithText("", scrollableColumnWidths[1], fixedRowHeight));
                    if ((imped2[i] != null))
                        row.addView(enterValueTableRowWithText(""+ imped2[i], scrollableColumnWidths[1], fixedRowHeight));
                    else
                        row.addView(enterValueTableRowWithText("", scrollableColumnWidths[1], fixedRowHeight));
                    if ((imped3[i] != null))
                        row.addView(enterValueTableRowWithText(""+ imped3[i], scrollableColumnWidths[1], fixedRowHeight));
                    else
                        row.addView(enterValueTableRowWithText("", scrollableColumnWidths[1], fixedRowHeight));
                    if  ((imped4[i] != null))
                        row.addView(enterValueTableRowWithText(""+ imped4[i], scrollableColumnWidths[1], fixedRowHeight));
                    else
                        row.addView(enterValueTableRowWithText("", scrollableColumnWidths[1], fixedRowHeight));
                    if ( (imped5[i] != null))
                        row.addView(enterValueTableRowWithText(""+ imped5[i], scrollableColumnWidths[1], fixedRowHeight));
                    else
                        row.addView(enterValueTableRowWithText("", scrollableColumnWidths[1], fixedRowHeight));
                    if ((imped6[i] != null))
                        row.addView(enterValueTableRowWithText(""+ imped6[i], scrollableColumnWidths[1], fixedRowHeight));
                    else
                        row.addView(enterValueTableRowWithText("", scrollableColumnWidths[1], fixedRowHeight));
                    scrollablePart.addView(row);
                }
            }*/
        }
        // for second table ...end of the code

    }
    public void creatingRXTableOnScreen() {
        // for Second table...Start of the code here
       /* frequencyMode = "mono";*/
        LayoutParams wrapWrapTableRowParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        int[] fixedColumnWidths = new int[]{20, 20, 20, 20, 20};
        int[] scrollableColumnWidths = new int[]{25, 10, 10, 10, 10, 10};
        String[] parameter = new String[]{"R and X", "R6(kPa/L/s)", "R12(kPa/L/s)", "R20(kPa/L/s)"/*,"R30(kPa/L/s)"*/, "X6(cmH20/L/s)", "X12(cmH20/L/s)", "X20(cmH20/L/s)"/*,"X30(kPa/L/s)"*/, "Fres(Hz)","Test Status"};
        String[] impedenceTable = new String[]{"  Parameter", "Z6(kPa/L/s)", "Z10(kPa/L/s)", " Z14(kPa/L/s)", "Z20(kPa/L/s)", "Z22(kPa/L/s)", "Fres(Hz)", " Signal quality"};
        int fixedRowHeight = 90;
        int fixedHeaderHeight = 90;

        TableRow row = new TableRow(this);

        TableLayout header = (TableLayout) findViewById(R.id.Ztable_header);
        row.setLayoutParams(wrapWrapTableRowParams);
        row.setGravity(Gravity.LEFT);

        row.setBackgroundColor(getResources().getColor(R.color.primary));
        //  row.addView(makeTableRowWithText("R and X", 60, 50));
        row.addView(makeTableRowWithText("Act 1", scrollableColumnWidths[0], fixedHeaderHeight));
        row.addView(makeTableRowWithText("Act 2", scrollableColumnWidths[1], fixedHeaderHeight));
        row.addView(makeTableRowWithText("Act 3", scrollableColumnWidths[2], fixedHeaderHeight));
        row.addView(makeTableRowWithText("Act 4", scrollableColumnWidths[3], fixedHeaderHeight));
        row.addView(makeTableRowWithText("Act 5", scrollableColumnWidths[4], fixedHeaderHeight));
        row.addView(makeTableRowWithText("Act 6", scrollableColumnWidths[4], fixedHeaderHeight));
        header.addView(row);

        //header (fixed horizontally)
        TableLayout fixedColumn = (TableLayout) findViewById(R.id.Zfixed_column);
        //rest of the table (within a scroll view)
        TableLayout scrollablePart = (TableLayout) findViewById(R.id.Zscrollable_part);
        for (int i = 0; i < (/*frequencyMode.equals("multi")? 9 :*/ 8); i++) {
            TextView fixedView = makeTableRowWithText("" +(/*frequencyMode.equals("multi")? parameter[i]:*/impedenceTable[i]), scrollableColumnWidths[0], 90);
            fixedView.setTextSize(9.0f);
            GradientDrawable gd = new GradientDrawable();
            // gd.setColor(0x4fa5d5);
            gd.setCornerRadius(2);
            gd.setStroke(1,0xFF000000);
            gd.setColor(getResources().getColor(R.color.primary));

            GradientDrawable redBG = new GradientDrawable();
            redBG.setCornerRadius(2);
            redBG.setColor(getResources().getColor(R.color.red));
            redBG.setStroke(1,0xFF000000);
            //  gd.setColor(getResources().getColor(R.color.red));

            GradientDrawable greenBG = new GradientDrawable();
            greenBG.setCornerRadius(2);
            greenBG.setStroke(1,0xFF000000);
            greenBG.setColor(getResources().getColor(R.color.green));
            //    greenBG.setColor(getResources().getColor(R.color.green));
            // fixedView.setBackgroundColor(getResources().getColor(R.color.primary));

            fixedView.setBackground(gd);
            // fixedView.setBackgroundColor(getResources().getColor(R.color.primary));
            fixedColumn.addView(fixedView);
            row = new TableRow(this);
            row.setLayoutParams(wrapWrapTableRowParams);
            row.setGravity(Gravity.CENTER);
            row.setBackgroundColor(Color.WHITE);
          /*  if(frequencyMode.equals("multi")) {
                if (i != 9 ) {
                    if (i < 8) {
                        // row.addView(enterValueTableRowWithText("  "+act1[i], scrollableColumnWidths[1], fixedRowHeight));
                        if (action1[6] != 0.0) {
                            if (i == 7){
                                row.addView(enterValueTableRowWithText("" +(action1[i]== 1.0 ? "true":"false"), scrollableColumnWidths[1], fixedRowHeight));
                                row.setBackground((*//*action1[i]*//*1.0 == 1.0 ? greenBG : redBG));
                            }
                            else {
                                row.addView(enterValueTableRowWithText("" + (action1[i]), scrollableColumnWidths[1], fixedRowHeight));
                            }
                        }
                        if (action2[6] != 0.0) {
                            if (i == 7){
                                row.addView(enterValueTableRowWithText("" +(action2[i]== 1.0 ? "true":"false"), scrollableColumnWidths[1], fixedRowHeight));
                                // row.setBackground((*//*action2[i]*//*0.0 == 1.0 ? greenBG : redBG));
                            }
                            else {
                                row.addView(enterValueTableRowWithText("" + action2[i], scrollableColumnWidths[2], fixedRowHeight));
                            }
                        }
                        if (action3[6] != 0.0) {
                            if (i == 7){
                                row.addView(enterValueTableRowWithText("" +(action3[i]== 1.0 ? "true":"false"), scrollableColumnWidths[1], fixedRowHeight));
                                //   row.setBackground((action3[i]== 1.0 ? greenBG : redBG));
                            }
                            else{
                                row.addView(enterValueTableRowWithText("" + action3[i], scrollableColumnWidths[3], fixedRowHeight));
                            }
                        }
                        if (action4[6] != 0.0) {
                            if (i == 7){
                                row.addView(enterValueTableRowWithText("" +(action4[i]== 1.0 ? "true":"false"), scrollableColumnWidths[1], fixedRowHeight));
                                //   row.setBackground((action4[i]== 1.0 ? greenBG : redBG));
                            }
                            else {
                                row.addView(enterValueTableRowWithText("" + action4[i], scrollableColumnWidths[4], fixedRowHeight));
                            }
                        }
                        if (action5[6] != 0.0) {
                            if (i == 7) {
                                row.addView(enterValueTableRowWithText("" + (action5[i] == 1.0 ? "true" : "false"), scrollableColumnWidths[1], fixedRowHeight));
                                //   row.setBackground((action5[i]== 1.0 ? greenBG : redBG));
                            }
                            else {
                                row.addView(enterValueTableRowWithText("" + action5[i], scrollableColumnWidths[5], fixedRowHeight));
                            }
                        }
                        if (action6[6] != 0.0) {
                            if (i == 7){
                                row.addView(enterValueTableRowWithText("" +(action6[i]== 1.0 ? "true":"false"), scrollableColumnWidths[1], fixedRowHeight));
                                //  row.setBackground((action6[i]== 1.0 ? greenBG : redBG));
                            }
                            else {
                                row.addView(enterValueTableRowWithText("" + action6[i], scrollableColumnWidths[5], fixedRowHeight));
                            }
                        }
                    }
                    scrollablePart.addView(row);
                }
            }
            else */{
                if (i !=  7){
                    if ((imped1[i] != null))
                        row.addView(enterValueTableRowWithText(""+ imped1[i], scrollableColumnWidths[1], fixedRowHeight));
                    else
                        row.addView(enterValueTableRowWithText("", scrollableColumnWidths[1], fixedRowHeight));
                    if ((imped2[i] != null))
                        row.addView(enterValueTableRowWithText(""+ imped2[i], scrollableColumnWidths[1], fixedRowHeight));
                    else
                        row.addView(enterValueTableRowWithText("", scrollableColumnWidths[1], fixedRowHeight));
                    if ((imped3[i] != null))
                        row.addView(enterValueTableRowWithText(""+ imped3[i], scrollableColumnWidths[1], fixedRowHeight));
                    else
                        row.addView(enterValueTableRowWithText("", scrollableColumnWidths[1], fixedRowHeight));
                    if  ((imped4[i] != null))
                        row.addView(enterValueTableRowWithText(""+ imped4[i], scrollableColumnWidths[1], fixedRowHeight));
                    else
                        row.addView(enterValueTableRowWithText("", scrollableColumnWidths[1], fixedRowHeight));
                    if ( (imped5[i] != null))
                        row.addView(enterValueTableRowWithText(""+ imped5[i], scrollableColumnWidths[1], fixedRowHeight));
                    else
                        row.addView(enterValueTableRowWithText("", scrollableColumnWidths[1], fixedRowHeight));
                    if ((imped6[i] != null))
                        row.addView(enterValueTableRowWithText(""+ imped6[i], scrollableColumnWidths[1], fixedRowHeight));
                    else
                        row.addView(enterValueTableRowWithText("", scrollableColumnWidths[1], fixedRowHeight));
                    scrollablePart.addView(row);
                }
            }
        }
        // for second table ...end of the code
       /* frequencyMode = "multi";*/
    }

    public TextView makeTableRowWithText(String text, int widthInPercentOfScreenWidth, int fixedHeightInPixels) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(0x4fa5d5);
        gd.setCornerRadius(2);
        gd.setStroke(1,0xFF000000);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        recyclableTextView = new TextView(this);
        recyclableTextView.setText(text);
        recyclableTextView.setGravity(Gravity.CENTER);
        recyclableTextView.setBackground(gd);
        recyclableTextView.setTextColor(Color.BLACK);
        recyclableTextView.setTextSize(11);
        recyclableTextView.setTypeface(null, Typeface.BOLD);
        //recyclableTextView.setWidth(widthInPercentOfScreenWidth * screenWidth / 100);
        recyclableTextView.setWidth(280);
        recyclableTextView.setHeight(fixedHeightInPixels);
        return recyclableTextView;
    }

    public TextView enterValueTableRowWithText(String text, int widthInPercentOfScreenWidth, int fixedHeightInPixels) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(0x4fa5d5);
        gd.setCornerRadius(2);
        gd.setStroke(1,0xFF000000);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        recyclableTextView = new TextView(this);
        recyclableTextView.setText(text);
        recyclableTextView.setGravity(Gravity.CENTER);
        recyclableTextView.setBackground(gd);
        recyclableTextView.setTextColor(resultStatus == false ? Color.BLACK : resultStatus == true ? Color.GREEN : Color.RED );
        recyclableTextView.setTextSize(11);
        //recyclableTextView.setWidth(widthInPercentOfScreenWidth * screenWidth / 100);
        recyclableTextView.setWidth(280);
        recyclableTextView.setHeight(fixedHeightInPixels);
        return recyclableTextView;
    }

    public void addRGraphtoScreen() {

       /* if (frequencyMode.equals("multi")) {*/
            double r6 = defaultAction[0];
            float r66 = (float) r6;
            double r12 = defaultAction[1];
            float r122 = (float) r12;
            double r20 = defaultAction[2];
            float r200 = (float) r20;
            //   double r30 = defaultAction[3];
            //   float r300 = (float) r30;
            entriesR.add(new Entry(r66, 1));
            entriesR.add(new Entry(r122, 2));
            entriesR.add(new Entry(r200, 3));
            datasetR = new LineDataSet(entriesR, "Hz--------------------->");

            labelsR.add("0");
            labelsR.add("6");
            labelsR.add("12");
            labelsR.add("20");
            // labels.add("30");
            labelsR.add("Hz");
      /*  } else {
            float [] floatValues = new float[flowData.size()];
            for (int i = 0; i < flowData.size(); i++){
                floatValues[i] = Float.parseFloat(flowData.get(i));
                entries.add(new Entry(floatValues[i],i));
            }
            datasetR = new LineDataSet(entries, "");
            datasetR.setDrawValues(false);
            datasetR.setLineWidth(2f);
            datasetR.setDrawCubic(true);
            datasetR.setDrawCircles(false);
            chart.setVisibleXRange(4000.f);
            chart.setDescription("Flow Chart for 12s");
            //  mResultFlowChart.getAxisLeft().setDrawGridLines(false);
            chart.getXAxis().setDrawGridLines(false);
            chart.getLegend().setEnabled(false);
            chart.getXAxis().setDrawLabels(false);
            for (int j = 0; j < flowData.size(); j++ ){
                labels.add(""+j);
            }
        }
*/
        dataR = new LineData(labelsR, datasetR);
        Rchart.setData(dataR);
    //    chart.setDescription("X-axis in Hz and Y-axis in kPa/L/sec");
        Rchart.setDragEnabled(false); // on by default
        // chart.setVisibleXRange( 3 ); // sets the viewport to show 3 bars
        Rchart.animateY(5000);
        Rchart.animateX(2000);
        datasetR.setColors(ColorTemplate.COLORFUL_COLORS);
        datasetR.setDrawCubic(true);

        datasetR.setDrawFilled(true);
        Rchart.setVisibleXRange(125.0f);
        Rchart.getAxisLeft().setStartAtZero(false);
        Rchart.getAxisRight().setStartAtZero(false);
        datasetR.setColors(ColorTemplate.COLORFUL_COLORS);

    }
    public void addFlowRawGraphtoScreen() {

        /*if (frequencyMode.equals("multi")) {
            double r6 = defaultAction[0];
            float r66 = (float) r6;
            double r12 = defaultAction[1];
            float r122 = (float) r12;
            double r20 = defaultAction[2];
            float r200 = (float) r20;
            //   double r30 = defaultAction[3];
            //   float r300 = (float) r30;
            entries.add(new Entry(r66, 1));
            entries.add(new Entry(r122, 2));
            entries.add(new Entry(r200, 3));
            dataset = new LineDataSet(entries, "Hz--------------------->");

            labels.add("0");
            labels.add("6");
            labels.add("12");
            labels.add("20");
            // labels.add("30");
            labels.add("Hz");
        } else{*/
            float [] floatValues = new float[flowData.size()];
            for (int i = 0; i < flowData.size(); i++){
                floatValues[i] = Float.parseFloat(flowData.get(i));
                entriesRawFlow.add(new Entry(floatValues[i],i));
            }
            datasetRawFLow = new LineDataSet(entriesRawFlow, "");
            datasetRawFLow.setDrawValues(false);
            datasetRawFLow.setLineWidth(2f);
            datasetRawFLow.setDrawCubic(true);
            datasetRawFLow.setDrawCircles(false);
            RawFlowchart.setVisibleXRange(4000.f);
            RawFlowchart.setDescription("Flow Chart for 12s");
            //  mResultFlowChart.getAxisLeft().setDrawGridLines(false);
            RawFlowchart.getXAxis().setDrawGridLines(false);
            RawFlowchart.getLegend().setEnabled(false);
            RawFlowchart.getXAxis().setDrawLabels(false);
            for (int j = 0; j < flowData.size(); j++ ){
                labelsRawFLow.add(""+j);
            }


        dataRawFlow = new LineData(labelsRawFLow, datasetRawFLow);
        RawFlowchart.setData(dataRawFlow);
        //    chart.setDescription("X-axis in Hz and Y-axis in kPa/L/sec");
        RawFlowchart.setDragEnabled(false); // on by default
        // chart.setVisibleXRange( 3 ); // sets the viewport to show 3 bars
        RawFlowchart.animateY(5000);
        RawFlowchart.animateX(2000);
        datasetRawFLow.setColors(ColorTemplate.COLORFUL_COLORS);
        datasetRawFLow.setDrawCubic(true);

        datasetRawFLow.setDrawFilled(true);
        RawFlowchart.setVisibleXRange(125.0f);
        RawFlowchart.getAxisLeft().setStartAtZero(false);
        RawFlowchart.getAxisRight().setStartAtZero(false);
        datasetRawFLow.setColors(ColorTemplate.COLORFUL_COLORS);

    }
    public void addMPRawGraphtoScreen() {

        /*if (frequencyMode.equals("multi")) {
            double r6 = defaultAction[0];
            float r66 = (float) r6;
            double r12 = defaultAction[1];
            float r122 = (float) r12;
            double r20 = defaultAction[2];
            float r200 = (float) r20;
            //   double r30 = defaultAction[3];
            //   float r300 = (float) r30;
            entries.add(new Entry(r66, 1));
            entries.add(new Entry(r122, 2));
            entries.add(new Entry(r200, 3));
            dataset = new LineDataSet(entries, "Hz--------------------->");

            labels.add("0");
            labels.add("6");
            labels.add("12");
            labels.add("20");
            // labels.add("30");
            labels.add("Hz");
        } else{*/
        float [] floatValues = new float[MpData.size()];
        for (int i = 0; i < MpData.size(); i++){
            floatValues[i] = Float.parseFloat(MpData.get(i));
            entriesRawMP.add(new Entry(floatValues[i],i));
        }
        datasetRawMP = new LineDataSet(entriesRawMP, "");
        datasetRawMP.setDrawValues(false);
        datasetRawMP.setLineWidth(2f);
        datasetRawMP.setDrawCubic(true);
        datasetRawMP.setDrawCircles(false);
        RawMPchart.setVisibleXRange(4000.f);
        RawMPchart.setDescription("MP Chart for 12s");
        //  mResultFlowChart.getAxisLeft().setDrawGridLines(false);
        RawMPchart.getXAxis().setDrawGridLines(false);
        RawMPchart.getLegend().setEnabled(false);
        RawMPchart.getXAxis().setDrawLabels(false);
        for (int j = 0; j < MpData.size(); j++ ){
            labelsRawMP.add(""+j);
        }

        dataRawMP = new LineData(labelsRawMP, datasetRawMP);
        RawMPchart.setData(dataRawMP);
        //    chart.setDescription("X-axis in Hz and Y-axis in kPa/L/sec");
        RawMPchart.setDragEnabled(false); // on by default
        // chart.setVisibleXRange( 3 ); // sets the viewport to show 3 bars
        RawMPchart.animateY(5000);
        RawMPchart.animateX(2000);
        datasetRawMP.setColors(ColorTemplate.COLORFUL_COLORS);
        datasetRawMP.setDrawCubic(true);

        datasetRawMP.setDrawFilled(true);
        RawMPchart.setVisibleXRange(125.0f);
        RawMPchart.getAxisLeft().setStartAtZero(false);
        RawMPchart.getAxisRight().setStartAtZero(false);
        datasetRawMP.setColors(ColorTemplate.COLORFUL_COLORS);

    }

    public void addXGraphtoScreen() {
       /* if (frequencyMode.equals("multi")) {*/
            double x6 = defaultAction[3];
            float x66 = (float) x6;
            double x12 = defaultAction[4];
            float x122 = (float) x12;
            double x20 = defaultAction[5];
            float x200 = (float) x20;
            //   double x30 = defaultAction[7];
            //   float x300 = (float) x30;
            entriesX.add(new Entry(x66, 1));
            entriesX.add(new Entry(x122, 2));
            entriesX.add(new Entry(x200, 3));
            datasetX = new LineDataSet(entriesX, "Hz--------------------->");

            labelsX.add("0");
            labelsX.add("6");
            labelsX.add("12");
            labelsX.add("20");
            //   labels2.add("30");
            labelsX.add("Hz");
       /* } else {
            float [] floatValues = new float[MpData.size()];
            for (int i = 0; i < MpData.size(); i++){
                floatValues[i] = Float.parseFloat(MpData.get(i));
                entries2.add(new Entry(floatValues[i],i));
            }
            dataset2 = new LineDataSet(entries2, "");
            dataset2.setDrawValues(false);
            dataset2.setLineWidth(2f);
            dataset2.setDrawCubic(true);
            dataset2.setDrawCircles(false);
            chart2.setVisibleXRange(4000.f);
            chart2.setDescription("MP Chart for 12s");
            //  mResultFlowChart.getAxisLeft().setDrawGridLines(false);
            chart2.getXAxis().setDrawGridLines(false);
            chart2.getLegend().setEnabled(false);
            chart2.getXAxis().setDrawLabels(false);
            for (int j = 0; j < MpData.size(); j++ ){
                labels2.add(""+j);
            }
        }*/
        dataX = new LineData(labelsX, datasetX);
        Xchart.setData(dataX);
      //  chart2.setDescription("X-axis in Hz and Y-axis in kPa/L/sec");
        Xchart.setDragEnabled(false); // on by default
        // chart.setVisibleXRange( 3 ); // sets the viewport to show 3 bars
        Xchart.animateY(5000);
        Xchart.animateX(2000);
        datasetX.setColors(ColorTemplate.COLORFUL_COLORS);
        datasetX.setDrawCubic(true);
        datasetX.setDrawFilled(true);
        Xchart.setVisibleXRange(125.0f);
        Xchart.getAxisLeft().setStartAtZero(false);
        Xchart.getAxisRight().setStartAtZero(false);
        datasetX.setColors(ColorTemplate.COLORFUL_COLORS);
    }

    public void addDatatochartintopdf() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeightth = getResources().getDisplayMetrics().heightPixels;
        // adding the chart in pdf
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = Rchart.getChartBitmap(); // get the bitmap from chart which is drawn on the screen
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        Image myImg = null;
        try {
            myImg = Image.getInstance(stream.toByteArray());
            myImg.setWidthPercentage(screenWidth * 10 / 100);
            if (screenWidth > 1080) {
                myImg.scaleToFit(screenWidth - screenWidth * 60 / 100, screenHeightth - screenHeightth * 60 / 100);
            }
            else myImg.scaleToFit(screenWidth - screenWidth * 45 / 100, screenHeightth - screenHeightth * 45 / 100);

        } catch (IOException | BadElementException e) {
            e.printStackTrace();
        }
        if (myImg != null) {
            myImg.setAlignment(Image.ALIGN_MIDDLE);
        }
        try {

            Paragraph paragraph2 = new Paragraph("R value Graph:\n", title);
            Paragraph flowData = new Paragraph("Flow raw data Graph:\n", title);

            if (frequencyMode.equals("multi")) {
                document.add(paragraph2);
            } else {
                document.add(flowData);
            }
            document.add(myImg);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    public void addSecondDatatochartintopdf() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeightth = getResources().getDisplayMetrics().heightPixels;
        // adding the chart in pdf
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = Xchart.getChartBitmap();
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        Image myImg = null;
        try {
            myImg = Image.getInstance(stream.toByteArray());
            myImg.setWidthPercentage(screenWidth * 10 / 100);


           if (screenWidth > 1080) {
               myImg.scaleToFit(screenWidth - screenWidth * 60 / 100, screenHeightth - screenHeightth * 60 / 100);
           }
           else myImg.scaleToFit(screenWidth - screenWidth * 45 / 100, screenHeightth - screenHeightth * 45 / 100);

        } catch (IOException | BadElementException e) {
            e.printStackTrace();
        }
        if (myImg != null) {
            myImg.setAlignment(Image.ALIGN_MIDDLE);
        }
        try {
            document.add(myImg);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addMetaData(com.itextpdf.text.Document document) {
        document.addTitle("Patient Report");
        document.addSubject("Patient info");
        document.addKeywords("Personal,Health,disease");
        document.addAuthor("TAG");
        document.addCreator("TAG");
    }

    public void addTitlePageinPDF(com.itextpdf.text.Document document) throws DocumentException {
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.BOLD);
        Font title = new Font(Font.FontFamily.TIMES_ROMAN, 28, Font.BOLD | Font.UNDERLINE, BaseColor.GRAY);
        Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.BOLD);
        Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

        //start new Paragrapgh
        Paragraph pgHead = new Paragraph();
        // set Font in this paragraph
        pgHead.setFont(title);
        //add item into paragraph
        pgHead.add("Patient - Report\n");
        // create Table into Document with 1 Row
        PdfPTable myTable = new PdfPTable(1);
        myTable.setWidthPercentage(75.0f);

        // create new Cell into Table
        PdfPCell myCell = new PdfPCell(new Paragraph("Patient Name : "+patientDetails.getmPatientname(), smallBold));
        myCell.setBorder(Rectangle.NO_BORDER);
        // Add cell into Table
        myTable.addCell(myCell);

        pgHead.setFont(catFont);
        // pgHead.add("\n Name1 Name2\n");
        pgHead.setAlignment(Element.ALIGN_CENTER);

        //add all above details into Document
        try {

            document.add(pgHead);
            // add a single of blank lines
            document.add(Chunk.NEWLINE);


            document.add(myTable);

            // add a couple of blank lines
            document.add(Chunk.NEWLINE);
            document.add(myCell);
            document.add(createFirstTable());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates our first table
     *
     * @return our first table
     */
    public  PdfPTable createFirstTable() {
        // a table with two columns
        PdfPTable table = new PdfPTable(2);
        // the cell object
        table.setWidthPercentage(75.0f);
        Font header = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font follower = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.NORMAL);
        table.getDefaultCell().setFixedHeight(30.0f);
        // table.getDefaultCell().setPaddingTop(10.0f);
        table.getDefaultCell().setPadding(6.0f);

        Phrase phrase1 = new Phrase();
        phrase1.add(new Chunk("Patient id : ", header));
        phrase1.add(new Chunk(""+patientDetails.getmPatientID(), follower));
        table.addCell(phrase1);

        Phrase phrase2 = new Phrase();
        phrase2.add(new Chunk("Gender : ", header));
        phrase2.add(new Chunk(""+gender, follower));
        table.addCell(phrase2);

        Phrase phrase5 = new Phrase();
        phrase5.add(new Chunk("DoB          : ", header));
        phrase5.add(new Chunk(""+patientDetails.getmAge(), follower));
        table.addCell(phrase5);

        Phrase phrase6 = new Phrase();
        phrase6.add(new Chunk("Smoker : ", header));
        phrase6.add(new Chunk(""+smoker, follower));
        table.addCell(phrase6);

        return table;
    }

    /**
     * Creates our first table
     *
     * @return our first table
     */
    public PdfPTable createSecondTable() {
        // a table with two columns
        PdfPTable table = new PdfPTable(7);
        // the cell object
        try {
            table.setWidths(new float[] {(float) 1.5,1,1,1,1,1,1});
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        table.setWidthPercentage(100.0f);
        Font header = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font green = new Font(Font.FontFamily.TIMES_ROMAN, 15,  Font.NORMAL, new BaseColor(0,102,0));
        Font red = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.NORMAL, BaseColor.RED);
        table.getDefaultCell().setFixedHeight(30.0f);
        // table.getDefaultCell().setPaddingTop(10.0f);
        table.getDefaultCell().setPadding(6.0f);

        Phrase phrase1 = new Phrase();
        phrase1.add(new Chunk("R and X ", header));
        // phrase1.add(new Chunk("1", follower));
        table.addCell(phrase1);

        Phrase phrase2 = new Phrase();
        phrase2.add(new Chunk("Act 1", header));
        //   phrase2.add(new Chunk("male", follower));
        table.addCell(phrase2);

        Phrase phrase3 = new Phrase();
        phrase3.add(new Chunk("Act 2 ", header));
        //  phrase3.add(new Chunk("5.10 inch",follower));
        table.addCell(phrase3);

        Phrase phrase4 = new Phrase();
        phrase4.add(new Chunk("Act 3 ", header));
        //   phrase4.add(new Chunk("70 kg", follower));
        table.addCell(phrase4);

        Phrase phrase5 = new Phrase();
        phrase5.add(new Chunk("Act 4 ", header));
        //  phrase5.add(new Chunk("56 years", follower));
        table.addCell(phrase5);

        Phrase phrase6 = new Phrase();
        phrase6.add(new Chunk("Act 5 ", header));
        table.addCell(phrase6);

        Phrase phrase7 = new Phrase();
        phrase7.add(new Chunk("Act 6 ", header));
        table.addCell(phrase7);
/*
        Phrase phrase8 = new Phrase();
        phrase8.add(new Chunk("Act 7 ", header));
        table.addCell(phrase8);

        Phrase phrase9 = new Phrase();
        phrase9.add(new Chunk("Act 8 ", header));
        table.addCell(phrase9);

        Phrase phrase10 = new Phrase();
        phrase10.add(new Chunk("Act 9 ", header));
        table.addCell(phrase10);

        Phrase phrase11 = new Phrase();
        phrase11.add(new Chunk("Act 10 ", header));
        table.addCell(phrase11);

*/

      //  int[] act1 = new int[]{1,2,3,4,5,6,7};
      //  "R and X", "1 R6(kPa/L/s)", "2 R12(kPa/L/s)", "3 R20(kPa/L/s)", "4 X6(cmH20/L/s)", "5 X12(cmH20/L/s)", "6 X20(cmH20/L/s)", "7 Fres(Hz)"
        String[] column = new String[]{"R6(kPa/L/s)", "R12(kPa/L/s)", "R20(kPa/L/s)"/*,"R30(kPa/L/s)"*/, "X6(cmH20/L/s)", "X12(cmH20/L/s)", "X20(cmH20/L/s)"/*,"X30(cmH20/L/s)"*/, "Fres(Hz)"};
        //String[] column = new String[]{"R6(kPa/L/s)", "R12(kPa/L/s)", "R20(kPa/L/s)", "X6(cmH20/L/s)", "X12(cmH20/L/s)", "X20(cmH20/L/s)", "Fres(Hz)"};
        String[] impedenceTable = new String[]{"Z6(kPa/L/s)", "Z10(kPa/L/s)", "Z14(kPa/L/s)", "Z20(kPa/L/s)", "Z22(kPa/L/s)", "Fres(Hz)",""};

        for (int j = 0; j < (frequencyMode.equals("multi")?7:6); j++) {
            for (int i = 0; i < table.getNumberOfColumns(); i++) {
                Phrase phrase31 = new Phrase();
                if (i == 0) {
                        phrase31.add(new Chunk("" +(frequencyMode.equals("multi")?column[j]:impedenceTable[j]), header));

                } else {
                   // phrase11.add(new Chunk("" + data[i - 1], follower));
                    if(i == 1){
                        if (resultStatus)
                             phrase31.add(new Chunk("" +(frequencyMode.equals("multi")? action1[j]:imped1[j]), green));
                        else {
                            if (frequencyMode.equals("multi")) {
                                if (action1[6] != 0.00)
                                    phrase31.add(new Chunk("" +action1[j], red));
                            } else {
                                if (imped1[5] != null){
                                    phrase31.add(new Chunk("" +(imped1[j] != null ? imped1[j] : "" ), red));
                                } else
                                    phrase31.add(new Chunk("", red));
                            }

                        }
                    }
                    if(i == 2){
                        if (resultStatus)
                            phrase31.add(new Chunk("" +(frequencyMode.equals("multi")? action2[j]:imped2[j]), green));
                        else {
                            if (frequencyMode.equals("multi")) {
                            if (action2[6] != 0.00)
                            phrase31.add(new Chunk("" +action2[j], red));
                            } else {
                                if (imped2[5] != null){
                                    phrase31.add(new Chunk("" +(imped2[j] != null ? imped2[j] : "" ), red));
                                }else
                                    phrase31.add(new Chunk("", red));
                            }
                        }
                    }
                   if(i == 3){
                        if (resultStatus)
                            phrase31.add(new Chunk("" +(frequencyMode.equals("multi")? action3[j]:imped3[j]), green));
                        else {

                            if (frequencyMode.equals("multi")) {
                            if (action3[6] != 0.00)
                            phrase31.add(new Chunk("" + action3[j], red));
                            } else {
                                if (imped3[5] != null){
                                    phrase31.add(new Chunk("" +(imped3[j] != null ? imped3[j] : "" ), red));
                                }else
                                    phrase31.add(new Chunk("", red));
                            }
                        }
                    }
                    if(i == 4){
                        if (resultStatus)
                            phrase31.add(new Chunk("" +(frequencyMode.equals("multi")? action4[j]:imped4[j]), green));
                        else {
                            if (frequencyMode.equals("multi")) {
                            if (action4[6] != 0.00)
                                phrase31.add(new Chunk("" +action4[j], red));
                            } else {
                                if (imped4[5] != null){
                                    phrase31.add(new Chunk("" +(imped4[j] != null ? imped4[j] : "" ), red));
                                }else
                                    phrase31.add(new Chunk("", red));
                            }
                        }
                    }
                    if(i == 5){
                        if (resultStatus)
                            phrase31.add(new Chunk("" +(frequencyMode.equals("multi")? action5[j]:imped5[j]), green));
                        else {
                            if (frequencyMode.equals("multi")) {
                            if (action5[6] != 0.00)
                                phrase31.add(new Chunk("" +action5[j], red));
                            } else {
                                if (imped5[5] != null){
                                    phrase31.add(new Chunk("" +(imped5[j] != null ? imped5[j] : "" ), red));
                                }else
                                    phrase31.add(new Chunk("", red));
                            }
                        }
                    } if(i == 6){
                        if (resultStatus)
                            phrase31.add(new Chunk("" +(frequencyMode.equals("multi")? action6[j]:imped6[j]), green));
                        else {
                            if (frequencyMode.equals("multi")) {
                            if (action6[6] != 0.00)
                                phrase31.add(new Chunk("" +action6[j], red));
                            } else {
                                if (imped6[5] != null){
                                    phrase31.add(new Chunk("" +(imped6[j] != null ? imped6[j] : "" ), red));
                                }else
                                    phrase31.add(new Chunk("", red));
                            }
                        }
                    } /*if(i == 7){
                        if (resultStatus)
                            phrase31.add(new Chunk("" +(frequencyMode.equals("multi")? action7[j]:imped7[j]), green));
                        else
                            phrase31.add(new Chunk("" + (frequencyMode.equals("multi")? action7[j]:imped7[j]), red));
                    } if(i == 8){
                        if (resultStatus)
                            phrase31.add(new Chunk("" +(frequencyMode.equals("multi")? action8[j]:imped8[j]), green));
                        else
                            phrase31.add(new Chunk("" + (frequencyMode.equals("multi")? action8[j]:imped8[j]), red));
                    } if(i == 9){
                        if (resultStatus)
                            phrase31.add(new Chunk("" +(frequencyMode.equals("multi")? action9[j]:imped9[j]), green));
                        else
                            phrase31.add(new Chunk("" + (frequencyMode.equals("multi")? action9[j]:imped9[j]), red));
                    } if(i == 10){
                        if (resultStatus)
                            phrase31.add(new Chunk("" +(frequencyMode.equals("multi")? action10[j]:imped10[j]), green));
                        else
                            phrase31.add(new Chunk("" + (frequencyMode.equals("multi")? action10[j]:imped10[j]), red));
                    }*/

                }
                table.addCell(phrase31);

            }
        }
        return table;
    }

}

