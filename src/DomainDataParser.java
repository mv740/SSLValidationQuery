import com.sun.org.apache.xerces.internal.util.URI;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by micha on 10/8/2015.
 */
public class DomainDataParser {

    public ArrayList<Domain> domains;

    public DomainDataParser() {
        domains = new ArrayList();
    }


    public void get(String url){

        String USER_AGENT = "Mozilla/5.0";
        String ACCEPT_LANGUAGE = "en-US,en;q=0.5";

        try {
            URL obj = new URL(url);
            URLConnection con = obj.openConnection();
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", ACCEPT_LANGUAGE);
            con.setReadTimeout(3000);


            //Create new Domain objects
            Domain domainData = new Domain(1, "gov");

            Map<String, List<String>> map = con.getHeaderFields();
            if (!map.isEmpty()) {

            /*for (Map.Entry<String, List<String>> header : con.getHeaderFields().entrySet()) {
                System.out.println(header.getKey() + "=" + header.getValue());
            }
            */

                List<String> strictTransportSecurity = map.get("Strict-Transport-Security");
                if (strictTransportSecurity == null) {
                    domainData.setIsHSTS("false");
                    domainData.setIsHSTSlong("false");
                    System.out.println("'Strict-Transport-Security ' not present in Header!");

                } else {
                    for (String header : strictTransportSecurity) {
                        domainData.setIsHSTS("true");
                        domainData.setIsHSTSlong(String.valueOf(isHSTSlong(header)));

                        System.out.println(header);
                        System.out.println("isHSTS : " + domainData.getIsHSTS());
                        System.out.println("isHSTSlong: " + isHSTSlong(header));
                    }
                }
            } else {
                System.out.println("TIMEOUT");
            }
            //Create a new list of Domain objects
            domains.add(domainData);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //using index
    public void start(int index)
    {

    }


    public ArrayList<Domain> getDomains() {
        return domains;
    }

    private static boolean isHSTSlong(String input) {
        int maxAge = Integer.parseInt(input.substring(8));
        return maxAge < 2629744;
    }
}
