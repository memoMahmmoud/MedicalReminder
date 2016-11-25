package apps.mai.medicalreminder;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import apps.mai.medicalreminder.data.MedicineProvider;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.fab_add)
    FloatingActionButton floatingActionButtonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent !=null){
            int flag = intent.getIntExtra("flag",-1);
            switch (flag){
                case 0:
                    Toast.makeText(this,"Medicine added successfully",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(this,"Medicine updated successfully",Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(this,"def",Toast.LENGTH_LONG).show();
                    break;
            }
        }
        Cursor cursor = getContentResolver().query(MedicineProvider.Medicines.withId("d")
                ,null,null,null,null);
        //Cursor cursor= getContentResolver().query(MedicineProvider.Medicines.withId("d"),null,null,null,null);
        //cursor.moveToFirst();
        //Toast.makeText(this,""+cursor.getString(1),Toast.LENGTH_LONG).show();
        //Toast.makeText(this,""+cursor.getColumnCount(),Toast.LENGTH_LONG).show();

        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddMedicine.class);
                startActivity(intent);
            }
        });

    }
}
