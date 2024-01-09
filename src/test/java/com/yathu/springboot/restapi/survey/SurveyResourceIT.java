package com.yathu.springboot.restapi.survey;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyResourceIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    private static String SPECIFIC_QUESTION_URL = "/surveys/Survey1/questions/Question1";
    String str = """
            {
                "id": "Question1",
                "description": "Most Popular Cloud Platform Today",
                "options": [
                    "AWS",
                    "Azure",
                    "Google Cloud",
                    "Oracle Cloud"
                ],
                "correctAnswer": "AWS"
            }
            """;

    @Test
    void retriveSpecificQuestion_basicScenario() throws JSONException {
        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(SPECIFIC_QUESTION_URL, String.class);
        String expectedResponse = """
                  {"id":"Question1","description":"Most Popular Cloud Platform Today","correctAnswer":"AWS"}
                """;
       // assertEquals(expectedResponse.trim(), responseEntity.getBody());
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(),false);
        System.out.println(responseEntity.getHeaders());
        ;
    }
}
