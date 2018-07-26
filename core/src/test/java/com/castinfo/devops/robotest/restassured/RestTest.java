package com.castinfo.devops.robotest.restassured;

import static io.restassured.matcher.RestAssuredMatchers.matchesXsdInClasspath;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestTest {

    @LocalServerPort
    private int port;

    private final String localhost = "http://localhost:";
    private final String echoJsonSchema = "echo.json";
    private final String xmlSchema = "echo.xsd";
    private final String keyEcho = "echo";

    private String getUrlApi(final String service) {
        return localhost + port + service;
    }

    @Test
    public void doHtmlRestTest() {
        RestAssuredWrapper restClient = new RestAssuredWrapper();
        String html = restClient.withContentType(ContentType.HTML).doCall("https://www.cast-info.es", Method.GET)
                                .getResponse().then().extract().asString();
        XmlPath xmlPath = new XmlPath(CompatibilityMode.HTML, html);
        Assert.assertTrue(xmlPath.getString("html.head.title")
                                 .equals("Cast Info | Cast Info s.a > Soluciones y Servicios tecnológicos de Vanguardia"));

    }

    @Test
    public void doPingTextGet() {
        RestAssuredWrapper restClient = new RestAssuredWrapper();
        String pingService = getUrlApi("/ping");
        ValidatableResponse response = restClient.doCall(pingService, Method.GET).getResponse().then();
        Assert.assertTrue(response.extract().asByteArray().length > 0);
        Assert.assertTrue(response.extract().asString().equals(TestController.HELLO_WORLD));
    }

    @Test
    public void doEchoTextGet() {
        RestAssuredWrapper restClient = new RestAssuredWrapper();
        String echoService = getUrlApi("/echo");
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(keyEcho, "TEST");
        ValidatableResponse response = restClient.withQueryParams(queryParams).doCall(echoService, Method.GET)
                                                 .getResponse().then();
        Assert.assertTrue(response.extract().asByteArray().length > 0);
        Assert.assertTrue(response.extract().asString().equals(queryParams.get(keyEcho)));
    }

    @Test
    public void doPingJsonGet() {
        RestAssuredWrapper restClient = new RestAssuredWrapper();
        String jacksonService = getUrlApi("/jackson");
        Response response = restClient.doCall(jacksonService, Method.GET).getResponse();
        response.then().assertThat().body("echo", is(TestController.HELLO_WORLD)).and()
                .body(matchesJsonSchemaInClasspath(echoJsonSchema));
        Assert.assertTrue(response.as(JacksonPojo.class).getEcho().equals(TestController.HELLO_WORLD));
    }

    @Test
    public void doEchoJsonGet() {
        RestAssuredWrapper restClient = new RestAssuredWrapper();
        String jacksonService = getUrlApi("/jacksonEcho");
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(keyEcho, "TEST");
        Response response = restClient.withQueryParams(queryParams).doCall(jacksonService, Method.GET).getResponse();
        response.then().assertThat().body("echo", is(queryParams.get(keyEcho))).and()
                .body(matchesJsonSchemaInClasspath(echoJsonSchema));
        Assert.assertTrue(response.as(JacksonPojo.class).getEcho().equals(queryParams.get(keyEcho)));
    }

    @Test
    public void doPingXmlGet() {
        RestAssuredWrapper restClient = new RestAssuredWrapper();
        String jaxbService = getUrlApi("/xmljaxb");
        Response response = restClient.doCall(jaxbService, Method.GET).getResponse();
        response.then().assertThat().body("response.echo", is(TestController.HELLO_WORLD)).and()
                .body(hasXPath("/response/echo", is(TestController.HELLO_WORLD)))
                .body(matchesXsdInClasspath(xmlSchema));
        Assert.assertTrue(response.as(JaxbPojoType.class).getEcho().equals(TestController.HELLO_WORLD));
    }

    @Test
    public void doEchoXmlGet() {
        RestAssuredWrapper restClient = new RestAssuredWrapper();
        String jaxbEchoService = getUrlApi("/xmljaxbEcho");
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(keyEcho, "TEST");
        Response response = restClient.withQueryParams(queryParams).doCall(jaxbEchoService, Method.GET).getResponse();
        Assert.assertTrue(response.as(JaxbPojoType.class).getEcho().equals(queryParams.get(keyEcho)));
    }

}
