package it.polimi.kundera.web.servlet;

import com.google.appengine.api.taskqueue.*;
import it.polimi.kundera.generate.Randomizable;
import it.polimi.kundera.web.Controller;
import it.polimi.kundera.web.Navigation;
import it.polimi.kundera.web.PagePath;
import it.polimi.modaclouds.cpimlibrary.entitymng.CloudEntityManager;
import it.polimi.modaclouds.cpimlibrary.entitymng.PersistenceMetadata;
import it.polimi.modaclouds.cpimlibrary.mffactory.MF;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class GenerateServlet extends Controller {

    private static final long serialVersionUID = 1L;
    private static final int GEN_NUMBER = 100;

    @Override
    protected void get(Navigation nav) throws IOException, ServletException {
        DeferredTask deferredTask;
        Queue defaultQueue = QueueFactory.getDefaultQueue();
        for (String table : PersistenceMetadata.getInstance().getPersistedTables()) {
            try {
                String className = PersistenceMetadata.getInstance().getMappedClass(table);
                deferredTask = new GenerateTask(Class.forName(className), GEN_NUMBER);
                defaultQueue.add(TaskOptions.Builder.withRetryOptions(RetryOptions.Builder.withTaskRetryLimit(0)).payload(deferredTask));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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
class GenerateTask implements DeferredTask {

    private Class clazz;
    private int quantity;

    @Override
    public void run() {
        CloudEntityManager em = MF.getFactory().getEntityManager();
        log.info("Generating [" + quantity + "] entities for [" + clazz.getSimpleName() + "]");
        for (Object o : generate(quantity, clazz)) {
            em.persist(o);
        }
    }

    private List<Object> generate(int number, Class<? extends Randomizable> clazz) {
        List<Object> results = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            try {
                results.add(clazz.newInstance().randomize());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
