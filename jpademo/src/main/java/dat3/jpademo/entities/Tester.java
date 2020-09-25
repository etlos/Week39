
package dat3.jpademo.entities;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author Nyxis
 */
public class Tester {
    public static void main(String[] args) {
        
    
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        
        Person p1 = new Person("Jønke",1963);
        Person p2 = new Person("Blondie",1959);
        
        Address a1 = new Address("Store Torv 1",3232,"nr snede");
        Address a2 = new Address("Langgade 34",1212,"Valby");
        
        p1.setAddress(a1);
        p2.setAddress(a2);
        
        Fee f1 = new Fee(100);
        Fee f2 = new Fee(200);
        Fee f3 = new Fee(300);
        
        
        p1.addFee(f1);
        p1.addFee(f3);
        p2.addFee(f2);
        
        SwimStyle s1 = new SwimStyle("Crawl");
        SwimStyle s2 = new SwimStyle("Butterfly");        
        SwimStyle s3 = new SwimStyle("Breast Stroke");
        
        p1.AddSwimStyle(s1);
        p1.AddSwimStyle(s3);
        p2.AddSwimStyle(s2);

        
        
        em.getTransaction().begin();
        em.persist(p1);
        em.persist(p2);
        em.getTransaction().commit();
        
        
        em.getTransaction().begin();
        p1.removeSwimStyle(s3);
        em.getTransaction().commit();
        
        System.out.println("p1: " + p1.getP_id() + ", " + p1.getName());
        System.out.println("p2: " + p2.getP_id() + ", " + p2.getName());
        
        System.out.println("Jønkes gade: " + p1.getAddress().getStreet());
        
        System.out.println("Lad os se 2 vejs virker: " + a1.getPerson().getName());
        
        System.out.println("HVem har betalt f2? Det har: " + f2.getPerson().getName());
        
        System.out.println("Hvad har Jønke betalt?");
        
        TypedQuery<Fee> q1 = em.createQuery("SELECT f FROM Fee f",Fee.class);
        List<Fee> fees = q1.getResultList();
        
        for(Fee f : fees){
            System.out.println(f.getPerson().getName() + ": " + f.getAmount()+ " kr. Den: " + f.getPaydate() + "Adr: " + f.getPerson().getAddress().getCity());
        }
        
    }
}
