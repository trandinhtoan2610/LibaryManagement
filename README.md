# Library Management System (LibaryManagement)

## Introduction

The Library Management System is a desktop application built with Java Swing, designed to manage and automate the operations of a library. It helps in efficiently maintaining records of books, members (readers), employees, and library transactions like borrowing, returning, purchasing, and handling penalties.

## Features

* **Book Management**: Add, update, delete, and search for books, including details like category, author, publisher, quantity, price, and year of publication.
* **Category Management**: Manage book categories.
* **Author Management**: Manage author details and view their works available in the library.
* **Publisher Management**: Manage publisher information (name, phone, address).
* **Reader (Member) Management**: Register new readers, update their information, and manage their records.
* **Employee Management**: Manage employee details, including roles (Admin, Staff, Employee), login credentials, and salary information.
* **Transaction Management**:
   * **Borrowing**: Manage borrowing sheets, track due dates, and record actual return dates. Handle borrow details including quantity and status (borrowing, returned, lost, damaged, overdue).
   * **Penalties**: Manage penalty sheets for overdue, lost, or damaged books, tracking total amount, payment status, and processing employee.
   * **Purchasing**: Manage purchase orders from suppliers, including details like supplier, employee, total amount, purchase date, and order status (pending, completed, cancelled).
* **Supplier Management**: Manage supplier information (ID, name, phone, address).
* **Statistics**: View various statistics related to books, borrowing, penalties, and purchases.
* **User Authentication**: Secure login for employees based on username and password.
* **Data Export/Import**: Supports exporting data to Excel/PDF and importing from Excel for certain modules (e.g., Employees, Publishers).

## Technology Stack

* **Language**: Java (Requires JDK 23 or compatible)
* **Database**: MySQL (Schema provided in `Library.sql`)
* **Build Tool**: Apache Maven
* **GUI**: Java Swing
* **UI Libraries**:
   * FlatLaf (for modern look and feel, including IntelliJ themes)
   * MigLayout
   * SVG Salamander & Batik Swing (for SVG support)
   * Swing DateTime Picker
   * JCalendar
* **Reporting/Export**:
   * Apache POI (for Excel export/import)
   * iText 7 (for PDF export)
* **Charting**: JFreeChart
* **Utilities**: Lombok, SLF4j, Guava

## Database Setup

1.  **Create Database**: Ensure you have a MySQL server running. Execute the `Library.sql` script to create the `library_management` database and all necessary tables with sample data.
    ```sql
    -- Example using mysql command line:
    -- mysql -u your_username -p < Library.sql
    ```
2.  **Configure Connection**: Modify the `database.properties` file (located in `src/main/resources` and potentially duplicated in `target/classes`) with your MySQL database credentials if they differ from the defaults:
    ```properties
    db_name = library_management
    db_user = root
    db_password = your_mysql_password 
    db_driver = com.mysql.cj.jdbc.Driver
    ```

## Installation & Running

1.  **Prerequisites**:
   * JDK 23 or later.
   * Apache Maven.
   * MySQL Server.
2.  **Clone the Repository**:
    ```sh
    git clone [https://github.com/trandinhtoan2610/LibraryManagement.git](https://github.com/trandinhtoan2610/LibraryManagement.git) 
    ```
3.  **Navigate to Project Directory**:
    ```sh
    cd LibraryManagement
    ```
4.  **Build the Project**: Use Maven to compile the project and download dependencies.
    ```sh
    mvn clean install
    ```
5.  **Run the Application**: Execute the main class.
    ```sh
    # You might need to adjust the classpath depending on your setup
    # or run directly from your IDE (e.g., IntelliJ, Eclipse)
    mvn exec:java -Dexec.mainClass="GUI.Main" 
    ```
6.  **Login**: Use the sample employee credentials provided in `Library.sql` (e.g., username `thanhnv_admin`, password `hashed_pw` - *Note: Real application should use proper password hashing*).

## Usage

* Launch the application using the steps above.
* Log in using employee credentials.
* Navigate through the sidebar to access different management modules (Books, Readers, Employees, Transactions, etc.).
* Use the provided buttons (Add, Update, Delete, Export, Import) within each module for respective actions.

## Contributing

Contributions are welcome! Please read the [contributing guidelines](CONTRIBUTING.md) for more details. (Note: `CONTRIBUTING.md` file was not provided in the upload).

## License

* **Project Code**: Licensed under the MIT License. See the [LICENSE](LICENSE) file for details. (Note: `LICENSE` file was not provided in the upload).
* **Fonts**: The project uses Noto Sans fonts which are licensed under the SIL Open Font License, Version 1.1. The Roboto font is licensed under the Apache License, Version 2.0. See the respective license files (`OFL.txt` and `LICENSE.txt` within the font directories) for details.

## Contact

For any inquiries or feedback, please update this section with the appropriate contact information.