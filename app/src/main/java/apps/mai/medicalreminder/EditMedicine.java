package apps.mai.medicalreminder;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

import apps.mai.medicalreminder.data.MedicineColumns;
import apps.mai.medicalreminder.data.MedicineDaysColumns;
import apps.mai.medicalreminder.data.MedicineProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditMedicine extends AppCompatActivity implements
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
    @BindView(R.id.duration_no_days)
    RadioButton duration_no_days;

    @BindView(R.id.save_medicine)
    Button save_medicine;

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
    @BindView(R.id.certain_days_week_edit) TextView certain_days_week_edit;
    @BindView(R.id.days_every_day)
    RadioButton days_every_day;
    @BindView(R.id.days_every_week)
    RadioButton days_every_week;
    @BindView(R.id.days_every_month)
    RadioButton days_every_month;
    @BindView(R.id.certain_days_week)
    RadioButton certain_days_week;
    MaterialBetterSpinner reminderTimesSpinner;
    int medicine_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        ButterKnife.bind(this);

        save_medicine.setText(R.string.edit_medicine);
        final ArrayAdapter<String> reminderTimesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,getResources().getStringArray
                (R.array.reminder_times_array)
        );
        reminderTimesSpinner = (MaterialBetterSpinner)
                findViewById(R.id.reminder_times_spinner);
        reminderTimesSpinner.setAdapter(reminderTimesAdapter);

        Intent intentFromAllMedicines = getIntent();
        Uri medicineURI = intentFromAllMedicines.getData();
        if (medicineURI != null){
            Cursor cursor_medicine = getContentResolver().query(medicineURI,null,null,null,null);
            cursor_medicine.moveToFirst();
            medicine_id = cursor_medicine.getInt(cursor_medicine.getColumnIndex(MedicineColumns.
                    _ID));
            String name = cursor_medicine.getString(cursor_medicine.getColumnIndex(MedicineColumns.
                    MEDICINE_NAME));
            int frequency = cursor_medicine.getInt(cursor_medicine.getColumnIndex(MedicineColumns.
                    FREQUENCY));
            hour = cursor_medicine.getInt(cursor_medicine.getColumnIndex(MedicineColumns.
                    START_TIME_HOUR));
            minute = cursor_medicine.getInt(cursor_medicine.getColumnIndex(MedicineColumns.
                    START_TIME_MINUTE));
            doses = cursor_medicine.getFloat(cursor_medicine.getColumnIndex(MedicineColumns.
                    NO_DOSAGE));
            Year = cursor_medicine.getInt(cursor_medicine.getColumnIndex(MedicineColumns.
                    START_YEAR));
            Month = cursor_medicine.getInt(cursor_medicine.getColumnIndex(MedicineColumns.
                    START_MONTH));
            Day = cursor_medicine.getInt(cursor_medicine.getColumnIndex(MedicineColumns.
                    START_DAY));
            int duration = cursor_medicine.getInt(cursor_medicine.getColumnIndex(MedicineColumns.
                    DURATION));
            switch (duration){
                case 0:
                    duration_continuous.setChecked(true);
                    break;
                case 1:
                    duration_no_days.setChecked(true);
                    no_days_edit.setVisibility(View.VISIBLE);
                    no_days_edit.setText(String.valueOf(cursor_medicine.getInt(cursor_medicine.
                            getColumnIndex(MedicineColumns.
                            REMAIN_DAYS))));
                    break;

            }
            Days = cursor_medicine.getInt(cursor_medicine.getColumnIndex(MedicineColumns.
                            DAYS));
            switch (Days){
                case 0:
                    days_every_day.setChecked(true);
                    break;
                case 1:
                    days_every_week.setChecked(true);
                    break;
                case 2:
                    days_every_month.setChecked(true);
                    break;
                case 3:
                    certain_days_week.setChecked(true);
                    certain_days_week_edit.setVisibility(View.VISIBLE);
                    Cursor cursor = getContentResolver().query(MedicineProvider.MedicinesDays.
                            withId(medicine_id), null,
                            MedicineDaysColumns.MED_ID+"=?",new String[]
                                    {String.valueOf(medicine_id)},null);
                    selectedDaysInPicker = new Integer[cursor.getCount()];

                    if (cursor.moveToFirst()){
                        int i=0;
                        do {
                            selectedDaysInPicker[i] = cursor.getInt(cursor.getColumnIndex
                                    (MedicineDaysColumns.DAYS));
                            i++;
                        }while (cursor.moveToNext());
                        this.certain_days_week_edit.setText(Utilities.showCertainDaysWeekString(
                                selectedDaysInPicker,EditMedicine.this));
                    }



            }
            medicine_name.setText(name);
            reminderTimesSpinner.setText(reminderTimesAdapter.getItem(Utilities.setFrequency
                    (frequency)).toString());
            time.setText(Utilities.formatTime(hour,minute));
            dosage.setText(String.valueOf(doses));
            start_date.setText(Utilities.formatDate(Year,Month,Day));
            cursor_medicine.close();
        }

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

        //if user click on specific date, he will see date picker
        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = DatePickerDialog.newInstance(EditMedicine.this, Year, Month, Day);

                datePickerDialog.setThemeDark(false);

                datePickerDialog.showYearPickerFirst(false);

                datePickerDialog.setAccentColor(getResources().getColor(R.color.colorAccent));

                datePickerDialog.setTitle("Select Date From DatePickerDialog");

                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");

            }
        });
        //if user click on specific start time, he will see time picker
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog = TimePickerDialog.newInstance(EditMedicine.this, CalendarHour, CalendarMinute,false);

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
            getContentResolver().update(MedicineProvider.Medicines.MEDICINES,contentValues,
                    MedicineColumns._ID+"=?", new String[]{String.valueOf(medicine_id)});
            if (Days ==3){
                getContentResolver().delete(MedicineProvider.MedicinesDays.withId(medicine_id),
                        null,null);
                for (int i=0;i<selectedDaysInPicker.length;i++){
                    Toast.makeText(getBaseContext(),""+selectedDaysInPicker[i],Toast.LENGTH_SHORT).show();
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put(MedicineDaysColumns.MED_ID,medicine_id);
                    contentValues1.put(MedicineDaysColumns.DAYS,selectedDaysInPicker[i]);
                    getContentResolver().insert(MedicineProvider.MedicinesDays.withId(medicine_id),
                            contentValues1);
                }
            }
            Intent intent = new Intent(this,AllMedicines.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this,R.string.medicine_empty,Toast.LENGTH_SHORT).show();
        }
    }



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
        MaterialDialog doses_picker_dialog = new MaterialDialog.Builder(EditMedicine.this).
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
        MaterialDialog days_picker_dialog = new MaterialDialog.Builder(EditMedicine.this).
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

                                certain_days_week_edit.setVisibility(View.VISIBLE);
                                certain_days_week_edit.setText(Utilities.showCertainDaysWeekString
                                        (selectedDaysInPicker,getBaseContext()));

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

