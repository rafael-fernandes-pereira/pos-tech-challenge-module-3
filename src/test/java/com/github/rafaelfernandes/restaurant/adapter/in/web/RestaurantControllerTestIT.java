package com.github.rafaelfernandes.restaurant.adapter.in.web;

import com.github.rafaelfernandes.restaurant.adapter.in.web.request.AddressRequest;
import com.github.rafaelfernandes.restaurant.adapter.in.web.request.RestaurantRequest;
import com.github.rafaelfernandes.restaurant.adapter.out.persistence.RestaurantRepository;
import com.github.rafaelfernandes.restaurant.common.enums.State;
import com.github.rafaelfernandes.restaurant.util.GenerateData;
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

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantControllerTestIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @AfterEach
    void tearDown(){
        restaurantRepository.deleteAll();
    }

    @Nested
    class Create {

        @Test
        void createSuccess(){

            // TODO: voltar para colocar teste de validação do ID gerado (GET)

            RestaurantRequest request = GenerateData.gerenRestaurantRequest();

            ResponseEntity<String> response = createRestaurantPost(request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

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

    private ResponseEntity<String> createRestaurantPost(RestaurantRequest request) {
        return restTemplate
                .postForEntity(
                        "/restaurants/",
                        request,
                        String.class
                );

    }

}
