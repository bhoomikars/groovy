/////////////////////////////////////////////////////////////
//
// CR: SM-26065 DF: [DF] Need to retrieve Supplier Registration Project
//
// Set RegistrationStatus to 'Registered' for few Vendors for SURA customer.
// Realm -  SURA
// TENANT_ID - 1266
////////////////////////////////////
//
// Steps for OPS:
//
// 1. In Ops page, go to 'USA (prod) data centre
// 2. Go to inspector by clicking on SUPPLIERMANAGEMENT->Http Urls Status>Default Community>SupplierManagementApp>ins
// 3. In the 'Supplier Management Inspector' page, click on 'Script Executor'
// 4. Copy the below query into the 'Groovy Script' section and click 'Execute'.
// 5. Paste the results into the HOA ticket
//
//////////////////////////////////////////////////////////////////////////////

import ariba.sm.util.PropertiesUtil
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder

import javax.sql.DataSource
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import java.sql.SQLException
import java.util.regex.Pattern
import ariba.sm.api.db.DataSourceConfig


DataSource dataSource = getDataSource(false);
StringBuilder logMessage = new StringBuilder();
Connection conn = null;
int tenantId = 1266; // SURA

String smVendor_id_list="('S12278145','S12278147','S12278148','S12278155','S12278168','S12294809','S12294853','S12294865','S12294878','S12295114','S12295124','S12295142','S12295167','S12295170','S12295177' \
'S12295193','S12295207','S12295209','S12295212','S12295213','S12295219','S12295223','S12295230','S12295231','S12295232','S12295233','S12295235','S12295239','S12295243' \
'S12295245','S12295248' ,'S12295252','S12295263', 'S12295265', 'S1229527','S12295281','S12295295', 'S12295308','S12295337', 'S12295343', 'S12295344', 'S12295345', 'S12295348', 'S12295357', 'S12295363', 'S12295368'\
'S12295369', 'S12295378','S12295380','S12295389','S12295393','S12295395','S12295501','S12295502','S12295505','S12295506','S12295512','S12295514','S12295516'\
'S12295528','S12295529','S12295540','S12295547','S12295554','S12295555','S12295557','S12295558','S12295559','S12295560','S12295564','S12295569','S12295574','S12295585','S12295596'\
'S12295822','S12295829','S12295831','S12295836','S12295845','S12295851'\
'S12295856','S12333712','S12410944','S12412064','S12447587','S12456484','S12536596','S12604220','S12635029','S12635624','S12637274','S12649698','S12649699','S12650900','S12650907'\
'S12650913','S12681854','S12760482','S12847796','S12847902','S12847909')";

String selectRegistrationQuery="SELECT SM_VENDOR_ID, REGISTRATION_STATUS " +
        " FROM VENDOR " +
        " WHERE " +
        " SM_VENDOR_ID IN " + smVendor_id_list +
        " AND TENANT_ID=" + tenantId;

// PendingResubmit
String updateRegistrationQuery1="UPDATE VENDOR SET REGISTRATION_STATUS='PendingResubmit' " +
        " WHERE " +
        " SM_VENDOR_ID IN ('S12278145','S12294853','S12295114','S12295167','S12295213','S12295233','S12295263','S12295295','S12295337','S12295345','S12295348','S12295368','S12295395'\
        'S12295502','S12295506','S12295512','S12295528','S12295529','S12295540','S12295554','S12295555','S12295558','S12295564','S12295585','S12295596','S12295822'\
        'S12295829','S12333712','S12410944','S12604220') "
        " AND TENANT_ID=" + tenantId;

// Registered
String updateRegistrationQuery2="UPDATE VENDOR SET REGISTRATION_STATUS='Registered' " +
        " WHERE " +
        " SM_VENDOR_ID IN ('S12278155','S12278168','S12294865','S12294878','S12295142','S12295223','S12295248','S12295281','S12295363','S12295505'\
        'S12295514','S12295516','S12295547','S12295559','S12295574','S12295856','S12847902','S12847909') "
        " AND TENANT_ID=" + tenantId;

 // Invited
String updateRegistrationQuery3="UPDATE VENDOR SET REGISTRATION_STATUS='Invited' " +
        " WHERE " +
        " SM_VENDOR_ID IN ('S12278148','S12295193','S12295212','S12295219','S12295230','S12295231','S12295232','S12295239','S12295243','S12295245','S12295276','S12295343','S12295357','S12295369','S12295378'\
        'S12295380','S12295389','S12295557','S12295560','S12295831','S12295836','S12295845','S12295851','S12295857','S12412064','S12456484','S12536596','S12635029','S12637274','S12650900'\
        'S12650907','S12650913','S12681854') "
        " AND TENANT_ID=" + tenantId;

  //InRegistration
String updateRegistrationQuery4="UPDATE VENDOR SET REGISTRATION_STATUS='InRegistration' " +
        " WHERE " +
        " SM_VENDOR_ID IN ('S12278147','S12294809','S12295124','S12295170','S12295177','S12295207','S12295209','S12295235','S12295265','S12295308','S12295344','S12295393','S12295501'\
        'S12295569','S12447587','S12635624','S12649698','S12649699','S12760482','S12847796') "
        " AND TENANT_ID=" + tenantId;

    // PendingApproval
  String updateRegistrationQuery5="UPDATE VENDOR SET REGISTRATION_STATUS='PendingApproval' " +
         " WHERE " +
         " SM_VENDOR_ID IN ('S12295252') "
         " AND TENANT_ID=" + tenantId;

