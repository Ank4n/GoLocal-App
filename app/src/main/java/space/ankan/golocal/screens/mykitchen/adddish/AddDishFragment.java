package space.ankan.golocal.screens.mykitchen.addDish;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import space.ankan.golocal.R;
import space.ankan.golocal.core.BaseFragment;
import space.ankan.golocal.model.kitchens.Dish;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddDishFragment extends BaseFragment {

    private View mRootView;

    @BindView(R.id.dish_name)
    EditText dishName;
    @BindView(R.id.dish_price)
    EditText dishPrice;
    @BindView(R.id.is_nonveg)
    CheckBox isNonveg;
    @BindView(R.id.add_dish_action)
    Button addDishAction;

    public AddDishFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_add_dish, container, false);
        ButterKnife.bind(this, mRootView);
        addDishAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getUser() == null) {
                    Toast.makeText(getActivity(), R.string.error_occurred, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(dishName.getText()) || TextUtils.isEmpty(dishPrice.getText())) {
                    Toast.makeText(getActivity(), R.string.error_fields_not_filled, Toast.LENGTH_SHORT).show();
                    return;
                }
                Dish dish = new Dish(dishName.getText().toString(), null, null, Integer.valueOf(dishPrice.getText().toString()), isNonveg.isChecked());
                getFirebaseHelper().push(dish, getUser().kitchen);
                getActivity().finish();
            }
        });
        return mRootView;
    }
}
