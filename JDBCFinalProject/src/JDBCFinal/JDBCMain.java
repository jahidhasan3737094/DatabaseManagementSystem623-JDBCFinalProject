package JDBCFinal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCMain {

	public static void main(String[] args) {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs1=null;
		ResultSet rs2=null;
		ResultSet rs3=null;
		ResultSet rs4 = null;
		

		try {
		    // Establish the database connection
		    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbcproject", "root", "jahid37094");

		    // Disable auto-commit to start a transaction
		    connection.setAutoCommit(false);

		    // Create a statement object
		    statement = connection.createStatement();
		 
		    statement.executeUpdate("CREATE TABLE Product (prodid VARCHAR(50), pname VARCHAR(50), price DECIMAL(10,3))");
			statement.executeUpdate("CREATE TABLE Depot (depid VARCHAR(50) , addr VARCHAR(50), volume DECIMAL(10,3))");
			statement.executeUpdate("CREATE TABLE Stock (prodid VARCHAR(50), depid VARCHAR(50), quantity INT)");


			System.out.println("Tables created...");
			
			// Alter the Product table to add a primary key constraint
			String alterProductQuery = "ALTER TABLE Product ADD PRIMARY KEY (prodid)";
			statement.executeUpdate(alterProductQuery);
			// Alter the Product Depot to add a primary key constraint
			String alterDepotQuery = "ALTER TABLE Depot ADD PRIMARY KEY (depid)";
			statement.executeUpdate(alterDepotQuery);

			// Alter the Stock table to add a primary key constraint
			String alterStockQuery = "ALTER TABLE Stock ADD PRIMARY KEY (prodid, depid)";
			statement.executeUpdate(alterStockQuery);
			
			
			statement.executeUpdate("INSERT INTO Product (prodid, pname, price) VALUES ('p1', 'tape', 2.5)");
			statement.executeUpdate("INSERT INTO Product (prodid, pname, price) VALUES ('p2', 'tv', 250)");
			statement.executeUpdate("INSERT INTO Product (prodid, pname, price) VALUES ('p3', 'vcr', 80)");

			statement.executeUpdate("INSERT INTO Depot (depid, addr, volume) VALUES ('d1', 'New York', 9000)");
			statement.executeUpdate("INSERT INTO Depot (depid, addr, volume) VALUES ('d2', 'Syracuse', 6000)");
			statement.executeUpdate("INSERT INTO Depot (depid, addr, volume) VALUES ('d4', 'New York', 2000)");

			statement.executeUpdate("INSERT INTO Stock (prodid, depid, quantity) VALUES ('p1', 'd1', 1000)");
			statement.executeUpdate("INSERT INTO Stock (prodid, depid, quantity) VALUES ('p1', 'd2', -100)");
			statement.executeUpdate("INSERT INTO Stock (prodid, depid, quantity) VALUES ('p1', 'd4', 1200)");
			statement.executeUpdate("INSERT INTO Stock (prodid, depid, quantity) VALUES ('p3', 'd1', 3000)");
			statement.executeUpdate("INSERT INTO Stock (prodid, depid, quantity) VALUES ('p3', 'd4', 2000)");
			statement.executeUpdate("INSERT INTO Stock (prodid, depid, quantity) VALUES ('p2', 'd4', 1500)");
			statement.executeUpdate("INSERT INTO Stock (prodid, depid, quantity) VALUES ('p2', 'd1', -400)");
			statement.executeUpdate("INSERT INTO Stock (prodid, depid, quantity) VALUES ('p2', 'd2', 2000)");

			System.out.println("Data inserted...");

			// Alter the Stock table to add a foreign key constraint to the Product table
			String addForeignKeyToProductQuery = "ALTER TABLE Stock ADD FOREIGN KEY (prodid) REFERENCES Product(prodid)";
			statement.executeUpdate(addForeignKeyToProductQuery);

			// Alter the Stock table to add a foreign key constraint to the Depot table
			String addForeignKeyToDepotQuery = "ALTER TABLE Stock ADD FOREIGN KEY (depid) REFERENCES Depot(depid)";
			statement.executeUpdate(addForeignKeyToDepotQuery);
			
			System.out.println("Before update product table .....");
			// Retrieve the updated data from the Product table
		    rs1 = statement.executeQuery("SELECT * FROM Product");
		    while (rs1.next()) {
		        System.out.println("prodid: " + rs1.getString("prodid") + ", pname: " + rs1.getString("pname") + ", price: " + rs1.getDouble("price"));
		    }
			
			// Disable foreign key checks
			String disableFKQuery = "SET FOREIGN_KEY_CHECKS = 0;";
			statement.executeUpdate(disableFKQuery);

			// Update product name
			String updateProductQuery = "UPDATE Product SET prodid = 'pp1' WHERE prodid = 'p1';";
			int rowsAffected = statement.executeUpdate(updateProductQuery);
			System.out.println("Rows affected: " + rowsAffected);
           
			// Enable foreign key checks
			String enableFKQuery = "SET FOREIGN_KEY_CHECKS = 1;";
			statement.executeUpdate(enableFKQuery);
			
			
			System.out.println("After update product table .....");
			// Retrieve the updated data from the Product table
		    rs2 = statement.executeQuery("SELECT * FROM Product");
		    while (rs2.next()) {
		        System.out.println("prodid: " + rs2.getString("prodid") + ", pname: " + rs2.getString("pname") + ", price: " + rs2.getDouble("price"));
		    }
			
			System.out.println("Before update Stock table .....");
		    rs3 = statement.executeQuery("SELECT * FROM Stock");
			while (rs3.next()) {
			    System.out.println("prodid: " + rs3.getString("prodid") + ", depid: " + rs3.getString("depid") + ", quantity: " + rs3.getDouble("quantity"));
			}

			

            // Update the product name in the Stock table
            String updateStockQuery = "UPDATE Stock SET prodid = 'pp1' WHERE prodid = 'p1'";
            statement.executeUpdate(updateStockQuery);
		   
		    // Commit the transaction if everything is successful
		    connection.commit();
		    System.out.println("After update Stock table .....");
		    rs4 = statement.executeQuery("SELECT * FROM Stock");
			while (rs4.next()) {
			    System.out.println("prodid: " + rs4.getString("prodid") + ", depid: " + rs4.getString("depid") + ", quantity: " + rs4.getDouble("quantity"));
			}
		    System.out.println("Transaction completed successfully.");
		} catch (SQLException e) {
		    // Rollback the transaction if an error occurs
		    try {
		        if (connection != null) {
		            connection.rollback();
		        }
		    } catch (SQLException rollbackException) {
		        rollbackException.printStackTrace();
		    }

		    System.out.println("Transaction failed. Rolled back changes.");
		    e.printStackTrace();
		} finally {
		    // Close the result set, statement, and connection
		    try {
		        if (rs1 != null || rs2!=null|| rs3!=null ||rs4!=null) {
		            rs1.close();
		            rs2.close();
		            rs3.close();
		            rs4.close();
		        }
		        if (statement != null) {
		            statement.close();
		        }
		        if (connection != null) {
		            connection.close();
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}

	}

}
