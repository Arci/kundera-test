package it.polimi.kundera.web.servlet;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.taskqueue.*;
import it.polimi.kundera.web.Controller;
import it.polimi.kundera.web.Navigation;
import it.polimi.kundera.web.PagePath;
import it.polimi.modaclouds.cpimlibrary.entitymng.PersistenceMetadata;
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
        DeferredTask deferredTask;
        Queue defaultQueue = QueueFactory.getDefaultQueue();
        for (String table : PersistenceMetadata.getInstance().getPersistedTables()) {
            deferredTask = new CleanTask(table);
            defaultQueue.add(TaskOptions.Builder.withRetryOptions(RetryOptions.Builder.withTaskRetryLimit(0)).payload(deferredTask));
        }
        nav.redirect(PagePath.INDEX);
    }

    @Override
    protected void post(Navigation nav) throws IOException, ServletException {
        get(nav);
    }
}

@Log
@AllArgsConstructor
class CleanTask implements DeferredTask {

    private String table;

    @Override
    public void run() {
        log.info("deleting all entities from table [" + table + "]");
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(table).setKeysOnly();
        for (Entity entity : datastore.prepare(query).asList(FetchOptions.Builder.withDefaults())) {
            log.info("kind= [" + entity.getKind() + "], key =[" + entity.getKey() + "]");
            datastore.delete(entity.getKey());
        }
    }
}
