package com.realnet.actionbuilder.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.realnet.actionbuilder.repository.Rn_CFF_ActionBuilder_Rules_Repository;
import com.realnet.users.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Rn_Cff_ActionBuilderRules_ServiceImpl implements Rn_Cff_ActionBuilderRules_Service {

	@Autowired(required=false)
	private UserService userService;

	@Autowired
	private Rn_CFF_ActionBuilder_Rules_Repository rn_cff_ActionBuilderRulesRepository;

	@Autowired
	private Environment environment;

	@Autowired
	private JdbcTemplate jdbcTemplate;

//	@Autowired(required=true)
//	SessionFactory entityManagerFactory;
	
	@Autowired
    private EntityManager entityManager;
	
	@Override
	@Transactional
	public void updateUsingJDBC(String table_name, String column_name, String value, int id) {
		String regex = ".*[a-zA-Z].*";
		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(value);
		if (mat.find()) {
			value = "'" + value + "'";
		}

		String jdbcURL = environment.getProperty("spring.datasource.url");
		String jdbcUsername = environment.getProperty("spring.datasource.username");
		String jdbcPassword = environment.getProperty("spring.datasource.password");
		System.out.println("URL = " + jdbcURL + "\nUSERNAME = " + jdbcUsername + "\nPASSWORD = " + jdbcPassword);

		String updateQuery = "UPDATE " + table_name + " SET " + column_name + " = " + value + " WHERE id = " + id + ";";
		System.out.println("QUERY = " + updateQuery);
		Connection connect = null;
		Statement statement = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connect = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
			if (connect != null) {
				System.out.println("Database Connected");
			}
			statement = connect.createStatement();
			int rowsAffected = statement.executeUpdate(updateQuery);
			System.out.println("ROW EFFECTED = " + rowsAffected);
			if (rowsAffected > 0) {
				System.out.println("Update Successfully...");
			}
			connect.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	@Transactional
	public void updateUsingJdbcTemplate(String table_name, String column_name, String value, int id) {
		String sql = "UPDATE " + table_name + " SET " + column_name + " = ? WHERE id = ?";
		System.out.println("QUERY = " + sql + ";");
		int row = jdbcTemplate.update(sql, value, id);
		System.out.println("ROW EFFECTED = " + row);
		if (row > 0) {
			System.out.println("Update Successfully...");
		}
	}

	@Override
	@Transactional
	public void updateUsingHibernate(String table_name, String column_name, String value, int id) {
		
		String SQL = "UPDATE " + table_name + " SET " + column_name + " =:value WHERE id =:id";
		Session  session = entityManager.unwrap(Session.class);
		//Session session = null;
		Transaction tx = null;
		try {
			
			
			
			//session = sessionFactory.openSession();
			tx = session.beginTransaction();
			int row = session.createSQLQuery(SQL).setParameter("value", value).setParameter("id", id).executeUpdate();

			if (row > 0) {
				System.out.println("Update Successfully...");
			}
			tx.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected static String convertStringForSql(String str) {
		String regex = ".*[a-zA-Z].*";
		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(str);
		if (mat.find()) {
			return (str = "'" + str + "'");
		}
		return str;
	}

	@Override
	public void fileStringReplace(String path, String oldString, String newString) {
		File file = new File(path);

		try {
			String finalString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
			oldString = "(?i)".concat(oldString);
			finalString = finalString.replaceAll(oldString, newString);
			System.out.println(finalString);
			BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			writer.write(finalString);
			System.out.println("String Replaced");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String variableStringReplace(String str, String oldString, String newString) {
		if (str.contains(oldString)) {
			System.out.println("String found to replace.");
		}
		str = str.replace(oldString, newString);
		if (!str.contains(oldString)) {
			System.out.println("String replaced");
		}
		return str;

	}

	@Override
	public String stringAppendInVariable(String str, String newString) {
		str = str.concat(newString);
		if (str.contains(newString)) {
			System.out.println("String Appended");
		}
		return str;
	}

	@Override
	public void stringAppendInFile(String path, String str) {
		File file = new File(path);
		try {
			String finalString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
			finalString = finalString.concat(str);
			if (finalString.contains("\n" + str)) {
				System.out.println("String appended to a File");
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(finalString);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
