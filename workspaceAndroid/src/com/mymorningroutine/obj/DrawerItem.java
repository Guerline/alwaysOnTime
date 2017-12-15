package com.mymorningroutine.obj;

public class DrawerItem {
	 private String title;
    private Integer icon;
     
    public DrawerItem(){}
 
    public DrawerItem(String title, Integer icon){
        this.title = title;
        this.icon = icon;
    }
  
     
    public String getTitle(){
        return this.title;
    }
     
    public Integer getIcon(){
        return this.icon;
    }
     
 
     
    public void setTitle(String title){
        this.title = title;
    }
     
    public void setIcon(int icon){
        this.icon = icon;
    }
     
 }
