package com.haulmont.sample.petclinic.service;

import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import java.time.LocalDate;
import java.util.List;

public interface RegularCheckupService {

  String NAME = "petclinic_RegularCheckupService";


  LocalDate calculateNextRegularCheckupDate(
      Pet pet,
      List<Visit> vistsOfPet
  );
}