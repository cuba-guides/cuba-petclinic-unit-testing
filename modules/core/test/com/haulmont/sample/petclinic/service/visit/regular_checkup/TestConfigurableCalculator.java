package com.haulmont.sample.petclinic.service.visit.regular_checkup;

import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.service.visit.regular_checkup.calculator.RegularCheckupDateCalculator;
import java.time.LocalDate;
import java.util.List;

/**
 * test calculator implementation that allows to statically define the calculation result
 */
public class TestConfigurableCalculator implements RegularCheckupDateCalculator {

  private final boolean supports;
  private final LocalDate result;

  private TestConfigurableCalculator(boolean supports, LocalDate result) {
    this.supports = supports;
    this.result = result;
  }

  /**
   * creates a Calculator that will answer false to {@link RegularCheckupDateCalculator#supports(Pet)}
   * for test case purposes
   */
  static RegularCheckupDateCalculator notSupporting(LocalDate theoreticalDate) {
    return new TestConfigurableCalculator(false, theoreticalDate);
  }

  /**
   * creates a Calculator that will answer false to {@link RegularCheckupDateCalculator#supports(Pet)}
   * for test case purposes
   */
  static RegularCheckupDateCalculator notSupporting() {
    return new TestConfigurableCalculator(false, null);
  }

  /**
   * creates a Calculator that will answer true to {@link RegularCheckupDateCalculator#supports(Pet)}
   * for test case purposes and returns the provided date as a result
   */
  static RegularCheckupDateCalculator supportingWithDate(LocalDate date) {
    return new TestConfigurableCalculator(true, date);
  }

  @Override
  public boolean supports(Pet pet) {
    return supports;
  }

  @Override
  public LocalDate calculateRegularCheckupDate(
      Pet pet,
      List<Visit> visitHistory,
      TimeSource timeSource
  ) {
    return result;
  }
}