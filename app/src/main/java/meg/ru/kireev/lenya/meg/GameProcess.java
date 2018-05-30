package meg.ru.kireev.lenya.meg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.RelativeLayout.*;
import static java.util.Arrays.sort;


public class GameProcess {
    Cursor cursor;
    DBHelper dbHelper;
    SQLiteDatabase db;
    ImageView iv;
    static TextView hpbar;
    static TextView prg;
    TextView tv, tv2, tv3, tvlvl, tvadv;
    ListView lv;
    ListView lv2;
    private static Activity activity;
    ArrayAdapter<String> first, second;
    Context context;
    String[] words, answer, w1, w2;
    String name, nameru, rusent;
    int cnt;
    int adv_count, adv_check, extra = 0, wid;
    ImageButton advise;
    static RelativeLayout.LayoutParams barsize;
    ViewGroup.LayoutParams bar;
    Double lvl;
    static Double hp;
    static Double hpmax;
    static Double hpm;
    final String FILENAME = "file";

    public GameProcess(Context context, TextView tvad, ImageButton advise, TextView prg, RelativeLayout.LayoutParams bar, ImageView iv, TextView hpbar, TextView tv, TextView tv2, TextView tv3, TextView tvlvl, ListView lv, ListView lv2, Activity activity) {
        this.context = context;
        this.iv = iv;
        this.tv = tv;
        this.tv2 = tv2;
        this.tvlvl = tvlvl;
        this.prg = prg;
        this.bar = bar;
        this.tv3 = tv3;
        this.lv = lv;
        this.lv2 = lv2;
        this.activity = activity;
        this.hpbar = hpbar;
        this.tvadv = tvad;
        this.advise = advise;
        wid = hpbar.getLayoutParams().width;

    }

