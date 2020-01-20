package com.haulmont.sample.petclinic.service.visit.regular_checkup.calculator;

import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("petclinic_ElectricPetTypeCalculator")
@Order(1)
public class ElectricPetTypeCalculator implements RegularCheckupDateCalculator {

  @Override
  public boolean supports(Pet pet) {
    return Objects.equals(pet.getType().getName(), "Electric");
  }


  @Override
  public LocalDate calculateRegularCheckupDate(
      Pet pet,
      List<Visit> visitHistory,
      TimeSource timeSource
  ) {

    return visitHistory.stream()
        .filter(Visit::matchesRegularCheckup)
        .map(Visit::getVisitDate)
        .max(Date::compareTo)
        .map(date -> max(
            toLocalDate(date).plusYears(1),
            nextMonth(timeSource)
        ))
        .orElseGet(() -> nextMonth(timeSource));
  }

  private LocalDate max(LocalDate first, LocalDate second) {
    return Stream.of(first, second)
        .max(LocalDate::compareTo)
        .get();
  }

  private LocalDate nextMonth(TimeSource timeSource) {
    return timeSource.now().toLocalDate().plusMonths(1);
  }


  public LocalDate toLocalDate(Date dateToConvert) {
    return dateToConvert.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate();
  }
}
