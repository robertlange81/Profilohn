package com.profilohn.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import static com.profilohn.Activities.InputActivity.getBigDecimal;
import static com.profilohn.Activities.InputActivity.getDecimalString_Up;

/**
 * Created by profilohn on 04.02.2016.
 *
 * The calculation result data class.
 */
public class CalculationData implements Parcelable {

    public String LohnsteuerPflBrutto;
    public String SVPflBrutto;
    public String Summe_Altersvorsorge;
    public String AG_Zuschuss_Altersvorsorge;
    public String AN_Anteil_Altersvorsorge;
    public String Lohnsteuer;
    public String Pausch_LohnSteuer_AG;
    public String Pausch_LohnSteuer_AN;
    public String Soli;
    public String Pausch_Soli_AG;
    public String Pausch_Soli_AN;
    public String Kirchensteuer;
    public String Pausch_Kirchensteuer_AG;
    public String Pausch_Kirchensteuer_AN;
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
    public String IGU;
    public String Umlagen_AG;
    public String pauschSt_AG;
    public String pauschSt_AN;
    public String Abgaben_AG;
    public String Pfaendung;
    //public String brutto_pflichtig_Altersvorsorge;
    //public String brutto_pflichtig_Firmenwagen;


    private static NumberFormat format;

    protected CalculationData(Parcel in) {
        LohnsteuerPflBrutto = in.readString();
        SVPflBrutto = in.readString();
        Summe_Altersvorsorge = in.readString();
        AG_Zuschuss_Altersvorsorge = in.readString();
        AN_Anteil_Altersvorsorge = in.readString();
        Lohnsteuer = in.readString();
        Pausch_LohnSteuer_AG = in.readString();
        Pausch_LohnSteuer_AN = in.readString();
        Soli = in.readString();
        Pausch_Soli_AG = in.readString();
        Pausch_Soli_AN = in.readString();
        Kirchensteuer = in.readString();
        Pausch_Kirchensteuer_AG = in.readString();
        Pausch_Kirchensteuer_AN = in.readString();
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
        Abgaben_AG = in.readString();
        Pfaendung = in.readString();

        _summarizeEmployerAndEmployeeCats();
    }

