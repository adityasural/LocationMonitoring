package coventryuniversity.locationmonitoring;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class EditUserActivity extends Activity {

    private String name="";
    private String phoneNumber="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        Intent intent=getIntent();

        name=intent.getStringExtra("Name");
        phoneNumber=intent.getStringExtra("PhoneNumber");

        TextView editName=(TextView) findViewById(R.id.editName);
        editName.setText(name);
        TextView editPhoneNumber=(TextView) findViewById(R.id.editPhoneNumber);
        editPhoneNumber.setText(phoneNumber);

        Button editPermissionButton=(Button)findViewById(R.id.editUserButton);
        editPermissionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final RadioGroup editPermissionRadioButtonGroup=(RadioGroup) findViewById(R.id.editPermissionRadioButtonGroup);
                int permissionId=editPermissionRadioButtonGroup.getCheckedRadioButtonId();
                int allowed=permissionId==R.id.editGivePermission?1:0;
                DatabaseHandler db = new DatabaseHandler(EditUserActivity.this);
                db.updateUserPermission(name,phoneNumber,allowed);

                Intent intent = new Intent(EditUserActivity.this, ManageNumbersActivity.class);
                startActivity(intent);
                Toast.makeText(EditUserActivity.this, name + "'s permission changed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_user, menu);
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
