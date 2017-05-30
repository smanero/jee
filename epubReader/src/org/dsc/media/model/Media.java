package org.dsc.media.model;

public abstract class Media {
   private String Name;
   private String Surname;
   private String Title;
   private String Album;
   private String Year;

   public String getName() {
      return Name;
   }

   public void setName(String name) {
      Name = name;
   }

   public String getSurname() {
      return Surname;
   }

   public void setSurname(String surname) {
      Surname = surname;
   }

   public String getTitle() {
      return Title;
   }

   public void setTitle(String title) {
      Title = title;
   }

   public String getAlbum() {
      return Album;
   }

   public void setAlbum(String album) {
      Album = album;
   }

   public String getYear() {
      return Year;
   }

   public void setYear(String year) {
      Year = year;
   }
}