    public static final Creator<CalculationData> CREATOR = new Creator<CalculationData>() {
        @Override
        public CalculationData createFromParcel(Parcel in) {
            try {
                return new CalculationData(in);
            } catch (Exception x) {
                String y = x.getMessage();
                return null;
            }
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
        dest.writeString(LohnsteuerPflBrutto == null ? "0,00" : LohnsteuerPflBrutto);
        dest.writeString(SVPflBrutto == null ? "0,00" : SVPflBrutto);
        dest.writeString(Summe_Altersvorsorge == null ? "0,00" : Summe_Altersvorsorge);
        dest.writeString(AG_Zuschuss_Altersvorsorge == null ? "0,00" : AG_Zuschuss_Altersvorsorge);
        dest.writeString(AN_Anteil_Altersvorsorge == null ? "0,00" : AN_Anteil_Altersvorsorge);
        dest.writeString(Lohnsteuer == null ? "0,00" : Lohnsteuer);
        dest.writeString(Pausch_LohnSteuer_AG == null ? "0,00" : Pausch_LohnSteuer_AG);
        dest.writeString(Pausch_LohnSteuer_AN == null ? "0,00" : Pausch_LohnSteuer_AN);
        dest.writeString(Soli == null ? "0,00" : Soli);
        dest.writeString(Pausch_Soli_AG == null ? "0,00" : Pausch_Soli_AG);
        dest.writeString(Pausch_Soli_AN == null ? "0,00" : Pausch_Soli_AN);
        dest.writeString(Kirchensteuer == null ? "0,00" : Kirchensteuer);
        dest.writeString(Pausch_Kirchensteuer_AG == null ? "0,00" : Pausch_Kirchensteuer_AG);
        dest.writeString(Pausch_Kirchensteuer_AN == null ? "0,00" : Pausch_Kirchensteuer_AN);
        dest.writeString(Krankenversicherung_AN == null ? "0,00" : Krankenversicherung_AN);
        dest.writeString(Rentenversicherung_AN == null ? "0,00" : Rentenversicherung_AN);
        dest.writeString(Arbeitslosenversicherung_AN == null ? "0,00" : Arbeitslosenversicherung_AN);
        dest.writeString(Pflegeversicherung_AN == null ? "0,00" : Pflegeversicherung_AN);
        dest.writeString(Krankenversicherung_AG == null ? "0,00" : Krankenversicherung_AG);
        dest.writeString(Rentenversicherung_AG == null ? "0,00" : Rentenversicherung_AG);
        dest.writeString(Arbeitslosenversicherung_AG == null ? "0,00" : Arbeitslosenversicherung_AG);
        dest.writeString(Pflegeversicherung_AG == null ? "0,00" : Pflegeversicherung_AG);
        dest.writeString(Umlage1 == null ? "0,00" : Umlage1);
        dest.writeString(Umlage2 == null ? "0,00" : Umlage2);
        dest.writeString(Netto == null ? "0,00" : Netto);
        dest.writeString(Auszahlung == null ? "0,00" : Auszahlung);
        dest.writeString(AGAnteil == null ? "0,00" : AGAnteil);
        dest.writeString(IGU == null ? "0,00" : IGU);
        dest.writeString(ANAnteil == null ? "0,00" : ANAnteil);
        dest.writeString(Steuern == null ? "0,00" : Steuern);
        dest.writeString(Umlagen_AG == null ? "0,00" : Umlagen_AG);
        dest.writeString(Abgaben_AG == null ? "0,00" : Abgaben_AG);
        dest.writeString(Pfaendung == null ? "0,00" : Pfaendung);
    }

    private void _summarizeEmployerAndEmployeeCats()
    {
        BigDecimal grossPayEmpl    = new BigDecimal(0.00);
        BigDecimal contributionSum = new BigDecimal(0.00);

        BigDecimal sumkv       = new BigDecimal(0.00);
        BigDecimal sumrv       = new BigDecimal(0.00);
        BigDecimal sumav       = new BigDecimal(0.00);
        BigDecimal sumpv       = new BigDecimal(0.00);
        BigDecimal sumSocial       = new BigDecimal(0.00);

        BigDecimal taxLst          = new BigDecimal(0.00);
        BigDecimal taxSoli         = new BigDecimal(0.00);
        BigDecimal taxKiSt         = new BigDecimal(0.00);
        BigDecimal sumTax          = new BigDecimal(0.00);
        BigDecimal sumEmployer     = new BigDecimal(0.00);

        BigDecimal taxLstAn          = new BigDecimal(0.00);
        BigDecimal taxSoliAN         = new BigDecimal(0.00);
        BigDecimal taxKiStAn         = new BigDecimal(0.00);
        BigDecimal sumTaxAn          = new BigDecimal(0.00);

        BigDecimal provisionEmployer          = new BigDecimal(0.00);
        BigDecimal provisionEmployee          = new BigDecimal(0.00);

        try {
            grossPayEmpl    = getBigDecimal(LohnsteuerPflBrutto);
            contributionSum = getBigDecimal(Umlagen_AG);

            sumkv           = getBigDecimal(Krankenversicherung_AG);
            sumrv           = getBigDecimal(Rentenversicherung_AG);
            sumav           = getBigDecimal(Arbeitslosenversicherung_AG);
            sumpv           = getBigDecimal(Pflegeversicherung_AG);
            sumSocial       = sumkv.add(sumrv).add(sumav).add(sumpv);

            taxLst          = getBigDecimal(Pausch_LohnSteuer_AG);
            taxSoli         = getBigDecimal(Pausch_Soli_AG);
            taxKiSt         = getBigDecimal(Pausch_Kirchensteuer_AG);
            sumTax          = taxLst.add(taxSoli).add(taxKiSt);

            taxLstAn          = getBigDecimal(Pausch_LohnSteuer_AN);
            taxSoliAN         = getBigDecimal(Pausch_Soli_AN);
            taxKiStAn         = getBigDecimal(Pausch_Kirchensteuer_AN);
            sumTaxAn          = taxLstAn.add(taxSoliAN).add(taxKiStAn);

            provisionEmployer = getBigDecimal(AG_Zuschuss_Altersvorsorge);
            provisionEmployee = getBigDecimal(AN_Anteil_Altersvorsorge);

        } catch (Exception e) {
            e.printStackTrace();
        }

        sumEmployer = grossPayEmpl.add(contributionSum).add(sumSocial).add(sumTax).add(provisionEmployer);

        AGAnteil    = getDecimalString_Up(sumSocial);
        pauschSt_AG = getDecimalString_Up(sumTax);
        Abgaben_AG  = getDecimalString_Up(sumEmployer);

        pauschSt_AN = getDecimalString_Up(sumTaxAn);

        BigDecimal netto = getBigDecimal(Netto).subtract(sumTaxAn);
        Auszahlung = getDecimalString_Up(netto.subtract(getBigDecimal(Pfaendung)));
        Netto = getDecimalString_Up(netto);
    }
}