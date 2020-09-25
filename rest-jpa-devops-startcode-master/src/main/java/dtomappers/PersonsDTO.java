/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtomappers; 
/**
 *
 * @author Nyxis
 */

import entities.Person;
import java.util.ArrayList;
import java.util.List;


public class PersonsDTO {
    private String fName;
    private String lName;
    private String phone;
    List<PersonDTO> all = new ArrayList();

    public PersonsDTO(List<Person> personEntities) {
        personEntities.forEach((p) -> {
            all.add(new PersonDTO(p));
        });
    }
       
}
