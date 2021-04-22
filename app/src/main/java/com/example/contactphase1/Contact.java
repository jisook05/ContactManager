package com.example.contactphase1;

import java.io.Serializable;

public class Contact implements Serializable {
    int id;
    String f_name;
    String l_name;
    String phoneNum;
    String dob;
    String f_contact;
    String addr1;
    String addr2;
    String city;
    String state;
    String zipcode;


    public Contact(String f_name, String l_name, String phoneNum, String dob, String f_contact){
        this.f_name = f_name;
        this.l_name = l_name;
        this.phoneNum= phoneNum;
        this.dob=dob;
        this.f_contact=f_contact;
    }

    public Contact(String f_name, String l_name, String phoneNum, String dob, String f_contact, String addr1, String addr2, String city, String state, String zipcode){
        this.f_name = f_name;
        this.l_name = l_name;
        this.phoneNum= phoneNum;
        this.dob=dob;
        this.f_contact=f_contact;
        this.addr1=addr1;
        this.addr2=addr2;
        this.city=city;
        this.state=state;
        this.zipcode=zipcode;

    }

    public Contact(int id,String f_name, String l_name, String phoneNum, String dob, String f_contact){
        this.id = id;
        this.f_name = f_name;
        this.l_name = l_name;
        this.phoneNum= phoneNum;
        this.dob=dob;
        this.f_contact=f_contact;
    }

    public int getId(){
        return id;
    }
    public void setId(){
        this.id =id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getF_contact() {
        return f_contact;
    }

    public void setF_contact(String f_contact) {
        this.f_contact = f_contact;
    }
}
