import java.util.concurrent.Callable;

/**
 * Created by michal on 11/1/2015.
 *
 * Worker thread
 *
 *  execute one domain query and return the data
 */
public class WorkerThread implements Callable<Domain> {
    private Domain domain;
    public WorkerThread(Domain domain){
        this.domain = domain;
    }

    @Override
    public Domain call() throws Exception {
        return DomainDataParser.queryOneDomain(this.domain);
    }
}