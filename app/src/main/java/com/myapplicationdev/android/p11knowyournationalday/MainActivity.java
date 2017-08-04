package com.myapplicationdev.android.p11knowyournationalday;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SharedPreferences settings;
    ListView lv;

    ArrayList<String> al = new ArrayList<String>();
    ArrayAdapter aa;
    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences("JSON", MODE_PRIVATE);
        if (!settings.getString("Access", "").equals("auth")){
            login();
        }

        lv = (ListView) findViewById(R.id.lvList);

        al.add("Singapore National Day is on 9 August");
        al.add("Singapore is 52 years old");
        al.add("Theme is #OneNationTogether");

        aa = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,al);
        lv.setAdapter(aa);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sendFriend) {
            sendFriend();
        } else if (item.getItemId() == R.id.Quit) {
            Quit();
        } else if (item.getItemId() == R.id.Quiz) {
            Quiz();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    public void login(){
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout login =
                (LinearLayout) inflater.inflate(R.layout.login, null);
        final EditText etAccessCode = (EditText) login
                .findViewById(R.id.editTextAccessCode);

        dialog = new AlertDialog.Builder(this)
                .setView(login)
                .setTitle("Please Enter")
                .setPositiveButton("Done", null)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        if (etAccessCode.getText().toString().equals("738964")){
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("Access", "auth");
                            editor.commit();
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "Your Access Code correcy, logged in",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Your Access Code was incorrect, please try again ",
                                Toast.LENGTH_LONG).show();
                    }
                    }
                });
            }

        });
        dialog.show();
    }

    public void sendFriend(){
        String [] list = new String[] { "Email", "SMS" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select the way to enrich your friend")
                // Set the list of items easily by just supplying an
                //  array of the items
                .setItems(list, new DialogInterface.OnClickListener() {
                    // The parameter "which" is the item index
                    // clicked, starting from 0
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //Open email App
                            Intent email = new Intent(Intent.ACTION_SEND);
                            // Put essentials like email address, subject & body text
                            email.putExtra(Intent.EXTRA_SUBJECT,
                                    "National Day Facts!");
                            email.putExtra(Intent.EXTRA_TEXT, al.get(0) + "\n" + al.get(1) + "\n" + al.get(2));
                            // This MIME type indicates email
                            email.setType("message/rfc822");
                            // createChooser shows user a list of app that can handle
                            // this MIME type, which is, email
                            startActivity(Intent.createChooser(email,
                                    "Choose an Email client :"));
                        } else {
                            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                            sendIntent.putExtra(al.get(0) + "\n" + al.get(1) + "\n" + al.get(2), "default content");
                            sendIntent.setType("vnd.android-dir/mms-sms");
                            startActivity(sendIntent);
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void Quit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?")
                // Set text for the positive button and the corresponding
                //  OnClickListener when it is clicked
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        System.exit(0);
                    }
                })
                // Set text for the negative button and the corresponding
                //  OnClickListener when it is clicked
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MainActivity.this, "You clicked no",
                                Toast.LENGTH_LONG).show();
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void Quiz(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout quiz = (LinearLayout) inflater.inflate(R.layout.quiz, null);

        dialog = new AlertDialog.Builder(this)
                .setView(quiz)
                .setTitle("Test Yourself!")
                .setNegativeButton("DON'T KNOW LAH", null)
                .setPositiveButton("Ok", null)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {


                Button btnOk = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnCancel = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    Integer score = 0;
                    @Override
                    public void onClick(View view) {

                        final RadioGroup rd1 = (RadioGroup) quiz.findViewById(R.id.firstAns);
                        final RadioGroup rd2 = (RadioGroup) quiz.findViewById(R.id.secondAns);
                        final RadioGroup rd3 = (RadioGroup) quiz.findViewById(R.id.thirdAns);

                        int selectedId1 = rd1.getCheckedRadioButtonId();
                        int selectedId2 = rd2.getCheckedRadioButtonId();
                        int selectedId3 = rd3.getCheckedRadioButtonId();

                        RadioButton checkedRadio1 = (RadioButton) quiz.findViewById(selectedId1);
                        RadioButton checkedRadio2 = (RadioButton) quiz.findViewById(selectedId2);
                        RadioButton checkedRadio3 = (RadioButton) quiz.findViewById(selectedId3);

                        if (checkedRadio1.getText().toString().equals("No")){score += 1;}
                        if (checkedRadio2.getText().toString().equals("Yes")){score += 1;}
                        if (checkedRadio3.getText().toString().equals("Yes")){score += 1;}

                        Toast.makeText(MainActivity.this, "Your score is " + score,
                                Toast.LENGTH_LONG).show();

                        score = 0;
                        dialog.dismiss();
                    }});

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }

        });
        dialog.show();
    }
}