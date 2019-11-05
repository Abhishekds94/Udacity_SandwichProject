
package com.udacity.sandwichclub;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private TextView tvAlsoKnown;
    private TextView tvAlsoKnownLabel;
    private TextView tvOrigin;
    private TextView tvOriginLabel;
    private TextView tvDescription;
    private TextView tvIngredient;
    private ImageView ivSandwich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivSandwich = findViewById(R.id.image_iv);
        tvAlsoKnown = findViewById(R.id.also_known_tv);
        tvAlsoKnownLabel = findViewById(R.id.alsoKnownAs_label);
        tvOrigin = findViewById(R.id.origin_tv);
        tvOriginLabel = findViewById(R.id.placeOfOrigin_label);
        tvDescription = findViewById(R.id.description_tv);
        tvIngredient = findViewById(R.id.ingredients_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            closeOnError();
            return;
        }

        populateUI(sandwich);

        setTitle(sandwich.getMainName());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {


        //setText to alsoKnown
        if (sandwich.getAlsoKnownAs() != null && sandwich.getAlsoKnownAs().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(sandwich.getAlsoKnownAs().get(0));

            for (int i = 1; i < sandwich.getAlsoKnownAs().size(); i++) {
                stringBuilder.append(", ");
                stringBuilder.append("\n");
                stringBuilder.append(sandwich.getAlsoKnownAs().get(i));
            }
            tvAlsoKnown.setText(stringBuilder.toString());
        } else {
            tvAlsoKnown.setVisibility(View.GONE);
            tvAlsoKnownLabel.setVisibility(View.GONE);
        }

        //setText to origin
        if (sandwich.getPlaceOfOrigin().isEmpty()) {
            tvOrigin.setVisibility(View.GONE);
            tvOriginLabel.setVisibility(View.GONE);
        } else {
            tvOrigin.setText(sandwich.getPlaceOfOrigin());
        }

        //setText to description
        tvDescription.setText(sandwich.getDescription());

        //setText to ingredient
        if (sandwich.getIngredients() != null && sandwich.getIngredients().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\u2022");
            stringBuilder.append(sandwich.getIngredients().get(0));

            for (int i = 1; i < sandwich.getIngredients().size(); i++) {
                stringBuilder.append("\n");
                stringBuilder.append("\u2022");
                stringBuilder.append(sandwich.getIngredients().get(i));
            }
            tvIngredient.setText(stringBuilder.toString());
        }

        //set the header image
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ivSandwich);
    }
}