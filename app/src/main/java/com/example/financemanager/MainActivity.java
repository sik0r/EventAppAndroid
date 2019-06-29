package com.example.financemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.financemanager.Models.LoginModel;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button signInBtn;
    EditText login;
    EditText password;
    TextView errors;
    RequestQueue queue;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValid(login, password)) {
                    Toast.makeText(MainActivity.this, getString(R.string.empty_fields), Toast.LENGTH_LONG).show();
                    return;
                }

                JSONObject json = prepareJson(createLoginModel(login, password));
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.Url.LOGIN, json,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject json = new JSONObject(response.toString());
                                    storeJwtToken(json);

                                    Intent intent = new Intent(getBaseContext(), EventListActivity.class);
                                    startActivity(intent);
                                } catch (JSONException ex) {
                                    Log.d("JSONException", ex.getMessage());
                                    Toast.makeText(MainActivity.this, R.string.invalid_credentials, Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                JSONObject response = null;
                                try {
                                    response = new JSONObject(new String(error.networkResponse.data));
                                    Log.d("API ERROR", response.get("message").toString());
                                    Toast.makeText(MainActivity.this, response.get("message").toString(), Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
                queue.add(request);
            }
        });
    }

    private void init() {
        queue = Volley.newRequestQueue(this);
        preferences = this.getSharedPreferences("string", Context.MODE_PRIVATE);
        initWidgets();
    }

    private void initWidgets() {
        signInBtn = (Button) findViewById(R.id.signIn);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        errors = (TextView) findViewById(R.id.errorsText);
    }

    private boolean isValid(EditText login, EditText password) {
        return !TextUtils.isEmpty(login.getText()) && !TextUtils.isEmpty(password.getText());
    }

    private JSONObject prepareJson(LoginModel model) {
        JSONObject json = new JSONObject();

        try {
            json.put("email", model.getLogin());
            json.put("password", model.getPassword());
        } catch (JSONException ex) {
            Log.e("ERROR JSONException", ex.getMessage(), ex);
            errors.setText(R.string.json_object_exception_on_create);
        }

        return json;
    }

    private LoginModel createLoginModel(EditText loginText, EditText passwordText) {
        String login = loginText.getText().toString();
        String password = passwordText.getText().toString();

        return new LoginModel(login, password);
    }

    private void storeJwtToken(JSONObject json) throws JSONException {
        String token = json.getString("token");
        Log.d("JWT TOKEN", token);

        preferences.edit()
                .putString(Constants.JWT_NAME, token)
                .apply();
    }
}
