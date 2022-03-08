# Week 2 Day 1

[Miro link](https://miro.com/app/board/uXjVOHEVc9g=/)

****

<h1>SQL (continued...)</h1>

## **Constraints:**
1. **Primary Key** - uniquely identify a record
2. **Foreign Key** - links to primary key
   - onto-to-one: foreign key in one of the tables, UNIQUE for that foreign key
   - one-to-many: foreign key in the many tables
   - many-to-many
3. **NOT NULL**: can't have null values in the designated column
4. **UNIQUE**: you can't have two records with the same value for that particular column
5. **CHECK**: used to check whether a value meets a certian condition
    - ex. CHECK(age) >= 18)
6. **DEFAULT**: if you insert a record without specifying a value for a Default column, it will use the default value by default
    - Example: ```balance INTERGER DEFAULT 0```

**Composite key:** a primary key made up of two or more different columns
- Each column is called "candidate key"

****

**<h3>Referential Integrity</h3>**

- The idead of making sure that foreign keys always have a corresponding primary key that actually exists
- Prevents a phenomenon known as orphan records (records that actually have a proper link to another record in a different table)
- This also has implications for deleting a student (record)
- If a student has 3 grades and you try to delete that student, it won't allow you to do so unless you delete the grades
- This is where a CASCADE delete would be very useful
****
<h1>SQL Examples</h1>

Create Table:

```sql
CREATE TABLE students (
	id SERIAL PRIMARY KEY,
	first_name VARCHAR(200) NOT NULL,
	last_name VARCHAR(200) NOT NULL,
	age INTEGER NOT NULL
);
```

Insert Data (Rows):

```sql
INSERT INTO students (first_name, last_name, age) 
VALUES 
('Bach', 'Tran', 24),
('John', 'Doe', 18),
('Jane', 'Doe', 25);
```

Select all columns and rows from a table:
```sql
SELECT *
FROM students;
```

Drop (remove) a given table:

```sql
DROP TABLE IF EXISTS students;
```

Create a junction table:

```sql
CREATE TABLE students_teachers (
	student_id INTEGER NOT NULL,
	teacher_id INTEGER NOT NULL,
	CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES students(id),
	CONSTRAINT fk_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id),
	PRIMARY KEY(student_id, teacher_id)
);
```

Join tables from left:

```sql
SELECT *
FROM students -- left table
LEFT JOIN contacts -- right table
ON students.id = contacts.student_id;
```

Inner Join:

```sql
SELECT *
FROM students -- left table
INNER JOIN contacts -- right table
ON students.id = contacts.student_id;
```

Join tables from right:

```sql
SELECT *
FROM students -- left table
INNER JOIN contacts -- right table
ON students.id = contacts.student_id;
```

Multiple joins in one query:

```sql
SELECT *
FROM students s
INNER JOIN students_teachers st 
ON s.id = st.student_id 
INNER JOIN teachers t 
ON t.id = st.teacher_id;
```

Subquerying example:

```sql
SELECT *
FROM students
WHERE age = (
	SELECT MAX(age)
	FROM students
);
```



****

<h1>Java Database Connectivity (JDBC)</h1>

**<h3>Classes and interfaces:</h3>**

- **DriverManager**: register our Postgres Driver (which is a dependency we grab from mvn using Gradle) and then get a Connection object using .getConnection(url, username, password)
- **Connection**: obtained from the DriverManager and is used to create a Statement, PreparedStatement, or CallableStatement object
- **Statement**: takes in a SQL string without any placeholders (vulnerable to **SQL injection**)
- **PreparedStatement**: provides the ability to have ? placeholders whenever we need to provide parameters to our queries (prevents **SQL injection**)
  - A PreparedStatement will "pre-compile" a SQL query such that the SQL query can only match the template you have provided
  - Think of a PreparedStatement as a template for a particular query
- **CallableStatement**: used to execute stored procedures / user defined functions
- **ResultSet**: a pointer to the actual results in the database, which you can iterate through using .next()
- **SQLException**: checked exception that is involved with any JDBC "issues"

****

**<h3>Steps to utilize JDBC:</h3>**

1. Get the Postgres driver dependency from mvn through gradle
2. Created a class called ConnectionUtility that contains a method that we called getConnection() to place all of our code responsible for getting a connection
3. Have the URL, username and password (preferably grabbed from environment variables)
4. Register the Postgres driver with the DriverManager (JDBC API)
5. DriverManager.getConnection(url, username, password)
6. Return the connection object
7. In the DAO layer, using the connection object obtained by calling ConnectionUtility.getConnection(), create a Statement/PreparedStatement/CallableStatement object
8. Execute the query to retrieve a ResultSet
9. Iterate through the ResultSet to grab your information

****
**JDBC connection string:** 

```jbdc:postgresql://localhost:5432/postgres```
- postgresql = database type
- localhost = hostname/ip address
- 5432 = port
- postgres = database name
****

<h1>Setting up JDBC project</h1>

- Create new gradle project
1. Add postgresql driver dependency
   - ```implementation 'org.postgresql:postgresql:42.3.3'```
2. create src/main/java/com.revature.utlility pkg
3. Create ConnectionUtility class in pkg
   - Will contain the getConnection method

****
****
Side Note: Setting up system environment variables:

- db_url : postgres/??? need to fix this url
- db_username : postgres
- db_password : password

****
****

4. Register the Postgres driver with the DriverManager

