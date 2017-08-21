
package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Supplier {

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("address_ur")
    @Expose
    private String addressUr;
    @SerializedName("contact1_name")
    @Expose
    private String contact1Name;
    @SerializedName("contact1_phone")
    @Expose
    private String contact1Phone;
    @SerializedName("contact2_name")
    @Expose
    private String contact2Name;
    @SerializedName("contact2_phone")
    @Expose
    private String contact2Phone;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("inn")
    @Expose
    private String inn;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("requisites")
    @Expose
    private String requisites;
    @SerializedName("type")
    @Expose
    private int type;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressUr() {
        return addressUr;
    }

    public void setAddressUr(String addressUr) {
        this.addressUr = addressUr;
    }

    public String getContact1Name() {
        return contact1Name;
    }

    public void setContact1Name(String contact1Name) {
        this.contact1Name = contact1Name;
    }

    public String getContact1Phone() {
        return contact1Phone;
    }

    public void setContact1Phone(String contact1Phone) {
        this.contact1Phone = contact1Phone;
    }

    public String getContact2Name() {
        return contact2Name;
    }

    public void setContact2Name(String contact2Name) {
        this.contact2Name = contact2Name;
    }

    public String getContact2Phone() {
        return contact2Phone;
    }

    public void setContact2Phone(String contact2Phone) {
        this.contact2Phone = contact2Phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequisites() {
        return requisites;
    }

    public void setRequisites(String requisites) {
        this.requisites = requisites;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString()  {
        return this.name;
    }

}
