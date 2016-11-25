package apps.mai.medicalreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.Calendar;

import apps.mai.medicalreminder.alarm_medicine.AlarmReceiver;
import apps.mai.medicalreminder.data.MedicineColumns;
import apps.mai.medicalreminder.data.MedicineDaysColumns;
import apps.mai.medicalreminder.data.MedicineProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddMedicine extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    @BindView(R.id.medicine_name)
    EditText medicine_name;

    @BindView(R.id.time_picker)
    TextView time;

    @BindView(R.id.dosage) TextView dosage;
    @BindView(R.id.pills) TextView pills;
    @BindView(R.id.no_days_edit) TextView no_days_edit;

    @BindView(R.id.start_date) TextView start_date;
    @BindView(R.id.groupDuration)
    RadioGroup groupDuration;
    @BindView(R.id.duration_continuous)
    RadioButton duration_continuous;

    @BindView(R.id.groupDays)
    RadioGroup groupDays;

    Calendar calendar ;
    DatePickerDialog datePickerDialog ;
    TimePickerDialog timePickerDialog ;
    int Year, Month, Day ;
    int hour,minute;
    int CalendarHour, CalendarMinute;
    float doses;
    int durationdays;
    int Days;
    Integer[] selectedDaysInPicker = new Integer[]{};
    StringBuilder str;
    @BindView(R.id.certain_days_week_edit) TextView certain_days_week_edit;
    @BindView(R.id.days_every_day)
    RadioButton days_every_day;
    MaterialBetterSpinner reminderTimesSpinner;

    Intent AlarmIntent;
    AlarmManager alarmManager;
    private PendingIntent pending_intent;
    Intent medicineIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        ButterKnife.bind(this);

        AlarmIntent = new Intent(this,AlarmReceiver.class);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        medicineIntent = new Intent(this, AlarmReceiver.class);
        hour = Integer.parseInt(getString(R.string.initial_hour));
        minute = Integer.parseInt(getString(R.string.initial_minute));
        doses = Float.parseFloat(getString(R.string.initial_dosage));

        final ArrayAdapter<String> reminderTimesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,getResources().getStringArray
                (R.array.reminder_times_array)
                );
        reminderTimesSpinner = (MaterialBetterSpinner)
                findViewById(R.id.reminder_times_spinner);
        reminderTimesSpinner.setAdapter(reminderTimesAdapter);
        reminderTimesSpinner.setText(reminderTimesAdapter.getItem(1).toString());

        medicine_name.setFocusable(false);
        medicine_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                medicine_name.setFocusableInTouchMode(true);

                return false;
            }
        });
        medicine_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    medicine_name.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });

        dosage.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDosagePicker();
            }
        });
        pills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDosagePicker();
            }
        });
        calendar = Calendar.getInstance();

        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        start_date.setText(Utilities.formatDate(Year,Month,Day));
        //if user click on specific date, he will see date picker
        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = DatePickerDialog.newInstance(AddMedicine.this, Year, Month, Day);

                datePickerDialog.setThemeDark(false);

                datePickerDialog.showYearPickerFirst(false);

                datePickerDialog.setAccentColor(getResources().getColor(R.color.colorAccent));

                datePickerDialog.setTitle("Select Date From DatePickerDialog");

                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");

            }
        });

        //time.setText(getString(R.string.initial_time));
        time.setText(Utilities.formatTime(hour,minute,0));
        //if user click on specific start time, he will see time picker
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog = TimePickerDialog.newInstance(AddMedicine.this, CalendarHour, CalendarMinute,false);

                timePickerDialog.setThemeDark(false);
                timePickerDialog.show(getFragmentManager(), "Material Design TimePicker Dialog");
            }
        });
    }
    //if user click on Number of days, he will see
    @OnClick({R.id.duration_no_days,R.id.no_days_edit})
    public void duration_no_days(){
        showNumberDaysPicker();
    }
    
    @OnClick(R.id.duration_continuous)
    public void duration_continuous(){
        no_days_edit.setVisibility(View.GONE);
    }

    @OnClick(R.id.days_every_day)
    public void days_every_day(){

        Days = 0;
        certain_days_week_edit.setVisibility(View.GONE);
    }
    @OnClick(R.id.days_every_week)
    public void days_every_week(){
        Days = 1;
        certain_days_week_edit.setVisibility(View.GONE);
    }
    @OnClick(R.id.days_every_month)
    public void days_every_month(){
        Days = 2;
        certain_days_week_edit.setVisibility(View.GONE);
    }
    @OnClick({R.id.certain_days_week,R.id.certain_days_week_edit})
    public void certain_days_week(){
        Days = 3;
        showCertainDaysOfWeekPicker();
    }
    @OnClick(R.id.save_medicine)
    public void save_medicine(){
        //ensure medicine name not empty
        if (!medicine_name.getText().toString().isEmpty()){
            String name = medicine_name.getText().toString();
            int frequency = Utilities.getFrequency(reminderTimesSpinner.getText().toString());
            ContentValues contentValues = new ContentValues();
            contentValues.put(MedicineColumns.MEDICINE_NAME,name);
            contentValues.put(MedicineColumns.FREQUENCY,frequency);
            contentValues.put(MedicineColumns.START_TIME_HOUR,hour);
            contentValues.put(MedicineColumns.START_TIME_MINUTE,minute);
            contentValues.put(MedicineColumns.START_YEAR,Year);
            contentValues.put(MedicineColumns.START_MONTH,Month);
            contentValues.put(MedicineColumns.START_DAY,Day);
            contentValues.put(MedicineColumns.NO_DOSAGE,doses);

            switch (groupDuration.getCheckedRadioButtonId()){
                case R.id.duration_continuous:
                    contentValues.put(MedicineColumns.DURATION,0);
                    break;
                default:
                    contentValues.put(MedicineColumns.DURATION,1);
                    contentValues.put(MedicineColumns.REMAIN_DAYS,Integer.parseInt(no_days_edit.
                            getText().toString()));
            }
            contentValues.put(MedicineColumns.DAYS,Days);
            switch (groupDays.getCheckedRadioButtonId()){
                case R.id.days_every_day:
                    contentValues.put(MedicineColumns.DAYS,0);
                    break;
                case R.id.days_every_week:
                    contentValues.put(MedicineColumns.DAYS,1);
                    break;
                case R.id.days_every_month:
                    contentValues.put(MedicineColumns.DAYS,2);
                    break;
                default:
                    contentValues.put(MedicineColumns.DAYS,3);

            }
            Cursor cursor = getContentResolver().query(MedicineProvider.Medicines.withId(name),
                    null,null,null,null);
            if (cursor ==null || cursor.getCount() ==0)
                getContentResolver().insert(MedicineProvider.Medicines.withId(name),contentValues);
            else
                getContentResolver().update(MedicineProvider.Medicines.withId(name),
                    contentValues,null,null);
            cursor = getContentResolver().query(MedicineProvider.Medicines.withId(name),
                    null,null,null,null);
            cursor.moveToFirst();
            int id = cursor.getInt(cursor.getColumnIndex(MedicineColumns._ID));

            if (Days ==3){
                    int i=0;
                    do {
                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put(MedicineDaysColumns.MED_ID,id);
                        contentValues1.put(MedicineDaysColumns.DAYS,selectedDaysInPicker[i]);
                        getContentResolver().insert(MedicineProvider.MedicinesDays.withId(id),
                                contentValues1);
                    }while (cursor.moveToNext());


            }
            AlarmIntent.putExtra("med_id",id);
            pending_intent = PendingIntent.getBroadcast(this,id,AlarmIntent,PendingIntent.
                    FLAG_UPDATE_CURRENT);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            switch (Days){
                //every day
                case 0:
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_HOUR*frequency,pending_intent);
                    break;
                //every week
                case 1:
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY*7,pending_intent);
                    break;
                //every month
                case 2:
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY*30,pending_intent);
                    break;
            }

            Intent intent = new Intent(this,AllMedicines.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this,R.string.medicine_empty,Toast.LENGTH_SHORT).show();
        }
    }
    //cancel pending intent
    //cancel ringtone through sendBroadcast(myIntent);



    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Year = year;
        Month = monthOfYear;
        Day = dayOfMonth;
        start_date.setText(Utilities.formatDate(year,monthOfYear,dayOfMonth));
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        this.hour = hourOfDay;
        this.minute = minute;
        time.setText(Utilities.formatTime(hourOfDay,minute,view.getIsCurrentlyAmOrPm()));
    }
    public void showDosagePicker(){
        MaterialDialog doses_picker_dialog = new MaterialDialog.Builder(AddMedicine.this).
                customView(R.layout.doses_picker,true).
                positiveText(R.string.set).negativeText(R.string.cancel).onAny(
                new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        if (which.name().equals("POSITIVE")){
                            dosage.setText(String.valueOf(doses));
                        }
                        else {

                        }
                    }
                }).build();
        Button decrease_btn = (Button) doses_picker_dialog.getCustomView().
                findViewById(R.id.decrease);
        Button increase_btn = (Button) doses_picker_dialog.getCustomView().
                findViewById(R.id.increase);
        final EditText doses_edit = (EditText) doses_picker_dialog.getCustomView().
                findViewById(R.id.doses_edit_text);
        doses_edit.setText(dosage.getText().toString());
        doses = Float.parseFloat(doses_edit.getText().toString());
        decrease_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( doses != .5){
                    doses -= 0.50;
                    doses_edit.setText(String.valueOf(doses));
                }
            }
        });
        increase_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( doses < 5.00){
                    doses += 0.50;
                    doses_edit.setText(String.valueOf(doses));
                }
            }
        });

        doses_picker_dialog.show();
    }
    public void showNumberDaysPicker(){
        MaterialDialog days_picker_dialog = new MaterialDialog.Builder(AddMedicine.this).
                customView(R.layout.num_days_picker,true).
                positiveText(R.string.set).negativeText(R.string.cancel).onAny(
                new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        if (which.name().equals("POSITIVE")){
                            no_days_edit.setVisibility(View.VISIBLE);
                            no_days_edit.setText(String.valueOf(durationdays));
                        }
                        else {
                            if (no_days_edit.getVisibility() == View.GONE){
                                duration_continuous.setChecked(true);
                            }
                        }

                    }
                }).build();
        Button decrease_btn = (Button) days_picker_dialog.getCustomView().
                findViewById(R.id.decrease);
        Button increase_btn = (Button) days_picker_dialog.getCustomView().
                findViewById(R.id.increase);
        final EditText days_edit = (EditText) days_picker_dialog.getCustomView().
                findViewById(R.id.days_edit_text);
        days_edit.setText(no_days_edit.getText().toString());
        durationdays = Integer.parseInt(days_edit.getText().toString());
        decrease_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( durationdays != 1){
                    durationdays -= 1;
                    days_edit.setText(String.valueOf(durationdays));
                }
            }
        });
        increase_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    durationdays += 1;
                    days_edit.setText(String.valueOf(durationdays));

            }
        });

        days_picker_dialog.show();
    }
    public void showCertainDaysOfWeekPicker(){
        new MaterialDialog.Builder(this)
                .items(R.array.days_week)
                .itemsCallbackMultiChoice(selectedDaysInPicker
                        , new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        return true; // allow selection
                    }
                })
                .alwaysCallMultiChoiceCallback()
                .positiveText(R.string.set)
                .negativeText(R.string.cancel)
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (which.name().equals("POSITIVE")){
                            selectedDaysInPicker = dialog.getSelectedIndices();
                            if (selectedDaysInPicker.length >0){

                                str = new StringBuilder();
                                String[] days = getResources().getStringArray(R.array.days_week);
                                for (int i = 0; i < selectedDaysInPicker.length; i++) {
                                    str.append(days[selectedDaysInPicker[i]]);
                                    if (i != selectedDaysInPicker.length-1)
                                        str.append(", ");
                                }
                                certain_days_week_edit.setVisibility(View.VISIBLE);
                                certain_days_week_edit.setText(str.toString());

                            }
                            else{
                                selectedDaysInPicker = new Integer[]{};
                                certain_days_week_edit.setVisibility(View.GONE);
                                days_every_day.setChecked(true);
                            }
                        }
                        else {
                            if (certain_days_week_edit.getVisibility() == View.GONE){
                                days_every_day.setChecked(true);
                                dialog.clearSelectedIndices();
                            }
                        }
                    }
                })
                .show();
    }
}
