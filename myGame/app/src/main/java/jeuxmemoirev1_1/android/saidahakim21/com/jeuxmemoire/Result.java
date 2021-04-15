package jeuxmemoirev1_1.android.saidahakim21.com.jeuxmemoire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
// ** Custom code here ** //

public class Result extends AppCompatActivity {

    TextView resultAffichage;
    Button restartButton;
    public Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        resultAffichage = (TextView) findViewById(R.id.affichage);
        int result = getIntent().getIntExtra("result",0);
        int numAction = getIntent().getIntExtra("numAction",0);
        resultAffichage.setText("your result is  "+result+"/"+numAction);
        restartButton = (Button) findViewById(R.id.buttonrestart);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
    }


    public void send(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
