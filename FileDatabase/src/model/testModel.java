package model;

public class testModel {
	public int getCnt(String name){
		//经过DB的一系列操作之后
		switch(name){
		case "yes":
			return 5;
		case "no":
			return 100;
		default:
			return 0;
		}
	}
}
