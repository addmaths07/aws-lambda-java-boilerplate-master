package com.example.lambda;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.config.SpringConfig;
import com.example.services.Service;
import com.example.model.Employee;
import com.example.services.EmployeeService;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;

/**
 * Handler class should extend AbstractHandler<T> abstract class
 * T should be a Spring @Configuration class for Spring DI manager
 */
@SuppressWarnings("unused")
public class MainHandler
        extends AbstractHandler<SpringConfig>
        implements RequestHandler<MainHandler.Request, MainHandler.Response> {

    public MainHandler() {
        /**
         * Set example event for local running
         */
        exampleEvent = "{\"firstName\":\"John\",\"lastName\":\"John\"}";
    }

    /**
     * Standard logger. For logger configuration check resources/log4j.properties file.
     *
     * @link http://docs.aws.amazon.com/lambda/latest/dg/java-logging.html
     */
    static final Logger log = Logger.getLogger(MainHandler.class);


    /**
     * Request class is a POJO. You should modify this class according to your needs.
     * <p>
     * Event json that lambda function got is automatically serialized to this POJO. For more details see Lambda documentation:
     *
     * @link http://docs.aws.amazon.com/lambda/latest/dg/java-handler-io-type-pojo.html
     */
    public static class Request {
        String firstName;
        String lastName;

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

        public Request(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public Request() {
        }
    }

    /**
     * Response is also a POJO that will handler return and
     * Lambda runtime automatically serializes it to JSON. Again see the documentation.
     *
     * @link http://docs.aws.amazon.com/lambda/latest/dg/java-handler-io-type-pojo.html
     */
    public static class Response {
        String greetings;

        public String getGreetings() {
            return greetings;
        }

        public void setGreetings(String greetings) {
            this.greetings = greetings;
        }

        public Response(String greetings) {
            this.greetings = greetings;
        }

        public Response() {
        }

    }


    /**
     * Main handler method is invoked when Lambda function is invoked. You should configure the name of this method in the AWS Console.
     * In this example, the value would be com.cagataygurturk.lambda.MainHandler. As we implement RequestHandler interface Lambda runtime
     * detects this method automatically and invokes it.
     * <p>
     * <p>
     * <p>
     * As a best practice, this method should be kept very short and all the business logic should sit
     * in "Service" instance that we will fetch from Spring IoC container and will enjoy from all
     * Spring IoC features.
     * <p>
     * Lambda specific code ends here and beginning from this point old good JAVA starts.
     *
     * @param request Request object
     * @param context Context object
     * @return Response
     * @throws RuntimeException
     * @see RequestHandler
     */
    public Response handleRequest(Request request, Context context)
            throws RuntimeException {

        /**
         * BusinessService is where all our business logic sits.
         */
        Service businessService = getApplicationContext().getBean(Service.class);
        
        /*
        Statement stmt = null;
        try{
           
           stmt = getConnection().createStatement();
           
           String sql = "INSERT INTO REGISTRATION columns(id, first, last, age) values(1, 'abc', 'ras', 12)";
           stmt.executeUpdate(sql);
           System.out.println("Created table in given database...");
        }catch(SQLException se){
           //Handle errors for JDBC
           se.printStackTrace();
        }catch(Exception e){
           //Handle errors for Class.forName
           e.printStackTrace();
        }finally{
           //finally block used to close resources
           try{
              if(stmt!=null)
            	  getConnection().close();
           }catch(SQLException se){
           }// do nothing
           try{
              if(getConnection()!=null)
            	  getConnection().close();
           }catch(SQLException se){
              se.printStackTrace();
           }//end finally try
        }
        */
        
        EmployeeService service = (EmployeeService) getApplicationContext().getBean("employeeService");
        
        /*
         * Create Employee1
         */
        Employee employee1 = new Employee();
        employee1.setName("Han Yenn");
        employee1.setJoiningDate(new LocalDate(2010, 10, 10));
        employee1.setSalary(new BigDecimal(10000));
        employee1.setSsn("ssn00000001");
 
        /*
         * Create Employee2
         */
        Employee employee2 = new Employee();
        employee2.setName("Dan Thomas");
        employee2.setJoiningDate(new LocalDate(2012, 11, 11));
        employee2.setSalary(new BigDecimal(20000));
        employee2.setSsn("ssn00000002");
 
        /*
         * Persist both Employees
         */
        service.saveEmployee(employee1);
        service.saveEmployee(employee2);
 
//        /*
//         * Get all employees list from database
//         */
//        List<Employee> employees = service.findAllEmployees();
//        for (Employee emp : employees) {
//            System.out.println(emp);
//        }
// 
//        /*
//         * delete an employee
//         */
//        service.deleteEmployeeBySsn("ssn00000002");
// 
//        /*
//         * update an employee
//         */
// 
//        Employee employee = service.findBySsn("ssn00000001");
//        employee.setSalary(new BigDecimal(50000));
//        service.updateEmployee(employee);
// 
        /*
         * Get all employees list from database
         */
        List<Employee> employeeList = service.findAllEmployees();
        for (Employee emp : employeeList) {
            System.out.println(emp);
        }
 
        //context.close();
        
        return new Response(businessService.getText(request.getFirstName() + " " + request.getLastName()));
    }
}
