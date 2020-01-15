package com.haulmont.sample.petclinic.service.visit.regular_checkup;

import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.pet.PetType;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


/**
 * PetclinicData represents an API abstraction for the Regular Checkup Service use-case
 */
public class PetclinicData {


  public Visit regularCheckup(LocalDate visitDate) {
    Visit visit = new Visit();
    visit.setDescription("Regular Checkup");
    visit.setVisitDate(toDate(visitDate));
    return visit;
  }

  public Visit surgery(LocalDate surgeryDate) {
    Visit visit = new Visit();
    visit.setDescription("Surgery");
    visit.setVisitDate(toDate(surgeryDate));
    return visit;
  }


  public Pet petWithType(PetType electricType) {
    Pet electricPet = new Pet();
    electricPet.setType(electricType);
    return electricPet;
  }

  public PetType electricType() {
    PetType type = new PetType();
    type.setName("Electric");
    return type;
  }
  public PetType waterType() {
    PetType type = new PetType();
    type.setName("Water");
    return type;
  }

  public PetType fireType() {
    PetType type = new PetType();
    type.setName("Fire");
    return type;
  }


  private Date toDate(LocalDate dateToConvert) {
    return Date.from(dateToConvert.atStartOfDay()
        .atZone(ZoneId.systemDefault())
        .toInstant());
  }

}