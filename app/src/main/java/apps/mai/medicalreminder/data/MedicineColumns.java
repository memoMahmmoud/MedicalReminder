package apps.mai.medicalreminder.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by Mai_ on 31-Oct-16.
 */

public interface MedicineColumns {
    @DataType(INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(TEXT) @NotNull
    public static final String MEDICINE_NAME = "medicine_name";

    @DataType(INTEGER) @NotNull
    public static final String FREQUENCY = "frequency";

    @DataType(REAL)
    public static final String NO_DOSAGE = "no_dosage";

    @DataType(INTEGER) @NotNull
    public static final String START_TIME_HOUR = "hour";

    @DataType(INTEGER) @NotNull
    public static final String START_TIME_MINUTE = "minute";

    @DataType(INTEGER) @NotNull
    public static final String START_YEAR = "start_year";

    @DataType(INTEGER) @NotNull
    public static final String START_MONTH = "start_month";

    @DataType(INTEGER) @NotNull
    public static final String START_DAY = "start_day";

    @DataType(INTEGER)
    public static final String DURATION = "duration";

    @DataType(INTEGER)
    public static final String REMAIN_DAYS = "remain_days";

    @DataType(INTEGER)
    public static final String DAYS = "days";



}
