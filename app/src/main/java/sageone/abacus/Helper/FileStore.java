package sageone.abacus.Helper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.SortedMap;

import sageone.abacus.Models.Calculation;
import sageone.abacus.Models.Insurances;

/**
 * Created by otomaske on 02.03.2016.
 */
public class FileStore {

    private static final String FILENAME_CALC_RESULT = "last_result";
    private static final String FILENAME_INSURANCES = "insurances";
    private String cachePath;
    private final Gson gson;


    /**
     * Init the cache file objects.
     * @param c
     */
    public FileStore(Context c)
    {
        Context c1 = c;
        cachePath = c.getCacheDir().getAbsolutePath();
        gson = new GsonBuilder().create();
    }


    /**
     * Write the calculation result to
     * a local application cache file.
     *
     * @param data
     * @return
     */
    public boolean writeCalculationResult(Calculation data)
    {
        File cacheFile = new File(cachePath, FILENAME_CALC_RESULT);
        String _data = gson.toJson(data);
        return write(_data, cacheFile);
    }


    /**
     * Fetch the last calculation
     * data from file cache store.
     *
     * @return
     * @throws IOException
     */
    public Calculation readCalculationResult() throws IOException
    {
        File cacheFile = new File(cachePath, FILENAME_CALC_RESULT);
        String json = read(cacheFile);
        return gson.fromJson(json, Calculation.class);
    }


    /**
     * Write the insurances result to
     * a local application cache file.
     *
     * @param data
     * @return
     */
    public boolean writeInsurancesResult(Insurances data)
    {
        File cacheFile = new File(cachePath, FILENAME_INSURANCES);
        String _data = gson.toJson(data);
        return write(_data, cacheFile);
    }


    /**
     * Fetch the last insurances
     * data from file cache store.
     *
     * @return
     */
    public Insurances readInsurancesResult() throws IOException, FileNotFoundException
    {
        File cacheFile = new File(cachePath, FILENAME_INSURANCES);
        String json = read(cacheFile);
        return gson.fromJson(json, Insurances.class);
    }


    /**
     * Save data into file.
     *
     * @param data
     * @return
     */
    public boolean write(String data, File cacheFile)
    {
        try {
            FileOutputStream fos = new FileOutputStream(cacheFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(data);
            osw.close();

            return true;
        } catch (Exception e) {
            Log.e("FileStorage", e.getMessage());
        }

        return false;
    }


    /**
     * Try to get the previous result.
     *
     * @return
     */
    public String read(File cacheFile) throws IOException
    {
        if (cacheFile.exists()) {
            FileInputStream fis = new FileInputStream(cacheFile);
            String json = "";
            try {
                json = convertStreamToString(fis);
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
            fis.close();

            if (0 == json.length()) {
                throw new FileNotFoundException("No data found");
            }

            return json;
        } else {
            throw new FileNotFoundException();
        }
    }


    /**
     * Clean the cache folder.
     *
     * @return
     */
    public boolean flush()
    {
        try {
            String[] cmd = { "/system/bin/sh", "-c", "rm -f " + cachePath + "/{" + FILENAME_CALC_RESULT + "," + FILENAME_INSURANCES + "}"};
            Process process = Runtime.getRuntime().exec(cmd);

            try {
                process.waitFor();
                Log.i("FileStore", "cache flushed ..");
            } catch (InterruptedException e) {
                Log.e("FileStore", e.getMessage());
            }

            return true;
        } catch (IOException e) {
            Log.e("FileStorage", e.getMessage());
        }

        return false;
    }


    /**
     *
     * @param is
     * @return
     * @throws Exception
     */
    private static String convertStreamToString(InputStream is) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }

}
