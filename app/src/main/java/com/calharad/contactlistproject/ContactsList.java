package com.calharad.contactlistproject;

public class ContactsList {
    private long id;
    private String name;
    private String phone;
    private String email;
    private boolean selected;

    public ContactsList(long id, String name, String phone, String email, boolean selected) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.selected = selected;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}