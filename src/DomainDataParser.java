import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author michal wozniak id 21941097
 * @author sebastian proctor-shah id 29649727
 *
 * Date Created on 10/7/2015
 *
 * DomainDataParser
 *
 * query a website using https connection (SSL) and fetch information about the certificate, key size, algorithm used...
 *
 */
public class DomainDataParser {

    private ArrayList<Domain> domains;

    public DomainDataParser() {

        domains = new ArrayList<>();
    }
    //https://books.google.ca/books?id=G_hGOkywlhEC&pg=PT128&lpg=PT128&dq=detect+the+HTTP+header+SSL+socket+java&source=bl&ots=-luPSDBt-Q&sig=CHy0uEeTypgXzn7moy6nQeXX0rQ&hl=en&sa=X&ved=0CB4Q6AEwATgKahUKEwj84LaaurjIAhWKFT4KHdJqBj4#v=onepage&q=detect%20the%20HTTP%20header%20SSL%20socket%20java&f=false

    /**
     * connect to the current Domain and fetch the necessary information
     *
     * @param currentDomain
     */
    private void query(Domain currentDomain) {

        String USER_AGENT = "Mozilla/5.0";
        String ACCEPT_LANGUAGE = "en-US,en;q=0.5";

        java.security.Security.setProperty("jdk.tls.disabledAlgorithms", "");
        System.setProperty("https.protocols", "SSLv3,TLSv1,TLSv1.1,TLSv1.2");

        //ssl port
        int port = 443;
        //SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();


        //error handling
        boolean unknownHost = false;
        boolean connectionTimedOut = false;

        //set timeout
        int timeOut = 500;


        try {

            //SSLSocket socket = (SSLSocket) factory.createSocket(currentDomain.getDomain(), port);

            // if connection take more than 3 second, stop it
            SSLSocket socket = (SSLSocket)SSLSocketFactory.getDefault().createSocket();
            socket.connect(new InetSocketAddress(currentDomain.getDomain(),port),timeOut);
            //socket.startHandshake();

            //Re-enable deprecated
            String[] suites = socket.getEnabledCipherSuites();
            ArrayList<String> newSuitesList =
                    new ArrayList<String>(Arrays.asList(suites));
            newSuitesList.add("SSL_RSA_WITH_RC4_128_SHA");
            newSuitesList.add("SSL_RSA_WITH_RC4_128_MD5");
            String[] newSuitesArray = new String[newSuitesList.size()];
            newSuitesArray = newSuitesList.toArray(newSuitesArray);
            socket.setEnabledCipherSuites(newSuitesArray);


            //socket.setSoTimeout(3000);
            SSLSession sslSession = socket.getSession(); // does the startHandshake()

            if (sslSession.isValid()) {



                // set all the attributes related to the ssl connection
                setHTTPSInfo(sslSession, currentDomain);

                //verify the Strict-Transport-Security header
                verifyStrictTransportSecurity(currentDomain, USER_AGENT, ACCEPT_LANGUAGE);


            } else {
                currentDomain.setIsHTTPS("false");
            }
        } catch (SSLHandshakeException sslHE) {
            currentDomain.setIsHTTPS("false");

        } catch (SocketTimeoutException ste) {
            connectionTimedOut = true;
            currentDomain.setIsHTTPS("timeout");
            currentDomain.setSSLversion("timeout");
            currentDomain.setKeyType("timeout");
            currentDomain.setKeySize("timeout");
            currentDomain.setSignatureAlgorithm("timeout");
            currentDomain.setIsHSTS("timeout");
            currentDomain.setIsHSTSlong("timeout");

        } catch (ConnectException ce) {

            // determine if connection is refused or timed out
            if (ce.getMessage().contains("Connection refused")) {
                currentDomain.setIsHTTPS("false");
            } else {
                connectionTimedOut = true;
            }

        } catch (UnknownHostException e) {
            //unknown Host exception
            unknownHost = true;
            currentDomain.setIsHTTPS("unknownHost");
            currentDomain.setSSLversion("unknownHost");
            currentDomain.setKeyType("unknownHost");
            currentDomain.setKeySize("unknownHost");
            currentDomain.setSignatureAlgorithm("unknownHost");
            currentDomain.setIsHSTS("unknownHost");
            currentDomain.setIsHSTSlong("unknownHost");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            printSocketInfo(currentDomain, connectionTimedOut, unknownHost);
            domains.add(currentDomain);

        }

    }

