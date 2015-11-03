
import java.util.ArrayList;


/**
 * @author Michal Wozniak id 21941097
 * @author sebastian proctor-shah id 29649727
 *
 * Date Created on 10/7/2015
 * Date Updated : 11/01/2015
 *
 * HTTPS ANALYSER
 *
 * 1- create csv file
 * 1- Connect to multiple website and determine if HTTPS is supported
 * 2- if it is then you will parse the ssl session and determine which ssl, certificate key, algorithim they use
 * 3- when all queries are done, write all domains to the file 
 */

public class Driver {

    public static void main(String[] args)  {

        String filename = "test3.csv";

        ArrayList<Domain> csvList = CSV.readFileCustomIndex("top-1m.csv", 21941097,29649727);

        //testing
        //ArrayList<Domain> csvList = CSV.readFileStudentTest("top-1m.csv", 21941097, 50);

        //create thread pool, each worker query one domain, get List of results, and write them to file
        MultiThreadedQueryProcess.getInstance().start(csvList, filename);



    }
}