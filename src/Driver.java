
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

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
 * 3-  every time a domain information has been set, write it to the cvs file
 */

public class Driver {

    public static void main(String[] args)  {

        String filename = "test.csv";



        ArrayList<Domain> csvList = CSV.readFileCustomIndex("top-1m.csv", 21941097,29649727);
        //ArrayList<Domain> csvList = CSV.readFileStudentTest("top-1m.csv", 21941097, 1000);
        System.out.println(csvList.size());

        //create thread pool, each worker query one domain, get List of result, sort them and write them to file
        MultiThreadQueryProcess.getInstance().start(csvList, filename);



    }
}