package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		PreparedStatement pt = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			pt = conn.prepareStatement("INSERT INTO seller\r\n"
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId)\r\n" + "VALUES\r\n" + "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			pt.setString(1, obj.getName());
			pt.setString(2, obj.getEmail());
			pt.setDate(3, new java.sql.Date(obj.getBithDate().getTime()));
			pt.setDouble(4, obj.getBaseSalary());
			pt.setInt(5, obj.getDepartment().getId());

			int rowsAffect = pt.executeUpdate();
			if (rowsAffect > 0) {
				ResultSet rs = pt.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No Rows affected!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeConnection();
			DB.closeStatement(pt);
		}

	}

	@Override
	public void update(Seller obj) {
		PreparedStatement pt = null;
		try {
			pt = conn.prepareStatement("UPDATE seller\r\n"
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ?\r\n" + "WHERE Id = ?");

			pt.setString(1, obj.getName());
			pt.setString(2, obj.getEmail());
			pt.setDate(3, new java.sql.Date(obj.getBithDate().getTime()));
			pt.setDouble(4, obj.getBaseSalary());
			pt.setInt(5, obj.getDepartment().getId());
			pt.setInt(6, obj.getId());

			pt.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeConnection();
			DB.closeStatement(pt);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement pt = null;
		try {
			pt = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");
			pt.setInt(1, id);

			pt.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeConnection();
			DB.closeStatement(pt);
		}

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement pt = null;
		ResultSet rs = null;

		try {
			pt = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName\r\n" + "FROM seller INNER JOIN department\r\n"
							+ "ON seller.DepartmentId = department.Id\r\n" + "WHERE seller.Id = ?");
			pt.setInt(1, id);
			rs = pt.executeQuery();
			if (rs.next()) {
				Department dep = (instantiateDepartment(rs));
				Seller seller = instantiateSeller(rs, dep);
				return seller;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeConnection();
			DB.closeStatement(pt);
			DB.closeResultSet(rs);
		}

		return null;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement pt = null;
		ResultSet rs = null;

		try {
			pt = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName\r\n" + "FROM seller INNER JOIN department\r\n"
							+ "ON seller.DepartmentId = department.Id\r\n" + "ORDER BY Name");

			rs = pt.executeQuery();

			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<Integer, Department>();

			while (rs.next()) {

				Department dep = map.get(rs.getInt("DepartmentId"));

				// salvar esse departamento
				if (dep == null) {
					dep = (instantiateDepartment(rs));
					map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller seller = instantiateSeller(rs, dep);
				list.add(seller);
			}
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeConnection();
			DB.closeStatement(pt);
			DB.closeResultSet(rs);
		}

		return null;

	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement pt = null;
		ResultSet rs = null;

		try {
			pt = conn.prepareStatement("SELECT seller.*,department.Name as DepName\r\n"
					+ "FROM seller INNER JOIN department\r\n" + "ON seller.DepartmentId = department.Id\r\n"
					+ "WHERE DepartmentId = ?\r\n" + "ORDER BY Name");

			pt.setInt(1, department.getId());

			rs = pt.executeQuery();

			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<Integer, Department>();

			while (rs.next()) {

				Department dep = map.get(rs.getInt("DepartmentId"));

				// salvar esse departamento
				if (dep == null) {
					dep = (instantiateDepartment(rs));
					map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller seller = instantiateSeller(rs, dep);
				list.add(seller);
			}
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeConnection();
			DB.closeStatement(pt);
			DB.closeResultSet(rs);
		}

		return null;

	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

}
