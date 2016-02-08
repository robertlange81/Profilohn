package sageone.abacus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.*;
import org.json.JSONException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 *
 */
public class ResultActivity extends Activity implements
        OnItemClickListener, AdapterView.OnItemSelectedListener {

    private static boolean isWorking = false;
    public static ResultActivity instance;
    public CalculationInput input;
    public CalculationOutput output;

    public static TextView LohnsteuerPflBrutto,
            Lohnsteuer,
            Soli,
            Kirchensteuer,
            Krankenversicherung_AN,
            Rentenversicherung_AN,
            Arbeitslosenversicherung_AN,
            Pflegeversicherung_AN,
            netto;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.result);

        ResultActivity.LohnsteuerPflBrutto = (TextView) findViewById(R.id.LohnsteuerPflBrutto);
        ResultActivity.Lohnsteuer = (TextView) findViewById(R.id.Lohnsteuer);
        ResultActivity.Soli = (TextView) findViewById(R.id.Soli);
        ResultActivity.Kirchensteuer = (TextView) findViewById(R.id.Kirchensteuer);
        ResultActivity.Krankenversicherung_AN = (TextView) findViewById(R.id.Krankenversicherung_AN);
        ResultActivity.Rentenversicherung_AN = (TextView) findViewById(R.id.Rentenversicherung_AN);
        ResultActivity.Arbeitslosenversicherung_AN = (TextView) findViewById(R.id.Arbeitslosenversicherung_AN);
        ResultActivity.Pflegeversicherung_AN = (TextView) findViewById(R.id.Pflegeversicherung_AN);
        ResultActivity.netto = (TextView) findViewById(R.id.netto);

        TextView link = (TextView) findViewById(R.id.linkToSage);
        String linkText = "<a href='https://www.sageone.de/lohn-und-gehalt/'>Sage One Lohn und Buchhaltung</a>";
        link.setText(Html.fromHtml(linkText));
        link.setMovementMethod(LinkMovementMethod.getInstance());

        refresh();
        instance = this;
    }

    public void refresh() {
        this.accessWebService();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.accessWebService();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void accessWebService() {
        if (!isWorking) {
            isWorking = true;
            try {
                this.input = ReadInput();
                FuelServiceAccessTask task = new FuelServiceAccessTask();
                task.execute(new String[]{""});
            } catch (Exception ex) {
                AlertDialog alertDialog = new AlertDialog.Builder(ResultActivity.this).create();
                alertDialog.setTitle("Fehler");
                alertDialog.setMessage(ex.getMessage());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return;
            }
        }
    }

    private CalculationInput ReadInput() throws Exception {

        CalculationInput input = new CalculationInput();
        input.setAbrjahr(2015); //TODO;
        input.setAzwo(40f);
        input.setBerechnungsart("Bruttolohn");
        String br = InputAdapter.wage.getText().toString();
        if (br.matches("[0-9]+([,.][0-9]{1,2})?")) {
            input.setBrutto(Float.parseFloat(InputAdapter.wage.getText().toString()));
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(ResultActivity.this).create();
            alertDialog.setTitle("Fehler");
            alertDialog.setMessage("Bitte geben Sie einen numerischen Wert bei Bruttolohn ein!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            throw new Exception();
        }
        if (InputAdapter.wage.getText() == "Sachsen") {
            input.setBundesland(13); // TODO key values in spinner
        } else {
            input.setBundesland(1); // TODO key values in spinner
        }

        input.setGleit(false);
        input.setKindFrei(Float.parseFloat(InputAdapter.children.getTag().toString()));
        input.setKindu23(input.getKindFrei() > 0);
        //input.setKirche(InputAdapter.kist.get
        input.setKkbetriebsnummer(1086312);
        input.setKv(true);
        input.setRv(true);
        input.setAv(true);
        input.setStfreibetrag(0f);
        input.setZeitraum("m");
        input.setStkl(Integer.parseInt(InputAdapter.taxclass.getTag().toString()));

        return input;
    }

    private CalculationOutput ReadResult(JSONObject data) throws JSONException, ParseException {

        CalculationOutput output = new CalculationOutput();
        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
        output.setArbeitslosenversicherung_AG(data.getString("Arbeitslosenversicherung_AG") + " €");
        output.setArbeitslosenversicherung_AN(data.getString("Arbeitslosenversicherung_AN") + " €");
        output.setAGAnteil(data.getString("AGAnteil") + " €");
        output.setAuszahlung(data.getString("Auszahlung") + " €");
        output.setBruttoDecimal(data.getString("BruttoDecimal") + " €");
        output.setIGU(data.getString("IGU") + " €");
        output.setKirchensteuer(data.getString("Kirchensteuer") + " €");
        output.setKrankenversicherung_AG(data.getString("Krankenversicherung_AG") + " €");
        output.setKrankenversicherung_AN(data.getString("Krankenversicherung_AN") + " €");
        output.setArbeitslosenversicherung_AN(data.getString("Arbeitslosenversicherung_AN") + " €");
        output.setArbeitslosenversicherung_AG(data.getString("Arbeitslosenversicherung_AG") + " €");
        output.setLohnsteuer(data.getString("Lohnsteuer") + " €");
        output.setLohnsteuerPflBrutto(data.getString("LohnsteuerPflBrutto") + " €");
        output.setRentenversicherung_AG(data.getString("Rentenversicherung_AG") + " €");
        output.setRentenversicherung_AN(data.getString("Rentenversicherung_AN") + " €");
        output.setKirchensteuer(data.getString("Kirchensteuer") + " €");
        output.setNetto(data.getString("Netto") + " €");
        output.setPflegeversicherung_AG(data.getString("Pflegeversicherung_AG") + " €");
        output.setPflegeversicherung_AN(data.getString("Pflegeversicherung_AN") + " €");
        output.setSoli(data.getString("Soli") + " €");
        output.setSozialabgaben(data.getString("Sozialabgaben") + " €");
        output.setUmlage1(data.getString("Umlage1") + " €");
        output.setUmlage2(data.getString("Umlage2") + " €");

        return output;
    }

    public class FuelServiceAccessTask extends AsyncTask<String, Void, String> {

        private final JsonClient rest = new JsonClient();

        private LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        private LinearLayout resultPanel = (LinearLayout) findViewById(R.id.resultPanel);

        private static final String ABACUS_API = "http://10.59.3.139:8080/api/wage/calc";

        @Override
        protected void onPreExecute() {
            resultPanel.setVisibility(View.INVISIBLE);
            linlaHeaderProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            if (InputAdapter.relevantChange) {

                try {

                    output = GetData(
                            ResultActivity.this.input
                    );

                } catch (Exception e) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ResultActivity.this).create();
                    alertDialog.setTitle("Fehler");
                    alertDialog.setMessage(e.getMessage());
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return e.getMessage() + e.getCause() + e.getStackTrace();
                }
                return "ok";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            showOutputOnView();
            resultPanel.setVisibility(View.VISIBLE);
            linlaHeaderProgress.setVisibility(View.GONE);
            isWorking = false;
        }

        private CalculationOutput GetData(CalculationInput input) throws JsonException, JSONException, ParseException {

            CalculationOutput retval = new CalculationOutput();

            JSONObject data = new JSONObject();
            data.put("Brutto", input.getBrutto().toString().replace(".", ","));
            data.put("Berechnungsart", "Bruttolohn");
            data.put("AzWo", "40.00");
            data.put("StKl", input.getStkl().toString());
            data.put("AbrJahr", "2015");
            data.put("Bundesland", input.getBundesland().toString());
            data.put("KV", "1");
            data.put("KKBetriebsnummer", "1086312");
            data.put("KindFrei", input.getKindFrei().toString());
            data.put("StFreibetrag", "0");
            data.put("Zeitraum", "m");
            data.put("KindU23", input.getKindu23() ? "1" : "0");
            data.put("Kirche", input.getKirche() ? "1" : "0");
            data.put("RV", "1");
            data.put("AV", "1");
            data.put("Gleit", "");

            Map<String, Object> dataWrapper = new HashMap<String, Object>();
            dataWrapper.put("data", data);

            String homeApiRequestString = ABACUS_API;
            JSONObject json = rest.request(
                    homeApiRequestString,
                    "POST",
                    dataWrapper
            );

            String status = json.getString("status");
            if (status.equals("success")) {
                JSONObject result = json.getJSONObject("data");
                retval = ReadResult(result);
            }

            return retval;
        }
    }

    private void showOutputOnView() {
        ResultActivity.this.LohnsteuerPflBrutto.setText(output.getLohnsteuerPflBrutto());
        ResultActivity.this.Lohnsteuer.setText("-" + output.getLohnsteuer());
        ResultActivity.this.Soli.setText("-" + output.getSoli());
        ResultActivity.this.Kirchensteuer.setText("-" + output.getKirchensteuer());
        ResultActivity.this.Krankenversicherung_AN.setText("-" + output.getKrankenversicherung_AN());
        ResultActivity.this.Rentenversicherung_AN.setText(" -" + output.getRentenversicherung_AN());
        ResultActivity.this.Arbeitslosenversicherung_AN.setText("-" + output.getArbeitslosenversicherung_AN());
        ResultActivity.this.Pflegeversicherung_AN.setText("-" + output.getPflegeversicherung_AN());
        ResultActivity.this.netto.setText(output.getNetto());
    }

}