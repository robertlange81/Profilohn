package sageone.abacus.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

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
    public String Krankenversicherung_AN;
    public String Rentenversicherung_AN;
    public String Arbeitslosenversicherung_AN;
    public String Pflegeversicherung_AN;
    public String Krankenversicherung_AG;
    public String Rentenversicherung_AG;
    public String Arbeitslosenversicherung_AG;
    public String Pflegeversicherung_AG;
    public String Umlage1;
    public String Umlage2;
    public String Netto;
    public String Auszahlung;
    public String AGAnteil;
    public String ANAnteil;
    public String Steuern;
    public String Sozialabgaben_AG;
    public String IGU;
    public String Umlagen_AG;
    public String Abgaben_AG;

    protected CalculationData(Parcel in) {
        LohnsteuerPflBrutto = in.readString();
        SVPflBrutto = in.readString();
        Lohnsteuer = in.readString();
        Soli = in.readString();
        Kirchensteuer = in.readString();
        Krankenversicherung_AN = in.readString();
        Rentenversicherung_AN = in.readString();
        Arbeitslosenversicherung_AN = in.readString();
        Pflegeversicherung_AN = in.readString();
        Krankenversicherung_AG = in.readString();
        Rentenversicherung_AG = in.readString();
        Arbeitslosenversicherung_AG = in.readString();
        Pflegeversicherung_AG = in.readString();
        Umlage1 = in.readString();
        Umlage2 = in.readString();
        Netto = in.readString();
        Auszahlung = in.readString();
        AGAnteil = in.readString();
        IGU = in.readString();
        ANAnteil = in.readString();
        Steuern = in.readString();
        Umlagen_AG = in.readString();
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
        dest.writeString(Krankenversicherung_AN);
        dest.writeString(Rentenversicherung_AN);
        dest.writeString(Arbeitslosenversicherung_AN);
        dest.writeString(Pflegeversicherung_AN);
        dest.writeString(Krankenversicherung_AG);
        dest.writeString(Rentenversicherung_AG);
        dest.writeString(Arbeitslosenversicherung_AG);
        dest.writeString(Pflegeversicherung_AG);
        dest.writeString(Umlage1);
        dest.writeString(Umlage2);
        dest.writeString(Netto);
        dest.writeString(Auszahlung);
        dest.writeString(AGAnteil);
        dest.writeString(IGU);
        dest.writeString(ANAnteil);
        dest.writeString(Steuern);
        dest.writeString(Umlagen_AG);
        dest.writeString(Abgaben_AG);
    }

    private void _summarizeEmployerCats()
    {

        Double contribution1   = 0.00;
        Double contribution2   = 0.00;
        Double contributionSum = 0.00;
        Double sumSocial       = 0.00;
        Double sumEmployer     = 0.00;

        try {
            contribution1   = parse(Umlage1);
            contribution2   = parse(Umlage2);
            sumSocial       = parse(AGAnteil);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        contributionSum = contribution1 + contribution2;
        sumEmployer     = contributionSum + sumSocial;

        Umlagen_AG  = contributionSum.toString();
        Abgaben_AG  = sumEmployer.toString();
    }

    private static NumberFormat format;
    private static Double parse(String value) throws ParseException {
        if(format == null) {
            format = NumberFormat.getInstance(Locale.GERMANY);
        }

        return format.parse(value).doubleValue();
    }
}