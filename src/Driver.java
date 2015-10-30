
import java.util.ArrayList;

/**
 * @author Michal Wozniak id 21941097
 * @author sebastian proctor-shah id 29649727
 *
 * Date Created on 10/7/2015
 *
 * HTTPS ANALYSER
 *
 * 1- Connect to multiple website and determine if HTTPS is supported
 * 2- if it is then you will parse the ssl session and determine which ssl, certificate key, algorithim they use
 * 3- write the list of domains that were queried into a cvs file
 */


public class Driver {

    public static void main(String[] args)  {


        ArrayList<Domain> csvList = CSV.readFileCustomIndex("top-1m.csv", 21941097,29649727);
        DomainDataParser parser = new DomainDataParser();
        parser.queryDomains(csvList);
        ArrayList<Domain> domains = parser.getDomains();
        CSV.writeCSV("test2.csv", domains);


    }
}