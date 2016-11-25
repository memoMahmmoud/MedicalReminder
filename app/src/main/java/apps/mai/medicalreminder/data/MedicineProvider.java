package apps.mai.medicalreminder.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Mai_ on 03-Nov-16.
 */
@ContentProvider(authority = MedicineProvider.AUTHORITY, database = MedicineDatabase.class)
public final class MedicineProvider {
    public static final String AUTHORITY =
            "apps.mai.medicalreminder.data.MedicineProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path{
        String Medicines = "medicines";
        String MedicinesDays = "medicines_days";

    }

    private static Uri buildUri(String ... paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths){
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = MedicineDatabase.MEDICINES)
    public static class Medicines{
        @ContentUri(
                path = Path.Medicines,
                type = "vnd.android.cursor.dir/medicine",
                defaultSort = MedicineColumns.MEDICINE_NAME + " ASC")
        public static final Uri MEDICINES = buildUri(Path.Medicines);

        @InexactContentUri(
                name = "MEDICINE_NAME",
                path = Path.Medicines + "/*",
                type = "vnd.android.cursor.item/medicine",
                whereColumn = MedicineColumns.MEDICINE_NAME,
                pathSegment = 1)
        public static Uri withId(String id){
            return buildUri(Path.Medicines, id);
        }

    }
    @TableEndpoint(table = MedicineDatabase.MEDICINES_DAYS)
    public static class MedicinesDays{
        @ContentUri(
                path = Path.MedicinesDays,
                type = "vnd.android.cursor.dir/medicine_day",
                defaultSort = MedicineDaysColumns.MED_ID + " ASC")
        public static final Uri MEDICINES_DAYS = buildUri(Path.MedicinesDays);

        @InexactContentUri(
                name = "MEDICINE_ID",
                path = Path.MedicinesDays + "/#",
                type = "vnd.android.cursor.item/medicine_day",
                whereColumn = MedicineDaysColumns.MED_ID,
                pathSegment = 1)
        public static Uri withId(int id){
            return buildUri(Path.MedicinesDays, String.valueOf(id));
        }


    }
}
