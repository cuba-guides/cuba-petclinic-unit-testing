package com.haulmont.sample.petclinic.web.visit.visit;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.components.Action.ActionPerformedEvent;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.service.RegularCheckupService;
import com.haulmont.sample.petclinic.service.VisitService;
import java.time.LocalDate;
import java.util.List;
import javax.inject.Inject;
import org.apache.poi.ss.formula.functions.T;

@UiController("petclinic_Visit.edit")
@UiDescriptor("visit-edit.xml")
@EditedEntityContainer("visitDc")
@LoadDataBeforeShow
public class VisitEdit extends StandardEditor<Visit> {


  @Inject
  protected RegularCheckupService regularCheckupService;

  @Inject
  protected DataManager dataManager;
  @Inject
  protected Notifications notifications;

  @Subscribe("proposeRegularCheckupDate")
  protected void onProposeRegularCheckupDate(ActionPerformedEvent event) {
    Pet pet = getEditedEntity().getPet();

    LocalDate nextRegularCheckupDate = regularCheckupService.calculateNextRegularCheckupDate(
        pet,
        visitsForPet(pet)
    );

    notifications.create(NotificationType.TRAY)
        .withCaption(nextRegularCheckupDate.toString());
  }

  private List<Visit> visitsForPet(Pet pet) {
    return dataManager.load(Visit.class)
        .query("e.pet = ?1", pet)
        .list();
  }


}