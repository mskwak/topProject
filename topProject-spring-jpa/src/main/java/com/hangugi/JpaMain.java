package com.hangugi;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

	public static void main(String[] args) {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpabook");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();
		logic(entityManager);
		entityTransaction.commit();

		entityManager.close();
		entityManagerFactory.close();
	}


	public static void logic(EntityManager entityManager) {
		String id = "mskw01";
		String username = "행국이";
		int age = 39;

		Member member = new Member();
		member.setId(id);
		member.setUsername(username);
		member.setAge(age);

		entityManager.persist(member);
		member.setAge(40);
	}
}
