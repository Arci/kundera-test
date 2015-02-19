package it.polimi.test.web.servlet;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.taskqueue.*;
import it.polimi.modaclouds.cpimlibrary.entitymng.PersistenceMetadata;
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
        for (String table : PersistenceMetadata.getInstance().getPersistedTables()) {
            pushTask(table);
        }
        // fix for join table
        pushTask("EMPLOYEE_PROJECT");
        // YCSB table
        pushTask("usertable");
        nav.redirect(PagePath.INDEX);
    }

    @Override
    protected void post(Navigation nav) throws IOException, ServletException {
        get(nav);
    }

    private void pushTask(String table) {
        Queue defaultQueue = QueueFactory.getDefaultQueue();
        DeferredTask deferredTask = new CleanTask(table);
        defaultQueue.add(TaskOptions.Builder.withRetryOptions(RetryOptions.Builder.withTaskRetryLimit(0)).payload(deferredTask));
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
