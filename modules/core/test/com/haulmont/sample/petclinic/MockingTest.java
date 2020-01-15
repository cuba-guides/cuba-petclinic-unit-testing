package com.haulmont.sample.petclinic;

import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.core.global.TimeSource;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MockingTest {

  @Mock
  private TimeSource timeSource;

  @Test
  public void theBehaviorOfTheTimeSource_canBeDefined_inATestCase() {

    // given:
    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTime yesterday = now.minusDays(1);

    // and: the timeSource Mock is configured to return yesterday when now() is called
    Mockito
        .when(timeSource.now())
        .thenReturn(yesterday);

    // when: the current time is retrieved from the TimeSource Interface
    ZonedDateTime receivedTimestamp = timeSource.now();

    // then: the received Timestamp is not now
    assertThat(receivedTimestamp)
        .isNotEqualTo(now);

    // but: the received Timestamp is yesterday
    assertThat(receivedTimestamp)
        .isEqualTo(yesterday);

  }

}