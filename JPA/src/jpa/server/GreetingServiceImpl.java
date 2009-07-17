package jpa.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import jpa.client.GreetingService;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/*
 * * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

  public String greetServer(String input) {
    Person p = new Person("ford", "prefect");
    Contact c = new Contact("ford@zaphod.net");
    p.getContacts().add(c);

    EntityManager em = EMF.get().createEntityManager();
    //    EntityTransaction transaction = em.getTransaction();
    //    transaction.begin();
    //    em.persist(p);
    //    //    em.flush();
    //    transaction.commit();

    Query query;
    List results;
    query = em.createQuery("SELECT p from Person p");
    results = query.getResultList();
    p = (Person) results.get(0);

    query = em.createQuery("SELECT c from Contact c");
    results = query.getResultList();
    c = (Contact) results.get(0);

    query = em.createQuery("SELECT c from Contact c WHERE c.key = '" + c.getKey() + "'");
    results = query.getResultList();
    c = (Contact) results.get(0);

    String serverInfo = getServletContext().getServerInfo();
    String userAgent = getThreadLocalRequest().getHeader("User-Agent");
    return "Hello, " + input + "!<br><br>I am running " + serverInfo
        + ".<br><br>It looks like you are using:<br>" + userAgent + "<br><br>CONTACTS COUNT = "
        + results.size() + "<br>CONTACT KEY = " + c.getKey() + "<br><br>PERSON KEY = " + p.getId();
  }
}
