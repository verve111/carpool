package ru.poputi.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "routes")

public class RouteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    
    @Column(name = "creatorId")
	private String creatorId;

    @Column(name = "creationDate")
	private Date creationDate;
    
    @Column(name = "isActive")
    private boolean isActive = true;
    
    @Column(name = "fromCity")
	private String regFrom;

    @Column(name = "through")
	private String throughCities;
    
    @Column(name = "toCity")
	private String regTo;
    
    @Column(name = "date")
	private Date regDate;

    @Column(name = "time")
	private String regTime;
    
    @Column(name = "mesta")
	private String regMesta;
    
    @Column(name = "dopinfo")
	private String regDopInfo;
    
    @Column(name = "drivername")
	private String regDriverName;
    
    @Column(name = "email")
	private String regEmail;
    
    @Column(name = "checkboxemail")
	private boolean regCheckboxEmail;
    
    @Column(name = "phone")
	private String regPhone;
    
    @Column(name = "checkboxphone")
	private boolean regCheckboxPhone;
    
    @Column(name = "price")
	private String regPrice;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRegFrom() {
		return regFrom;
	}

	public void setRegFrom(String regFrom) {
		this.regFrom = regFrom;
	}

	public String getThroughCities() {
		return throughCities;
	}

	public void setThroughCities(String throughCities) {
		this.throughCities = throughCities;
	}

	public String getRegTo() {
		return regTo;
	}

	public void setRegTo(String regTo) {
		this.regTo = regTo;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public String getRegMesta() {
		return regMesta;
	}

	public void setRegMesta(String regMesta) {
		this.regMesta = regMesta;
	}

	public String getRegDopInfo() {
		return regDopInfo;
	}

	public void setRegDopInfo(String regDopInfo) {
		this.regDopInfo = regDopInfo;
	}

	public String getRegDriverName() {
		return regDriverName;
	}

	public void setRegDriverName(String regDriverName) {
		this.regDriverName = regDriverName;
	}

	public String getRegEmail() {
		return regEmail;
	}

	public void setRegEmail(String regEmail) {
		this.regEmail = regEmail;
	}

	public boolean isRegCheckboxEmail() {
		return regCheckboxEmail;
	}

	public void setRegCheckboxEmail(boolean regCheckboxEmail) {
		this.regCheckboxEmail = regCheckboxEmail;
	}

	public String getRegPhone() {
		return regPhone;
	}

	public void setRegPhone(String regPhone) {
		this.regPhone = regPhone;
	}

	public boolean isRegCheckboxPhone() {
		return regCheckboxPhone;
	}

	public void setRegCheckboxPhone(boolean regCheckboxPhone) {
		this.regCheckboxPhone = regCheckboxPhone;
	}

	public String getRegPrice() {
		return regPrice;
	}

	public void setRegPrice(String regPrice) {
		this.regPrice = regPrice;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
