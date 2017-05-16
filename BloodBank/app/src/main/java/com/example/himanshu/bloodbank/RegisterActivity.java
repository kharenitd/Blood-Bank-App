package com.example.himanshu.bloodbank;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    EditText eName,eMail,eContactNo,eAddress,eCity,eUsername,ePassword,eId;
    String Name,Mail,ContactNo,Address,City,Username,Password,Id,gender;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioGender);
        eName= (EditText) findViewById(R.id.editText);
        eMail= (EditText) findViewById(R.id.editText2);
        eContactNo= (EditText) findViewById(R.id.editText3);
        eAddress= (EditText) findViewById(R.id.editText4);
        eCity= (EditText) findViewById(R.id.editText5);
        eUsername= (EditText) findViewById(R.id.editText6);
        ePassword= (EditText) findViewById(R.id.editText7);
        eId= (EditText) findViewById(R.id.editText8);

    }
    public void userReg(View view )
    {
        Name= eName.getText().toString();
        Mail=eMail.getText().toString();
        final String emailPattern= "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        boolean flag = true;
        if (Mail == null) {
            flag = false;
        } else {
            flag = Mail.matches(emailPattern);
        }

        if(flag == false)
            Toast.makeText(getApplicationContext(),
                    "Enter email correctly", Toast.LENGTH_LONG)
                    .show();
        ContactNo= eContactNo.getText().toString();
        Address= eAddress.getText().toString();
        City= eCity.getText().toString();
        Username= eUsername.getText().toString();
        Password= ePassword.getText().toString();
        Id= eId.getText().toString();

        int selectedId = radioSexGroup.getCheckedRadioButtonId();

        radioSexButton = (RadioButton) findViewById(selectedId);
        gender=radioSexButton.getText().toString();
        try {
            JSONObject toSend = new JSONObject();
            toSend.put("name", Name);
            toSend.put("email",Mail);
            toSend.put("phone",ContactNo);
            toSend.put("address",Address);
            toSend.put("city",City);
            toSend.put("username",Username);
            toSend.put("password",Password);
            toSend.put("id",Id);
            toSend.put("gender",gender);
            SendMessage s1=new SendMessage();
            s1.execute(toSend);



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public class SendMessage extends AsyncTask<JSONObject,JSONObject,JSONObject> {

        private Dialog loadingDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        String url = "http://192.168.100.8/android_login_api/register.php";
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
                Toast.makeText(getApplicationContext(),"Registerred Successfully...",Toast.LENGTH_LONG).show();

            }
            else{

                String status=null;

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
