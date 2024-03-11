package performance;

import io.gatling.javaapi.core.ActionBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import org.springframework.http.HttpStatus;
import util.GenerateData;

import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.status;

public class RestaurantPerformance extends Simulation{

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080/restaurants")
            .header("Content-Type", "application/json");


    Iterator<Map<String, Object>> feeder = GenerateData.generatePerformanceRequest();

    ActionBuilder createRestaurantSuccess = http("Create Restaurant Success")
            .post("/")
            .body(StringBody("""
                    {
                        "name": "${name}",
                        "address": {
                            "street": "${address_street}",
                            "number": "${address_number}",
                            "addittionalDetails": "${address_addittionalDetails}",
                            "neighborhood": "${address_neighborhood}",
                            "city": "${address_city}",
                            "state": "${address_state}"
                        }
                    }
                    """))
            .check(status().is(HttpStatus.CREATED.value()));

    ScenarioBuilder scenarioCreateRestaurant = scenario("Create Restaurant")
            .feed(feeder)
            .exec(createRestaurantSuccess);

    {
        setUp(
                scenarioCreateRestaurant.injectOpen(
                        rampUsersPerSec(1)
                                .to(10)
                                .during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10)
                                .during(Duration.ofSeconds(60)),
                        rampUsersPerSec(10)
                                .to(1)
                                .during(Duration.ofSeconds(10))
                )
        )
        .protocols(httpProtocol)
        .assertions(
                    global().responseTime().max().lt(50),
                    global().failedRequests().count().is(0L));
    }

}
