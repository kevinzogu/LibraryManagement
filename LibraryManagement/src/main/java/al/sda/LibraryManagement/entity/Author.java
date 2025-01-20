package al.sda.LibraryManagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Author {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String birthday;
    private int age;
    private int nrOfBooks;
    private String email;
    private String phone;
    private String nationality;
    private String biography;
    private String gender;
    private String deathDate;
    private String website;
    private String image;
    
    public Author() {
    }
    
    public Author(Long id, String name, String surname, String birthday, int age, int nrOfBooks, String email, String phone, String nationality, String biography, String gender, String deathDate, String website, String image) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.age = age;
        this.nrOfBooks = nrOfBooks;
        this.email = email;
        this.phone = phone;
        this.nationality = nationality;
        this.biography = biography;
        this.gender = gender;
        this.deathDate = deathDate;
        this.website = website;
        this.image = image;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSurname() {
        return surname;
    }
    
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public String getBirthday() {
        return birthday;
    }
    
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public int getNrOfBooks() {
        return nrOfBooks;
    }
    
    public void setNrOfBooks(int nrOfBooks) {
        this.nrOfBooks = nrOfBooks;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getNationality() {
        return nationality;
    }
    
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    
    public String getBiography() {
        return biography;
    }
    
    public void setBiography(String biography) {
        this.biography = biography;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getDeathDate() {
        return deathDate;
    }
    
    public void setDeathDate(String deathDate) {
        this.deathDate = deathDate;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthday=" + birthday +
                ", age=" + age +
                ", nrOfBooks=" + nrOfBooks +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", nationality='" + nationality + '\'' +
                ", biography='" + biography + '\'' +
                ", gender='" + gender + '\'' +
                ", deathDate=" + deathDate +
                ", website='" + website + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
