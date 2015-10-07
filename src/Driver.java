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

        final String USER_AGENT = "Mozilla/5.0";
        final String ACCEPT_LANGUAGE = "en-US,en;q=0.5";

        try {

            URL obj = new URL("https://www.gov.uk/");
            URLConnection con = obj.openConnection();
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", ACCEPT_LANGUAGE);
            con.setReadTimeout(3000);

            Map<String, List<String>> map = con.getHeaderFields();
            if (!map.isEmpty()) {

            /*for (Map.Entry<String, List<String>> header : con.getHeaderFields().entrySet()) {
                System.out.println(header.getKey() + "=" + header.getValue());
            }
            */

                List<String> strictTransportSecurity = map.get("Strict-Transport-Security");
                if (strictTransportSecurity == null) {
                    System.out.println("'Strict-Transport-Security ' not present in Header!");
                } else {
                    for (String header : strictTransportSecurity) {
                        System.out.println("stricTransportSecurityt: " + header);
                        System.out.println("stricTransportSecurityt: " + isHSTSlong(header));
                    }
                }
            } else {
                System.out.println("TIMEOUT");
            }



            //Delimiter used in CSV file
            final String COMMA_DELIMITER = ",";
            final String NEW_LINE_SEPARATOR = "\n";

            //CSV file header
            final String FILE_HEADER = "rank,domain,isHTTPS,SSLversion,key-type,key-size,signature-algorithm,isHSTS,isHSTSlong";


            //Create new students objects
            Domain d = new Domain(1, "gov");
            Domain d1 = new Domain(1, "test");

            //Create a new list of student objects
            ArrayList<Domain> domains = new ArrayList();
            domains.add(d);
            domains.add(d1);


            CSV.writeCSV("testing", domains);

        } catch (SocketTimeoutException e) {
            System.out.println("timeout");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}