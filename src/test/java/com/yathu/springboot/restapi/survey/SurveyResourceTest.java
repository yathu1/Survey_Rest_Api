package com.yathu.springboot.restapi.survey;

import ch.qos.logback.core.read.CyclicBufferAppender;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

//SurveyResourceTest class is used to test the SurveyResource class
//Mock the SurveyService.retriveSpecificSurveyQuestion(surveyId,questionId) method

@WebMvcTest(controllers = SurveyResource.class)
public class SurveyResourceTest {

    @MockBean
    private SurveyService surveyService;

    @Autowired
    private MockMvc mockMvc;

    //Fire a request to /surveys/Survey1/questions/Question1
    //localhost:8080/surveys/Survey1/questions/Question1 GET
    private static String SPECIFIC_QUESTION_URL = "http://localhost:8080/surveys/Survey1/questions/Question1";
    private static String GENERIC_QUESTION_URL = "http://localhost:8080/surveys/Survey1/questions";
    @Test
    void retriveSpecificSurveyQuestion_404Scenario() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(SPECIFIC_QUESTION_URL).accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(404,mvcResult.getResponse().getStatus());

    }

    @Test
    void retriveSpecificSurveyQuestion_basicScenario() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(SPECIFIC_QUESTION_URL).accept(MediaType.APPLICATION_JSON);

        Question question = new Question("Question1",
                "Most Popular Cloud Platform Today", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");
        when(surveyService.retrieveQuestion("Survey1","Question1")).thenReturn(question);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

    String expectedResponse = """
        {"id":"Question1","description":"Most Popular Cloud Platform Today",
        "options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"} """;

        assertEquals(200,mvcResult.getResponse().getStatus());
        JSONAssert.assertEquals(expectedResponse,mvcResult.getResponse().getContentAsString(),false);
    }

    //addNewSurveyQuestion
    //POST
    //localhost:8080/surveys/Survey1/questions
    //201
    //location : http://localhost:8080/surveys/Survey1/questions/2738765602
    @Test
    void addNewSurveyQuestion_basicScenario() throws Exception {
        String requestBody = """
                        {
                            "id": "4219256940",
                            "description": "Your Favorite Framework",
                            "options": [
                                "Springboot",
                                "Laraval",
                                ".Net",
                                "ExpressJS"
                            ],
                            "correctAnswer": "Springboot"}
                """;
        String expectedResponse = """
                {
                    "id": "4219256940",
                    "description": "Your Favorite Framework",
                    "options": [
                        "Springboot",
                        "Laraval",
                        ".Net",
                        "ExpressJS"
                    ],
                    "correctAnswer": "Springboot"
                }
                """;
        when(surveyService.addNewSurveyQuestion(anyString(), any())).thenReturn("SOME_ID");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(GENERIC_QUESTION_URL)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody).contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        String locationHeader = response.getHeader("location");
        assertEquals(201, response.getStatus());

        assertTrue(locationHeader.contains("/surveys/Survey1/questions/"));


    }
}
