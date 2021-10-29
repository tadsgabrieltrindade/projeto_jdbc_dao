package application;

import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		SellerDao seller = DaoFactory.createSellerDao();
		Scanner sc = new Scanner(System.in);
		System.out.print("Digite um id: ");
		int id = sc.nextInt();
		
		Seller resposta = seller.findById(id);
		
		System.out.println(resposta);
	}

}
