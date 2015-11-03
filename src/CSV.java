import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Michal Wozniak id 21941097
 * @author sebastian proctor-shah id 29649727
 *
 * Date Created  on 10/7/2015.
 * Date Updated : 10/30/2015
 *
 * based on http://examples.javacodegeeks.com/core-java/writeread-csv-files-in-java-example/
 *
 * CSV
 *
 * permit to read cvs file and create them
 */
public class CSV {

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private static final String FILE_HEADER = "rank,domain,isHTTPS,SSLversion,key-type,key-size,signature-algorithm,isHSTS,isHSTSlong";

    //Domain CSV attributes index
    private static final int RANK_ID = 0;
    private static final int DOMAIN_URL = 1;


    /**
     *
     * Create a new csv file
     * @param fileName csv file name
     */
    public static void createFile(String fileName) {


        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADER);

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

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

    /**
     * Write domain's information to the specified file
     *
     * @param fileName name of csv file
     * @param domain current domain to be used
     */
    public static void writeToFile(String fileName, Domain domain) {

        //Write a new domain object list to the CSV file


        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName, true);

            //rank,domain,isHTTPS,SSLversion,key-type,key-size,signature-algorithm,isHSTS,isHSTSlong
            fileWriter.append(String.valueOf(domain.getRank()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(domain.getDomain());
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(domain.isHTTPS());
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

    /**
     *
     * Write domain's information to the specified file
     *  @param fileName name of the csv file
     * @param domains List of domains
     */
    public static void writeToFile(String fileName, List<Domain> domains) {

        //Write a new domain object list to the CSV file


        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName, true);

            for (Domain domain : domains)
            {
                //rank,domain,isHTTPS,SSLversion,key-type,key-size,signature-algorithm,isHSTS,isHSTSlong
                fileWriter.append(String.valueOf(domain.getRank()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(domain.getDomain());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(domain.isHTTPS());
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



    /**
     * for testing read function
     *  read only 10 row
     *
     * @param fileName file to read
     * @param startIndex which row to start reading
     * @return domain list of the selected row
     */
    public ArrayList<Domain> readDomainFileTest(String fileName, int startIndex) {

        int stopIndex = startIndex + 10;
        BufferedReader fileReader = null;

        //Create a new list of Domain to be filled by CSV file data
        ArrayList<Domain> domainList = new ArrayList<>();

        try {

            String line = "";

            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileName));

            //Read the CSV file header to skip it
            fileReader.readLine();

            //Read the file line by line starting from the second line
            while ((line = fileReader.readLine()) != null) {
                //Get all tokens available in line
                String[] tokens = line.split(COMMA_DELIMITER);
                if (tokens.length > 0) {
                    //Create a new domain object and fill his  data
                    if (Integer.parseInt(tokens[RANK_ID]) >= startIndex || Integer.parseInt(tokens[RANK_ID]) < stopIndex) {
                        Domain newDomain = new Domain(Integer.parseInt(tokens[RANK_ID]), tokens[DOMAIN_URL]);
                        domainList.add(newDomain);
                    }

                }
            }

            //Print the new domain list
            for (Domain domain : domainList) {
                System.out.println(domain.toString());
            }


        } catch (Exception e) {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader !!!");
                e.printStackTrace();
            }
        }
        return domainList;
    }


    private static int getStartIndex(int studentId) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(new Integer(studentId).toString().getBytes());
        BigInteger bi = new BigInteger(1, md.digest());
        return bi.mod(new BigInteger("9890"))
                .multiply(new BigInteger("100"))
                .intValue() + 1000;
    }


    /**
     * read the csv file and get only the information required by the studentID
     * for debugging purpose only
     * eg: howmany = 10; will get only 10 domains
     *
     * @param fileName  cvs input file
     * @param studentId student id
     * @param HowMany the number of domains you want to get
     * @return list of required domains
     */
    public static ArrayList<Domain> readFileStudentTest(String fileName, int studentId, int HowMany) {

        int startIndex = 0;
        try {
            startIndex = getStartIndex(studentId);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        int stopIndex = startIndex + HowMany;
        BufferedReader fileReader = null;

        //Create a new list of Domain to be filled by CSV file data
        ArrayList<Domain> domainList = new ArrayList<>();

        try {


            String line = "";

            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileName));


            //Read the file line by line starting from the second line
            while ((line = fileReader.readLine()) != null) {
                //Get all tokens available in line
                String[] tokens = line.split(COMMA_DELIMITER);
                if (tokens.length > 0) {
                    //Create a new student object and fill his  data
                    if (Integer.parseInt(tokens[0]) >= startIndex && Integer.parseInt(tokens[0]) < stopIndex) {
                        Domain newDomain = new Domain(Integer.parseInt(tokens[0]), tokens[1]);
                        domainList.add(newDomain);
                    }

                }
            }

            //Print the new student list
            for (Domain domain : domainList) {
                System.out.println(domain.toString());
            }
        } catch (Exception e) {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader !!!");
                e.printStackTrace();
            }
        }

        return domainList;
    }

    /**
     * read the csv file and get the first 1000 domains and the one required by student 1 and student 2
     *
     * @param fileName   cvs input file
     * @param student1Id first student id
     * @param student2Id second student id
     * @return list of required domains
     */
    public static ArrayList<Domain> readFileCustomIndex(String fileName, int student1Id, int student2Id) {

        int startIndex = 1;
        int startIndexStudent1 = 0;
        int startIndexStudent2 = 0;
        try {
            startIndexStudent1 = getStartIndex(student1Id);
            startIndexStudent2 = getStartIndex(student2Id);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        int stopIndex = startIndex + 1000;
        int stopIndexStudent1 = startIndexStudent1 + 10000;
        int stopIndexStudent2 = startIndexStudent2 + 10000;


        BufferedReader fileReader = null;

        //Create a new list of Domain to be filled by CSV file data
        ArrayList<Domain> domainList = new ArrayList<>();

        try {


            String line;

            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileName));


            //Read the file line by line starting from the second line
            while ((line = fileReader.readLine()) != null) {
                //Get all tokens available in line
                String[] tokens = line.split(COMMA_DELIMITER);
                if (tokens.length > 0) {
                    //Create a new student object and fill his  data
                    if ((Integer.parseInt(tokens[0]) >= startIndex && Integer.parseInt(tokens[0]) < stopIndex) ||
                            (Integer.parseInt(tokens[0]) >= startIndexStudent1 && Integer.parseInt(tokens[0]) < stopIndexStudent1) ||
                            (Integer.parseInt(tokens[0]) >= startIndexStudent2 && Integer.parseInt(tokens[0]) < stopIndexStudent2)) {
                        Domain newDomain = new Domain(Integer.parseInt(tokens[0]), tokens[1]);
                        domainList.add(newDomain);
                    }

                }
            }

            //Print the new student list
            for (Domain domain : domainList) {
                System.out.println(domain.toString());
            }
        } catch (Exception e) {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader !!!");
                e.printStackTrace();
            }
        }

        return domainList;
    }
}
