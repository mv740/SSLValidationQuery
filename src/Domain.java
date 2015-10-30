/**
 * @author Michal Wozniak id 21941097
 * @author sebastian proctor-shah id 29649727
 *
 * Date Created  on 10/7/2015.
 *
 */
public class Domain {

    //rank,domain,isHTTPS,SSLversion,key-type,key-size,signature-algorithm,isHSTS,isHSTSlong
    private int rank;
    private String domain;
    private String isHTTPS;
    private String SSLversion;
    private String keyType;
    private String keySize;
    private String signatureAlgorithm;
    private String isHSTS;
    private String isHSTSlong;

    public Domain(int rank, String domain) {
        this.rank = rank;
        this.domain = domain;
        this.isHTTPS = "?";
        this.SSLversion = "?";
        this.keyType = "?";
        this.keySize = "?";
        this.signatureAlgorithm = "?";
        this.isHSTS = "?";
        this.isHSTSlong = "?";

    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String isHTTPS() {
        return isHTTPS;
    }

    public void setIsHTTPS(String isHTTPS) {
        this.isHTTPS = isHTTPS;
    }

    public String getSSLversion() {
        return SSLversion;
    }

    public void setSSLversion(String SSLversion) {
        this.SSLversion = SSLversion;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getKeySize() {
        return keySize;
    }

    public void setKeySize(String keySize) {
        this.keySize = keySize;
    }

    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public String getIsHSTS() {
        return isHSTS;
    }

    public void setIsHSTS(String isHSTS) {
        this.isHSTS = isHSTS;
    }

    public String getIsHSTSlong() {
        return isHSTSlong;
    }

    public void setIsHSTSlong(String isHSTSlong) {
        this.isHSTSlong = isHSTSlong;
    }


    @Override
    public String toString() {
        return "Domain{" +
                "rank=" + rank +
                ", domain='" + domain + '\'' +
                ", isHTTPS=" + isHTTPS +
                ", SSLversion='" + SSLversion + '\'' +
                ", keyType='" + keyType + '\'' +
                ", keySize='" + keySize + '\'' +
                ", signatureAlgorithm='" + signatureAlgorithm + '\'' +
                ", isHSTS='" + isHSTS + '\'' +
                ", isHSTSlong='" + isHSTSlong + '\'' +
                '}';
    }
}
