/**
 * Created by michal on 10/7/2015.
 */


import java.util.ArrayList;

/**
 * @author Michal Wozniak
 */


public class Driver {

    public static void main(String[] args)  {


        ArrayList<Domain> csvList = CSV.readFileCustomIndex("top-1m.csv", 21941097,29649727);
        DomainDataParser parser = new DomainDataParser();
        parser.queryDomains(csvList);
        ArrayList<Domain> domains = parser.getDomains();
        CSV.writeCSV("test1.csv", domains);


    }
}