package application;

import java.util.Date;

import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		Department d = new Department(05, "Computer");
		Seller s = new Seller(23, "Marcos", "Marcos@gmail.com", new Date(), 4521.0, d);

		System.out.println(s);
	}

}
