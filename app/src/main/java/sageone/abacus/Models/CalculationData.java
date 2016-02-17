package sageone.abacus.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import sageone.abacus.Exceptions.FormatException;
import sageone.abacus.Helper.FormatHelper;
import sageone.abacus.Helper.MessageHelper;
import sageone.abacus.R;

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
    public String IGU;
    public String Sozialabgaben;
    public String Steuern;

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
        Sozialabgaben = in.readString();

        _summarizeTaxes();
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
        dest.writeString(Sozialabgaben);
        dest.writeString(Steuern);
    }

    /**
     *
     */
    private void _summarizeTaxes()
    {
        Double taxWage   = Double.parseDouble(Lohnsteuer.replace(".", "").replace(",", "."));
        Double taxSoli   = Double.parseDouble(Soli.replace(".", "").replace(",", "."));
        Double taxChurch = Double.parseDouble(Kirchensteuer.replace(".", "").replace(",", "."));

        Double tax = taxChurch + taxSoli + taxWage;
        Steuern = tax.toString();
    }
}