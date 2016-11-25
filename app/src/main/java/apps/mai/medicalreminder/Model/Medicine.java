package apps.mai.medicalreminder.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mai_ on 23-Nov-16.
 */

public class Medicine implements Parcelable {
    private int id;
    private String name;
    private int frequency;
    private float doses;
    private int duration;
    //De-parcel object
    protected Medicine(Parcel in) {
        id = in.readInt();
        name = in.readString();
        frequency = in.readInt();
        doses = in.readFloat();
        duration = in.readInt();
    }

    public static final Creator<Medicine> CREATOR = new Creator<Medicine>() {
        @Override
        public Medicine createFromParcel(Parcel in) {
            return new Medicine(in);
        }

        @Override
        public Medicine[] newArray(int size) {
            return new Medicine[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(frequency);
        parcel.writeFloat(doses);
        parcel.writeInt(duration);
    }
}
