import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by michal on 11/1/2015.
 *
 * Multithreaded query process
 * 1- create a pool of available thread
 * 2- create a # number of worker thread depending on the number of domain to be queried
 * 3-  1 worker will query 1 domain
 * 4- return queried domain object into a list
 * 5- when all domains were queried, sort the list than contain them by their rank
 * 6- write the list to the specified file
 */
public class MultiThreadedQueryProcess {

    private static MultiThreadedQueryProcess instance;
    private static int availableProcessors;

    private MultiThreadedQueryProcess() {
    }

    //only one instance of this
    public static MultiThreadedQueryProcess getInstance() {
        if (instance == null) {
            instance = new MultiThreadedQueryProcess();
            availableProcessors = Runtime.getRuntime().availableProcessors();
        }
        return instance;
    }


    /**
     * Start parsing process
     *
     * @param csvList list of domain to be parse
     * @param filename data saved
     * @param ThreadPool set custom value, write 0 for default value of availableProcessors*2
     */
    public void start(ArrayList<Domain> csvList, String filename, int ThreadPool) {

        //track the time
        long startTime = System.nanoTime();

        //System.out.println(availableProcessors);

        List<Domain> finalList = new ArrayList<>();

        int nThreads;
        //create pool of threads
        if(ThreadPool ==0)
        {
            nThreads = availableProcessors*2; //create double because of context switching
            //you can increase your performance by adding 1 more until you get a max
        }else
            nThreads = ThreadPool;

        ExecutorService executor = Executors.newFixedThreadPool(nThreads);

        // each worker will execute one query
        List<Future<Domain>> resultList = new ArrayList<>();
        for (Domain currentDomain : csvList) {

            //send domain to be queried to the worker
            WorkerThread worker = new WorkerThread(currentDomain);
            Future<Domain> future = executor.submit(worker);
            resultList.add(future);

        }

        for (Future<Domain> future : resultList) {
            try {
                Domain domain = future.get();
                finalList.add(domain);
                System.out.println("domain rank:" + domain.getRank() +" complete");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }

        //Collections.sort(finalList);

        CSV.writeToFile(filename, finalList);
        executor.shutdown();


        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;
        long ElapsedTimeInSeconds = TimeUnit.SECONDS.convert(elapsedTime,TimeUnit.NANOSECONDS);


        System.out.println("Multi-threaded Query Process complete ! ");
        System.out.println("Elapsed time : "+ElapsedTimeInSeconds +"seconds");
    }


}
