package com.profilohn.Helper;

import android.content.Context;
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

import com.profilohn.Models.Calculation;
import com.profilohn.Models.Insurances;

/**
 * Created by profilohn on 02.03.2016.
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
    public Insurances readInsurancesResult() throws FileNotFoundException
    {
        File cacheFile = new File(cachePath, FILENAME_INSURANCES);
        String json = null;

        try {
            json = read(cacheFile);
        } catch (Exception e) {
            throw new FileNotFoundException("File not in cache");
        }

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
    public String read(File cacheFile) throws IOException, NullPointerException
    {
        FileInputStream fis = new FileInputStream(cacheFile);
        String json = "";

        json = convertStreamToString(fis);
        fis.close();

        return json;
    }


    /**
     * Clean the cache folder.
     *
     * @return
     */
    public void flush()
    {
        File r = new File(cachePath, FILENAME_CALC_RESULT);
        File i = new File(cachePath, FILENAME_INSURANCES);
        r.delete();
        i.delete();
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
