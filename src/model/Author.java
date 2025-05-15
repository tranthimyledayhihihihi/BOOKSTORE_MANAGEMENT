/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
public class Author {
    private int authorId;
    private String image;
    private String name;
    private String bio;
    private Integer birthYear;
    private String country;

    // For display purpose (join queries)
    private int bookCount;
    
    public Author() {
    }

    public Author(int authorId, String image, String name, String bio, Integer birthYear, String country, int bookCount) {
        this.authorId = authorId;
        this.image = image;
        this.name = name;
        this.bio = bio;
        this.birthYear = birthYear;
        this.country = country;
        this.bookCount = bookCount;
    }
    
    public Author(int authorId, String image, String name, String bio, Integer birthYear, String country) {
        this.authorId = authorId;
        this.image = image;
        this.name = name;
        this.bio = bio;
        this.birthYear = birthYear;
        this.country = country;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getimage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }

    @Override
    public String toString() {
        return "Author{" +
                "authorId=" + authorId +
                ", name='" + name + '\'' +
                ", bio='" + bio + '\'' +
                ", birthYear=" + birthYear +
                ", bookCount='" + bookCount + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
} 