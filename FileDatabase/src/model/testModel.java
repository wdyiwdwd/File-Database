package model;

public class testModel {
	public int getCnt(String name){
		//����DB��һϵ�в���֮��
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
