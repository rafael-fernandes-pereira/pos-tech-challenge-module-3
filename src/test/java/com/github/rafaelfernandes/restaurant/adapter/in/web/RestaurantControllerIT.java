package com.github.rafaelfernandes.restaurant.adapter.in.web;

import com.github.rafaelfernandes.restaurant.adapter.in.web.request.AddressRequest;
import com.github.rafaelfernandes.restaurant.adapter.in.web.request.RestaurantRequest;
import com.github.rafaelfernandes.restaurant.adapter.out.persistence.RestaurantRepository;
import com.github.rafaelfernandes.restaurant.common.enums.State;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import util.GenerateData;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    void setup(){
        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                    return execution.execute(request, body);
                }));
    }

    @AfterEach
    void tearDown(){
        restaurantRepository.deleteAll();
    }

    @Nested
    class Create {

        @Test
        void createSuccess(){

            RestaurantRequest request = GenerateData.gerenRestaurantRequest();

            ResponseEntity<String> create = createRestaurantPost(request);

            assertThat(create.getStatusCode()).isEqualTo(HttpStatus.CREATED);

            URI location = create.getHeaders().getLocation();

            ResponseEntity<String> response = restTemplate
                    .getForEntity(
                            location,
                            String.class
                    );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);


        }

        @Test
        void duplicateNameError(){

            AddressRequest addressRequest = GenerateData.generateAddressRequest();
            String name = "COMIDA BOA";

            RestaurantRequest request = new RestaurantRequest(name, addressRequest);

            ResponseEntity<String> response = createRestaurantPost(request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

            ResponseEntity<String> responseDuplicate = createRestaurantPost(request);

            assertThat(responseDuplicate.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

            DocumentContext documentContext = JsonPath.parse(responseDuplicate.getBody());

            String error = documentContext.read("$.errors");

            assertThat(error).contains("Nome já cadastrado!");

        }

        @Nested
        class Name {
            @ParameterizedTest
            @ValueSource(strings = {""})
            @NullSource
            void validateNameErrorNullEmpty(String name){

                AddressRequest addressRequest = GenerateData.generateAddressRequest();

                RestaurantRequest request = new RestaurantRequest(name, addressRequest);

                ResponseEntity<String> response = createRestaurantPost(request);

                DocumentContext documentContext = JsonPath.parse(response.getBody());

                String error = documentContext.read("$.errors");

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                assertThat(error).contains("name: O campo deve estar preenchido");
            }


            @Test
            void validateNameLessMinimum(){

                String name = "AB";

                AddressRequest addressRequest = GenerateData.generateAddressRequest();

                RestaurantRequest request = new RestaurantRequest(name, addressRequest);

                ResponseEntity<String> response = createRestaurantPost(request);

                DocumentContext documentContext = JsonPath.parse(response.getBody());

                String error = documentContext.read("$.errors");

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                assertThat(error).contains("name: O campo deve ter no minimo 3 e no maximo 100 caracteres");
            }

            @Test
            void validateNameMoreMaximum(){

                String name = "Esmeralda Carolina da Silva Santos Pereira Oliveira Martins MartinsMartinsMartinsMartinsMartins AAOAA";

                AddressRequest addressRequest = GenerateData.generateAddressRequest();

                RestaurantRequest request = new RestaurantRequest(name, addressRequest);

                ResponseEntity<String> response = createRestaurantPost(request);

                DocumentContext documentContext = JsonPath.parse(response.getBody());

                String error = documentContext.read("$.errors");

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                assertThat(error).contains("name: O campo deve ter no minimo 3 e no maximo 100 caracteres");
            }
        }

        @Nested
        class Address {

            String street = "Alameda dos Anjos";
            Integer number = 6;
            String addittionalDetails = "Lanchonete da escola";
            String neighborhood = "Silver Lake";
            String city = "Los Angeles";
            String state = State.MG.name();

            @Nested
            class Street {


                @Test
                void validateNullStreet(){

                    AddressRequest addressRequest = new AddressRequest(
                            null,
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );

                    RestaurantRequest request = new RestaurantRequest("Sr K.ebab", addressRequest);

                    ResponseEntity<String> response = createRestaurantPost(request);

                    DocumentContext documentContext = JsonPath.parse(response.getBody());

                    String error = documentContext.read("$.errors");

                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(error).contains("street: O campo deve estar preenchido");

                }

                @Test
                void validateEmptyStreet(){

                    AddressRequest addressRequest = new AddressRequest(
                            "",
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );

                    RestaurantRequest request = new RestaurantRequest("Sr K.ebab", addressRequest);

                    ResponseEntity<String> response = createRestaurantPost(request);

                    DocumentContext documentContext = JsonPath.parse(response.getBody());

                    String error = documentContext.read("$.errors");

                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(error).contains("street: O campo deve estar preenchido",  "O campo deve ter no minimo 10 e no maximo 150 caracteres");

                }

                @Test
                void validateLessMinimumStreet(){

                    String street = "Rafael F"; // 8 caracteres

                    AddressRequest addressRequest = new AddressRequest(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );

                    RestaurantRequest request = new RestaurantRequest("Sr K.ebab", addressRequest);

                    ResponseEntity<String> response = createRestaurantPost(request);

                    DocumentContext documentContext = JsonPath.parse(response.getBody());

                    String error = documentContext.read("$.errors");

                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(error).contains("O campo deve ter no minimo 10 e no maximo 150 caracteres");

                }

                @Test
                void validateMoreMaximumStreet(){

                    String street = "Esmeralda Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida";

                    AddressRequest addressRequest = new AddressRequest(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );

                    RestaurantRequest request = new RestaurantRequest("Sr K.ebab", addressRequest);

                    ResponseEntity<String> response = createRestaurantPost(request);

                    DocumentContext documentContext = JsonPath.parse(response.getBody());

                    String error = documentContext.read("$.errors");

                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(error).contains("O campo deve ter no minimo 10 e no maximo 150 caracteres");

                }

            }

            @Nested
            class Number {

                @Test
                void validateNullNumber(){

                    AddressRequest addressRequest = new AddressRequest(
                            street,
                            null,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );

                    RestaurantRequest request = new RestaurantRequest("Sr K.ebab", addressRequest);

                    ResponseEntity<String> response = createRestaurantPost(request);

                    DocumentContext documentContext = JsonPath.parse(response.getBody());

                    String error = documentContext.read("$.errors");

                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(error).contains("number: O campo deve estar preenchido");

                }

                @Test
                void validateNegativeNumber(){

                    AddressRequest addressRequest = new AddressRequest(
                            street,
                            -1,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );

                    RestaurantRequest request = new RestaurantRequest("Sr K.ebab", addressRequest);

                    ResponseEntity<String> response = createRestaurantPost(request);

                    DocumentContext documentContext = JsonPath.parse(response.getBody());

                    String error = documentContext.read("$.errors");

                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(error).contains("number: O campo deve ser maior que zero (0)");


                }

                @Test
                void validateZeroNumber(){

                    AddressRequest addressRequest = new AddressRequest(
                            street,
                            0,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );

                    RestaurantRequest request = new RestaurantRequest("Sr K.ebab", addressRequest);

                    ResponseEntity<String> response = createRestaurantPost(request);

                    DocumentContext documentContext = JsonPath.parse(response.getBody());

                    String error = documentContext.read("$.errors");

                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(error).contains("number: O campo deve ser maior que zero (0)");

                }



            }

            @Nested
            class AddittionalDetails {

                @Test
                void validateAddittionalDetailsNull(){

                    AddressRequest addressRequest = new AddressRequest(
                            street,
                            number,
                            null,
                            neighborhood,
                            city,
                            state
                    );

                    RestaurantRequest request = new RestaurantRequest("Sr K.ebab", addressRequest);

                    ResponseEntity<String> response = createRestaurantPost(request);

                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

                }

                @Test
                void validateAddittionalDetailsEmpty(){

                    AddressRequest addressRequest = new AddressRequest(
                            street,
                            number,
                            "",
                            neighborhood,
                            city,
                            state
                    );

                    RestaurantRequest request = new RestaurantRequest("Sr K.ebab", addressRequest);

                    ResponseEntity<String> response = createRestaurantPost(request);

                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

                }

                @Test
                void validateMoreMaximumAddittionalDetails(){

                    String addittionalDetails = "Esmeralda Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida";

                    AddressRequest addressRequest = new AddressRequest(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );

                    RestaurantRequest request = new RestaurantRequest("Sr K.ebab", addressRequest);

                    ResponseEntity<String> response = createRestaurantPost(request);

                    DocumentContext documentContext = JsonPath.parse(response.getBody());

                    String error = documentContext.read("$.errors");

                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(error).contains("addittionalDetails: O campo deve ter no máximo 150 caracteres");


                }




            }

            @Nested
            class Neighborhood {

                @Test
                void validateNullNeighborhood(){

                    AddressRequest addressRequest = new AddressRequest(
                            street,
                            number,
                            addittionalDetails,
                            null,
                            city,
                            state
                    );

                    RestaurantRequest request = new RestaurantRequest("Sr K.ebab", addressRequest);

                    ResponseEntity<String> response = createRestaurantPost(request);

                    DocumentContext documentContext = JsonPath.parse(response.getBody());

                    String error = documentContext.read("$.errors");

                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(error).contains("neighborhood: O campo deve estar preenchido");

                }

                @Test
                void validateEmptyNeighborhood(){

                    AddressRequest addressRequest = new AddressRequest(
                            street,
                            number,
                            addittionalDetails,
                            "",
                            city,
                            state
                    );

                    RestaurantRequest request = new RestaurantRequest("Sr K.ebab", addressRequest);

                    ResponseEntity<String> response = createRestaurantPost(request);

                    DocumentContext documentContext = JsonPath.parse(response.getBody());

                    String error = documentContext.read("$.errors");

                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(error).contains("neighborhood: O campo deve estar preenchido", "neighborhood: O campo deve ter no minimo 3 e no máximo 30 caracteres");

                }

                @Test
                void validateLessMinimumNeighborhood(){

                    String neighborhood = "BH";

                    AddressRequest addressRequest = new AddressRequest(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );

                    RestaurantRequest request = new RestaurantRequest("Sr K.ebab", addressRequest);

                    ResponseEntity<String> response = createRestaurantPost(request);

                    DocumentContext documentContext = JsonPath.parse(response.getBody());

                    String error = documentContext.read("$.errors");

                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(error).contains("neighborhood: O campo deve ter no minimo 3 e no máximo 30 caracteres");


                }

                @Test
                void validateMoreMaximumNeighborhood(){

                    String neighborhood = "Esmeralda Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida";

                    AddressRequest addressRequest = new AddressRequest(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );

                    RestaurantRequest request = new RestaurantRequest("Sr K.ebab", addressRequest);

                    ResponseEntity<String> response = createRestaurantPost(request);

                    DocumentContext documentContext = JsonPath.parse(response.getBody());

                    String error = documentContext.read("$.errors");

                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(error).contains("neighborhood: O campo deve ter no minimo 3 e no máximo 30 caracteres");


                }

            }

            @Nested
            class City {

                @Test
                void validateNullCity(){

                    AddressRequest addressRequest = new AddressRequest(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            null,
                            state
                    );

                    RestaurantRequest request = new RestaurantRequest("Sr K.ebab", addressRequest);

                    ResponseEntity<String> response = createRestaurantPost(request);

                    DocumentContext documentContext = JsonPath.parse(response.getBody());

                    String error = documentContext.read("$.errors");

                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(error).contains("city: O campo deve estar preenchido");


                }

                @Test
                void validateEmptyCity(){

                    AddressRequest addressRequest = new AddressRequest(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            "",
                            state
                    );

                    RestaurantRequest request = new RestaurantRequest("Sr K.ebab", addressRequest);

                    ResponseEntity<String> response = createRestaurantPost(request);

                    DocumentContext documentContext = JsonPath.parse(response.getBody());

                    String error = documentContext.read("$.errors");

                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(error).contains("city: O campo deve estar preenchido", "city: O campo deve ter no minimo 3 e no máximo 60 caracteres");

                }

                @Test
                void validateLessMinimumCity(){

                    String city = "BH";

                    AddressRequest addressRequest = new AddressRequest(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );

                    RestaurantRequest request = new RestaurantRequest("Sr K.ebab", addressRequest);

                    ResponseEntity<String> response = createRestaurantPost(request);

                    DocumentContext documentContext = JsonPath.parse(response.getBody());

                    String error = documentContext.read("$.errors");

                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(error).contains("city: O campo deve ter no minimo 3 e no máximo 60 caracteres");

                }

                @Test
                void validateMoreMaximumCity(){

                    String city = "Esmeralda Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida";

                    AddressRequest addressRequest = new AddressRequest(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );

                    RestaurantRequest request = new RestaurantRequest("Sr K.ebab", addressRequest);

                    ResponseEntity<String> response = createRestaurantPost(request);

                    DocumentContext documentContext = JsonPath.parse(response.getBody());

                    String error = documentContext.read("$.errors");

                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(error).contains("city: O campo deve ter no minimo 3 e no máximo 60 caracteres");

                }

            }

            @Nested
            class States {

                @Test
                void validateNullState(){

                    AddressRequest addressRequest = new AddressRequest(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            null
                    );

                    RestaurantRequest request = new RestaurantRequest("Sr K.ebab", addressRequest);

                    ResponseEntity<String> response = createRestaurantPost(request);

                    DocumentContext documentContext = JsonPath.parse(response.getBody());

                    String error = documentContext.read("$.errors");

                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(error).contains("state: O campo deve estar preenchido");

                }

                @Test
                void validateOtherState() {

                    AddressRequest addressRequest = new AddressRequest(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            "teste"
                    );

                    RestaurantRequest request = new RestaurantRequest("Sr K.ebab", addressRequest);

                    ResponseEntity<String> response = createRestaurantPost(request);

                    DocumentContext documentContext = JsonPath.parse(response.getBody());

                    String error = documentContext.read("$.errors");

                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

                    assertThat(error).contains("state: O campo deve ser uma sigla de estado válida");

                }

            }





        }





    }

    @Nested
    class FindById {

        @Test
        void validateCommandError() {

            ResponseEntity<String> response = restTemplate
                    .getForEntity(
                    "/restaurants/uuid-invalid",
                        String.class
                    );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

            DocumentContext documentContext = JsonPath.parse(response.getBody());

            String error = documentContext.read("$.errors");

            assertThat(error).isEqualTo("id: O campo deve ser do tipo UUID");

        }

        @Test
        void validateNotFound() {
            ResponseEntity<String> response = restTemplate
                    .getForEntity(
                            "/restaurants/e903732e-9d20-4023-a71a-5c761253fc1c",
                            String.class
                    );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

            DocumentContext documentContext = JsonPath.parse(response.getBody());

            String error = documentContext.read("$.errors");

            assertThat(error).isEqualTo("Restaurante(s) não existe!");
        }


        @Test
        void validateFound(){

            var request = GenerateData.gerenRestaurantRequest();

            ResponseEntity<String> create = createRestaurantPost(request);

            URI location = create.getHeaders().getLocation();

            ResponseEntity<String> response = restTemplate
                    .getForEntity(
                            location,
                            String.class
                    );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            DocumentContext documentContext = JsonPath.parse(response.getBody());

            String name = documentContext.read("$.name");
            assertThat(name).isEqualTo(request.name());

            String addressStreet = documentContext.read("$.address.street");
            assertThat(addressStreet).isEqualTo(request.address().street());

            Integer addressNumber = documentContext.read("$.address.number");
            assertThat(addressNumber).isEqualTo(request.address().number());

            String addressAddittionalDetails = documentContext.read("$.address.addittionalDetails");
            assertThat(addressAddittionalDetails).isEqualTo(request.address().addittionalDetails());

            String addressNeighborhood = documentContext.read("$.address.neighborhood");
            assertThat(addressNeighborhood).isEqualTo(request.address().neighborhood());

            String addressCity = documentContext.read("$.address.city");
            assertThat(addressCity).isEqualTo(request.address().city());

            String addressState = documentContext.read("$.address.state");
            assertThat(addressState).isEqualTo(request.address().state());

        }

    }

    private ResponseEntity<String> createRestaurantPost(RestaurantRequest request) {
        return restTemplate
                .postForEntity(
                        "/restaurants/",
                        request,
                        String.class
                );

    }

}
