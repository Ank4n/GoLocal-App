package space.ankan.golocal.screens.mykitchen.addDish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import space.ankan.golocal.R;
import space.ankan.golocal.core.LoggedInActivity;
import space.ankan.golocal.model.kitchens.Dish;

public class AddDishActivity extends LoggedInActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, AddDishFragment.newInstance((Dish) getIntent().getSerializableExtra("dish")), "frag")
                .commit();


    }

    public static void createIntent(Context context) {
        context.startActivity(new Intent(context, AddDishActivity.class));
    }

    public static void createIntent(Context context, Dish dish) {
        Intent intent = new Intent(context, AddDishActivity.class);
        intent.putExtra("dish", dish);
        context.startActivity(intent);
    }

}
