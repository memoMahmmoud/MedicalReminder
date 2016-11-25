package apps.mai.medicalreminder;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import apps.mai.medicalreminder.data.MedicineProvider;
import apps.mai.medicalreminder.rest.MedicineCursorAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllMedicines extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{
    @BindView(R.id.listView_medicines)
    RecyclerView recyclerView;
    private MedicineCursorAdapter medicineCursorAdapter;
    private static final int CURSOR_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_medicines);
        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        medicineCursorAdapter = new MedicineCursorAdapter(this, null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(medicineCursorAdapter);
        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null,this);

    }
    @OnClick(R.id.fab_add)
    public void fab_add(){
        Intent intent = new Intent(this,AddMedicine.class);
        startActivity(intent);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, MedicineProvider.Medicines.MEDICINES,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        medicineCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        medicineCursorAdapter.swapCursor(null);
    }
}
