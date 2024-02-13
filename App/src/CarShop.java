import java.sql.*;
import java.util.Scanner;
//I used CRUD (create, read, update, delete)
public class CarShop {
    private static final String url = "jdbc:postgresql://localhost:5432/person";
    private static final String user = "postgres";
    private static final String password = "D310106";

    public static void main(String[] args) { //this is main void method that will return infinite loop (while (true))
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database server successfully.\n");
            System.out.println("-------------------------------------------------------------------------------------------\n");
            System.out.println("Welcome to CarShop! \nHere you can buy, sell, add, upgrade or just look at all our cars. \nPlease select an option from the menu: ...");
            while (true) {
                System.out.println("\n1. Add a new car");
                System.out.println("2. View all cars");
                System.out.println("3. Update car information");
                System.out.println("4. Delete a car");
                System.out.println("5. Buy a car");
                System.out.println("6. View user purchases");
                System.out.println("7. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addCar(connection, scanner);
                        break;
                    case 2:
                        viewAllCars(connection);
                        break;
                    case 3:
                        updateCar(connection, scanner);
                        break;
                    case 4:
                        deleteCar(connection, scanner);
                        break;
                    case 5:
                        buyCar(connection, scanner);
                        break;
                    case 6:
                        viewUserPurchases(connection, scanner);
                        break;
                    case 7:
                        System.out.println("Exiting...\n");
                        System.out.println("You exited the CarShop((");
                        System.out.println("Goodbye, check us out again!");
                        System.out.println("-------------------------------------------------------------------------------------------");
                        return;
                    default:
                        System.out.println("Invalid choice!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void addCar(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter car details:");
        System.out.print("Car id: ");
        int id = scanner.nextInt();
        System.out.print("Car Name: ");
        String car_name = scanner.next();
        System.out.print("Model: ");
        String model = scanner.next();
        System.out.print("Horse Power: ");
        int horsePower = scanner.nextInt();
        System.out.print("Price: ");
        double price = scanner.nextDouble();
        System.out.print("Engine: ");
        String engine = scanner.next();
        System.out.print("Drive Train: ");
        String driveTrain = scanner.next();
        System.out.print("Other Info: ");
        String otherInfo = scanner.next();

        String sql = "INSERT INTO carsdata (id, car_name, model, horsePower, price, engine, driveTrain, otherInfo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.setString(2, car_name);
        statement.setString(3, model);
        statement.setInt(4, horsePower);
        statement.setDouble(5, price);
        statement.setString(6, engine);
        statement.setString(7, driveTrain);
        statement.setString(8, otherInfo);
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("A new car has been added successfully.");
        }
    }

    private static void viewAllCars(Connection connection) throws SQLException {
        String sql = "SELECT * FROM carsdata ORDER BY price";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        System.out.println("ID\tCAR NAME\tMODEL\tPRICE\tH.P\tENGINE\tDRIVETRAIN\tOTHER INFO");
        while (resultSet.next()) {
            System.out.println(resultSet.getInt("id") + ". " +
                    resultSet.getString("car_name") + " " +
                    resultSet.getString("model") + " - " +
                    resultSet.getDouble("price") + "$" + " - " +
                    resultSet.getInt("horsePower") + " - " +
                    resultSet.getString("engine") + " - " +
                    resultSet.getString("driveTrain") + " - " +
                    resultSet.getString("otherInfo"));

        }
    }

    private static void updateCar(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter the ID of the car to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter new car details:");
        System.out.print("Car Name: ");
        String car_name = scanner.next();
        System.out.print("Model: ");
        String model = scanner.next();
        System.out.print("Horse Power: ");
        int horsePower = scanner.nextInt();
        System.out.print("Price: ");
        double price = scanner.nextDouble();
        System.out.print("Engine: ");
        String engine = scanner.next();
        System.out.print("Drive Train: ");
        String driveTrain = scanner.next();
        System.out.print("Other Info: ");
        String otherInfo = scanner.next();

        String sql = "UPDATE carsdata SET car_name=?, model=?, horsePower=?, price=?, engine=?, driveTrain=?, otherInfo=? WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, car_name);
        statement.setString(2, model);
        statement.setInt(3, horsePower);
        statement.setDouble(4, price);
        statement.setString(5, engine);
        statement.setString(6, driveTrain);
        statement.setString(7, otherInfo);
        statement.setInt(8, id);
        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Car information has been updated successfully.");
        } else {
            System.out.println("No car found with the given ID.");
        }
    }

    private static void deleteCar(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter the ID of the car to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM carsdata WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        int rowsDeleted = statement.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Car has been deleted successfully.");
        } else {
            System.out.println("No car found with the given ID.");
        }
    }

    private static void buyCar(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter the ID of the car you want to buy: ");
        int carId = scanner.nextInt();
        System.out.println("Enter your username: ");
        String username = scanner.next();

        String sql = "INSERT INTO purchases (car_id, username) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, carId);
        statement.setString(2, username);
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Congratulations! You have successfully purchased the car.");
        } else {
            System.out.println("Failed to buy the car. Please check the car ID and try again.");
        }
    }

    private static void viewUserPurchases(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter your username to view your purchases: ");
        String username = scanner.next();

        String sql = "SELECT p.purchase_id, c.car_name, c.model, c.price, p.purchase_date " +
                "FROM purchases p " +
                "INNER JOIN carsdata c ON p.car_id = c.id " +
                "WHERE p.username=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        System.out.println("Your purchases:");
        while (resultSet.next()) {
            int purchaseId = resultSet.getInt("purchase_id");
            String carName = resultSet.getString("car_name");
            String model = resultSet.getString("model");
            double price = resultSet.getDouble("price");
            String purchaseDate = resultSet.getString("purchase_date");
            System.out.println("Purchase ID: " + purchaseId + ", Car: " + carName + " " + model + ", Price: " + price + "$" + ", Purchase Date: " + purchaseDate);
        }
    }
}