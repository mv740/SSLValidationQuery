
import java.util.ArrayList;

/**
 * @author Michal Wozniak id 21941097
 * @author sebastian proctor-shah id 29649727
 *
 * Date Created on 10/7/2015
 * Date Updated : 10/30/2015
 *
 * HTTPS ANALYSER
 *
 * 1- create csv file
 * 1- Connect to multiple website and determine if HTTPS is supported
 * 2- if it is then you will parse the ssl session and determine which ssl, certificate key, algorithim they use
 * 3-  every time a domain information has been set, write it to the cvs file
 */


public class Driver {

    public static void main(String[] args)  {
        String filename = "FinalWebParsing.csv";

        CSV.createFile(filename);

        ArrayList<Domain> csvList = CSV.readFileCustomIndex("top-1m.csv", 21941097,29649727);
        DomainDataParser parser = new DomainDataParser(filename);
        parser.queryDomains(csvList);


    }
}