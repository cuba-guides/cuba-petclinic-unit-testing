package com.haulmont.sample.petclinic.service;

import com.haulmont.cuba.core.global.validation.RequiredView;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import java.time.LocalDate;
import java.util.List;
import org.springframework.validation.annotation.Validated;

public interface RegularCheckupService {

  String NAME = "petclinic_RegularCheckupService";


  @Validated
  LocalDate calculateNextRegularCheckupDate(
      @RequiredView("pet-with-owner-and-type") Pet pet,
      List<Visit> visitHistory
  );
}