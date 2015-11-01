import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by michal on 11/1/2015.
 */
public class MultiThreadQueryProcess {

    private static MultiThreadQueryProcess instance;
    private static int availableProcessors;

    private MultiThreadQueryProcess() {
    }

    public static MultiThreadQueryProcess getInstance() {
        if (instance == null) {
            instance = new MultiThreadQueryProcess();
            availableProcessors = Runtime.getRuntime().availableProcessors();
        }
        return instance;
    }


    public void start(ArrayList<Domain> csvList, String filename) {

        long startTime = System.currentTimeMillis();
        //int availableProcessors = Runtime.getRuntime().availableProcessors();

        //System.out.println(availableProcessors);

        List<Domain> finalList = new ArrayList<>();


        //create pool of threads
        ExecutorService executor = Executors.newFixedThreadPool(availableProcessors);

        // each worker will execute one query
        List<Future<Domain>> resultList = new ArrayList<>();
        for (Domain currentDomain : csvList) {

            //send domain to be queried to the worker
            WorkerThread worker = new WorkerThread(currentDomain, filename);
            Future<Domain> future = executor.submit(worker);
            resultList.add(future);

        }

        for (Future<Domain> future : resultList) {
            try {
                //receive the
                finalList.add(future.get());
                System.out.println("DONE: " + future.isDone());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }

        Collections.sort(finalList);

        String sortfile = "sort.csv";

        CSV.createFile(sortfile);
        CSV.writeToFile(sortfile, finalList);
        System.out.println(finalList.toString());
        executor.shutdown();
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;


        System.out.println("Finished all threads");
        System.out.println(elapsedTime);
    }


}
