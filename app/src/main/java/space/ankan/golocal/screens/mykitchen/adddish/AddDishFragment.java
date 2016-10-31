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
import space.ankan.golocal.utils.CommonUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddDishFragment extends BaseFragment {

    private View mRootView;
    private Dish dish;

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

    public static AddDishFragment newInstance(Dish dish) {

        Bundle args = new Bundle();
        args.putSerializable("dish", dish);
        AddDishFragment fragment = new AddDishFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = super.inflate(inflater, container, savedInstanceState, R.layout.fragment_add_dish);
        dish = (Dish) getArguments().getSerializable("dish");
        ButterKnife.bind(this, mRootView);
        fill();
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

                getFirebaseHelper().push(getDish(), getUser().kitchen);
                Toast.makeText(getActivity(), R.string.dish_saved, Toast.LENGTH_SHORT).show();

                if (!isTwoPane())
                    getActivity().finish();
                else
                    CommonUtils.closeKeyBoard(getActivity());


            }
        });
        return mRootView;
    }

    private Dish getDish() {
        String dishKey = null;
        if (dish != null)
            dishKey = dish.key;
        dish = new Dish(dishKey, dishName.getText().toString(), null, null, Integer.valueOf(dishPrice.getText().toString()), isNonveg.isChecked());
        return dish;
    }

    private void fill() {
        if (dish == null) return;
        dishName.setText(dish.name);
        dishPrice.setText(String.valueOf(dish.price));
        isNonveg.setChecked(dish.nonVeg);
    }
}
