package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

//respons�vel por instanciar os DAOs
public class DaoFactory {
	
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}
}