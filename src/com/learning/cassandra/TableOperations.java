package com.learning.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class TableOperations {
	public static void main(String[] args) {
		Cluster.Builder builder = Cluster.builder().addContactPoint("127.0.0.1");
		try(Cluster cluster = builder.build();
		Session session = cluster.connect("CASS_LEARNING"))
		{
			System.out.println("Session created");
			String insertData = "INSERT INTO emp\n" + 
					" (emp_id, emp_name, emp_city, emp_sal, emp_bonus, emp_phone)" + 
					" values\n" + 
					"( 9,'Anup', 'Pune', 30000, 0, 9876543210);"; 
			session.execute(insertData);
			System.out.println("data inserted");
			insertData = "INSERT INTO emp\n" + 
					" (emp_id, emp_name, emp_sal, emp_bonus, emp_phone)" + 
					" values\n" + 
					"( 7,'Scott', 30000, 560, 9876543210);"; 
			session.execute(insertData);
			System.out.println("data inserted");
			String selectQuery = "select * from emp;";
			ResultSet resultSet = session.execute(selectQuery);
			System.out.println(resultSet.all());
			
		}
		finally
		{
			System.out.println("Cluster and session closed");
		}
	}

}

