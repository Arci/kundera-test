package it.polimi.kundera.web.servlet;

import it.polimi.kundera.entities.Employee;
import it.polimi.kundera.generate.Randomizable;
import it.polimi.kundera.web.Controller;
import it.polimi.kundera.web.Navigation;
import it.polimi.kundera.web.PagePath;
import it.polimi.modaclouds.cpimlibrary.entitymng.CloudEntityManager;
import it.polimi.modaclouds.cpimlibrary.mffactory.MF;
import lombok.NoArgsConstructor;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class GenerateServlet extends Controller {

    private static final long serialVersionUID = 1L;

    @Override
    protected void get(Navigation nav) throws IOException, ServletException {
        CloudEntityManager em = MF.getFactory().getEntityManagerFactory().createCloudEntityManager();
        for (Object o : generate(30, Employee.class)) {
            em.persist(o);
        }
        nav.redirect(PagePath.INDEX);
    }

    @Override
    protected void post(Navigation nav) throws IOException, ServletException {
        get(nav);
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
