package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rafaelfernandes.restaurant.adapter.in.web.request.AddressRequest;
import com.github.rafaelfernandes.restaurant.adapter.in.web.request.RestaurantRequest;
import com.github.rafaelfernandes.restaurant.domain.Restaurant;
import com.github.rafaelfernandes.restaurant.application.port.in.GetRestarauntDataCommand;
import com.github.rafaelfernandes.restaurant.common.enums.State;
import net.datafaker.Faker;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class GenerateData {

    private static final Faker faker = new Faker(new Locale("pt", "BR"));

    private static final Random random = new Random();

    public static List<Restaurant.OpeningHour> createDefaultOpeningHours(){
        List<Restaurant.OpeningHour> openingHours = new ArrayList<>();

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            LocalTime start = LocalTime.of(9, 0);
            LocalTime end = LocalTime.of(18, 0);
            openingHours.add(new Restaurant.OpeningHour(dayOfWeek, start, end));
        }

        return openingHours;
    }

    public static Restaurant createRestaurant(){
        String name = faker.restaurant().name();
        Restaurant.Address address = generateAddress();

        return new Restaurant(name, address);
    }

    public static Restaurant.Address generateAddress() {
        return new Restaurant.Address(
                faker.address().streetAddress(),
                Integer.valueOf(faker.address().streetAddressNumber()),
                faker.address().secondaryAddress(),
                faker.name().lastName(),
                faker.address().city(),
                faker.address().stateAbbr());
    }

    public static AddressRequest generateAddressRequest(){
        return new AddressRequest(faker.address().streetAddress(),
                Integer.valueOf(faker.address().streetAddressNumber()),
                faker.address().secondaryAddress(),
                faker.name().lastName(),
                faker.address().city(),
                faker.address().stateAbbr()
        );
    }

    public static RestaurantRequest gerenRestaurantRequest(){
        String name = faker.restaurant().name();
        AddressRequest addressRequest = generateAddressRequest();

        return new RestaurantRequest(name, addressRequest);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Iterator<Map<String, Object>> generatePerformanceRequest(){



        return
                Stream.generate((Supplier<Map<String, Object>>) () -> {

                    var request = new HashMap<String, Object>();

                    request.put("address_street", faker.address().streetAddress());
                    request.put("address_number", Integer.valueOf(faker.address().streetAddressNumber()));
                    request.put("address_addittionalDetails", faker.address().secondaryAddress());
                    request.put("address_neighborhood", faker.name().lastName());
                    request.put("address_city", faker.address().city());
                    request.put("address_state", faker.address().stateAbbr());

                    request.put("name", faker.restaurant().name());


                    return request;


                }).iterator();

    }

    public static GetRestarauntDataCommand createGetRestarauntDataCommandId(){

        return new GetRestarauntDataCommand(UUID.randomUUID().toString());

    }


}
