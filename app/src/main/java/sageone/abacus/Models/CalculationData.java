package sageone.abacus.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by otomaske on 04.02.2016.
 *
 * The calculation result data class.
 */
public class CalculationData implements Parcelable {

    public String LohnsteuerPflBrutto;
    public String SVPflBrutto;
    public String Lohnsteuer;
    public String Soli;
    public String Kirchensteuer;
    public String KrankenversicherungAN;
    public String RentenversicherungAN;
    public String ArbeitslosenversicherungAN;
    public String PflegeversicherungAN;
    public String KrankenversicherungAG;
    public String RentenversicherungAG;
    public String ArbeitslosenversicherungAG;
    public String PflegeversicherungAG;
    public String Umlage1;
    public String Umlage2;
    public String Netto;
    public String Auszahlung;
    public String AGAnteil;
    public String IGU;
    public String Sozialabgaben;
    public Double BruttoDecimal;
    public sageone.abacus.Models.Trace Trace;

    protected CalculationData(Parcel in) {
        LohnsteuerPflBrutto = in.readString();
        SVPflBrutto = in.readString();
        Lohnsteuer = in.readString();
        Soli = in.readString();
        Kirchensteuer = in.readString();
        KrankenversicherungAN = in.readString();
        RentenversicherungAN = in.readString();
        ArbeitslosenversicherungAN = in.readString();
        PflegeversicherungAN = in.readString();
        KrankenversicherungAG = in.readString();
        RentenversicherungAG = in.readString();
        ArbeitslosenversicherungAG = in.readString();
        PflegeversicherungAG = in.readString();
        Umlage1 = in.readString();
        Umlage2 = in.readString();
        Netto = in.readString();
        Auszahlung = in.readString();
        AGAnteil = in.readString();
        IGU = in.readString();
        Sozialabgaben = in.readString();
    }

    public static final Creator<CalculationData> CREATOR = new Creator<CalculationData>() {
        @Override
        public CalculationData createFromParcel(Parcel in) {
            return new CalculationData(in);
        }

        @Override
        public CalculationData[] newArray(int size) {
            return new CalculationData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(LohnsteuerPflBrutto);
        dest.writeString(SVPflBrutto);
        dest.writeString(Lohnsteuer);
        dest.writeString(Soli);
        dest.writeString(Kirchensteuer);
        dest.writeString(KrankenversicherungAN);
        dest.writeString(RentenversicherungAN);
        dest.writeString(ArbeitslosenversicherungAN);
        dest.writeString(PflegeversicherungAN);
        dest.writeString(KrankenversicherungAG);
        dest.writeString(RentenversicherungAG);
        dest.writeString(ArbeitslosenversicherungAG);
        dest.writeString(PflegeversicherungAG);
        dest.writeString(Umlage1);
        dest.writeString(Umlage2);
        dest.writeString(Netto);
        dest.writeString(Auszahlung);
        dest.writeString(AGAnteil);
        dest.writeString(IGU);
        dest.writeString(Sozialabgaben);
    }
}