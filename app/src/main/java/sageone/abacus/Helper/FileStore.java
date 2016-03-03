package sageone.abacus.Helper;

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

import sageone.abacus.Models.Calculation;

/**
 * Created by otomaske on 02.03.2016.
 */
public class FileStore {

    private final File cacheFile;
    private static final String FILENAME = "last_result";
    private final Gson gson;


    /**
     * Init the cache file objects.
     * @param c
     */
    public FileStore(Context c)
    {
        Context c1 = c;
        String cachePath = c.getCacheDir().getAbsolutePath();
        cacheFile = new File(cachePath, FILENAME);
        gson = new GsonBuilder().create();
    }

    /**
     * Save data into file.
     *
     * @param data
     * @return
     */
    public boolean write(Calculation data)
    {
        String _data = gson.toJson(data);

        try {
            FileOutputStream fos = new FileOutputStream(cacheFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(_data);
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
    public Calculation read() throws IOException
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

            return gson.fromJson(json, Calculation.class);
        } else {
            throw new FileNotFoundException();
        }
    }


    /**
     * Flush the cache.
     *
     * @return
     */
    public boolean delete()
    {
        try {
            String[] cmd = { "/system/bin/sh", "-c", "rm -Rf " + cacheFile.getPath()};
            Runtime.getRuntime().exec(cmd);
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
