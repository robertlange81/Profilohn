package com.profilohn.Models;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by profilohn on 04.02.2016.
 */
public class Calculation implements Parcelable {

    public boolean success;
    public String status;
    public String message;
    public CalculationData data;
    public Object exception;

    protected Calculation(Parcel in) {
        success = in.readByte() != 0;
        status = in.readString();
        message = in.readString();
        data = in.readParcelable(CalculationData.class.getClassLoader());
    }

    public static final Creator<Calculation> CREATOR = new Creator<Calculation>() {
        @Override
        public Calculation createFromParcel(Parcel in) {
            return new Calculation(in);
        }

        @Override
        public Calculation[] newArray(int size) {
            return new Calculation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeString(status);
        dest.writeString(message);
        dest.writeParcelable(data, flags);
    }
}