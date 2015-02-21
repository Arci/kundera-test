package it.polimi.test.web.servlet;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.taskqueue.*;
import it.polimi.test.web.Controller;
import it.polimi.test.web.Navigation;
import it.polimi.test.web.PagePath;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import javax.servlet.ServletException;
import java.io.IOException;

@NoArgsConstructor
public class CleanServlet extends Controller {

    private static final long serialVersionUID = 1L;

    @Override
    protected void get(Navigation nav) throws IOException, ServletException {
        Query query = new Query(Entities.KIND_METADATA_KIND);
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

        for (Entity entity : datastoreService.prepare(query).asIterable()) {
            pushTask(entity.getKey().getName());
        }

        nav.redirect(PagePath.INDEX);
    }

    @Override
    protected void post(Navigation nav) throws IOException, ServletException {
        get(nav);
    }

    private void pushTask(String kind) {
        Queue defaultQueue = QueueFactory.getDefaultQueue();
        DeferredTask deferredTask = new CleanTask(kind);
        defaultQueue.add(TaskOptions.Builder.withRetryOptions(RetryOptions.Builder.withTaskRetryLimit(0)).payload(deferredTask));
    }
}

@Log
@AllArgsConstructor
class CleanTask implements DeferredTask {

    private String kind;

    @Override
    public void run() {
        log.info("deleting all entities of kind [" + kind + "]");
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(kind).setKeysOnly();
        for (Entity entity : datastore.prepare(query).asList(FetchOptions.Builder.withDefaults())) {
            datastore.delete(entity.getKey());
        }
    }
}
