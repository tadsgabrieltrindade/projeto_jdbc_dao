package application;

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
	}

}
