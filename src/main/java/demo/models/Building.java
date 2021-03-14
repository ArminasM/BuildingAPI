package demo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Building {
    @Id
    @GeneratedValue
    private int id;
    @Column
    private String address;
    @Column
    private String owner;
    @Column
    private float propSize;
    @Column
    private float marketValue;
    @Column
    private String propertyType;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public float getPropSize() {
        return propSize;
    }

    public void setPropSize(float propSize) {
        this.propSize = propSize;
    }

    public float getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(float marketValue) {
        this.marketValue = marketValue;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }


}
