package sqlmapreduce.shared;

public interface Constants {
  public static final String EMP_SAL_SQL = "DROP TABLE EMP;\n" + "DROP TABLE SAL;\n"
  + "CREATE TABLE EMP (ID INT PRIMARY KEY, NAME VARCHAR);\n"
  + "INSERT INTO EMP VALUES (1, 'BOB');\n" + "INSERT INTO EMP VALUES (1, 'BOB');\n"
  + "INSERT INTO EMP VALUES (2, 'JOHN');\n" + "INSERT INTO EMP VALUES (3, 'JANE');\n"
  + "CREATE TABLE SAL (EMP_ID INT, AMOUNT NUMERIC);\n" + "INSERT INTO SAL VALUES (1, 12.00);\n"
  + "INSERT INTO SAL VALUES (2, 5.00);\n" + "INSERT INTO SAL VALUES (3, 15.00);\n"
  + "SELECT NAME, AMOUNT FROM EMP, SAL WHERE EMP.ID = SAL.EMP_ID;\n";

  public static final String INITIAL_SQL = "SELECT * FROM CONTACT;\n" + "SELECT * FROM EMP;\n";

  public static final String KIND = "contact";
}
