package sageone.abacus.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by profilohn on 04.02.2016.
 */
public class Insurances
{
    public boolean success;
    public String status;
    public String message;
    public List<InsurancesData> data = new ArrayList<InsurancesData>();

}
