package userManagement;

import core.BaseTest;
import core.StatusCode;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.ExtentReport;
import utils.JsonReader;
import utils.PropertyReader;
import utils.SoftAssertionUtil;

import java.io.IOException;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.assertEquals;
import static utils.JsonReader.getJsonArray;

public class getUsers extends BaseTest {
    @Test
    public void getUserData() {
        given().
                when().get("https://reqres.in/api/users?page=2").
                then().
                assertThat().
                statusCode(200);

    }

    @Test
    public void validateGetResponseBody() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        given()
                .when()
                .get("/todos/1")
                .then()
                .assertThat()
                .statusCode(200)
                .body(not(isEmptyString()))
                .body("title", equalTo("delectus aut autem"))
                .body("userId", equalTo(1));
    }

    @Test
    public void validateResponseHasItems() {
        // Set base URI for the API
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        // Send a GET request and store the response in a variable
        Response response = given()
                .when()
                .get("/posts")
                .then()
                .extract()
                .response();

        // Use Hamcrest to check that the response body contains specific items
        assertThat(response.jsonPath().getList("title"), hasItems("sunt aut facere repellat provident occaecati excepturi optio reprehenderit", "qui est esse"));
    }

    @Test
    public void validateResponseHasSize() {
        // Set base URI for the API
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        // Send a GET request and store the response in a variable
        Response response = given()
                .when()
                .get("/comments")
                .then()
                .extract()
                .response();

        // Use Hamcrest to check that the response body has a specific size
        assertThat(response.jsonPath().getList(""), hasSize(500));
    }

    @Test
    public void validateListContainsInOrder() {
        // Set base URI for the API
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        // Send a GET request and store the response in a variable
        Response response = given()
                .when()
                .get("/comments?postId=1")
                .then()
                .extract()
                .response();

        // Use Hamcrest to check that the response body contains specific items in a specific order
        List<String> expectedEmails = Arrays.asList("Eliseo@gardner.biz", "Jayne_Kuhic@sydney.com", "Nikita@garfield.biz", "Lew@alysha.tv", "Hayden@althea.biz");
        assertThat(response.jsonPath().getList("email"), contains(expectedEmails.toArray(new String[0])));
    }

    @Test
    public void testGetUsersWithQueryParameters() {
        Response response = given()
                .queryParam("page", 2)
                .when()
                .get("https://reqres.in/api/users")
                .then()
                .statusCode(200)
                .extract()
                .response();

        // Assert that the response contains 6 users
        response.then().body("data", hasSize(6));

        // Assert that the first user in the list has the correct values
        response.then().body("data[0].id", is(7));
        response.then().body("data[0].email", is("michael.lawson@reqres.in"));
        response.then().body("data[0].first_name", is("Michael"));
        response.then().body("data[0].last_name", is("Lawson"));
        response.then().body("data[0].avatar", is("https://reqres.in/img/faces/7-image.jpg"));
    }

    @Test(description = "Validate the status code for GET users endpoint")
    public void validateStatusCodeGetUser() {

        //System.out.println("URI is ******:" + uri);

        Response resp = given()
                .queryParam("page", 2)
                .when()
                .get("https://reqres.in/api/users"); //RestAssured

        int actualStatusCode = resp.statusCode();  //RestAssured
        assertEquals(actualStatusCode, 200); //Testng
    }


    @Test
    public void testGetUsersWithMultipleQueryParams() {
        Response response =
                given()
                        .queryParam("page", 2)
                        .queryParam("per_page", 3)
                        .queryParam("rtqsdr", 4)
                        .when()
                        .get("https://reqres.in/api/users")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        // Assert that the response contains 6 users
        response.then().body("data", hasSize(3));

        // Assert that the first user in the list has the correct values
        response.then().body("data[0].id", is(4));
        response.then().body("data[0].email", is("eve.holt@reqres.in"));
        response.then().body("data[0].first_name", is("Eve"));
        response.then().body("data[0].last_name", is("Holt"));
        response.then().body("data[0].avatar", is("https://reqres.in/img/faces/4-image.jpg"));
    }

    @Test(description = "Validate the status code for GET users endpoint")
    public void validateResponseBodyGetPathParam() {

        String raceSeasonValue = "2017";
        Response resp = given()
                .pathParam("raceSeason", raceSeasonValue)
                .when()
                .get("http://ergast.com/api/f1/{raceSeason}/circuits.json"); //RestAssured

        int actualStatusCode = resp.statusCode();  //RestAssured
        assertEquals(actualStatusCode, 200); //Testng
        System.out.println(resp.body().asString());

    }

    @Test
    public void testCreateUserWithFormParam() {
        Response response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("name", "John Doe")
                .formParam("job", "Developer")
                .when()
                .post("https://reqres.in/users")
                .then()
                .statusCode(201)
                .extract()
                .response();

        // Assert that the response contains the correct name and job values
        response.then().body("name", equalTo("John Doe"));
        response.then().body("job", equalTo("Developer"));
    }

    @Test
    public void testGetUserListWithHeader() {
        given()
                .header("Content-Type", "application/json")
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200);
        System.out.println("testGetUserListWithHeader Executed Successfully");
    }


    @Test
    public void testWithTwoHeaders() {
        given()
                .header("Authorization", "bearer ywtefdu13tx4fdub1t3ygdxuy3gnx1iuwdheni1u3y4gfuy1t3bx")
                .header("Content-Type", "application/json")
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200);
        System.out.println("testWithTwoHeaders Executed Successfully");

    }

    @Test
    public void testGetUserList() {
        // Set base URI for the API
        RestAssured.baseURI = "https://reqres.in/api";

        // Create a Map to hold headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer <your_token_here>");

        // Send a GET request with headers
        given()
                .headers(headers)
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .body("page", equalTo(2))
                .body("data[0].first_name", equalTo("Michael"))
                .body("data[0].last_name", equalTo("Lawson"));
    }

    @Test
    public void testFetchHeaders() {
        Response response = given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .extract().response();

        Headers headers = response.getHeaders();
        //if you want to print all headers then comment 244,246,247
        for (Header h : headers) {
            if (h.getName().contains("Server")) {
                System.out.println(h.getName() + " : " + h.getValue());
                assertEquals(h.getValue(), "cloudflare");
                System.out.println("testFetchHeaders Executed Successfully");
            }
        }
    }

    @Test
    public void testUseCookies() {

        // Send the request with the cookies
        given()
                .cookie("test1", "testing1")
                .cookie("test2", "testing2")
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .body("cookies.cookie1", equalTo("value1"))
                .body("cookies.cookie2", equalTo("value2"));
    }

    @Test
    public void testUseCookiesBuilder() {
        Cookie cookies = new Cookie.Builder("cookieKey1", "cookieValue1")
                .setComment("using cookie key").build();

        // Send the request with the cookies
        given()
                .cookie(cookies)
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200);
    }

    @Test
    public void testMultipleCookies() {
        Response response = given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .extract().response();

        Map<String, String> cookies = response.getCookies();
        assertThat(cookies, hasKey("JSESSIONID"));
        assertThat(cookies, hasValue("ABCDEF123456"));
    }

    @Test
    public void testDetailedCookies() {
        Response response = given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .extract().response();

        Map<String, String> cookies = response.getCookies();

        Cookies cookies1 = response.getDetailedCookies();
        cookies1.getValue("server");
        assertEquals(cookies1.getValue("server"), "cloudflare");

    }

    @Test(description = "Validate the status code for GET users endpoint")
    public void validateResponseBodyGetBasicAuth() {

        Response resp = given()
                .auth()
                .basic("postman", "password")
                .when()
                .get("https://postman-echo.com/basic-auth"); //RestAssured

        int actualStatusCode = resp.statusCode();  //RestAssured
        assertEquals(actualStatusCode, 200); //Testng
        System.out.println(resp.body().asString());

    }

    @Test()
    public void validateResponseBodyGetDigestAuth() {

        Response resp = given()
                .auth()
                .digest("postman", "password")
                .when()
                .get("https://postman-echo.com/digest-auth"); //RestAssured

        int actualStatusCode = resp.statusCode();  //RestAssured
        assertEquals(actualStatusCode, 200); //Testng
        System.out.println(resp.body().asString());
    }

    @Test
    public void verifyStatusCodeDelete() {

        Response resp = given().delete("https://reqres.in/api/users/2");
        // assertEquals(resp.getStatusCode(),204);
        assertEquals(resp.getStatusCode(), StatusCode.NO_CONTENT.code);

        System.out.println("***********************************PASS*******************************");

    }


    @Test()
    public void validateWithTestDataFromJson() throws IOException, ParseException {
//        String username = JsonReader.getTestData("username");
//        String password = JsonReader.getTestData("password");
        //       System.out.println("username from json is: " + username + "***password from json is:" + password);
        Response resp = given()
                .auth()
                .basic(JsonReader.getTestData("username"), JsonReader.getTestData("password"))
                .when()
                .get("https://postman-echo.com/basic-auth"); //RestAssured

        int actualStatusCode = resp.statusCode();  //RestAssured
        assertEquals(actualStatusCode, 200); //Testng
        System.out.println("validateWithTestDataFromJson executed successfully");
    }

    @Test()
    public void validateFromProperties_TestData() throws IOException, ParseException {
        String serverAddress = PropertyReader.propertyReader("config.properties", "server1");
        String endpoint = JsonReader.getTestData("endpoint");
        String URL = serverAddress + endpoint;
        System.out.println("URL  is : " + URL);
        Response resp =
                given()
                        .queryParam("page", 2)
                        .when()
                        .get(URL);
        int actualStatusCode = resp.statusCode();  //RestAssured
        assertEquals(actualStatusCode, 200); //Testng
        System.out.println("validateFromProperties_TestData executed successfully" + URL);
    }

    @Test()
    public void validateDataFromPropertiesFile() throws IOException, ParseException {
        String serverAddress = PropertyReader.propertyReader("config.properties", "server");

        System.out.println("server address  is : " + serverAddress);
        Response resp =
                given()
                        .queryParam("page", 2)
                        .when()
                        .get(serverAddress);
        int actualStatusCode = resp.statusCode();  //RestAssured
        assertEquals(actualStatusCode, 200); //Testng
        System.out.println("validateDataFromPropertiesFile executed successfully" + serverAddress);
    }

    public String getUrl(String key) {
        String endpoint = null;
        try {
            endpoint = JsonReader.getTestData(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return endpoint;
    }

    @Test
    public void hardAssertion() {
        System.out.println("hardAssert");
        Assert.assertTrue(false);
        Assert.assertTrue(false);
        Assert.assertTrue(false);
        System.out.println("hardAssert");
    }

    @Test(description="branch")
    public void softAssertion() {

        System.out.println("softAssert");
        SoftAssertionUtil.assertTrue(true, "");
        SoftAssertionUtil.assertAll();
    }

    @Test
    public void validateWithSoftAssertUtil() {
        RestAssured.baseURI = "https://reqres.in/api";
        Response response = given()
                .queryParam("page", 2)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .extract()
                .response();

        SoftAssertionUtil.assertEquals(response.getStatusCode(), StatusCode.SUCCESS.code, "Status code is not 200");
        SoftAssertionUtil.assertAll();
        System.out.println("validateWithSoftAssertUtil executed successfully");
    }

    @DataProvider(name = "testdata")
    public Object[][] testData() {
        return new Object[][]{
                {"1", "John"},
                {"2", "Jane"},
                {"3", "Bob"}
        };
    }

    @Test(dataProvider = "testdata")
    @Parameters({"id", "name"})
    public void testEndpoint(String id, String name) {
        given()
                .queryParam("id", id)
                .queryParam("name", name)
                .when()
                .get("https://reqres.in/api/users")
                .then()
                .statusCode(200);
    }

    //groups smoke suite
    @Test(groups = {"SmokeSuite", "RegressionSuite"})
    public void verifyStatusCodeDelete1() {
        ExtentReport.extentlog =
                ExtentReport.extentreport.
                        startTest("verifyStatusCodeDelete", "Validate 204 status code for DELETE Method");

        Response resp = given().delete("https://reqres.in/api/users/2");
        // assertEquals(resp.getStatusCode(),204);
        assertEquals(resp.getStatusCode(), StatusCode.NO_CONTENT.code);

        System.out.println("***********************************PASS*******************************");

    }

    //groups smoke suite
    @Test(groups = "RegressionSuite")
    public void validateDataFromPropertiesFile1() throws IOException, ParseException {
        ExtentReport.extentlog =
                ExtentReport.extentreport.
                        startTest("validateWithDataFromPropertiesFile1", "Validate 200 Status Code for GET method");
        String serverAddress = PropertyReader.propertyReader("config.properties", "server");

        System.out.println("server address  is : " + serverAddress);
        Response resp =
                given()
                        .queryParam("page", 2)
                        .when()
                        .get(serverAddress);
        int actualStatusCode = resp.statusCode();  //RestAssured
       // assertEquals(actualStatusCode, 200); //Testng
        assertEquals(actualStatusCode, 400);
        System.out.println("validateDataFromPropertiesFile executed successfully" + serverAddress);
    }

    @Test
    public void Test() throws IOException, ParseException {
        System.out.println(JsonReader.getJsonArrayData("languages", 0));
    }

    @Test
    public void Test1() throws IOException, ParseException {
        JsonReader.getJsonArrayData("languages", 2);
        System.out.println(JsonReader.getJsonArrayData("languages", 2));//to print the value use syso
        JSONArray jsonArray = getJsonArray("contact");
        Iterator<String> iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}