    /**
     * verify if the domain use the http header field StrictTransportSecurity
     * set the currentDomain related parameter
     * @param currentDomain current domain used
     * @param USER_AGENT use fake user agent
     * @param ACCEPT_LANGUAGE
     */
    private void verifyStrictTransportSecurity(Domain currentDomain, String USER_AGENT, String ACCEPT_LANGUAGE) {

        URL UrlConnection;
        URLConnection con;
        Map<String, List<String>> map;

        try {
            UrlConnection = new URL("https://" + currentDomain.getDomain());
            System.out.println("VALID: " + UrlConnection.toString());

            con = UrlConnection.openConnection();
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", ACCEPT_LANGUAGE);

            map = con.getHeaderFields();
            //check if we get the header
            if(map.size()>0)
            {
                List<String> strictTransportSecurity = map.get("Strict-Transport-Security");

                //check if f
                if (strictTransportSecurity == null) {
                    currentDomain.setIsHSTS("false");
                    currentDomain.setIsHSTSlong("false");
                } else {
                    for (String header : strictTransportSecurity) {
                        currentDomain.setIsHSTS("true");
                        currentDomain.setIsHSTSlong(String.valueOf(isHSTSlong(header)));

                    }
                }
            }
            else
            {
                currentDomain.setIsHSTS("?");
                currentDomain.setIsHSTSlong("?");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Debugging purpose : print all header field of a http connection
     *
     * @param connection
     */
    private void printHeaderFields(URLConnection connection) {
        for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
            System.out.println(header.getKey() + "=" + header.getValue());
        }
    }


    /**
     * set ssl related information about your connection
     *
     * @param sslSession
     * @param domain     currentDomain
     */
    private void setHTTPSInfo(SSLSession sslSession, Domain domain) {

        domain.setIsHTTPS("true");
        domain.setSSLversion(sslSession.getProtocol());

        domain.setSSLversion(sslSession.getProtocol());
        X509Certificate[] certificates = new X509Certificate[0];
        try {
            certificates = sslSession.getPeerCertificateChain();
        } catch (SSLPeerUnverifiedException e) {
            e.printStackTrace();
        }

        domain.setKeyType(certificates[0].getPublicKey().getAlgorithm());

        String certificateString = (String.valueOf(certificates[0].getPublicKey()));
        domain.setKeySize(findKeySize(certificateString));

        String signatureAlgorithm = certificates[0].getSigAlgName();
        domain.setSignatureAlgorithm(convertSignatureAlgorithm(signatureAlgorithm));
    }


    /**
     * Debugging method that print all the current domain inform
     *
     * @param existingDomain
     * @param connectionTimedOut
     * @param unknownHost
     */
    private void printSocketInfo(Domain existingDomain, boolean connectionTimedOut, boolean unknownHost) {
        //rank,domain,isHTTPS,SSLversion,key-type,key-size,signature-algorithm,isHSTS,isHSTSlong

        System.out.println("UNKNOWN_HOST: " + unknownHost);
        System.out.println("TIMEOUT: " + connectionTimedOut);
        System.out.println("Rank: " + existingDomain.getRank());
        System.out.println("Domain :" + existingDomain.getDomain());
        System.out.println("isHTTPS: " + existingDomain.isHTTPS());
        System.out.println("SSLversion: " + existingDomain.getSSLversion());
        System.out.println("Key-type: " + existingDomain.getKeyType());
        System.out.println("Key-size: " + existingDomain.getKeySize());
        System.out.println("signature-algorithm: " + existingDomain.getSignatureAlgorithm());
        System.out.println("isHSTS: " + existingDomain.getIsHSTS());
        System.out.println("isHSTSLong: " + existingDomain.getIsHSTSlong());
        System.out.println("----------------------------------------");

    }

    private String convertSignatureAlgorithm(String algorithm) {

        //http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Signature
        switch (algorithm) {
            //"SSL_RSA_WITH_RC4_128_SHA
            case "SHA256withRSA":
                return "SHA256";
            case "SHA1withRSA":
                return "SHA1";
            case "SHA1withDSA":
                return "SHA1";
            case "SHA1withECDSA":
                return "SHA1";
            case "SHA256withDSA":
                return "SHA256";
            case "SHA2withECDSA":
                return "SHA2";
            case "SHA2withRSA":
                return "SHA2";
            case "MD2withRSA":
                return "MD2";
            case "MD5withRSA":
                return "MD5";
            case "SHA384withRSA":
                return "SHA384";
            case "SHA512withRSA":
                return "SHA512";
            case "SHA256withECDSA":
                return "SHA256";
            case "SHA384withECDSA":
                return "SHA384";
            case "SHA512withECDSA":
                return "SHA512";
            case "SHA128withRC4":
                return "RC4";
            case "MD5withRC4":
                return "RC4";
            //newSuitesList.add("SSL_RSA_WITH_RC4_128_SHA");
            //newSuitesList.add("SSL_RSA_WITH_RC4_128_MD5");
        }
        return null;
    }

    /**
     * parse the certificate to get the key size
     *
     * @param certificate
     * @return key size string
     */
    private String findKeySize(String certificate) {
        Pattern pattern = Pattern.compile(",(.+?)bits");
        Matcher matcher = pattern.matcher(certificate);
        matcher.find();
        return matcher.group(1);
    }


    /**
     * check if HSTS is longer than one month
     *
     * @param input
     * @return true/false
     */
    private boolean isHSTSlong(String input) {
        //System.out.println(input);
        String number = input.replaceAll("[^0-9]", "");
        int maxAge = Integer.parseInt(number);
        return maxAge >= 2629744;
    }


    public ArrayList<Domain> getDomains() {
        return domains;
    }

    /**
     * query the list of domains
     *
     * @param list list of domains
     */
    public void queryDomains(ArrayList<Domain> list) {

        for (Domain domain : list) {
            query(domain);
        }
    }
}
