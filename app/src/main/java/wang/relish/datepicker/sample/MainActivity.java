package wang.relish.datepicker.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * @author Relish Wang
 * @since 2017/12/21
 */
public class MainActivity extends AppCompatActivity {

    LinearLayout llRoot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        llRoot = new LinearLayout(this);
        llRoot.setOrientation(LinearLayout.VERTICAL);
        addViews();
        setContentView(llRoot);
    }

    private void addViews() {
        addView("时间范围选择", DateRangePickerActivity.class);
        addView("日历", CalendarActivity.class);
    }


    private void addView(String text, final Class cla) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, cla));
            }
        });
        llRoot.addView(button);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        button.setLayoutParams(layoutParams);
    }
}
