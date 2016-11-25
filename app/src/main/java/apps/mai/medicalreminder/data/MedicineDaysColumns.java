package apps.mai.medicalreminder.data;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by Mai_ on 05-Nov-16.
 */

public interface MedicineDaysColumns {
    @DataType(INTEGER)
    public static final String MED_ID = "med_id";

    @DataType(TEXT) @NotNull
    public static final String DAYS = "days";

}
