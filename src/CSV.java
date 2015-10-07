import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by michal on 10/7/2015.
 * based on http://examples.javacodegeeks.com/core-java/writeread-csv-files-in-java-example/
 */
public class CSV {

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private static final String FILE_HEADER = "rank,domain,isHTTPS,SSLversion,key-type,key-size,signature-algorithm,isHSTS,isHSTSlong";


    public static void writeCSV(String fileName, ArrayList<Domain> domainList)
    {


        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADER.toString());

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            //Write a new student object list to the CSV file
            for (Domain domain : domainList) {
                //rank,domain,isHTTPS,SSLversion,key-type,key-size,signature-algorithm,isHSTS,isHSTSlong
                fileWriter.append(String.valueOf(domain.getRank()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(domain.getDomain());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(domain.getIsHSTS());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(domain.getSSLversion());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(domain.getKeyType());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(domain.getKeySize());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(domain.getSignatureAlgorithm());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(domain.getIsHSTS());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(domain.getIsHSTSlong());
                fileWriter.append(NEW_LINE_SEPARATOR);
            }


            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            try {
                if (fileWriter != null) {
                    fileWriter.flush();
                }
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }
    }

}
