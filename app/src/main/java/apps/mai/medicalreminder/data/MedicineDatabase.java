package apps.mai.medicalreminder.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Mai_ on 03-Nov-16.
 */
@Database(version = MedicineDatabase.VERSION)
public class MedicineDatabase {
    public static final int VERSION = 11;

    @Table(MedicineColumns.class) public static final String MEDICINES = "medicines";
    @Table(MedicineDaysColumns.class) public static final String MEDICINES_DAYS = "medicines_days";

}
