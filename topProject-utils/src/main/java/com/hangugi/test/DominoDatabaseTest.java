package com.hangugi.test;


public class DominoDatabaseTest {

	/**
	 * @param args
	 * @throws NotesException
	 */
	/*
	public static void main(String[] args) throws NotesException {
		//String ip = "175.115.94.177";
		String ip = "172.22.1.26";
		String user = "Administrator";
		String password = "qwe123";
		String journalDbName = "mail/administ.nsf";
		//String user = "mskw01";
		//String password = "ffff";
		//String journalDbName = "mail/mskw01.nsf";
		//String journalDbName = "mailjrn.nsf";

		Session session = NotesFactory.createSession(ip, user, password);
		Database database = session.getDatabase("", journalDbName);
		database.setFolderReferencesEnabled(true);

		DocumentCollection documentCollection = database.getAllDocuments();

		int count = documentCollection.getCount();
		System.out.println("count:" + count);


		for (int i = 1; i <= count; i++) {
			Document document = documentCollection.getNthDocument(i);

			getFolderReferences(document);
			//getEmbeddedObjects(document);
			//getAuthors(document);
			//			getColumnValues(document);
			//			getItems(document);
		}
	}

	@SuppressWarnings("rawtypes")
	private static void getFolderReferences(Document document) throws NotesException {
		Vector vector = document.getFolderReferences();
		if (vector.isEmpty()) {
			System.out.println("empty");
		}

		for (int j = 0; j < vector.size(); j++) {
			String mailBoxName = (String) vector.elementAt(j);
			System.out.println(document.getUniversalID() + ":" + mailBoxName);
		}
	}

	@SuppressWarnings("rawtypes")
	private static void getEmbeddedObjects(Document document) throws NotesException {
		Vector vector = document.getEmbeddedObjects();
		//Vector vector = document.getAuthors();
		for (int j = 0; j < vector.size(); j++) {
			System.out.println(vector.elementAt(j).getClass().getName() + ":" + vector.elementAt(j));
		}
	}

	@SuppressWarnings("rawtypes")
	private static void getAuthors(Document document) throws NotesException {
		Vector vector = document.getAuthors();
		for (int j = 0; j < vector.size(); j++) {
			System.out.println(vector.elementAt(j).getClass().getName() + ":" + vector.elementAt(j));
		}
	}


	@SuppressWarnings("rawtypes")
	private static void getColumnValues(Document document) throws NotesException {
		Vector vector = document.getColumnValues();
		for (int j = 0; j < vector.size(); j++) {
			System.out.println(vector.elementAt(j).getClass().getName() + ":" + vector.elementAt(j));
		}
	}



	@SuppressWarnings("rawtypes")
	private static void getItems(Document document) throws NotesException {
		Vector vector = document.getItems();
		for (int j = 0; j < vector.size(); j++) {
			//System.out.println("111=" + vector.elementAt(j).getClass().getName() + ":" + vector.elementAt(j));
			Item item = (Item) vector.elementAt(j);

			Vector vectors = item.getValues();
			for (int jj = 0; jj < vectors.size(); jj++) {
				System.out.println("222=" + vector.elementAt(j) + ":" + vectors.elementAt(jj).getClass().getName() + ":" + vectors.elementAt(jj));

			}
		}

		System.out.println("==========");
		System.out.println("==========");
		System.out.println("==========");
		System.out.println("==========");
		System.out.println("==========");
		System.out.println("==========");
	}
	 */
}
