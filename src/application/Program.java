package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		SellerDao seller = null;

		System.out.println("=== TEST 1: seller findById ====");
		seller = DaoFactory.createSellerDao();
		Seller resposta = seller.findById(7);
		System.out.println(resposta);

		System.out.println("\n=== TEST 2: seller findByDepartment ====");
		seller = DaoFactory.createSellerDao();
		List<Seller> list = seller.findByDepartment(new Department(1, null));
		for (Seller i : list)
			System.out.println(i);

		System.out.println("\n=== TEST 3: seller findByAll ====");
		seller = DaoFactory.createSellerDao();
		list = seller.findAll();
		for (Seller i : list)
			System.out.println(i);
		
		System.out.println("\n=== TEST 3: seller insert ====");
		Seller newSeller = new Seller(null, "Pedro", "Pedro.ga@gmail.com", new Date(), 2559.0, new Department(1, null));
		seller = DaoFactory.createSellerDao();
		//seller.insert(newSeller); só para não ficar inserido no banco
		System.out.println("Inserted! New id = " + newSeller.getId());
		
		
		System.out.println("\n=== TEST 3: seller update ====");
		resposta = seller.findById(1);
		seller = DaoFactory.createSellerDao(); 
		resposta.setName("Martha Waine");
		seller.update(resposta);
		System.out.println("Update completed!");
	}

}
