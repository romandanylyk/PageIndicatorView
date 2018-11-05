package com.rd.pageindicatorview.base;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import com.rd.pageindicatorview.sample.R;

public class BaseActivity extends AppCompatActivity {

    private ActionBar toolbar;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initToolbar() {
        if (getSupportActionBar() == null) {
            setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        }

        toolbar = getSupportActionBar();
        if (toolbar != null) {
            toolbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
        }
    }

    public void displayBackButton(boolean display) {
        if (toolbar != null) {
            toolbar.setDisplayHomeAsUpEnabled(display);
        }
    }
}