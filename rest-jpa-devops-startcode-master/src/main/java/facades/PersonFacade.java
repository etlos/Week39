package facades;

import dtomappers.PersonDTO;
import dtomappers.PersonsDTO;
import entities.Address;
import entities.Person;
import exceptions.PersonNotFoundException;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;


public class PersonFacade implements IPersonFacade{
    
    private static PersonFacade instance;
    private static EntityManagerFactory emf;
    
    private PersonFacade(){}
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getFacadeExample(EntityManagerFactory _emf){
        if(instance == null){
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }
    
    private EntityManager getEntityManager(){
        return emf.createEntityManager();
    }

    @Override
    public PersonDTO addPerson(String fName, String lName, String phone, String street, int zip, String city) {
        EntityManager em = emf.createEntityManager();
        Person person = new Person(fName, lName, phone);

        try {
            em.getTransaction().begin();
            Query q = em.createQuery("SELECT a FROM Address a WHERE a.street = :street AND a.zip = :zip AND a.city = :city");
            q.setParameter("street", street);
            q.setParameter("zip", zip);
            q.setParameter("city", city);
            List<Address> adressess = q.getResultList();
            if(adressess.size() > 0){
                person.setAddress(adressess.get(0));
            }else {
                person.setAddress(new Address(street, zip, city));
            }
            em.persist(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(person);
    }

    @Override
    public PersonDTO deletePerson(int id) throws PersonNotFoundException{
        EntityManager em = emf.createEntityManager();
        Person p = em.find(Person.class, id);
        if(p == null){
            throw new PersonNotFoundException("Could not delete, provided id does not exist");
        }
        Address address = p.getAddress();
        Query q = em.createQuery("SELECT p FROM Person p WHERE p.address.id = :id");
        q.setParameter("id", id);
        
        try {
            em.getTransaction().begin();
            em.remove(p);
            if(q.getResultList().size() < 1){
            em.remove(address);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(p);
    }

    @Override
    public PersonDTO getPerson(int id) throws PersonNotFoundException{
        EntityManager em = getEntityManager();
        
        try {
            TypedQuery<Person> q = em.createQuery("SELECT p FROM Person p WHERE p.id = :id", Person.class);
            q.setParameter("id", id);
            PersonDTO person = new PersonDTO(q.getSingleResult());
            return person;
            }
        catch(Exception e){
            throw new PersonNotFoundException("No person with provided id found");
        } finally {
            em.close();
        }
    }

    @Override
    public PersonsDTO getAllPersons() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Person> q = em.createQuery("SELECT p FROM Person p", Person.class);
            PersonsDTO all = new PersonsDTO(q.getResultList());
            return all;
        } finally {
            em.close();
        }
    }

    
    @Override
    public PersonDTO editPerson(PersonDTO p) throws PersonNotFoundException{
        EntityManager em = emf.createEntityManager();
        Person person = em.find(Person.class, p.getId());
        if (person == null) {
            throw new PersonNotFoundException("Person with ID: " + p.getId() + " not found");
        }
        person.setFirstName(p.getfName());
        person.setLastName(p.getlName());
        person.setPhone(p.getPhone());
        person.setLastEdited(new Date());

        try {
            em.getTransaction().begin();
            em.merge(person);
            em.getTransaction().commit();
            return new PersonDTO(person);
        } finally {
            em.close();
        }    
    }
    
    

    
}
