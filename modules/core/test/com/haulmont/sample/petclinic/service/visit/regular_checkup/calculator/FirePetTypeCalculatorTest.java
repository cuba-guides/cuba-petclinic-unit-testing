package com.haulmont.sample.petclinic.service.visit.regular_checkup.calculator;


import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.service.visit.regular_checkup.PetclinicData;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FirePetTypeCalculatorTest {

  @Mock
  private TimeSource timeSource;

  private Pet firePet;

  private PetclinicData data = new PetclinicData();

  private RegularCheckupDateCalculator calculator = new FirePetTypeCalculator();


  @BeforeEach
  void createFirePet() {
    firePet = data.petWithType(data.fireType());
  }

  @Nested
  class Supports {

    @Test
    public void calculator_supportsPetsWithType_Fire() {
      // expect:
      assertThat(calculator.supports(firePet))
          .isTrue();
    }

    @Test
    public void calculator_doesNotSupportsPetsWithType_Water() {
      // when:
      Pet waterPet = data.petWithType(data.waterType());
      // then:
      assertThat(calculator.supports(waterPet))
          .isFalse();
    }

  }


  @Nested
  class CalculateRegularCheckupDate {


    private final LocalDate LAST_YEAR = now().minusYears(1);
    private final LocalDate LAST_MONTH = now().minusMonths(1);
    private final LocalDate SIX_MONTHS_AGO = now().minusMonths(6);
    private final LocalDate NEXT_MONTH = now().plusMonths(1);
    private final LocalDate TWO_MONTHS_AGO = now().minusMonths(2);

    private List<Visit> visits = new ArrayList<>();

    @BeforeEach
    void configureTimeSourceMockBehavior() {
      Mockito.lenient()
          .when(timeSource.now())
          .thenReturn(ZonedDateTime.now());
    }

    @Test
    public void forAFirePet_intervalIsSixMonths_fromTheLatestRegularCheckup() {

      // given:
      visits.add(data.regularCheckup(LAST_YEAR));
      visits.add(data.regularCheckup(TWO_MONTHS_AGO));
      visits.add(data.regularCheckup(SIX_MONTHS_AGO));

      // when:
      LocalDate nextRegularCheckup =
          calculator.calculateRegularCheckupDate(firePet, visits, timeSource);

      // then:
      assertThat(nextRegularCheckup)
          .isEqualTo(TWO_MONTHS_AGO.plusMonths(6));
    }


    @Test
    public void onlyRegularCheckupVisits_areTakenIntoConsideration_whenCalculatingNextRegularCheckup() {

      // given: there are two regular checkups in the visit history of this pet
      visits.add(data.regularCheckup(SIX_MONTHS_AGO));

      // and: a non-regular checkup happened last month
      visits.add(data.surgery(LAST_MONTH));

      // when:
      LocalDate nextRegularCheckup =
          calculator.calculateRegularCheckupDate(firePet, visits, timeSource);

      // then: the date of the last checkup is used
      assertThat(nextRegularCheckup)
          .isEqualTo(SIX_MONTHS_AGO.plusMonths(6));
    }


    @Test
    public void ifThePetDidNotHavePreviousCheckups_nextMonthIsProposed() {

      // given: there are two regular checkups in the visit history of this pet
      visits.add(data.surgery(LAST_MONTH));

      // when:
      LocalDate nextRegularCheckup =
          calculator.calculateRegularCheckupDate(firePet, visits, timeSource);

      // then:
      assertThat(nextRegularCheckup)
          .isEqualTo(NEXT_MONTH);
    }

  }

}