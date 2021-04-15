package jeuxmemoirev1_1.android.saidahakim21.com.jeuxmemoire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import java.util.concurrent.Semaphore;

public class MainActivity extends AppCompatActivity {
    private Button startButton;
    private EditText columnNumEdit;
    private EditText actionNumEdit;
    private SeekBar speedBar;
    public int columnNum;
    public int speed;
    public int NumActions;
    public static final int MAX_AVAILABLE = 0;
    public static final Semaphore available = new Semaphore(MAX_AVAILABLE, true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = (Button) findViewById(R.id.startButton);
        columnNumEdit = (EditText) findViewById(R.id.columnsNum);
        speedBar = (SeekBar) findViewById(R.id.speedBar);
        actionNumEdit = (EditText) findViewById(R.id.actionNum);
        speedBar.setProgress(50);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
    }

    public void send() {
        columnNum = Integer.parseInt(String.valueOf(columnNumEdit.getText()));
        speed = speedBar.getProgress() / 10;
        NumActions = Integer.parseInt(String.valueOf(actionNumEdit.getText()));
        //  OpenGLES20Activity.columnNum=columnNum;
        //  OpenGLES20Activity.speed=speed;
        //  OpenGLES20Activity.NumActions = NumActions;
        Intent intent = new Intent(this, OpenGLES20Activity.class);
        intent.putExtra("columnNum", columnNum);
        intent.putExtra("speed", speed);
        intent.putExtra("NumActions", NumActions);
        startActivity(intent);
    }
}
