package ru.otus.library.approve.data;

public class Approval {
    private Boolean approved;

    public Approval(Boolean approved) {
        this.approved = approved;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}
