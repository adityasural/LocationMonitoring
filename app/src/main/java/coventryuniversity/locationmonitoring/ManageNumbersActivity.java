package coventryuniversity.locationmonitoring;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;


public class ManageNumbersActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_numbers);

        final Button addNewUser = (Button) findViewById(R.id.addNewUser);
        addNewUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(ManageNumbersActivity.this, AddUserActivity.class));
                finish();
            }
        });

        DatabaseHandler db = new DatabaseHandler(this);
        List<String> names=db.getUserNameList();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, names);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner userNameSpinner=(Spinner) findViewById(R.id.userNameSpinner);
        userNameSpinner.setAdapter(dataAdapter);


        userNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String name = parentView.getItemAtPosition(position).toString();
                if (name != null || name != "") {
                    DatabaseHandler db = new DatabaseHandler(ManageNumbersActivity.this);
                    final UserDetails userDetails = db.getUserDetails(name);
                    TextView userNameDetailsMessage = (TextView) findViewById(R.id.userNameDetailsMessage);
                    userNameDetailsMessage.setText("User details are as follows :");
                    TextView userNameMessage = (TextView) findViewById(R.id.userNameMessage);
                    userNameMessage.setText("Name :");
                    TextView userName = (TextView) findViewById(R.id.userName);
                    userName.setText(userDetails.Name);
                    TextView userPhoneNumberMessage = (TextView) findViewById(R.id.userPhoneNumberMessage);
                    userPhoneNumberMessage.setText("Phone Number :");
                    TextView userPhoneNumber = (TextView) findViewById(R.id.userPhoneNumber);
                    userPhoneNumber.setText(userDetails.PhoneNumber);
                    TextView userEligibilityMessage = (TextView) findViewById(R.id.userEligibilityMessage);
                    userEligibilityMessage.setText("Can share location? :");
                    TextView userEligibility = (TextView) findViewById(R.id.userEligibility);
                    userEligibility.setText(userDetails.isEligible ? "Yes" : "No");
                    Button editUserPermission = (Button) findViewById(R.id.editPermissionButton);
                    editUserPermission.setVisibility(View.VISIBLE);
                    editUserPermission.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(ManageNumbersActivity.this, EditUserActivity.class);
                            intent.putExtra("Name", userDetails.Name);
                            intent.putExtra("PhoneNumber", userDetails.PhoneNumber);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

//        Button editUserPermission=(Button)findViewById(R.id.editPermissionButton);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_numbers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
