package com.daou.test;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class DirContextTest {

	public static void main(String[] args) throws NamingException, InterruptedException {
		Properties properties = new Properties();
		properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		properties.setProperty(Context.PROVIDER_URL, "ldap://175.115.94.177:389");
		properties.setProperty(Context.SECURITY_AUTHENTICATION, "simple");
		properties.setProperty(Context.SECURITY_PRINCIPAL, "CN=Administrator/O=daou");
		properties.setProperty(Context.SECURITY_CREDENTIALS, "qwe123");

		DirContext dirContext = new InitialDirContext(properties);

		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		NamingEnumeration<SearchResult> namingEnumeration = dirContext.search("", "CN=mskw01", searchControls);

		while (namingEnumeration.hasMore()) {
			if (namingEnumeration.hasMore()) {

				//SearchResult searchResult = namingEnumeration.next();
				//Attributes attrnamingEnumeration.next()tAttributes();
				//String emailAddress = (String) namingEnumeration.next().getAttributes().get("mail").get();
				//System.out.println(emailAddress);
			}
		}

		Thread.sleep(3 * 1000);

		dirContext.close();
	}
}
