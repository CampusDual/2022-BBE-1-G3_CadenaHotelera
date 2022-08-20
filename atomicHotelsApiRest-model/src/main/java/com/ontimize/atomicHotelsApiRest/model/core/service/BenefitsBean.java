package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.math.BigDecimal;

public class BenefitsBean {
	
	public BigDecimal BENEFITS;
	public BigDecimal EXPENSES;
    public BigDecimal INCOME;
    public String HOTEL;
    
    public BenefitsBean() {}
    
    public BenefitsBean(String HOTEL, BigDecimal EXPENSES,BigDecimal INCOME) {
    	this.HOTEL=HOTEL;
    	this.EXPENSES=EXPENSES;
    	this.INCOME=INCOME;
    }

	public BigDecimal getBENEFITS() {
		return BENEFITS;
	}

	public void setBENEFITS(BigDecimal bENEFITS) {
		BENEFITS = bENEFITS;
	}

	public BigDecimal getEXPENSES() {
		return EXPENSES;
	}

	public void setEXPENSES(BigDecimal eXPENSES) {
		EXPENSES = eXPENSES;
	}

	public BigDecimal getINCOME() {
		return INCOME;
	}

	public void setINCOME(BigDecimal iNCOME) {
		INCOME = iNCOME;
	}

	public String getHOTEL() {
		return HOTEL;
	}

	public void setHOTEL(String hOTEL) {
		HOTEL = hOTEL;
	}
	

}
