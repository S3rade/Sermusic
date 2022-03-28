package sg.edu.tp.musicstreamingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends Activity {

    private EditText username;
    private EditText password;
    private static Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_button = (Button) findViewById(R.id.buttonlogin);
    }


    public void loginchecking(View v)
    {username = (EditText) findViewById(R.id.editText_user);
    password = (EditText) findViewById(R.id.editText_password);
    if (!username.getText().toString().equals(" ") & !password.getText().toString().equals(" "))
    {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
    }
    }

    }
