package com.castinfo.devops.robotest.restassured;

import static io.restassured.matcher.RestAssuredMatchers.matchesXsdInClasspath;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasXPath;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.castinfo.devops.robotest.config.RobotestHttpConnConfig;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestTest {

    private static final String CAST_INFO_TITLE =
            "Cast Info | Cast Info s.a > Soluciones y Servicios tecnológicos de Vanguardia";

    @LocalServerPort
    private int port;

    @Value("${random.int[2080,2980]}")
    private int proxyPort;

    private final String localhost = "http://localhost:";
    private final String echoJsonSchema = "echo.json";
    private final String xmlSchema = "echo.xsd";
    private final String keyEcho = "echo";

    RestAssuredWrapper restClient = null;

    private String getUrlApi(final String service) {
        return localhost + port + service;
    }

    @Before
    public void prepareClient() {
        restClient = new RestAssuredWrapper();
        restClient.getResponseSpecBuilder()
                  .expectStatusCode(200);
    }

    @Test
    public void doHtmlRestTest() {
        String html = restClient.withContentType(ContentType.HTML)
                                .when("https://www.cast-info.es",
                                      Method.GET)
                                .then()
                                .extract()
                                .asString();
        XmlPath xmlPath = new XmlPath(CompatibilityMode.HTML,
                                      html);
        Assert.assertTrue(xmlPath.getString("html.head.title")
                                 .equals(CAST_INFO_TITLE));

    }

    @Test
    public void doPingTextGet() {
        String pingService = getUrlApi("/ping");
        ValidatableResponse responseThen = restClient.when(pingService,
                                                           Method.GET)
                                                     .then();
        Assert.assertTrue(responseThen.extract()
                                      .asByteArray().length > 0);
        Assert.assertTrue(responseThen.extract()
                                      .asString()
                                      .equals(TestController.HELLO_WORLD));
    }

    @Test
    public void doEchoTextGet() {
        String echoService = getUrlApi("/echo");
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(keyEcho,
                        "TEST");
        ValidatableResponse responseThen = restClient.withQueryParams(queryParams)
                                                     .when(echoService,
                                                           Method.GET)
                                                     .then();
        Assert.assertTrue(responseThen.extract()
                                      .asByteArray().length > 0);
        Assert.assertTrue(responseThen.extract()
                                      .asString()
                                      .equals(queryParams.get(keyEcho)));
    }

    @Test
    public void doPingJsonGet() {
        RestAssuredWrapper restClient = new RestAssuredWrapper();
        String jacksonService = getUrlApi("/jackson");
        Response response = restClient.when(jacksonService,
                                            Method.GET);
        response.then()
                .assertThat()
                .body("echo",
                      is(TestController.HELLO_WORLD))
                .and()
                .body(matchesJsonSchemaInClasspath(echoJsonSchema));
        Assert.assertTrue(response.as(JacksonPojo.class)
                                  .getEcho()
                                  .equals(TestController.HELLO_WORLD));
    }

    @Test
    public void doEchoJsonGet() {
        String jacksonService = getUrlApi("/jacksonEcho");
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(keyEcho,
                        "TEST");
        Response response = restClient.withQueryParams(queryParams)
                                      .when(jacksonService,
                                            Method.GET);
        response.then()
                .assertThat()
                .body("echo",
                      is(queryParams.get(keyEcho)))
                .and()
                .body(matchesJsonSchemaInClasspath(echoJsonSchema));
        Assert.assertTrue(response.as(JacksonPojo.class)
                                  .getEcho()
                                  .equals(queryParams.get(keyEcho)));
    }

    @Test
    public void doPingXmlGet() {
        String jaxbService = getUrlApi("/xmljaxb");
        Response response = restClient.when(jaxbService,
                                            Method.GET);
        response.then()
                .assertThat()
                .body("response.echo",
                      is(TestController.HELLO_WORLD))
                .and()
                .body(hasXPath("/response/echo",
                               is(TestController.HELLO_WORLD)))
                .body(matchesXsdInClasspath(xmlSchema));
        Assert.assertTrue(response.as(JaxbPojoType.class)
                                  .getEcho()
                                  .equals(TestController.HELLO_WORLD));
    }

    @Test
    public void doEchoXmlGet() {
        String jaxbEchoService = getUrlApi("/xmljaxbEcho");
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(keyEcho,
                        "TEST");
        Response response = restClient.withQueryParams(queryParams)
                                      .when(jaxbEchoService,
                                            Method.GET);
        Assert.assertTrue(response.as(JaxbPojoType.class)
                                  .getEcho()
                                  .equals(queryParams.get(keyEcho)));
    }

    @Test
    public void doPingWithProxy() {
        HttpProxyServer proxyServer = DefaultHttpProxyServer.bootstrap()
                                                            .withPort(proxyPort)
                                                            .start();
        RobotestHttpConnConfig httpConfig = new RobotestHttpConnConfig();
        httpConfig.setGeneralTimeout("1000");
        httpConfig.setProxyHost("localhost");
        httpConfig.setProxyPort("" + proxyPort);
        restClient = new RestAssuredWrapper(httpConfig);
        restClient.getResponseSpecBuilder()
                  .expectStatusCode(200);
        String pingService = getUrlApi("/ping");
        ValidatableResponse responseThen = restClient.when(pingService,
                                                           Method.GET)
                                                     .then();
        Assert.assertTrue(responseThen.extract()
                                      .asByteArray().length > 0);
        Assert.assertTrue(responseThen.extract()
                                      .asString()
                                      .equals(TestController.HELLO_WORLD));
    }

}
