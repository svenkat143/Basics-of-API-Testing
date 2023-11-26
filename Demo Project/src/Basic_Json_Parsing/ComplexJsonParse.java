// This is the second file after Basics.java 
// In this file we will learn about how to parse a complex JSON response and get the desired values.
// This Actual JSON and the tasks to be performed are explained in Complexjson.txt.
// The Actual JSON converted into string format is present in CoursePrice() of the Payload.java file.

package Basic_Json_Parsing;

import Basic_Json_Parsing.files.payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {

		// This process of getting response from real API is explained in Basics.java file.
        //Here we are passing the dummy response from the CoursePrice() of Payload.
        JsonPath js=new JsonPath(payload.CoursePrice());
		
//Task 1: Print No of courses returned by API Response.
// size() is a method of JsonPath class that is used to get the size of an array in json.
int count=	js.getInt("courses.size()");
System.out.println("The Courses size is "+ count);

//Task 2: Print the Purchase Amount of all courses.
int totalAmount= js.getInt("dashboard.purchaseAmount");
System.out.println("The Total Amount of all courses is " +totalAmount);

//Task 3: Print Title of the Second course.
// Since array index starts from 0 index of second course is 1.
  String titleSecondCourse=js.get("courses[1].title");
  System.out.println("The Title of the Second course is" +titleSecondCourse);
  
//Task 4: Print All course titles and their respective Prices
   for(int i=0; i<count; i++)
  {
	  String courseTitles=js.get("courses["+i+"].title");
      System.out.println("The Title of the course["+i+"] is: " +courseTitles);
	  System.out.println("The Price of the "+courseTitles+" is: " +js.get("courses["+i+"].price").toString());
  
  }
 
  
//Task 5: Print no of copies sold by RPA Course.
 System.out.println("Print no of copies sold by RPA Course");
 for(int i=0;i<count;i++)
 {
	  String courseTitles=js.get("courses["+i+"].title");
	  if(courseTitles.equalsIgnoreCase("RPA"))
	  {
		  int copies=js.get("courses["+i+"].copies");
		  System.out.println("No of copies sold by RPA Course is: " +copies);
		  break;
	  }
 } 
 
	}

}

//After this refer to SumValidation.java file.