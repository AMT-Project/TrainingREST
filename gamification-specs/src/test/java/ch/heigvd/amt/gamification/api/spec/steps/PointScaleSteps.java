package ch.heigvd.amt.gamification.api.spec.steps;

import ch.heigvd.amt.gamification.ApiException;
import ch.heigvd.amt.gamification.ApiResponse;
import ch.heigvd.amt.gamification.api.DefaultApi;
import ch.heigvd.amt.gamification.api.dto.Badge;
import ch.heigvd.amt.gamification.api.dto.PointScale;
import ch.heigvd.amt.gamification.api.spec.helpers.Environment;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PointScaleSteps {
    private Environment environment;
    private DefaultApi api;


    private ApiResponse lastApiResponse;
    private String lastReceivedLocationHeader;

    private PointScale lastReceivedPointScale;
    private PointScale pointScale;

    public PointScaleSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
    }

    @Given("there is a PointScales server")
    public void there_is_a_point_scales_server() {
        assertNotNull(api);
    }

    @Given("I have a pointScale payload")
    public void i_have_a_point_scale_payload() {
        pointScale = new PointScale()
                .name("scaleMockName")
                .description("scalemockdesc");
    }

    @When("I POST the pointScale payload to the \\/pointScales endpoint")
    public void i_post_the_point_scale_payload_to_the_point_scales_endpoint() {
        try {
            lastApiResponse = api.createPointScaleWithHttpInfo(pointScale);
            environment.processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @When("I send a GET to the \\/pointScales endpoint")
    public void i_send_a_get_to_the_point_scales_endpoint() {
        try {
            lastApiResponse = api.getPointScalesWithHttpInfo();
            environment.processApiResponse(lastApiResponse);
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @When("I send a GET to the URL in the location header for a pointScale")
    public void i_send_a_get_to_the_url_in_the_location_header_for_a_point_scale() {
        lastReceivedLocationHeader = environment.getLastReceivedLocationHeader();
        Integer id = Integer.parseInt(lastReceivedLocationHeader.substring(lastReceivedLocationHeader.lastIndexOf('/') + 1));
        try {
            lastApiResponse = api.getPointScaleWithHttpInfo(id);
            environment.processApiResponse(lastApiResponse);
            lastReceivedPointScale = (PointScale) lastApiResponse.getData();
        } catch (ApiException e) {
            environment.processApiException(e);
        }
    }

    @Then("I receive a payload that is the same as the pointScale payload")
    public void i_receive_a_payload_that_is_the_same_as_the_point_scale_payload() {
        assertEquals(pointScale, lastReceivedPointScale);
    }

    @Then("I receive a list of {int} pointScales")
    public void i_receive_a_list_of_point_scales(Integer nbPointScales) {
        List<PointScale> pointScaleList = (ArrayList) lastApiResponse.getData();
        assertEquals((long) nbPointScales, pointScaleList.size());
    }

    @Given("I have a pointScale payload {string}")
    public void i_have_a_point_scale_payload(String name) {
        pointScale = new PointScale().name(name).description(name + "mockDesc");
    }
}
