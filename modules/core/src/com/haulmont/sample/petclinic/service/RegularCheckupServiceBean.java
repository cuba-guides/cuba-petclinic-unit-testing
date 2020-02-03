package com.haulmont.sample.petclinic.service;

import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.service.visit.regular_checkup.calculator.NextMonthCalculator;
import com.haulmont.sample.petclinic.service.visit.regular_checkup.calculator.RegularCheckupDateCalculator;
import java.time.LocalDate;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

@Service(RegularCheckupService.NAME)
public class RegularCheckupServiceBean implements RegularCheckupService {


  final protected TimeSource timeSource;

  final protected List<RegularCheckupDateCalculator> calculators;

  @Inject
  public RegularCheckupServiceBean(
      TimeSource timeSource,
      List<RegularCheckupDateCalculator> calculators
  ) {
    this.timeSource = timeSource;
    this.calculators = calculators;
  }

  @Override
  public LocalDate calculateNextRegularCheckupDate(
      Pet pet,
      List<Visit> visitHistory
  ) {
    RegularCheckupDateCalculator calculator = calculators.stream()
        .filter(regularCheckupDateCalculator -> regularCheckupDateCalculator.supports(pet))
        .findFirst()
        .orElse(new NextMonthCalculator());

    return calculator.calculateRegularCheckupDate(pet, visitHistory, timeSource);

  }
}