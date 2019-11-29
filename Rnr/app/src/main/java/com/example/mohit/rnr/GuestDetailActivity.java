package com.example.mohit.rnr;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class GuestDetailActivity extends AppCompatActivity{

    //XML Elements
    TextView guestNameTextView;
    TextView totalAdultGuestsTextView;
    TextView totalChildGuestsTextView;
    TextView totalAdultGuestsArrivedTextView;
    TextView totalChildGuestsArrivedTextView;

    Spinner adultGuestsArrivedSpinner;
    Spinner childGuestsArrivedSpinner;

    SharedPreferences sharedPref;

    RequestQueue queue;

    Guest newGuest =  new Guest();
    //POJO class Object
    final Guest guest = new Guest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        //Getting refernece to XML objects
        guestNameTextView = findViewById(R.id.guestNameTextView);
        totalAdultGuestsTextView = (TextView)findViewById(R.id.totalAdultGuestsTextView);
        totalChildGuestsTextView = (TextView)findViewById(R.id.totalChildGuestsTextView);

        totalAdultGuestsArrivedTextView = (TextView)findViewById(R.id.totalAdultGuestsArrivedTextView);
        totalChildGuestsArrivedTextView = (TextView)findViewById(R.id.totalChildGuestsArrivedTextView);

        adultGuestsArrivedSpinner = (Spinner)findViewById(R.id.adultGuestsArrivedSpinner);
        childGuestsArrivedSpinner = (Spinner)findViewById(R.id.childGuestsArrivedSpinner);

        // To get BarcodeId from Intent
        Bundle extras = getIntent().getExtras();
        String guestBarcodeId = (extras.get("barcodeValue").toString());
        Log.e("Working till Line 69","Hello");

        //Setting up the URl
        sharedPref = getSharedPreferences("A", Context.MODE_PRIVATE);
        String url ="http://"+sharedPref.getString("IP",null)+":3000/guest/"+guestBarcodeId;
        Log.d("IP ",url);


        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

        Log.e("Working till Line 80 ","Hello");
        // Request a json response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
               Request.Method.GET,
               url,
               null,
               new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {
                       try {

                           //Adding all the properties of scanned Guest
                           guest.setUid(response.getString("uid"));
                           guest.setName(response.getString("name"));
                           guest.setMobile(response.getString("mobile"));
                           guest.setTotalAdults(Integer.parseInt(response.getString("totalAdults")));
                           guest.setTotalKids(Integer.parseInt(response.getString("totalKids")));
                           guest.setAdultsArrived(Integer.parseInt(response.getString("adultsArrived")));
                           guest.setKidsArrived(Integer.parseInt(response.getString("kidsArrived")));
                           guest.setEmail(response.getString("email"));
                           Log.e("Working till Line 100 ",guest.getName());
                           updateView(guest);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               },
               new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Something went wrong. Please try again.",Toast.LENGTH_SHORT).show();
                   }
               }
        );

        Log.e("Working till Line 114 ","Hello");

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    // Generate remaining Guest values for spinner dropdown
    public String[] spinnerValueGenerator(int remainingPeople){
        if(remainingPeople == 0){
            String strArray[] = new String[1];
            strArray[0]="0";
            return  strArray;
        }
        String strArray[] = new String[remainingPeople+1];

        for(int i=0;i<=remainingPeople;i++){
            strArray[i]=String.valueOf(i);
        }
        return strArray;
    }

    // updates the Textfield in UI
    public void updateView(final Guest guest){
        guestNameTextView.setText(guest.getName());
        totalAdultGuestsTextView.setText(String.valueOf(guest.getTotalAdults()));
        totalChildGuestsTextView.setText(String.valueOf(guest.getTotalKids()));

        totalAdultGuestsArrivedTextView.setText(String.valueOf(guest.getAdultsArrived()));
        totalChildGuestsArrivedTextView.setText(String.valueOf(guest.getKidsArrived()));

        String remainingAdultGuests[] = spinnerValueGenerator(guest.getTotalAdults()- guest.getAdultsArrived());
        String remainingChildGuests[] = spinnerValueGenerator(guest.getTotalKids()- guest.getKidsArrived());


        //Dropdown adapter for Adult Guest
        ArrayAdapter adapterAdultGuest = new ArrayAdapter(this,android.R.layout.simple_spinner_item,remainingAdultGuests);
        adapterAdultGuest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adultGuestsArrivedSpinner.setAdapter(adapterAdultGuest);
        adultGuestsArrivedSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),"Adult "+i,Toast.LENGTH_SHORT).show();
                newGuest.setAdultsArrived(guest.getAdultsArrived()+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Dropdown adapter for Child Guest
        ArrayAdapter adapterChildGuest = new ArrayAdapter(this,android.R.layout.simple_spinner_item,remainingChildGuests);
        adapterChildGuest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        childGuestsArrivedSpinner.setAdapter(adapterChildGuest);
        childGuestsArrivedSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(),"Child "+i,Toast.LENGTH_SHORT).show();
                newGuest.setKidsArrived(guest.getKidsArrived()+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Log.e("Working till Line 183 ","Hello");
    }



    public void submitForm(View view){
        Toast.makeText(this, "work in progress", Toast.LENGTH_SHORT).show();

        newGuest.setUid(guest.getUid());
        newGuest.setName(guest.getName());
        newGuest.setMobile(guest.getMobile());
        newGuest.setTotalAdults(guest.getTotalAdults());
        newGuest.setTotalKids(guest.getTotalKids());
        newGuest.setEmail(guest.getEmail());


        sharedPref = getSharedPreferences("A", Context.MODE_PRIVATE);
        String url ="http://"+sharedPref.getString("IP",null)+":3000/guest/"+newGuest.getUid();


        Log.e("Json request Object ",newGuest.toString());
        JSONObject reqObject = null;

        try {
            reqObject = new JSONObject(newGuest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Request a json response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                reqObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Update Response",response.toString());
                        //Adding all the properties of scanned Guest
                        try {
                            guest.setUid(response.getString("uid"));
                            guest.setName(response.getString("name"));
                            guest.setMobile(response.getString("mobile"));
                            guest.setTotalAdults(Integer.parseInt(response.getString("totalAdults")));
                            guest.setTotalKids(Integer.parseInt(response.getString("totalKids")));
                            guest.setAdultsArrived(Integer.parseInt(response.getString("adultsArrived")));
                            guest.setKidsArrived(Integer.parseInt(response.getString("kidsArrived")));
                            guest.setEmail(response.getString("email"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("Working till Line 100 ",guest.getName());
                        updateView(guest);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Update Response",error.toString());

                        Toast.makeText(getApplicationContext(),"Something went wrong. Please try again.",Toast.LENGTH_SHORT).show();
                    }
                }

        );

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
}
