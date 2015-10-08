/**
 * Created by michal on 10/7/2015.
 */

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

/**
 * @author Crunchify.com
 */
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;


public class Driver {


    private static boolean isHSTSlong(String input) {
        int maxAge = Integer.parseInt(input.substring(8));
        return maxAge < 2629744;
    }

    public static void main(String[] args) {

        //"https://www.gov.uk/"
        DomainDataParser test =new DomainDataParser();
        test.get("https://www.gov.uk/");
        ArrayList<Domain> list = test.getDomains();
        System.out.println(list.get(0).getDomain());
        CSV.writeCSV("test.csv",list);
    }
}