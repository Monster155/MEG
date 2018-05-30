package meg.ru.kireev.lenya.meg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.AbsoluteLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.ThreadFactory;

public class GameActivity extends Activity {
    ImageButton advise;
    ImageView iv;
    TextView hpbar,tvadv;
    RelativeLayout.LayoutParams bar;
    TextView tv, tv2,tv3,tvlvl,prg;
    ListView lv, lv2;
    String LOG_TAG = "GA";
    GameThread gameThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("GA", "--- start ---");
        setContentView(R.layout.activity_game);

        Log.d(LOG_TAG, "--- completed ---");
        advise = findViewById(R.id.advise);
        tvadv = findViewById(R.id.ttad);
        iv = findViewById(R.id.mnstr);
        tv = findViewById(R.id.tv);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tvlvl = findViewById(R.id.lvl);
        lv = findViewById(R.id.lv);
        lv2 = findViewById(R.id.lv2);
        hpbar = findViewById(R.id.progressBar);
        prg = findViewById(R.id.prg);
        bar = (RelativeLayout.LayoutParams) hpbar.getLayoutParams();
        Log.d("HPBAR", "bar params is --- "+bar.width+ " " + bar.height);
        gameThread = new GameThread(getResources());
        gameThread.setRunnable(true);
        gameThread.start();
        Log.d("GA", "- GT |||");
    }



    public class GameThread extends Thread {
        private boolean runnable;
        GameProcess gameProcess;


        public GameThread(Resources resources) {
            this.gameProcess = new GameProcess(getApplicationContext(),tvadv,advise,prg,bar, iv,hpbar, tv, tv2,tv3,tvlvl, lv, lv2,GameActivity.this);
        }

        public void setRunnable(boolean runnable) {
            this.runnable = runnable;
        }



        @SuppressLint("NewApi")
        @Override
        public void run() {
            Log.d("GA", "YEEE, we are running, dude");
            while(1==1) {
                gameProcess.gogame();
                Log.d("GA", "OUUUU, We didn't die");
            }

        }

    }


}
