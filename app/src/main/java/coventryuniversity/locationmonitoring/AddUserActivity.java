package coventryuniversity.locationmonitoring;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class AddUserActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        final Button button=(Button) findViewById(R.id.addUserButton);
        final EditText userNameEditText=(EditText) findViewById(R.id.userName);
        final EditText phoneNumberText=(EditText) findViewById(R.id.phoneNumber);
        final RadioGroup permissionRadioButtonGroup=(RadioGroup) findViewById(R.id.permissionRadioButtonGroup);
        final Context context=this;

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int permissionId=permissionRadioButtonGroup.getCheckedRadioButtonId();
                String userName=userNameEditText.getText().toString();
                String phoneNumber=phoneNumberText.getText().toString();
                int userAllowed=permissionId==R.id.givePermission?1:0;

                DatabaseHandler db=new DatabaseHandler(context);
                db.insertUserDetails(userName,phoneNumber,userAllowed);
                startActivity(new Intent(AddUserActivity.this, ManageNumbersActivity.class));
                Toast.makeText(AddUserActivity.this, userName + "Registered", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_user, menu);
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
