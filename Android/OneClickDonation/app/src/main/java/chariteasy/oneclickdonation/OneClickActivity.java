package chariteasy.oneclickdonation;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

public class OneClickActivity extends AppCompatActivity {
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_click);
        Bundle args = getIntent().getExtras();
        mEmail = args.getString("email");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_activity_one_click, menu);
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

    public void Donate(View view) {
        new OnClickPayment().execute();
    }
    public class OnClickPayment extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject  doInBackground(Void... params) {
            RestClient restClient = new RestClient();
            return restClient.get("/ws/api/v1/oneClickPayment?email=" + mEmail);
        }

        @Override
        protected void onPostExecute(final JSONObject result) {
            try {
                if (result.has("result") && result.getString("result").equals("OK")) {
                    Toast.makeText(getBaseContext(), "Payment Successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Payment Failed!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Payment Failed!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
