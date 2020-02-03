package com.haulmont.sample.petclinic.web.visit.visit;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.screen.DialogMode;
import com.haulmont.cuba.gui.screen.EditedEntityContainer;
import com.haulmont.cuba.gui.screen.LoadDataBeforeShow;
import com.haulmont.cuba.gui.screen.MessageBundle;
import com.haulmont.cuba.gui.screen.StandardEditor;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.service.RegularCheckupService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

@UiController("petclinic_Visit.createRegularCheckup")
@UiDescriptor("visit-create-regular-checkup.xml")
@EditedEntityContainer("visitDc")
@LoadDataBeforeShow
public class VisitCreateRegularCheckup extends StandardEditor<Visit> {

  @Inject
  protected RegularCheckupService regularCheckupService;
  @Inject
  protected DataManager dataManager;
  @Inject
  protected DateField<Date> visitDateField;

  @Subscribe
  protected void onInitEntity(InitEntityEvent<Visit> event) {
    Visit visit = event.getEntity();
    visit.setDescription("Regular Checkup");

    visit.setVisitDate(
        proposeRegularCheckupDate(
            getEditedEntity().getPet()
        )
    );
  }


  protected Date proposeRegularCheckupDate(Pet pet) {

    LocalDate nextRegularCheckupDate = regularCheckupService.calculateNextRegularCheckupDate(
        pet,
        visitHistory(pet)
    );

    return toDate(nextRegularCheckupDate);

  }

  public Date toDate(LocalDate dateToConvert) {
    return java.util.Date.from(dateToConvert.atStartOfDay()
        .atZone(ZoneId.systemDefault())
        .toInstant());
  }

  private List<Visit> visitHistory(Pet pet) {
    return dataManager.load(Visit.class)
        .query("e.pet = ?1", pet)
        .list();
  }


}