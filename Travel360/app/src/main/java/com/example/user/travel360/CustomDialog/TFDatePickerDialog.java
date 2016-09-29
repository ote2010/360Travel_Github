package com.example.user.travel360.CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.user.travel360.R;

public class TFDatePickerDialog extends Dialog {
    DatePicker ToDate, FromDate;
    String ToDateS, FromDateS;
    Button DateSetBtn;

    public TFDatePickerDialog(Context context) {
        super(context);

    }

    public String getFromDate() {
        return FromDateS;
    }

    public String getToDate() {
        return ToDateS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_date_picker_dialog);
        FromDate = (DatePicker) findViewById(R.id.FromDate);
        ToDate = (DatePicker) findViewById(R.id.ToDate);
        DateSetBtn = (Button) findViewById(R.id.DateSetBtn);
        DateSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), FromDateS + "\n" + ToDateS, Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        initToDate();
        initFromDate();

    }

    public void initToDate() {
        ToDate.init(ToDate.getYear(), ToDate.getMonth(), ToDate.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String month, day;
                        if(monthOfYear+1 < 10)
                        {
                            month = "0" + (monthOfYear+1);
                        }
                        else
                            month = "" + (monthOfYear+1);
                        if(dayOfMonth < 10)
                        {
                            day = "0" + dayOfMonth;
                        }
                        else
                            day = "" + dayOfMonth;
                        ToDateS = year + "년 " + month + "월 " + day + "일";
                    }
                });

    }

    public void initFromDate() {
        FromDate.init(FromDate.getYear(), FromDate.getMonth(), FromDate.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String month, day;
                        if(monthOfYear+1 < 10)
                        {
                            month = "0" + (monthOfYear+1);
                        }
                        else
                            month = "" + (monthOfYear+1);
                        if(dayOfMonth < 10)
                        {
                            day = "0" + dayOfMonth;
                        }
                        else
                            day = "" + dayOfMonth;
                        FromDateS = year + "년 " + month + "월 " + day + "일";
                    }
                });

    }
}
