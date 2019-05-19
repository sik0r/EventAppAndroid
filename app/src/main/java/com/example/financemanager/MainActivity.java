package com.example.financemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    Button signInBtn;
    EditText login;
    EditText password;
    TextView errors;
    RequestQueue queue;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInBtn = (Button) findViewById(R.id.signIn);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        errors = (TextView) findViewById(R.id.errorsText);

        queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.GET, "https://google.com",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        errors.setText("Response is: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String err = error.getMessage().toString() + error.toString();
                errors.setText(err);
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isValid(login, password)) {
                    errors.setText(getString(R.string.empty_fields));
                    return;
                }

                queue.add(stringRequest);
            }
        });
    }

    private boolean isValid(EditText login, EditText password) {
        return !TextUtils.isEmpty(login.getText()) && !TextUtils.isEmpty(password.getText());
    }
}
