package meg.ru.kireev.lenya.meg;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MenuActivity extends Activity {

    @SuppressWarnings("deprecation")
    Button btn,btn2,btn3;
    public boolean isNameFree(String name) {

        File file = new File(String.valueOf(getFilesDir()),name);
        return !file.exists();
    }
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

     // пишем данные

        btn = findViewById(R.id.button);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.how_to);
        if(isNameFree("lvl.txt"))
            try {
                // отрываем поток для записи
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                        openFileOutput("lvl.txt", MODE_PRIVATE)));
                // пишем данные
                bw.write(String.valueOf(1.0));
                // закрываем поток
                bw.close();
                Log.d("FILE_SYS", "Файл записан main");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
     btn.setOnClickListener(new View.OnClickListener() {

         @SuppressLint("NewApi")
         @Override
         public void onClick(View v) {
             Intent i = new Intent(MenuActivity.this, GameActivity.class);
             startActivity(i);
         }
     });

        btn2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                //выход из игры
                onBackPressed();

            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, how.class);
                startActivity(i);
            }
        });
    }
}
