package com.yathu.springboot.restapi.survey;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyResourceIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    private static String SPECIFIC_QUESTION_URL = "/surveys/Survey1/questions/Question1";
    private static String GENERIC_QUESTIONS_URL = "/surveys/Survey1/questions";


    @Test
    void retriveSpecificQuestion_basicScenario() throws JSONException {
        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(SPECIFIC_QUESTION_URL, String.class);
        String expectedResponse = """
                  {"id":"Question1","description":"Most Popular Cloud Platform Today","correctAnswer":"AWS"}
                """;
        //status code is 200
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);

    }

    @Test
    void retrieveAllSurveyQuestions_basicScenario() throws JSONException {
        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(GENERIC_QUESTIONS_URL, String.class);
        String expectedResponse = """
                [
                    {
                        "id": "Question1"
                       
                    },
                    {
                        "id": "Question2"
                        
                    },
                    {
                        "id": "Question3"
                       
                    }
                ]
                                              
                                 """;
        //status code is 200
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);

    }


    //location: http://localhost:8080/surveys/Survey1/questions/4219256940
    @Test
    void addNewSurveyQuestion_basicScenario(){
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

        //  localhost:8080/surveys/Survey1/questions
        //RequestBody
        //content-type application/json
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");

        HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody,headers);

        ResponseEntity<String> responseEntity =
                testRestTemplate.exchange(GENERIC_QUESTIONS_URL, HttpMethod.POST, httpEntity,String.class);
        System.out.println(responseEntity.getHeaders());

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());

        String locationHeader = responseEntity.getHeaders().get("Location").get(0);
        assertTrue(locationHeader.contains("/surveys/Survey1/questions/"));

        //Asserts
        //201

        //DELETE
        //locationHeader
        testRestTemplate.delete(locationHeader);
    }
}
