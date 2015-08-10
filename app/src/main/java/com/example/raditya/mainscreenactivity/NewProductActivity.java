package com.example.raditya.mainscreenactivity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewProductActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    EditText inputId;
    EditText inputNama;
    EditText inputLatitude;
    EditText inputLongitude;

    // url to create new product
    private static String url_create_product = "http://tegarankar.esy.es/Post.php";

    //JSON Node Names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        //Edit Text
        inputId = (EditText) findViewById(R.id.id);
        inputNama = (EditText) findViewById(R.id.nama);
        inputLatitude = (EditText) findViewById(R.id.latitude);
        inputLongitude = (EditText) findViewById(R.id.longitude);

        //Create Button
        Button btnSave = (Button) findViewById(R.id.btnSave);

        //button click event
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CreateNewProduct().execute();

            }
        });

    }

    class CreateNewProduct extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewProductActivity.this);
            pDialog.setMessage("Creating Product...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        protected String doInBackground(String... args) {
            String id = inputId.getText().toString();
            String nama = inputNama.getText().toString();
            String latitude = inputLatitude.getText().toString();
            String longitude = inputLongitude.getText().toString();

            //Building parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", id ));
            params.add(new BasicNameValuePair("nama", nama));
            params.add(new BasicNameValuePair("latitude", latitude));
            params.add(new BasicNameValuePair("longitude", longitude));

            // getting JSON Object
            // Note that create product url accepts POST method

            JSONObject json = jsonParser.makeHttpRequest(url_create_product, "POST", params);

            //Check log cat from response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    //successfully created product
                    Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
                    startActivity(i);

                    finish();

                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }
    }

    protected void onPostExecute(String file_url) {
        pDialog.dismiss();
    }

}

