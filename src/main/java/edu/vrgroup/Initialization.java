package edu.vrgroup;

import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.database.JpaEntityManagerFactoryProvider;
import edu.vrgroup.model.Scenario;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Initialization implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("vr.server.db");
    sce.getServletContext().setAttribute("my.application.emf", emf);
    JpaEntityManagerFactoryProvider.setEmf(emf);

    if (Scenario.DEFAULT == null) {
      Scenario.DEFAULT = DaoProvider.getDao().getDefaultScenario();
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    JpaEntityManagerFactoryProvider.setEmf(null);
    EntityManagerFactory emf = (EntityManagerFactory) sce.getServletContext().getAttribute("my.application.emf");
    if (emf != null) {
      emf.close();
    }
  }

}