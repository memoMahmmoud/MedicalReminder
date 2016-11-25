package apps.mai.medicalreminder.rest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import apps.mai.medicalreminder.EditMedicine;
import apps.mai.medicalreminder.R;
import apps.mai.medicalreminder.Utilities;
import apps.mai.medicalreminder.alarm_medicine.AlarmReceiver;
import apps.mai.medicalreminder.data.MedicineColumns;
import apps.mai.medicalreminder.data.MedicineDaysColumns;
import apps.mai.medicalreminder.data.MedicineProvider;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Mai_ on 08-Nov-16.
 */

public class MedicineCursorAdapter extends CursorRecyclerViewAdapter<MedicineCursorAdapter.ViewHolder> {
    Context mContext;
    ViewHolder mVh;
    public MedicineCursorAdapter(Context context, Cursor cursor){
        super(context,cursor);
        mContext = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        @BindView(R.id.medicine_name)
        TextView medicine_name;
        @BindView(R.id.no_dosage)
        TextView no_dosage;
        @BindView(R.id.frequency)
        TextView frequency;
        @BindView(R.id.duration)
        TextView duration;
        @BindView(R.id.btn_delete)
        Button btn_delete;
        @BindView(R.id.btn_edit)
        Button btn_edit;

        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            ButterKnife.bind(this,view);

        }


        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicine_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        mVh = vh;
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, Cursor cursor){
        DatabaseUtils.dumpCursor(cursor);
        final int id = cursor.getInt(cursor.getColumnIndex(MedicineColumns._ID));
        final String name = cursor.getString(cursor.getColumnIndex(MedicineColumns.MEDICINE_NAME));
        final int frequency = cursor.getInt(cursor.getColumnIndex(MedicineColumns.FREQUENCY));
        final float doses = cursor.getFloat(cursor.getColumnIndex(MedicineColumns.NO_DOSAGE));
        final int duration = cursor.getInt(cursor.getColumnIndex(MedicineColumns.DURATION));
        final int Days = cursor.getInt(cursor.getColumnIndex(MedicineColumns.DAYS));

        viewHolder.medicine_name.setText(name);
        viewHolder.no_dosage.setText(mContext.getString(R.string.doses,doses));
        switch (Days){
            //daily
            case 0:
                viewHolder.frequency.setText(mContext.getString(R.string.frequency_medicine_list,
                        frequency));
                break;
            case 1:
                viewHolder.frequency.setText("weekly");
                break;
            case 2:
                viewHolder.frequency.setText("monthly");
                break;
            case 3:
                cursor = mContext.getContentResolver().query(MedicineProvider.MedicinesDays.
                                withId(id), null,
                        MedicineDaysColumns.MED_ID+"=?",new String[]
                                {String.valueOf(id)},null);
                Integer[] selectedDaysInPicker = new Integer[cursor.getCount()];

                if (cursor.moveToFirst()){
                    int i=0;
                    do {
                        selectedDaysInPicker[i] = cursor.getInt(cursor.getColumnIndex
                                (MedicineDaysColumns.DAYS));
                        i++;
                    }while (cursor.moveToNext());
                    viewHolder.frequency.setText(Utilities.showCertainDaysWeekString(
                            selectedDaysInPicker,mContext));
                }
                break;

        }
        switch (duration){
            case 0:
                viewHolder.duration.setText(mContext.getString(R.string.continuous));
                break;
            default:
                int no_days = cursor.getInt(cursor.getColumnIndex(MedicineColumns.REMAIN_DAYS));
                viewHolder.duration.setText(mContext.getString(R.string.no_days_medicine_list,no_days));
        }

        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new MaterialDialog.Builder(mContext)
                            .content(R.string.confirm_delete_dialog)
                            .positiveText(R.string.confirm_dialog_ok)
                            .negativeText(R.string.confirm_dialog_cancel)
                            .onAny(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    if(which.name().equals("POSITIVE")){
                                        mContext.getContentResolver().delete(MedicineProvider.
                                                Medicines.withId(name),null,null);
                                        mContext.getContentResolver().delete(MedicineProvider.
                                                MedicinesDays.withId(id),null,null);
                                        Intent AlarmIntent = new Intent(mContext,AlarmReceiver.class);
                                        AlarmManager alarmManager = (AlarmManager) mContext.
                                                getSystemService(ALARM_SERVICE);
                                        PendingIntent pending_intent = PendingIntent.
                                                getBroadcast(mContext,id,AlarmIntent,PendingIntent.
                                                FLAG_UPDATE_CURRENT);
                                        alarmManager.cancel(pending_intent);
                                    }
                                }
                            })
                            .show();
                }
            });
        viewHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditMedicine.class);
                intent.setData(MedicineProvider.Medicines.withId(name));
                mContext.startActivity(intent);
            }
        });





    }
}
