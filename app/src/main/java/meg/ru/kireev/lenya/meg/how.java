package meg.ru.kireev.lenya.meg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class how extends Activity    {
    Button a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_to1);

        a = findViewById(R.id.back);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
