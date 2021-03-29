package info.fickle.fickleserver.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import info.fickle.fickleserver.R;
import info.fickle.fickleserver.activity.MainActivity;

/**
 * Created by Bharath on 03/11/16.
 */

public class OfferActivity extends AppCompatActivity {
    EditText off;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_offer);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        off = (EditText) findViewById(R.id.offer_text);

    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
    public void onClickOffer(View view){
        String offer = off.getText().toString();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("offer", offer);
        editor.commit();
        Intent i= new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition( R.anim.trans_slide_in_up, R.anim.trans_slide_out_up );

    }
}