    void writeFile() {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    context.openFileOutput("lvl.txt", MODE_PRIVATE)));
            // пишем данные
            bw.write(String.valueOf(lvl + 0.4));
            // закрываем поток
            bw.close();
            Log.d("FILE_SYS", "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readFile() {
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    context.openFileInput("lvl.txt")));
            String str = "", ans = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                Log.d("FILE_SYS", "Достали строку" + str);
                ans += str;
            }
            lvl = Double.parseDouble(ans);
            Log.d("FILE_SYS", "Впихнули в переменную" + lvl);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set_all() {
        cursor = db.rawQuery("SELECT * FROM senten", null);
        if (Math.round(lvl) < 8)
            cursor.move(1 + (int) (Math.random() * 30));
        else if (Math.round(lvl) < 14)
            cursor.move(10 + (int) (Math.random() * 60));
        else if (Math.round(lvl) < 21)
            cursor.move(30 + (int) (Math.random() * 120));
        else
            cursor.move(1 + (int) (Math.random() * 179));

        words = cursor.getString(1).split(" ");
        answer = Arrays.copyOf(words, words.length);
        rusent = cursor.getString(2);
        cursor.close();
        Log.d("GG", Arrays.toString(answer) + " --- " + Arrays.toString(words));

        {
            List<String> list = Arrays.asList(words);
            Collections.shuffle(list);
            list.toArray(words);
        }

        int mid = words.length % 2 == 0 ? words.length / 2 : words.length / 2 + 1;
        Log.d("GG", Arrays.toString(answer) + " --- " + Arrays.toString(words));
        w1 = new String[mid];
        w2 = new String[words.length / 2];
        for (int i = 0; i < mid; i++) {
            w1[i] = words[i];
        }
        for (int i = mid; i < words.length; i++) {
            w2[i - mid] = words[i];
        }
        first = new ArrayAdapter<>(context, R.layout.my_list_item, w1);
        second = new ArrayAdapter<>(context, R.layout.my_list_item, w2);
        cnt = 0;
        adv_count = answer.length / 3;
        adv_check = adv_count;
        adv_count += extra;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvadv.setText("Подсказок: "+adv_count);
                lv.setAdapter(first);
                lv2.setAdapter(second);
                tv.setText(rusent);
                tv2.setText("");
            }
        });

    }

    public void setView_enemy() {
        int kek = (int) (Math.random() * 9);
        name = activity.getResources().getStringArray(R.array.arrayWithStrings)[kek];
        nameru = activity.getResources().getStringArray(R.array.arrayWithStringsRUS)[kek];
        hpmax = Math.round(lvl) * 3 + Math.random() * Math.round(lvl) * 0.5;
        hp = hpmax;
        hpm = hpmax;
        barsize = (RelativeLayout.LayoutParams) bar;
        activity.runOnUiThread(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                //hpbar.getLayoutParams().width = bar.width;
                wid = hpbar.getLayoutParams().width;
                Log.d("HPBAR", "Set hpbar settings - " + bar.width + " " + hpbar.getWidth());
                prg.setText((int) Math.round(hp) + "/" + (int) Math.round(hpmax));
                iv.setImageResource(activity.getResources().
                        getIdentifier(name, "drawable", context.getPackageName()));
                tv3.setText(nameru);
                tvlvl.setText("Ур. " + (int) Math.round(lvl));
            }
        });
    }

    static public void update_enemy() {
        barsize.width = (int) Math.round(hp / hpmax * prg.getWidth());
        Log.d("HPBAR", "bar width --- " + barsize.width + " " + (int) Math.round(hp / hpmax * prg.getWidth()));
        activity.runOnUiThread(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                barsize.width = (int) Math.round(hp / hpmax * prg.getWidth());
                prg.setText((int) Math.round(hp) + "/" + (int) Math.round(hpmax));
                hpbar.getLayoutParams().width = barsize.width;

            }
        });
        Log.d("HPBAR", "Updated hpbar - " + hpbar.getWidth() + " " + prg.getWidth() + " " +
                (int) (((hpmax - hp) / hpmax) * prg.getWidth()) + " " + hp + " " + hpm);
        hpm -= hp;
    }

    void resize() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hpbar.getLayoutParams().width = prg.getLayoutParams().width;
                Log.d("HPBAR", "Resize --- " + hpbar.getWidth() + " " + prg.getWidth());
            }
        });
    }

    public void gogame() {

        dbHelper = new DBHelper(context);

        dbHelper.updateDataBase();
        try {
            db = dbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        readFile();
        set_all();
        setView_enemy();
        activity.runOnUiThread(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                        if (cnt == answer.length) return;
                        String item = (String) parent.getAdapter().getItem(position);
                        if (item.equals(answer[cnt])) {
                            Log.d("GG", answer[cnt] + " " + item);
                            tv2.setTextColor(Color.GREEN);
                            tv2.setText(tv2.getText() + " " + item);
                            cnt++;
                            hp--;
                            update_enemy();

                        } else {
                            tv2.setTextColor(Color.RED);
                            hp = hp + 1 > hpmax ? hpmax : hp + 1;
                            update_enemy();
                        }
                    }
                });
                lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                        if (cnt == answer.length) return;
                        String item = (String) parent.getAdapter().getItem(position);
                        if (item.equals(answer[cnt])) {
                            Log.d("GG", answer[cnt] + " " + item);
                            tv2.setTextColor(Color.GREEN);
                            tv2.setText(tv2.getText() + " " + item);
                            cnt++;
                            hp--;
                            Log.d("CNT", "cnt for now is --- " + cnt);
                            update_enemy();
                        } else {
                            tv2.setTextColor(Color.RED);
                            hp = hp + 1 > hpmax ? hpmax : hp + 1;
                            update_enemy();
                        }
                    }
                });
                advise.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cnt == answer.length || hp<2) return;
                        if (adv_count > 0) {
                            tv2.setTextColor(Color.YELLOW);
                            tv2.setText(tv2.getText() + " " + answer[cnt]);
                            adv_count--;
                            cnt++;
                            hp--;
                            Log.d("CNT", "cnt for now is --- " + cnt);
                            update_enemy();
                            tvadv.setText("Подсказок: " + adv_count );
                            //final Toast toast = Toast.makeText(context, "Осталось " + adv_count + " подсказки", Toast.LENGTH_LONG);
                            //toast.show();
                        }
                    }
                });
            }
            /*public void update_enemy(){
                barsize.width = (int) Math.round(hp/hpmax*prg.getWidth());
                Log.d("HPBAR", "bar width --- "+ barsize.width+" " + (int) Math.round(hp/hpmax*prg.getWidth()));
                activity.runOnUiThread(new Runnable() {
                    @SuppressLint("NewApi")
                    @Override
                    public void run() {
                        barsize.width = (int) Math.round(hp/hpmax*prg.getWidth());
                        prg.setText((int)Math.round(hp)+"/"+(int)Math.round(hpmax));
                        hpbar.getLayoutParams().width = barsize.width;

                    }
                });
                Log.d("HPBAR", "Updated hpbar - "+ hpbar.getWidth()+ " "+prg.getWidth()+ " "+
                        (int) (((hpmax-hp)/hpmax)*prg.getWidth())+ " "+hp+" "+ hpm);
                hpm-=hp;
            }*/
        });
        do {

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (cnt == answer.length && Math.round(hp)>0) {
                if (adv_check <= adv_count) {
                    extra++;
                    extra %= 5;
                } else extra = 0;
                Log.d("CNT", "cnt for now is --- " + cnt);
                set_all();
                Log.d("CNT", "cnt for now is --- " + cnt);
            }else{
                if (adv_check <= adv_count) {
                    extra++;
                    extra %= 5;
                } else extra = 0;
            }
        } while (Math.round(hp) > 0);

        resize();
        writeFile();
    }

}
