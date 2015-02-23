package it.polimi.test.web.servlet;

import com.google.appengine.api.taskqueue.*;
import it.polimi.modaclouds.cpimlibrary.entitymng.CloudEntityManager;
import it.polimi.modaclouds.cpimlibrary.entitymng.migration.SeqNumberProvider;
import it.polimi.modaclouds.cpimlibrary.mffactory.MF;
import it.polimi.test.generator.RandomUtils;
import it.polimi.test.generator.Randomizable;
import it.polimi.test.generator.entities.*;
import it.polimi.test.web.Controller;
import it.polimi.test.web.Navigation;
import it.polimi.test.web.PagePath;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class GenerateServlet extends Controller {

    private static final long serialVersionUID = 1L;
    private int quantity;

    @Override
    protected void get(Navigation nav) throws IOException, ServletException {

        String quantity = nav.getRequest().getParameter("quantity");
        try {
            this.quantity = Integer.valueOf(quantity);
        } catch (Exception e) {
            nav.getRequest().setAttribute("message", "[" + quantity + "] is malformed");
            nav.fwd(PagePath.ERROR);
            return;
        }

        pushTask(Department.class, EmployeeMTO.class, GenerateTask.DependencyType.SINGLE);

        pushTask(Employee.class);

        pushTask(ProjectMTM.class, EmployeeMTM.class, GenerateTask.DependencyType.COLLECTION);

        pushTask(Phone.class, EmployeeOTO.class, GenerateTask.DependencyType.SINGLE);

        nav.redirect(PagePath.INDEX);
    }

    @Override
    protected void post(Navigation nav) throws IOException, ServletException {
        get(nav);
    }

    private void pushTask(Class master) {
        pushTask(new GenerateTask(quantity, master));
    }

    private void pushTask(Class master, Class slave, GenerateTask.DependencyType type) {
        pushTask(new GenerateTask(quantity, master, slave, type));
    }

    private void pushTask(DeferredTask deferredTask) {
        Queue defaultQueue = QueueFactory.getDefaultQueue();
        defaultQueue.add(TaskOptions.Builder.withRetryOptions(RetryOptions.Builder.withTaskRetryLimit(0)).payload(deferredTask));
    }
}

@Log
class GenerateTask implements DeferredTask {

    private static final int MAX_OFFSET = 100;
    private Class master;
    private Class slave;
    private DependencyType type;
    private int quantity;

    public enum DependencyType {
        SINGLE, COLLECTION
    }

    public GenerateTask(int quantity, Class master) {
        this.quantity = quantity;
        this.master = master;
    }

    public GenerateTask(int quantity, Class master, Class slave, DependencyType type) {
        this.quantity = quantity;
        this.master = master;
        this.slave = slave;
        this.type = type;
    }

    @Override
    public void run() {
        CloudEntityManager em = MF.getFactory().getEntityManager();
        Map<Class, List> entities = new HashMap<>();

        log.info("Generating [" + quantity + "] entities for master class [" + master.getSimpleName() + "]");
        entities.put(master, generate(quantity, master));

        setSeqNumberOffset(master.getSimpleName(), quantity);
        for (Object o : entities.get(master)) {
            em.persist(o);
        }

        if (slave != null && type != null) {
            log.info("Generating [" + quantity + "] entities for slave class [" + slave.getSimpleName() + "]");
            entities.put(slave, generate(quantity, slave, entities.get(master), type));

            setSeqNumberOffset(slave.getSimpleName(), quantity);
            for (Object o : entities.get(slave)) {
                em.persist(o);
            }
        }
    }

    private void setSeqNumberOffset(String tableName, int quantity) {
        int offset;
        if (quantity >= MAX_OFFSET || (quantity * 2) >= MAX_OFFSET) {
            offset = MAX_OFFSET;
        } else {
            offset = quantity * 2;
        }
        SeqNumberProvider.getInstance().setOffset(tableName, offset);
    }

    private List generate(int quantity, Class<? extends Randomizable> clazz) {
        List<Object> results = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            try {
                results.add(clazz.newInstance().randomize(null));
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    private List<Object> generate(int number, Class<? extends Randomizable> clazz, List dependenciesSource, DependencyType type) {
        List<Object> results = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            try {
                if (type.equals(DependencyType.SINGLE)) {
                    results.add(clazz.newInstance().randomize(dependenciesSource.get(RandomUtils.randomInt(dependenciesSource.size()))));
                } else {
                    List<Object> depList = new ArrayList<>();
                    for (int j = 0; j < RandomUtils.randomInt(); j++) {
                        depList.add(dependenciesSource.get(RandomUtils.randomInt(dependenciesSource.size())));
                    }
                    results.add(clazz.newInstance().randomize(depList));
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
