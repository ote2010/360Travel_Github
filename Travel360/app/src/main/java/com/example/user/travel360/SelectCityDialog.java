package com.example.user.travel360;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class SelectCityDialog extends Dialog {
    String City;
    Button CitySelectBtn;
    Spinner CitySelect;
    ArrayAdapter<CharSequence> city;

    public String getCity() {
        return City;
    }

    public SelectCityDialog(Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_citi_dialog);
        CitySelect = (Spinner) findViewById(R.id.CitySelect);
        CitySelectBtn = (Button) findViewById(R.id.CitySelectBtn);
        CitySelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
city = ArrayAdapter.createFromResource(getContext(), R.array.city, android.R.layout.simple_spinner_dropdown_item);
CitySelect.setAdapter(city);
        CitySelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        City = city.getItem(position) +"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
