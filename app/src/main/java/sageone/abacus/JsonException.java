package sageone.abacus;

/**
 * Created by otomaske on 27.01.2016.
 */

import org.json.JSONObject;

public class JsonException extends Exception {

    private static final long serialVersionUID = 4491098305202657442L;

    public JsonException(String message){
        super(message);
    }

    public JsonException(JSONObject errorObject){
        super(errorObject.toString());
    }

}
