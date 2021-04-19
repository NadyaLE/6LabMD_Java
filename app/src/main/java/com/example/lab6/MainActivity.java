package com.example.lab6;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ActionBar actionBar;
    EditText txtIn;
    EditText txtOut;
    SeekBar sb;
    EditText editKey;
    Button button;
    FloatingActionButton fab;
    void CreateAndSetActBar(){
        actionBar = getSupportActionBar();
        actionBar.setTitle("Secret Messages");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CreateAndSetActBar();
        txtIn = (EditText) findViewById(R.id.txtIn);
        txtOut = (EditText) findViewById(R.id.txtOut);
        sb = (SeekBar) findViewById(R.id.sb);
        editKey = (EditText)findViewById(R.id.editKey);
        button = (Button) findViewById(R.id.button);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        editKey.setText(""+ (int)(sb.getProgress() - 16));
        Intent receivedIntent = getIntent();
        String receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT);
        if (receivedText!= null) {
            txtIn.setText(receivedText);
        }
        button.setOnClickListener(v -> {
            int key = Integer.parseInt(editKey.getText().toString());
            String message = txtIn.getText().toString();
            String output = encode(message, key);
            txtOut.setText(output);
        });

        fab.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Secret Message "+ DateFormat.getDateTimeInstance().format(new Date()));
            shareIntent.putExtra(Intent.EXTRA_TEXT, txtOut.getText().toString());
            try {startActivity(Intent.createChooser(shareIntent, "Share message..."));
                finish();
            }
            catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "Error: Couldn't share.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {
                int key = sb.getProgress() - 16;
                String message = txtIn.getText().toString();
                String output = encode(message, key);
                txtOut.setText(output);
                editKey.setText("" + key);
            }
            @Override
            public void onStartTrackingTouch(SeekBar sb) { }
            @Override
            public void onStopTrackingTouch(SeekBar sb) { }
        });
    }

    public String encode(String message, int keyVal) {
        String output = "";
        char key = (char) keyVal;
        for (int x = 0; x < message.length(); x++) {
            char input = message.charAt(x);
            if (input >= 'А' && input <= 'Я') {
                input += key;
                if (input > 'Я')
                    input -= 32;
                if (input < 'А')
                    input += 32;
            } else if (input >= 'а' && input <= 'я') {
                input += key;
                if (input > 'я')
                    input -= 32;
                if (input < 'а')
                    input += 32;
            } else if (input >= '0' && input <= '9') {
                input += (keyVal % 10);
                if (input > '9')
                    input -= 10;
                if (input < '0')
                    input += 10;
            }
            output += input;
        }
        return output;
    }

}