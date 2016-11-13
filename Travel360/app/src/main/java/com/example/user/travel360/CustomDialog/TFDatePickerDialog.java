package com.example.user.travel360.CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.user.travel360.R;

import java.util.Calendar;

public class TFDatePickerDialog extends Dialog {
    DatePicker ToDate, FromDate;
    String ToDateS="empty";
    String FromDateS="empty";
    Button DateSetBtn;
   // Calendar calendar;

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
/*
        Calendar calendar = Calendar.getInstance();
        Toast.makeText(getApplicationContext(),
                calendar.get(Calendar.YEAR)+"년 "+calendar.get(Calendar.MONTH)+"월 "+calendar.get(Calendar.DAY_OF_MONTH)+"일",
                Toast.LENGTH_LONG ).show();
*/
    //    calendar = Calendar.getInstance();
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
                        ToDateS = year + "년 " + month + "월 " + day + "일";
                    }
                });

    }
}