```
DriverManager.registerDriver(new Driver());
```

5. Get the Connection object from the DriverManager

```
Connection connection = DriverManager.getConnection(url, username, password);

return connection;
```

*code from 4 and 5 are used in the ConnectionUtility class (see below)*

****
****
Create Driver class with a main method for testing connection or create a test class

```
try {
    Connection con = ConnectionUtility.getConnection(url);

    System.out.println(con);

}
catch (SQLException e) {
    e.printStackTrace()

}
```

This can be deleted once the test has resulted in a proper connection

****
****
6. Create a DAO (data access object) class for a particular "entity"
7. Create com.revature.model pkg
   - Inside create Student class
   - Override equlas, hashCode, and toString
   - Create getter/setters
8. Create the methods for the "CRUD" operations within the DAO class
9. Call the getConnection method from ConnectionUtility (which we made)
10. Create a (Prepared)Statement object using the Connection object
11. If any parameters need to be set, set the parameters (?)
12. Execute the query and retrieve a ResultSet object
13. Iterate over record(s) using the Result's next() method
14. Grab the information from the record

****
****
```pstmt.executeUpdate()``` is used with INSERT, UPDATE, and DELETE
****
****
  
****
****

<h1>CRUD Demo Code</h1>

src/main/java/com/revature/utility.java

```java
// TODO 2: Create a ConnectionUtility class that will contain the getConnection method where our code will belong

public class ConnectionUtility {

    public static Connection getConnection() throws SQLException {
        // TODO 3: Grab the connection string (url), username, password for the database
        // Preferably, this will be from environment variables
        
        String url = System.getenv("db_url");
        String username = System.getenv("db_username");
        String password = System.getenv("db_password");

        // TODO 4: Register the Postgres driver with the DriverManager
        DriverManager.registerDriver(new Driver());

        // TODO 5: Get the Connection object from the DriverManager
        Connection connection = DriverManager.getConnection(url, username, password);

        return connection;
    }

}
```



src/main/java/com/revature/main/Driver.java

```java
package com.revature.main;

import com.revature.dao.StudentDao;
import com.revature.model.Student;
import com.revature.utility.ConnectionUtility;

import java.sql.Connection;
import java.sql.SQLException;

public class Driver {

    public static void main(String[] args) {

    }

}
```


src/main/java/com/revature/dao/StudentDao.java
```java
public class StudentDao {

    // TODO 8: Create the methods for the "CRUD" operations

    // C

    // Whenever you add a student, no id is associated yet
    // The id is automatically generated
    // So what we want to do is retrieve an id and return that with the Student object


    public Student addStudent(Student student) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "INSERT INTO students (first_name, last_name, age) VALUES (?, ?, ?)";

            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setInt(3, student.getAge());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            int generatedId = rs.getInt(1); // 1st column of the ResultSet

            return new Student(generatedId, student.getFirstName(), student.getLastName(), student.getAge());
        }
    }

    // R
    public Student getStudentById(int id) throws SQLException {

        // TODO 9: Call the getConnection method from ConnectionUtility (which we made)

        try (Connection con = ConnectionUtility.getConnection()) { //try-with-resources

            // TODO 10: Create a (Prepared)Statement object using the Connection object
            String sql = "SELECT * FROM students WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);


            // TODO 11: If any parameters need to be set, set the parameters (?)
            pstmt.setInt(1, id);


            // TODO 12: Execute the query and retrieve a ResultSet object
            ResultSet rs = pstmt.executeQuery(); // executeQuery() is used with SELECT


            // TODO 13: Iterate over record(s) using the ResultSet's next() method
            if (rs.next()) {

                // TODO 14: Grab the information from the record

                // int studentId = rs.getInt("id"); // not needed due to id already exists
                
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int age = rs.getInt("age");

                return new Student(id, firstName, lastName, age);
            }

        }

        return null;
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();

        try (Connection con = ConnectionUtility.getConnection()) { // try-with-resources
            String sql = "SELECT * FROM students";
            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery(); // executeQuery() is used with SELECT

            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int age = rs.getInt("age");

                students.add(new Student(id, firstName, lastName, age));
            }

        }

        return students;
    }

    // U
    public Student updateStudent(Student student) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "UPDATE students " +
                    "SET first_name = ?, " +
                    "last_name = ?, " +
                    "age = ? " +
                    "WHERE id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setInt(3, student.getAge());
            pstmt.setInt(4, student.getId());

            pstmt.executeUpdate();
        }

        return student;
    }

    // D
    // true if a record was deleted, false if a record was not deleted
    public boolean deleteStudentById(int id) throws SQLException {

        try (Connection con = ConnectionUtility.getConnection()) {

            String sql = "DELETE FROM students WHERE id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, id);

            int numberOfRecordsDeleted = pstmt.executeUpdate(); 

            // executeUpdate() is used with INSERT, UPDATE, DELETE

            if (numberOfRecordsDeleted == 1) {
                return true;
            }
        }

        return false;
    }
}
```

src/main/java/com/revature/model/Student.java

```java
// TODO 7: Create a class that will serve as a model for records in a database table (students table)

public class Student {

    private int id;
    private String firstName;
    private String lastName;
    private int age;

    public Student() {
    }

    public Student(int id, String firstName, String lastName, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id && age == student.age && Objects.equals(firstName, student.firstName) && Objects.equals(lastName, student.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, age);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                '}';
    }
}
```


