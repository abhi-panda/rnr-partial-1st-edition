package com.example.mohit.rnr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class IPActivity extends AppCompatActivity {

    EditText IpEditText;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPref = getSharedPreferences("A", Context.MODE_PRIVATE);



        IpEditText = (EditText)findViewById(R.id.ip);
        Log.e("IP page IP",sharedPref.getString("IP",null));
        //Setting the IP address
        IpEditText.setText(sharedPref.getString("IP",null));

    }

    public void changeIP(View view){

        Toast.makeText(this, IpEditText.getText().toString(), Toast.LENGTH_SHORT).show();
        editor = sharedPref.edit();
        editor.putString("IP",IpEditText.getText().toString());
        Log.e("IP",sharedPref.getString("IP",null));
        editor.commit();

        Intent intent = new Intent(this,ScanActivity.class);
        startActivity(intent);

    }

}
