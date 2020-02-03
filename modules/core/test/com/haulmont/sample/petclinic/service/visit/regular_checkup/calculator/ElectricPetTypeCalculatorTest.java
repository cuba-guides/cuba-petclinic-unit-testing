package com.haulmont.sample.petclinic.service.visit.regular_checkup.calculator;


import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.sample.petclinic.PetclinicData;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ElectricPetTypeCalculatorTest {

  private PetclinicData data;
  private RegularCheckupDateCalculator calculator;

  private Pet electricPet;

  @BeforeEach
  void createTestEnvironment() {
    data = new PetclinicData();
    calculator = new ElectricPetTypeCalculator();
  }

  @BeforeEach
  void createElectricPet() {
    electricPet = data.petWithType(data.electricType());
  }

  @Nested
  @DisplayName(
      "1. it should only be used if the name of the Pet Type " +
      "is 'Electric', otherwise not"
  )
  class Supports {

    @Test
    public void calculator_supportsPetsWithType_Electric() {
      // expect:
      assertThat(calculator.supports(electricPet))
          .isTrue();
    }

    @Test
    public void calculator_doesNotSupportsPetsWithType_Water() {
      // given:
      Pet waterPet = data.petWithType(data.waterType());
      // expect:
      assertThat(calculator.supports(waterPet))
          .isFalse();
    }
  }

  @Nested
  class CalculateRegularCheckupDate {

    @Mock
    private TimeSource timeSource;

    private final LocalDate LAST_YEAR = now().minusYears(1);
    private final LocalDate LAST_MONTH = now().minusMonths(1);
    private final LocalDate SIX_MONTHS_AGO = now().minusMonths(6);
    private final LocalDate NEXT_MONTH = now().plusMonths(1);

    private List<Visit> visits = new ArrayList<>();

    @BeforeEach
    void configureTimeSourceMockBehavior() {
      Mockito.lenient()
          .when(timeSource.now())
          .thenReturn(ZonedDateTime.now());
    }

    @Test
    @DisplayName(
        "2. the interval between two regular Checkups " +
        "is one year for electric pets"
    )
    public void intervalIsOneYear_fromTheLatestRegularCheckup() {
      // given: there are two regular checkups in the visit history of this pet
      visits.add(data.regularCheckup(LAST_YEAR));
      visits.add(data.regularCheckup(LAST_MONTH));

      // when:
      LocalDate nextRegularCheckup =
          calculate(electricPet, visits);

      // then:
      assertThat(nextRegularCheckup)
          .isEqualTo(LAST_MONTH.plusYears(1));
    }


    @Test
    @DisplayName(
        "3. Visits that are not regular checkups " +
        "should not influence the calculation"
    )
    public void onlyRegularCheckupVisitsMatter_whenCalculatingNextRegularCheckup() {

      // given: one regular checkup and one surgery
      visits.add(data.regularCheckup(SIX_MONTHS_AGO));
      visits.add(data.surgery(LAST_MONTH));

      // when:
      LocalDate nextRegularCheckup =
          calculate(electricPet, visits);

      // then: the date of the last checkup is used
      assertThat(nextRegularCheckup)
          .isEqualTo(SIX_MONTHS_AGO.plusYears(1));
    }


    @Test
    @DisplayName(
        "4. in case the Pet has not done a regular checkup " +
        "at the Petclinic before, next month should be proposed"
    )
    public void ifThePetDidNotHavePreviousCheckups_nextMonthIsProposed() {

      // given: there is no regular checkup, just a surgery
      visits.add(data.surgery(LAST_MONTH));

      // when:
      LocalDate nextRegularCheckup =
          calculate(electricPet, visits);

      // then:
      assertThat(nextRegularCheckup)
          .isEqualTo(NEXT_MONTH);
    }


    @Test
    @DisplayName(
        "5. if the last Regular Checkup was performed longer than " +
        "one year ago, next month should be proposed"
    )
    public void ifARegularCheckup_exceedsTheInterval_nextMonthIsProposed() {

      // given: one regular checkup thirteen month ago
      visits.add(data.regularCheckup(LAST_YEAR.minusMonths(1)));

      // when:
      LocalDate nextRegularCheckup =
          calculate(electricPet, visits);

      // then:
      assertThat(nextRegularCheckup)
          .isEqualTo(NEXT_MONTH);
    }

    private LocalDate calculate(Pet pet, List<Visit> visitHistory) {
      return calculator.calculateRegularCheckupDate(
          pet,
          visitHistory,
          timeSource
      );
    }
  }
}