try {
    conn = dataSource.getConnection();

    Statement stmt = conn.createStatement();
    ResultSet rs = null;

    /////////////////////////////////////////////////////////////////////////////////////////////
    // Select Query
    /////////////////////////////////////////////////////////////////////////////////////////////
    logMessage.append("----- BEFORE UPDATE IN REGISTRATION TABLE -----\n");
    rs = stmt.executeQuery(selectRegistrationQuery);
    while (rs.next()) {
        for (int i = 1; i <= 2; i++) {
            logMessage.append(rs.getString(i)).append(';\t');
        }
        logMessage.append("\n");
    }
    rs.close();
    /////////////////////////////////////////////////////////////////////////////////////////////

    // Update Query1
    /////////////////////////////////////////////////////////////////////////////////////////////
    logMessage.append("----- UPDATING Query1 -----\n");
    int result = stmt.executeUpdate(updateRegistrationQuery1);
    logMessage.append("Updated Results for :"+result+" rows\n");
    /////////////////////////////////////////////////////////////////////////////////////////////

    // Update Query2
    /////////////////////////////////////////////////////////////////////////////////////////////
    logMessage.append("----- UPDATING Query2 -----\n");
    result = stmt.executeUpdate(updateRegistrationQuery2);
    logMessage.append("Updated Results for :"+result+" rows\n");
    /////////////////////////////////////////////////////////////////////////////////////////////


    // Update Query3
    /////////////////////////////////////////////////////////////////////////////////////////////
    logMessage.append("----- UPDATING Query3 -----\n");
    result = stmt.executeUpdate(updateRegistrationQuery3);
    logMessage.append("Updated Results for :"+result+" rows\n");
    /////////////////////////////////////////////////////////////////////////////////////////////


    // Update Query4
    /////////////////////////////////////////////////////////////////////////////////////////////
    logMessage.append("----- UPDATING Query4 -----\n");
    result = stmt.executeUpdate(updateRegistrationQuery4);
    logMessage.append("Updated Results for :"+result+" rows\n");
    /////////////////////////////////////////////////////////////////////////////////////////////


    // Update Query5
    /////////////////////////////////////////////////////////////////////////////////////////////
    logMessage.append("----- UPDATING Query5 -----\n");
    result = stmt.executeUpdate(updateRegistrationQuery5);
    logMessage.append("Updated Results for :"+result+" rows\n");
    /////////////////////////////////////////////////////////////////////////////////////////////



    // Select Query
    /////////////////////////////////////////////////////////////////////////////////////////////
    logMessage.append("----- AFTER UPDATE in REGISTRATION TABLE -----\n")
    rs = stmt.executeQuery(selectRegistrationQuery);
    while (rs.next()) {
        for (int i = 1; i <= 2; i++) {
            logMessage.append(rs.getString(i)).append(';\t')
        }
        logMessage.append("\n")
    }
    rs.close();
    /////////////////////////////////////////////////////////////////////////////////////////////

} catch (Exception sqlException) {
    String lineNum = extractLineNum(sqlException);
    String msg = "**************************** at: " + lineNum + " Stacktrace=" + sqlException.stackTrace;
    System.out.println(msg);
    logMessage.append(msg);
} finally {
    try {
        if (conn != null) conn.close();
    } catch (SQLException sqlException) {
        String lineNum = extractLineNum(sqlException);
        String msg = "**************************** at: " + lineNum + " Stacktrace=" + sqlException.stackTrace;
        System.out.println(msg);
        logMessage.append(msg);
    }
}

logMessage.append("SCRIPT EXECUTION COMPLETED\n").append(new Date());
logMessage.toString();

private DataSource getDataSource(boolean sys)
{
    String dbType = sys ? "hanasmsys" : "hanasuppliermanagement";
    String serverId =
            PropertiesUtil.stringValueForKey("DBConnections." + dbType + ".serverId");

    String dbNamePropertyQualifier = "";

    String username = PropertiesUtil.stringValueForKey("DBConnections." + dbType + ".username");
    String password = PropertiesUtil.stringValueForKey("DBConnections." + dbType + ".password");
    String port = PropertiesUtil.stringValueForKey("DBConnections." + dbType + "" + dbNamePropertyQualifier + ".port")
    String hostName =  PropertiesUtil.stringValueForKey("DBConnections." + dbType + "" + dbNamePropertyQualifier + ".hostname")

    String connStr = DataSourceConfig.buildConnectionString(hostName, port, serverId, false);

    DataSourceBuilder factory = DataSourceBuilder
            .create(this.getClass().getClassLoader())
            .driverClassName(PropertiesUtil.stringValueForKey("DBConnections.driver"))
            .username(username)
            .password(password)
            .url(connStr);

    DataSource dataSource = factory.build();

    return dataSource;
}

private String extractLineNum(Exception exception)
{
    if (exception == null || exception.getStackTrace() == null)
        return null;

    String s = exception.getStackTrace();
    String regex = ".*?Script1.groovy:([0-9]*)\\).*?";
    Pattern pattern = Pattern.compile(regex);
    def matcher = pattern.matcher(s);
    if (matcher.find() && matcher.hasGroup()) {
        String g = matcher.group()
        return g;
    }
    return null;
}

///////////////////////////////////////////////////////////////////////////////////////////////
//// Expected output for the script
///////////////////////////////////////////////////////////////////////////////////////////////
//  ----- BEFORE UPDATE -----
//  S12278155;  Invited
//  S12278168;  Invited
//  ----- UPDATING -----
//  Updated Results for :3 rows
//  ----- AFTER UPDATE -----
//  S12278155;  Registered
//  S12278168;  Registered
///////////////////////////////////////////////////////////////////////////////////////////////
////  SCRIPT EXECUTION COMPLETED
///////////////////////////////////////////////////////////////////////////////////////////////
