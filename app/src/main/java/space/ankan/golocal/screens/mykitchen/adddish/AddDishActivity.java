package space.ankan.golocal.screens.mykitchen.addDish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import space.ankan.golocal.R;
import space.ankan.golocal.core.LoggedInActivity;

public class AddDishActivity extends LoggedInActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });

    }

    public static void createIntent(Context context) {
        context.startActivity(new Intent(context, AddDishActivity.class));
    }

}
