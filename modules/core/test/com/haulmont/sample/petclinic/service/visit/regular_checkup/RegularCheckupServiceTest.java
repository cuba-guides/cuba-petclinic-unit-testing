package com.haulmont.sample.petclinic.service.visit.regular_checkup;

import static com.haulmont.sample.petclinic.service.visit.regular_checkup.ConfigurableTestCalculator.notSupporting;
import static com.haulmont.sample.petclinic.service.visit.regular_checkup.ConfigurableTestCalculator.supportingWithDate;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.sample.petclinic.PetclinicData;
import com.haulmont.sample.petclinic.service.RegularCheckupService;
import com.haulmont.sample.petclinic.service.RegularCheckupServiceBean;
import com.haulmont.sample.petclinic.service.visit.regular_checkup.calculator.RegularCheckupDateCalculator;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegularCheckupServiceTest {

  private final LocalDate LAST_YEAR = now().minusYears(1);
  private final LocalDate THREE_MONTHS_AGO = now().minusMonths(3);
  private final LocalDate TWO_MONTHS_AGO = now().minusMonths(2);
  private final LocalDate ONE_MONTHS_AGO = now().minusMonths(1);
  private final LocalDate NEXT_MONTH = now().plusMonths(1);

  @Mock
  private TimeSource timeSource;

  private PetclinicData data = new PetclinicData();

  @BeforeEach
  void configureTimeSourceToReturnNowCorrectly() {

    Mockito.lenient()
        .when(timeSource.now())
        .thenReturn(ZonedDateTime.now());

  }

  @Test
  @DisplayName(
      "1. only Calculators that support the Pet instance " +
      "will be asked to calculate the checkup date"
  )
  public void one_supportingCalculator_thisOneIsChosen() {

    // given: first calculator does not support the Pet
    RegularCheckupDateCalculator threeMonthCalculator =
        notSupporting(THREE_MONTHS_AGO);

    // and: second calculator supports the Pet
    RegularCheckupDateCalculator lastYearCalculator =
        supportingWithDate(LAST_YEAR);

    // when:
    LocalDate nextRegularCheckup = calculate(
        calculators(threeMonthCalculator, lastYearCalculator)
    );

    // then: the result should be the result that the calculator that was supported
    assertThat(nextRegularCheckup)
        .isEqualTo(LAST_YEAR);
  }


  @Test
  @DisplayName(
      "2. in case multiple calculators support a Pet," +
      " the first one is chosen to calculate"
  )
  public void multiple_supportingCalculators_theFirstOneIsChosen() {

    // given: two calculators support the Pet
    //   the order within the list is used for fetching the first suitable calculator
    List<RegularCheckupDateCalculator> calculators = calculators(
        supportingWithDate(ONE_MONTHS_AGO),
        notSupporting(THREE_MONTHS_AGO),
        supportingWithDate(TWO_MONTHS_AGO)
    );

    // when:
    LocalDate nextRegularCheckup = calculate(calculators);

    // then: the result is the one from the first calculator
    assertThat(nextRegularCheckup)
        .isEqualTo(ONE_MONTHS_AGO);
  }

  @Test
  @DisplayName(
      "3. in case no Calculator was found, " +
      "next month as the proposed regular checkup date will be used"
  )
  public void no_supportingCalculators_nextMonthWillBeReturned() {

    // given: only not-supporting calculators are available for the Pet
    List<RegularCheckupDateCalculator> onlyNotSupportingCalculators = calculators(
        notSupporting(ONE_MONTHS_AGO)
    );

    // when:
    LocalDate nextRegularCheckup = calculate(onlyNotSupportingCalculators);

    // then: the default implementation will return next month
    assertThat(nextRegularCheckup)
        .isEqualTo(NEXT_MONTH);
  }

  /*
   * instantiates the SUT with the provided calculators as dependencies
   * and executes the calculation
   */
  private LocalDate calculate(List<RegularCheckupDateCalculator> calculators) {
    RegularCheckupService service = new RegularCheckupServiceBean(
        timeSource,
        calculators
    );

    return service.calculateNextRegularCheckupDate(
        data.petWithType(data.waterType()),
        Lists.emptyList()
    );
  }

  private List<RegularCheckupDateCalculator> calculators(
      RegularCheckupDateCalculator... calculators
  ) {
    return Stream
        .of(calculators)
        .collect(Collectors.toList());
  }

  /*
  @Test
  public void forAnyOtherPetType_intervalIsNineMonths_fromTheLatestRegularCheckup() {

    // given:
    Pet waterPet = data.petWithType(data.waterType());

    // and: there are two regular checkups in the visit history of this pet
    visits.add(data.regularCheckup(LAST_YEAR));
    visits.add(data.regularCheckup(THREE_MONTHS_AGO));

    // when:
    LocalDate nextRegularCheckup =
        regularCheckupService.calculateNextRegularCheckupDate(waterPet, visits);

    // then:
    assertThat(nextRegularCheckup)
        .isEqualTo(THREE_MONTHS_AGO.plusMonths(9));
  }
   */


}
