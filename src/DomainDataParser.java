import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;


/**
 * @author michal wozniak id 21941097
 * @author sebastian proctor-shah id 29649727
 *         <p>
 *         Date Created on 10/7/2015
 *         Date Updated : 11/01/2015
 *         <p>
 *         DomainDataParser
 *         <p>
 *         query a website using https connection (SSL) and fetch information about the certificate, key size, algorithm used...
 */
public class DomainDataParser {

    //https://books.google.ca/books?id=G_hGOkywlhEC&pg=PT128&lpg=PT128&dq=detect+the+HTTP+header+SSL+socket+java&source=bl&ots=-luPSDBt-Q&sig=CHy0uEeTypgXzn7moy6nQeXX0rQ&hl=en&sa=X&ved=0CB4Q6AEwATgKahUKEwj84LaaurjIAhWKFT4KHdJqBj4#v=onepage&q=detect%20the%20HTTP%20header%20SSL%20socket%20java&f=false

    /**
     * connect to the current Domain and fetch the necessary information
     *
     * @param currentDomain
     */
    private static Domain query(Domain currentDomain) {

        String USER_AGENT = "Mozilla/5.0";
        String ACCEPT_LANGUAGE = "en-US,en;q=0.5";

        java.security.Security.setProperty("jdk.tls.disabledAlgorithms", "");
        System.setProperty("https.protocols", "SSLv3,TLSv1,TLSv1.1,TLSv1.2");

        //ssl port
        int port = 443;
        //SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();


        //error handling used with printSocketInfo method for debugging
        boolean unknownHost = false;
        boolean connectionTimedOut = false;

        //set timeout in ms
        int timeOut = 2000;

        try {

            //SSLSocket socket = (SSLSocket) factory.createSocket(currentDomain.getDomain(), port);

            // if connection take more than 3 second, stop it
            SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket();
            socket.setSoTimeout(2000);
            socket.connect(new InetSocketAddress(currentDomain.getDomain(), port), timeOut);
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


            SSLSession sslSession = socket.getSession(); // does the startHandshake()


            if (sslSession.isValid()) {

                // set all the attributes related to the ssl connection
                setHTTPSInfo(sslSession, currentDomain);

                //verify the Strict-Transport-Security header
                // use same socket connection
                verifyStrictTransportSecurityField(currentDomain, USER_AGENT, ACCEPT_LANGUAGE, socket);


            } else {
                currentDomain.setIsHTTPS("false");
            }
        } catch (SSLHandshakeException sslHE) {
            currentDomain.setIsHTTPS("false");

        } catch (SocketTimeoutException ste) {
            connectionTimedOut = true;
            connectionError(currentDomain, "timeout");

        } catch (UnknownHostException e) {
            //unknown Host exception
            unknownHost = true;
            connectionError(currentDomain, "unknownHost");

        } catch (ConnectException ce) {
            //https://github.com/googlegsa/activedirectory/blob/master/src/com/google/enterprise/adaptor/ad/AdServer.java
            if (ce.getMessage().contains("Connection refused")) {
                currentDomain.setIsHTTPS("false");
            } else {
                connectionTimedOut = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            //for debugging
            //printSocketInfo(currentDomain, connectionTimedOut, unknownHost);
        }
        return currentDomain;

    }

    /**
     * verify if the domain use the http header field StrictTransportSecurity
     * set the currentDomain related parameter
     *
     * @param currentDomain
     * @param USER_AGENT
     * @param ACCEPT_LANGUAGE
     * @param socket          current socket connection
     */
    private static void verifyStrictTransportSecurityField(Domain currentDomain, String USER_AGENT, String ACCEPT_LANGUAGE, SSLSocket socket) {
        PrintWriter s_out;
        BufferedReader s_in;
        String message = "HEAD / HTTP/1.1";

        try {
            s_out = new PrintWriter(socket.getOutputStream(), true);
            s_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            s_out.println(message);
            s_out.println("Host: " + currentDomain.getDomain());
            s_out.println("Accept-Language: " + ACCEPT_LANGUAGE);
            s_out.println("User-Agent: " + USER_AGENT);
            s_out.println();
            String response;

            Map<String, String> headerFields = new HashMap<>();
            while ((response = s_in.readLine()) != null) {
                int divider = response.indexOf(":");
                if (divider == -1) // not found then it is status code line
                {
                    if (response.length() > 0) {
                        //store status message
                        //ignore "HTTP/1.1 " message part
                        headerFields.put("status", response.substring(("HTTP/1.1").length(), response.length()));
                    }

                } else {
                    String subject = response.substring(0, divider);
                    String parameter = response.substring(divider + 1, response.length());
                    headerFields.put(subject, parameter);
                }

                //System.out.println(response);

            }
            s_in.close();

            if (!headerFields.isEmpty()) {
                //map.get("Strict-Transport-Security")
                String value = headerFields.get("Strict-Transport-Security");
                if (value != null) {
                    //System.out.println(value);
                    currentDomain.setIsHSTS("true");
                    String subject = "max-age=";
                    currentDomain.setIsHSTSlong(String.valueOf(isHSTSlong(value.substring(subject.length()))));
                } else {
                    currentDomain.setIsHSTS("false");
                    currentDomain.setIsHSTSlong("false");
                }
            } else {
                currentDomain.setIsHSTS("?");
                currentDomain.setIsHSTSlong("?");
            }
        } catch (InterruptedIOException e) {
            //timeout during read
            currentDomain.setIsHSTS("?");
            currentDomain.setIsHSTSlong("?");

            //System.out.println("timeout"+ currentDomain.getDomain());


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void connectionError(Domain currentDomain, String timeout) {
        currentDomain.setIsHTTPS(timeout);
        currentDomain.setSSLversion(timeout);
        currentDomain.setKeyType(timeout);
        currentDomain.setKeySize(timeout);
        currentDomain.setSignatureAlgorithm(timeout);
        currentDomain.setIsHSTS(timeout);
        currentDomain.setIsHSTSlong(timeout);
    }


    /**
     * set ssl related information about your connection
     *
     * @param sslSession
     * @param domain     currentDomain
     */
    private static void setHTTPSInfo(SSLSession sslSession, Domain domain) {

        domain.setIsHTTPS("true");
        domain.setSSLversion(sslSession.getProtocol());

        domain.setSSLversion(sslSession.getProtocol());
        javax.security.cert.X509Certificate[] certificates = new javax.security.cert.X509Certificate[0];
        try {
            certificates = sslSession.getPeerCertificateChain();
        } catch (SSLPeerUnverifiedException e) {
            e.printStackTrace();
        }

        domain.setKeyType(certificates[0].getPublicKey().getAlgorithm());


        int keySize = 0;
        if (certificates[0].getPublicKey() instanceof RSAPublicKey) {
            //System.out.println(((RSAPublicKey) certificates[0].getPublicKey()).getModulus().bitLength());
            keySize = ((RSAPublicKey) certificates[0].getPublicKey()).getModulus().bitLength();

        } else if (certificates[0].getPublicKey() instanceof ECPublicKey) {
            //System.out.println(certificates[0].getPublicKey());
            //System.out.println(((ECPublicKey) certificates[0].getPublicKey()).getParams().getOrder().bitLength());
            keySize = ((ECPublicKey) certificates[0].getPublicKey()).getParams().getOrder().bitLength();
        }

        domain.setKeySize(String.valueOf(keySize));
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

    private static String convertSignatureAlgorithm(String algorithm) {

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
        }
        return null;
    }


    /**
     * check if HSTS is longer than one month
     *
     * @param input
     * @return true/false
     */
    private static boolean isHSTSlong(String input) {
        //System.out.println(input);
        String number = input.replaceAll("[^0-9]", "");
        int maxAge = Integer.parseInt(number);
        return maxAge >= 2629744;
    }


    /**
     * Query one domain
     *
     * @param domain domain to be queried
     * @return domain with complete information
     */
    public static Domain queryOneDomain(Domain domain) {
        return query(domain);
    }
}
