import java.util.concurrent.Callable;

/**
 * Created by micha on 11/1/2015.
 */
public class WorkerThread implements Callable<Domain> {
    private Domain domain;
    private String filename;
    public WorkerThread(Domain domain, String fileName){
        this.domain = domain;
        this.filename = fileName;
    }

    @Override
    public Domain call() throws Exception {
        DomainDataParser parser = new DomainDataParser(filename);
        return parser.queryOneDomain(this.domain);
    }
}