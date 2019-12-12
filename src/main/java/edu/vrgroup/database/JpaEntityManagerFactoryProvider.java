package edu.vrgroup.database;

import javax.persistence.EntityManagerFactory;

public class JpaEntityManagerFactoryProvider {

	private static EntityManagerFactory INSTANCE;

	private JpaEntityManagerFactoryProvider() {
	}

	public static EntityManagerFactory getEmf() {
		if (INSTANCE == null) {
			throw new IllegalStateException("EntityManagerFactory not initialised.");
		}
		return INSTANCE;
	}

	public static void setEmf(EntityManagerFactory emf) {
		JpaEntityManagerFactoryProvider.INSTANCE = emf;
	}
}
