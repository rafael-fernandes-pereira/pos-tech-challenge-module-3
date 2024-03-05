package com.github.rafaelfernandes.restaurant.application.port.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rafaelfernandes.restaurant.common.enums.State;
import com.github.rafaelfernandes.restaurant.util.GenerateData;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateRestaurantCommandTest {

    @Nested
    class Name {

        @Test
        void validateSucessName(){

            String name = "Rafael Fernandes Pereira";

            CreateRestaurantCommand command = new CreateRestaurantCommand(name, GenerateData.generateAddressCommand());

            assertEquals(name, command.name());

        }

        @Test
        void validateNullName(){

            assertThatThrownBy(() -> {
                new CreateRestaurantCommand(null, GenerateData.generateAddressCommand());
            })
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessage("name: O campo deve estar preenchido");

        }

        @Test
        void validateEmptyName(){

            String name = "";

            assertThatThrownBy(() -> {
                new CreateRestaurantCommand(name, GenerateData.generateAddressCommand());
            })
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContainingAll("name: O campo deve estar preenchido", "name: O campo deve ter no minimo 10 e no maximo 100 caracteres");

        }

        @Test
        void validateLessMinimumName(){

            String name = "Rafael F"; // 8 caracteres

            assertThatThrownBy(() -> {
                new CreateRestaurantCommand(name, GenerateData.generateAddressCommand());
            })
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessage("name: O campo deve ter no minimo 10 e no maximo 100 caracteres");

        }

        @Test
        void validateMinimumNameSucess(){

            String name = "Rafael Dio"; // 10 Caracteres

            CreateRestaurantCommand command = new CreateRestaurantCommand(name, GenerateData.generateAddressCommand());

            assertEquals(name, command.name());

        }

        @Test
        void validateMoreMaximumName(){

            String name = "Esmeralda Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida"; // 8 caracteres

            assertThatThrownBy(() -> {
                new CreateRestaurantCommand(name, GenerateData.generateAddressCommand());
            })
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessage("name: O campo deve ter no minimo 10 e no maximo 100 caracteres");

        }

        @Test
        void validateMaximumNameSucess(){

            String name = "Gabriela Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira"; // 100 caracteres

            CreateRestaurantCommand command = new CreateRestaurantCommand(name, GenerateData.generateAddressCommand());

            assertEquals(name, command.name());

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

        @Test
        void validateAddressSuccess(){

            CreateRestaurantAddressCommand command = new CreateRestaurantAddressCommand(
                    street,
                    number,
                    addittionalDetails,
                    neighborhood,
                    city,
                    state
            );

            assertThat(street).isEqualTo(command.street());
            assertThat(number).isEqualTo(command.number());
            assertThat(addittionalDetails).isEqualTo(command.addittionalDetails());
            assertThat(neighborhood).isEqualTo(command.neighborhood());
            assertThat(city).isEqualTo(command.city());
            assertThat(state).isEqualTo(command.state());


        }

        @Nested
        class Street {


            @Test
            void validateNullStreet(){

                assertThatThrownBy(() -> {
                    new CreateRestaurantAddressCommand(
                            null,
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessage("street: O campo deve estar preenchido")
                ;

            }

            @Test
            void validateEmptyStreet(){

                assertThatThrownBy(() -> {
                    new CreateRestaurantAddressCommand(
                            "",
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessageContainingAll("street: O campo deve estar preenchido", "O campo deve ter no minimo 10 e no maximo 150 caracteres")
                ;

            }

            @Test
            void validateLessMinimumStreet(){

                String street = "Rafael F"; // 8 caracteres

                assertThatThrownBy(() -> {
                    new CreateRestaurantAddressCommand(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessage("street: O campo deve ter no minimo 10 e no maximo 150 caracteres");

            }

            @Test
            void validateMinimumStreetSucess(){

                String street = "Rafael Dio"; // 10 Caracteres

                CreateRestaurantAddressCommand command = new CreateRestaurantAddressCommand(
                        street,
                        number,
                        addittionalDetails,
                        neighborhood,
                        city,
                        state
                );

                assertEquals(street, command.street());

            }

            @Test
            void validateMoreMaximumStreet(){

                String street = "Esmeralda Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida";

                assertThatThrownBy(() -> {
                    new CreateRestaurantAddressCommand(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessage("street: O campo deve ter no minimo 10 e no maximo 150 caracteres");

            }

            @Test
            void validateMaximumNameStreetSucess(){

                String street = "Esmeralda Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira M"; // 150 caracteres

                CreateRestaurantAddressCommand command = new CreateRestaurantAddressCommand(
                        street,
                        number,
                        addittionalDetails,
                        neighborhood,
                        city,
                        state
                );

                assertEquals(street, command.street());

            }
        }

        @Nested
        class Number {

            @Test
            void validateNullNumber(){

                assertThatThrownBy(() -> {
                    new CreateRestaurantAddressCommand(
                            street,
                            null,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessage("number: O campo deve estar preenchido");

            }

            @Test
            void validateNegativeNumber(){

                assertThatThrownBy(() -> {
                    new CreateRestaurantAddressCommand(
                            street,
                            -1,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessage("number: O campo deve ser maior que zero (0)");

            }

            @Test
            void validateZeroNumber(){

                assertThatThrownBy(() -> {
                    new CreateRestaurantAddressCommand(
                            street,
                            0,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessage("number: O campo deve ser maior que zero (0)");

            }



        }

        @Nested
        class AddittionalDetails {

            @Test
            void validateAddittionalDetailsNull(){

                String addittionalDetails = null;

                CreateRestaurantAddressCommand command = new CreateRestaurantAddressCommand(
                        street,
                        number,
                        addittionalDetails,
                        neighborhood,
                        city,
                        state
                );

                assertThat(command).isNotNull();

            }

            @Test
            void validateAddittionalDetailsEmpty(){

                String addittionalDetails = "";

                CreateRestaurantAddressCommand command = new CreateRestaurantAddressCommand(
                        street,
                        number,
                        addittionalDetails,
                        neighborhood,
                        city,
                        state
                );

                assertThat(command).isNotNull();

            }

            @Test
            void validateMoreMaximumAddittionalDetails(){

                String addittionalDetails = "Esmeralda Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida";

                assertThatThrownBy(() -> {
                    new CreateRestaurantAddressCommand(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessage("addittionalDetails: O campo deve ter no máximo 150 caracteres");


            }

            @Test
            void validateMaximumAddittionalDetails(){

                String addittionalDetails = "Esmeralda Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira M";

                CreateRestaurantAddressCommand command = new CreateRestaurantAddressCommand(
                        street,
                        number,
                        addittionalDetails,
                        neighborhood,
                        city,
                        state
                );

                assertThat(command).isNotNull();

            }




        }

        @Nested
        class Neighborhood {

            @Test
            void validateNullNeighborhood(){

                assertThatThrownBy(() -> {
                    new CreateRestaurantAddressCommand(
                            street,
                            number,
                            addittionalDetails,
                            null,
                            city,
                            state
                    );
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessage("neighborhood: O campo deve estar preenchido")
                ;

            }

            @Test
            void validateEmptyNeighborhood(){

                assertThatThrownBy(() -> {
                    new CreateRestaurantAddressCommand(
                            street,
                            number,
                            addittionalDetails,
                            "",
                            city,
                            state
                    );
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessageContainingAll("neighborhood: O campo deve estar preenchido", "neighborhood: O campo deve ter no minimo 3 e no máximo 30 caracteres")
                ;

            }

            @Test
            void validateLessMinimumNeighborhood(){

                String neighborhood = "BH";

                assertThatThrownBy(() -> {
                    new CreateRestaurantAddressCommand(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessage("neighborhood: O campo deve ter no minimo 3 e no máximo 30 caracteres");

            }

            @Test
            void validateMinimumNeighborhoodSucess(){

                String neighborhood = "Dio";

                CreateRestaurantAddressCommand command = new CreateRestaurantAddressCommand(
                        street,
                        number,
                        addittionalDetails,
                        neighborhood,
                        city,
                        state
                );

                assertEquals(neighborhood, command.neighborhood());

            }

            @Test
            void validateMoreMaximumNeighborhood(){

                String neighborhood = "Esmeralda Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida";

                assertThatThrownBy(() -> {
                    new CreateRestaurantAddressCommand(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessage("neighborhood: O campo deve ter no minimo 3 e no máximo 30 caracteres");

            }

            @Test
            void validateMaximumNameNeighborhoodSucess(){

                String neighborhood = "Esmeralda Carolina da Silva A";

                CreateRestaurantAddressCommand command = new CreateRestaurantAddressCommand(
                        street,
                        number,
                        addittionalDetails,
                        neighborhood,
                        city,
                        state
                );

                assertEquals(neighborhood, command.neighborhood());

            }

        }

        @Nested
        class City {

            @Test
            void validateNullCity(){

                assertThatThrownBy(() -> {
                    new CreateRestaurantAddressCommand(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            null,
                            state
                    );
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessage("city: O campo deve estar preenchido")
                ;

            }

            @Test
            void validateEmptyCity(){

                assertThatThrownBy(() -> {
                    new CreateRestaurantAddressCommand(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            "",
                            state
                    );
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessageContainingAll("city: O campo deve estar preenchido", "city: O campo deve ter no minimo 3 e no máximo 60 caracteres")
                ;

            }

            @Test
            void validateLessMinimumCity(){

                String city = "BH";

                assertThatThrownBy(() -> {
                    new CreateRestaurantAddressCommand(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessage("city: O campo deve ter no minimo 3 e no máximo 60 caracteres");

            }

            @Test
            void validateMinimumNeighborhoodSucess(){

                String city = "Dio";

                CreateRestaurantAddressCommand command = new CreateRestaurantAddressCommand(
                        street,
                        number,
                        addittionalDetails,
                        neighborhood,
                        city,
                        state
                );

                assertEquals(city, command.city());

            }

            @Test
            void validateMoreMaximumCity(){

                String city = "Esmeralda Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida";

                assertThatThrownBy(() -> {
                    new CreateRestaurantAddressCommand(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessage("city: O campo deve ter no minimo 3 e no máximo 60 caracteres");

            }

            @Test
            void validateMaximumNameCitySucess(){

                String city = "Esmeralda Carolina da Silva Santos Pereira Oliveira Martins";

                CreateRestaurantAddressCommand command = new CreateRestaurantAddressCommand(
                        street,
                        number,
                        addittionalDetails,
                        neighborhood,
                        city,
                        state
                );

                assertEquals(city, command.city());

            }

        }

        @Nested
        class States {

            @Test
            void validateNullState(){

                String state = null;

                assertThatThrownBy(() -> {
                    new CreateRestaurantAddressCommand(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            state
                    );
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessage("state: O campo deve estar preenchido");

            }

            @Test
            void validateOtherState() {

                assertThatThrownBy(() -> {
                    new CreateRestaurantAddressCommand(
                            street,
                            number,
                            addittionalDetails,
                            neighborhood,
                            city,
                            "Teste"
                    );
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessageContaining("state: O campo deve ser uma sigla de estado válida");

            }

        }





    }

}
