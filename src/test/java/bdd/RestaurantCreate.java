package bdd;


import com.github.rafaelfernandes.restaurant.adapter.in.web.request.AddressRequest;
import com.github.rafaelfernandes.restaurant.adapter.in.web.request.RestaurantRequest;
import io.restassured.http.ContentType;
import util.GenerateData;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.platform.engine.Constants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = Constants.FEATURES_PROPERTY_NAME,value = "src/test/resources/features/restaurant/RestaurantCreate.feature")
public class RestaurantCreate {

    private Response response;

    private RequestSpecification rspec;

    @Given("the system is ready to create a new restaurant")
    public void setUp(){
        this.rspec = given();
        this.rspec
                .baseUri("http://localhost:8080")
                .basePath("/restaurants")
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @When("a request is made to create a restaurant with valid details")
    public void validData(){
        RestaurantRequest request = GenerateData.gerenRestaurantRequest();
        response = given(rspec)
                .body(request)
                .when()
                .post("/");

    }

    @Then("the system should respond with a success status code")
    public void successRestaurant(){
        response.then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @When("a request is made to create a restaurant with missing required information")
    public void invalidRequestNameNull(){

        AddressRequest addressRequest = GenerateData.generateAddressRequest();

        RestaurantRequest request = new RestaurantRequest(null, addressRequest);

        response = given(rspec)
                .body(request)
                .when()
                .post("/");
    }

    @When("a request is made to create a restaurant with invalid details")
    public void invalidRequestNameMinimum(){

        AddressRequest addressRequest = GenerateData.generateAddressRequest();

        RestaurantRequest request = new RestaurantRequest("BH", addressRequest);

        response = given(rspec)
                .body(request)
                .when()
                .post("/");
    }

    @Then("the system should respond with a validation error status code")
    public void thenBadRequestNullName(){
        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @And("the response should indicate the missing information")
    public void messageErrorMissingInformation(){
        var error = response.getBody().asString();
        assertThat(error).contains("name: O campo deve estar preenchido");
    }

    @And("the response should indicate the specific validation errors")
    public void messageErrorValidationInformation(){
        var error = response.getBody().asString();
        assertThat(error).contains("name: O campo deve ter no minimo 3 e no maximo 100 caracteres");
    }


    @And("a restaurant with the name {string} already exists in the system")
    public void aRestaurantWithTheNameAlreadyExistsInTheSystem(String name) {

        AddressRequest addressRequest = GenerateData.generateAddressRequest();

        RestaurantRequest request = new RestaurantRequest(name, addressRequest);

        given(rspec)
            .body(request)
            .when()
            .post("/");

    }

    @When("a request is made to create a restaurant with the same name {string}")
    public void aRequestIsMadeToCreateARestaurantWithTheSameName(String name) {
        AddressRequest addressRequest = GenerateData.generateAddressRequest();

        RestaurantRequest request = new RestaurantRequest(name, addressRequest);

        response = given(rspec)
                .body(request)
                .when()
                .post("/");
    }

    @Then("the system should respond with a conflict status code")
    public void theSystemShouldRespondWithAConflictStatusCode() {
        response.then()
                .statusCode(HttpStatus.CONFLICT.value());
    }


    @And("the response should indicate that a restaurant with the same name already exists")
    public void theResponseShouldIndicateThatARestaurantWithTheSameNameAlreadyExists() {

        var error = response.getBody().asString();
        assertThat(error).contains("Nome já cadastrado!");

    }

    @And("the new restaurant should be persisted in the database")
    public void theNewRestaurantShouldBePersistedInTheDatabase() {

        var location = response.getHeader("Location");
        response = given()
                .contentType(ContentType.JSON)
                .when()
                .get(location);

        response.then().statusCode(HttpStatus.OK.value());

    }
}
