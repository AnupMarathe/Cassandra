package com.learning.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CreateKeyspace {
	public static void main(String[] args) {
		Cluster.Builder builder = Cluster.builder().addContactPoint("127.0.0.1");
		try(Cluster cluster = builder.build();
		Session session = cluster.connect())
		{
			System.out.println("Session created");
			/*PreparedStatement statement = session.prepare("CREATE KEYSPACE tp WITH replication "
			         + "= {'class':'SimpleStrategy', 'replication_factor':1};");
			session.execute(statement);*/
			session.execute("CREATE KEYSPACE CASS_LEARNING WITH replication "
			         + "= {'class':'SimpleStrategy', 'replication_factor':1};");
			System.out.println("Keyspace : CASS_LEARNING created");
			session.execute("USE cass_learning;");
			System.out.println("Connected to Keyspace : CASS_LEARNING created");
			session.execute("DROP KEYSPACE cass_learning;");
			System.out.println("Dropped Keyspace : CASS_LEARNING created");
		}
		finally
		{
			System.out.println("Cluster and session closed");
		}
	}

}