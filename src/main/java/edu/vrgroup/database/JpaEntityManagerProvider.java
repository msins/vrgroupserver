package edu.vrgroup.database;

import javax.persistence.EntityManager;

public class JpaEntityManagerProvider {

  private static ThreadLocal<LocalData> local = new ThreadLocal<>();

  public static EntityManager getEntityManager() {
    LocalData ldata = local.get();
    if (ldata == null) {
      ldata = new LocalData();
      ldata.em = JpaEntityManagerFactoryProvider.getEmf().createEntityManager();
      ldata.em.getTransaction().begin();
      local.set(ldata);
    }
    return ldata.em;
  }

  public static void close() {
    LocalData ldata = local.get();
    if (ldata == null) {
      return;
    }

    try {
      ldata.em.getTransaction().commit();
    } catch (Exception ex) {
      throw new DaoException("Unable to commit transaction.", ex);
    }
    try {
      ldata.em.close();
    } catch (Exception ex) {
      throw new DaoException("Unable to close transaction.", ex);
    }

    local.remove();
  }

  private static class LocalData {

    EntityManager em;
  }

}
