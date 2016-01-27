package sageone.abacus;

import android.content.res.Resources;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by otomaske on 27.01.2016.
 */
public class WebService
{
    private String apiUri = null;
    private String apiMethod = "POST";
    private final RestClient rest = new RestClient();

    private static WebService Instance = new WebService();

    public static WebService getInstance() {
        return Instance;
    }

    /**
     * The constructor.
     */
    private WebService() {
        this._Init();
    }

    public void Calculate() {

    }

    /**
     * Initializes the webservice.
     */
    private void _Init() {
        this.apiUri = Resources.getSystem().getString(R.string.api_uri);
    }


    private JSONObject Request(JSONObject data) throws RestException {

        Map<String, Object> dataWrapper = new HashMap<String, Object>();
        dataWrapper.put("data", data);
        JSONObject res = rest.request(this.apiUri, this.apiMethod, dataWrapper);

        return res;
    }
}
