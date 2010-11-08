package sqlmapreduce.shared;

public interface Constants {
  public static final String EMP_SAL_SQL = "CREATE TABLE CUST (ID INT PRIMARY KEY, NAME VARCHAR);\n"
      + "INSERT INTO CUST VALUES (1, 'BOB');\n"
      + "INSERT INTO CUST VALUES (1, 'BOB');\n"
      + "INSERT INTO CUST VALUES (2, 'JOHN');\n"
      + "INSERT INTO CUST VALUES (3, 'JANE');\n"
      + ";\n"
      + "CREATE TABLE AGREEMENT (CUST_ID INT PRIMARY KEY, RATE NUMERIC);\n"
      + "INSERT INTO AGREEMENT VALUES (1, 12.00);\n"
      + "INSERT INTO AGREEMENT VALUES (2, 5.00);\n"
      + "INSERT INTO AGREEMENT VALUES (3, 15.00);\n"
      + ";\n"
      + "SELECT NAME, RATE\n"
      + "FROM   CUST, AGREEMENT\n"
      + "WHERE  CUST.ID = AGREEMENT.CUST_ID;\n"
      + ";\n"
      + "SELECT * FROM USAGE;\n";

  public static final String INIT_SQL = "DROP TABLE CUST;\n" + "DROP TABLE AGREEMENT;\n"
      + "DROP TABLE USAGE;\n";
  public static final String INITIAL_QUERY = "SELECT * FROM CUST;\n" + "SELECT * FROM AGREEMENT;\n"
      + "SELECT * FROM USAGE;\n";

  public static final String KIND = "USAGE";

  public static final int LEN = 16;

  public static final int MAX_CUST_ID = 3;

  public static final String USAGE_SQL = "SELECT NAME, RATE,\n"
      + " SUM(DOWNLOAD) AS DOWNLOAD_MB,\n" + " SUM(UPLOAD) AS UPLOAD_MB,\n"
      + " SUM(UPLOAD * RATE)\n" + "  + SUM(DOWNLOAD * RATE) AS AMOUNT\n" + "FROM CUST,\n"
      + "     AGREEMENT\n" + "     LEFT JOIN USAGE ON (CUST.ID=AGREEMENT.CUST_ID)\n"
      + "WHERE CUST.ID = AGREEMENT.CUST_ID\n" + "GROUP BY NAME,\n" + "         RATE;\n";
}
