package org.treatsforlife.app.events;

public class PetListRowClickedEvent {
    public String petID;

    public PetListRowClickedEvent(String petID) {
        this.petID = petID;
    }
}
