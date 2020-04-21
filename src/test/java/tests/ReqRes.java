package tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;

public class ReqRes extends BaseTest {

    @Test
    public void getListUsers() {
        String expected = "{\"page\":2,\"per_page\":6,\"total\":123,\"total_pages\":2,\"data\":[{\"id\":7,\"email\":\"michael.lawson@reqres.in\",\"first_name\":\"Michael\",\"last_name\":\"Lawson\",\"avatar\":\"https://s3.amazonaws.com/uifaces/faces/twitter/follettkyle/128.jpg\"},{\"id\":8,\"email\":\"lindsay.ferguson@reqres.in\",\"first_name\":\"Lindsay\",\"last_name\":\"Ferguson\",\"avatar\":\"https://s3.amazonaws.com/uifaces/faces/twitter/araa3185/128.jpg\"},{\"id\":9,\"email\":\"tobias.funke@reqres.in\",\"first_name\":\"Tobias\",\"last_name\":\"Funke\",\"avatar\":\"https://s3.amazonaws.com/uifaces/faces/twitter/vivekprvr/128.jpg\"},{\"id\":10,\"email\":\"byron.fields@reqres.in\",\"first_name\":\"Byron\",\"last_name\":\"Fields\",\"avatar\":\"https://s3.amazonaws.com/uifaces/faces/twitter/russoedu/128.jpg\"},{\"id\":11,\"email\":\"george.edwards@reqres.in\",\"first_name\":\"George\",\"last_name\":\"Edwards\",\"avatar\":\"https://s3.amazonaws.com/uifaces/faces/twitter/mrmoiree/128.jpg\"},{\"id\":12,\"email\":\"rachel.howell@reqres.in\",\"first_name\":\"Rachel\",\"last_name\":\"Howell\",\"avatar\":\"https://s3.amazonaws.com/uifaces/faces/twitter/hebertialmeida/128.jpg\"}," +
                "\"ad\":{\"company\":\"StatusCode Weekly\",\"url\":\"http://statuscode.org/\",\"text\":\"A weekly newsletter focusing on software development, infrastructure, the server, performance, and the stack end of things.\"}}";
        Response response = when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON).extract().response();
        assertEquals(expected, response.asString().trim());
    }

    @Test
    public void getSingleUser() {
        String expected = "{\"data\":{\"id\":2,\"email\":\"janet.weaver@reqres.in\",\"first_name\":\"Janet\",\"last_name\":\"Weaver\",\"avatar\":\"https://s3.amazonaws.com/uifaces/faces/twitter/josephstein/128.jpg\"}," +
                "\"ad\":{\"company\":\"StatusCode Weekly\",\"url\":\"http://statuscode.org/\",\"text\":\"A weekly newsletter focusing on software development, infrastructure, the server, performance, and the stack end of things.\"}}";
        Response response = when()
                .get("https://reqres.in/api/users/2")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(JSON).extract().response();
        assertEquals(expected, response.asString().trim());
    }

    @Test
    public void getSingleUserNotFound() {
        String expected = "{}";
        Response response = when()
                .get("https://reqres.in/api/users/23")
                .then()
                .log().all()
                .statusCode(404)
                .contentType(JSON).extract().response();
        assertEquals(expected, response.asString().trim());
    }

    @Test
    public void getSingleResource() {
        String expected = "{\"data\":{\"id\":2,\"name\":\"fuchsia rose\",\"year\":2011,\"color\":\"#C74375\",\"pantone_value\":\"17-2031\"}," +
                "\"ad\":{\"company\":\"StatusCode Weekly\",\"url\":\"http://statuscode.org/\",\"text\":\"A weekly newsletter focusing on software development, infrastructure, the server, performance, and the stack end of things.\"}}";
        Response response = when()
                .get("https://reqres.in/api/unknown/2")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(JSON).extract().response();
        assertEquals(expected, response.asString().trim());
    }

    @Test
    public void getSingleResourceNotFound() {
        String expected = "{}";
        Response response = when()
                .get("https://reqres.in/api/unknown/23")
                .then()
                .log().all()
                .statusCode(404)
                .contentType(JSON).extract().response();
        assertEquals(expected, response.asString().trim());
    }

    @Test
    public void postCreateUser() {
        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .body("{\"name\":\"morpheus\",\"job\":\"leader\"}")
                        .when()
                        .post("https://reqres.in/api/users")
                        .then()
                        .statusCode(201)
                        .contentType(ContentType.JSON).extract().response();
    }

    @Test
    public void putUpdateUser() {
        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .body("{\"name\":\"morpheus\",\"job\":\"zion resident\"}")
                        .when()
                        .put("https://reqres.in/api/users/2")
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON).extract().response();
    }

    @Test
    public void patchUpdateUser() {
        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .body("{\"name\":\"morpheus\",\"job\":\"zion resident\"}")
                        .when()
                        .patch("https://reqres.in/api/users/2")
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON).extract().response();
    }

    @Test
    public void deleteUser() {
        when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    public void postRegisterSuccessful() {
        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .body("{\"email\":\"eve.holt@reqres.in\",\"password\":\"pistol\"}")
                        .when()
                        .post("https://reqres.in/api/register")
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON).extract().response();
    }

    @Test
    public void postRegisterUnsuccessful() {
        given()
                .body("{\"email\":\"sydney@fife\"}")
                .header("Content-Type", "application/json");
        when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void postLoginSuccessful() {
        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .body("{\"email\":\"eve.holt@reqres.in\",\"password\":\"cityslicka\"}")
                        .when()
                        .post("https://reqres.in/api/login")
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON).extract().response();
    }

    @Test
    public void postLoginUnsuccessful() {
        given()
                .body("{\"email\":\"peter@klaven\"}")
                .header("Content-Type", "application/json");
        when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }
}