package Oauth;
import Pojo.Api;
import Pojo.GetCourse;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.reporters.PojoReporterConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class Oauth1 {

    public static void main(String[] args) {

        String[] courseTitles= { "Selenium Webdriver Java","Cypress","Protractor"};


        String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0ARtbsJo1SgPJA0YekTeNazNhjD0ApSezKHj4tYDjhYr4ImCBeIUGrm_29uglozqP_2LVdQ&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
        String partialcode=url.split("code=")[1];

        String code=partialcode.split("&scope")[0];

        System.out.println(code);

       String acessTokenResponse = given().queryParams("code",code).queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .queryParams("client_secret","erZOWM9g3UtwNRj340YYaK_W")
                .queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
                .queryParams("grant_type","authorization_code")
                .when()
                .post("https://www.googleapis.com/oauth2/v4/token").asString();

        JsonPath js = new JsonPath(acessTokenResponse);

          String accrsstoken =  js.getString("access_token");


         GetCourse gc  =  given().queryParam("access_token",accrsstoken).expect().defaultParser(Parser.JSON)
                 .when().get("https://rahulshettyacademy.com/getCourse.php"). as(GetCourse.class);
                // asString();

        //System.out.println(response);

        System.out.println(gc.getLinkedIn());
        System.out.println(gc.getInstructor());
        System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());

        List<Api> apiCourses= gc.getCourses().getApi();
        for (int i=0;i<apiCourses.size();i++){

            if (apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices Testing")){

                System.out.println(apiCourses.get(i).getPrice());

            }

        }

        //Get the course names of WebAutomation
        ArrayList<String> a= new ArrayList<String>();


        List<Pojo.Webautomation> w=gc.getCourses().getWebAutomation();

        for(int j=0;j<w.size();j++)
        {
            a.add(w.get(j).getCourseTitle());
        }

        List<String> expectedList=	Arrays.asList(courseTitles);

        Assert.assertTrue(a.equals(expectedList));

}}
