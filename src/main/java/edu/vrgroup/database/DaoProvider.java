package edu.vrgroup.database;

public class DaoProvider {

	private static Dao dao = new JpaDaoImpl();

	public static Dao getDao() {
		return dao;
	}
}
