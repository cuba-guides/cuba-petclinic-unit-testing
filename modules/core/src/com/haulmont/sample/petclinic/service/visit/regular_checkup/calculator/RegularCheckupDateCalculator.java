package com.haulmont.sample.petclinic.service.visit.regular_checkup.calculator;

import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import java.time.LocalDate;
import java.util.List;


/**
 * API for Calculators that calculates a proposal date for the next regular checkup date
 */
public interface RegularCheckupDateCalculator {

  /**
   * defines if a calculator supports a Pet instance
   * @param pet the Pet to calculate the checkup date for
   * @return true if the calculator supports this pet, otherwise false
   */
  boolean supports(Pet pet);

  /**
   * calculates the next regular checkup date for the Pet
   * @param pet the Pet to calculate checkup date for
   * @param visitHistory the visit history of that Pet
   * @param timeSource the TimeSource CUBA API
   *
   * @return the calculated regular checkup date
   */
  LocalDate calculateRegularCheckupDate(
      Pet pet,
      List<Visit> visitHistory,
      TimeSource timeSource
  );
}
