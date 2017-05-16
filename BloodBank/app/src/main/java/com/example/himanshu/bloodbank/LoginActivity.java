package com.example.himanshu.bloodbank;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    String status=null;
EditText id,pass;
   String id2, pass2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        id= (EditText) findViewById(R.id.loginET);
        pass= (EditText) findViewById(R.id.passwordET);
        try {
            JSONObject toSend = new JSONObject();
          id2= id.getText().toString();
            pass2= pass.getText().toString();
            toSend.put("username",id2);
            toSend.put("password",pass2);
            SendMessageL s1=new SendMessageL();
            s1.execute(toSend);



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class SendMessageL extends AsyncTask<JSONObject,JSONObject,JSONObject> {

        private Dialog loadingDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        String url = "http://192.168.100.131/android_login_api/login.php";
        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            JSONObject json = params[0];
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);

            JSONObject jsonResponse = null;
            HttpPost post = new HttpPost(url);
            try {
                StringEntity se = new StringEntity("json="+json.toString());
                post.addHeader("content-type", "application/x-www-form-urlencoded");
                post.setEntity(se);

                HttpResponse response;
                response = client.execute(post);
                String resFromServer = org.apache.http.util.EntityUtils.toString(response.getEntity());

                jsonResponse=new JSONObject(resFromServer);
            } catch (Exception e) { e.printStackTrace();}

            return jsonResponse;
        }




        @Override
        protected void onPostExecute(JSONObject result) {
            if(result==null)
            {
                Toast.makeText(getApplicationContext(),"Logged in Successfully...",Toast.LENGTH_LONG).show();
                Intent i = new Intent(LoginActivity.this,Login.class);
               startActivity(i);
            }
            else{



                try{
                    status=result.getString("status");

                }
                catch (JSONException s){

                }
                Toast.makeText(getApplicationContext(),status, Toast.LENGTH_LONG).show();
            }





        }
    }
}
