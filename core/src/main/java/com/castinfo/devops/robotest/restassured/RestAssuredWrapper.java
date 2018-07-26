/********************************************************************************
 * ROBOTEST
 * Copyright (C) 2018 CAST-INFO, S.A. www.cast-info.es
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package com.castinfo.devops.robotest.restassured;

import static io.restassured.RestAssured.given;
import static io.restassured.authentication.FormAuthConfig.formAuthConfig;
import static io.restassured.authentication.FormAuthConfig.springSecurity;

import java.util.Map;

import org.apache.http.HttpStatus;

import com.castinfo.devops.robotest.RobotestException;

import io.restassured.authentication.FormAuthConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 *
 * RestAssured utility wrapper.
 *
 * Creates a object with RequestSpecBuilder and ResponseSpecBuilder RestAssured objects to take profit from his fluent
 * API to do REST API Calls.
 *
 * Feel free to use, extend, improve or implement your own utilities.
 *
 * See com.castinfo.devops.robotest.restassured.RestTest for basic use example.
 *
 */
public class RestAssuredWrapper {

    private RequestSpecBuilder requestSpecBuilder;
    private ResponseSpecBuilder responseSpecBuilder;
    private RequestSpecification requestSpec;
    private Response response = null;

    /**
     * Constructor.
     */
    public RestAssuredWrapper() {
        requestSpecBuilder = new RequestSpecBuilder();
        responseSpecBuilder = new ResponseSpecBuilder();
        requestSpec = null;
    }

    /**
     * Getter method for request.
     *
     * @return the request
     */
    public RequestSpecBuilder getRequestBuilder() {
        return requestSpecBuilder;
    }

    /**
     * Getter method for response.
     *
     * @return the response
     */
    public ResponseSpecBuilder getResponseBuilder() {
        return responseSpecBuilder;
    }

    /**
     * Getter method for response.
     *
     * @return the response
     */
    public Response getResponse() {
        return response;
    }

    /**
     * Add headers.
     *
     * @param headers
     *            map of headers
     * @return The same object for fluent api.
     */
    public RestAssuredWrapper withHeaders(final Map<String, String> headers) {
        requestSpecBuilder.addHeaders(headers);
        return this;
    }

    /**
     * Add cookies.
     *
     * @param cookies
     *            map of params
     * @return The same object for fluent api.
     */
    public RestAssuredWrapper withCookies(final Cookies cookies) {
        requestSpecBuilder.addCookies(cookies);
        return this;
    }

    /**
     * Add path params.
     *
     * @param pathParams
     *            map of params
     * @return The same object for fluent api.
     */
    public RestAssuredWrapper withPathParams(final Map<String, ?> pathParams) {
        requestSpecBuilder.addPathParams(pathParams);
        return this;
    }

    /**
     * Add qry params.
     *
     * @param queryParams
     *            map of params
     * @return The same object for fluent api.
     */
    public RestAssuredWrapper withQueryParams(final Map<String, ?> queryParams) {
        requestSpecBuilder.addQueryParams(queryParams);
        return this;
    }

    /**
     * Add body params.
     *
     * @param bodyParams
     *            map of params
     * @return The same object for fluent api.
     */
    public RestAssuredWrapper withBodyParams(final Map<String, ?> bodyParams) {
        requestSpecBuilder.addParams(bodyParams);
        return this;
    }

    /**
     * Add form params.
     *
     * @param formParams
     *            map of params
     * @return The same object for fluent api.
     */
    public RestAssuredWrapper withFormParams(final Map<String, ?> formParams) {
        requestSpecBuilder.addFormParams(formParams);
        return this;
    }

    /**
     * Add content type to the request spec.
     *
     * @param contentType
     *            use {@link ContentType}
     * @return The same object for fluent api.
     */
    public RestAssuredWrapper withContentType(final ContentType contentType) {
        loadGivenSpecWhen().contentType(contentType);
        return this;
    }

    /**
     * Digest auth.
     *
     * @param username
     *            user
     * @param password
     *            psw
     * @return The same object for fluent api.
     */
    public RestAssuredWrapper withDigest(final String username, final String password) {
        loadGivenSpecWhen().auth().digest(username, password);
        return this;
    }

    /**
     * Basic preemtive basic auth.
     *
     * @param username
     *            user
     * @param password
     *            psw
     * @return The same object for fluent api.
     */
    public RestAssuredWrapper withBasicPreemptive(final String username, final String password) {
        loadGivenSpecWhen().auth().preemptive().basic(username, password);
        return this;
    }

    /**
     * Basic auth.
     *
     * @param username
     *            user
     * @param password
     *            psw
     * @return The same object for fluent api.
     */
    public RestAssuredWrapper withChallengedBasic(final String username, final String password) {
        loadGivenSpecWhen().auth().basic(username, password);
        return this;
    }

    /**
     * Utility to minimize code for add additional form field.
     *
     * @param formConfig
     *            form
     * @param additionalFields
     *            add fields
     */
    private void formAdditionalFieldNames(final FormAuthConfig formConfig, final Map<String, ?> additionalFields) {
        if (null != additionalFields && !additionalFields.isEmpty()) {
            for (String fieldName : additionalFields.keySet()) {
                formConfig.withAdditionalField(fieldName);
            }
        }
    }

    /**
     * Form auth prepare call helper.
     *
     * @param authFormUrl
     *            formurl
     * @param formName
     *            form name
     * @param usernameInputName
     *            user input name
     * @param passwordInputName
     *            secret input name
     * @param username
     *            user
     * @param password
     *            secret
     * @param additionalFields
     *            additional fields
     *
     *
     * @return The same object for fluent api.
     */
    public RestAssuredWrapper withFormAuth(final String authFormUrl,
                                           final String formName,
                                           final String usernameInputName,
                                           final String passwordInputName,
                                           final String username,
                                           final String password,
                                           final Map<String, ?> additionalFields) {
        FormAuthConfig formConfig = new FormAuthConfig(formName,
                                                       usernameInputName,
                                                       passwordInputName);
        formAdditionalFieldNames(formConfig, additionalFields);
        given().auth().form(username, password, formConfig).when().get(authFormUrl).then().statusCode(HttpStatus.SC_OK);
        loadGivenSpecWhen();
        return this;
    }

    /**
     * CSRF prepare call helper for spring security form.
     *
     * @param authFormUrl
     *            formurl
     * @param username
     *            user
     * @param password
     *            secret
     * @param additionalFields
     *            additional fields
     *
     *
     * @return The same object for fluent api.
     */
    public RestAssuredWrapper withFormSpringSecurityAuth(final String authFormUrl,
                                                         final String username,
                                                         final String password,
                                                         final Map<String, ?> additionalFields) {
        FormAuthConfig formConfig = FormAuthConfig.springSecurity();
        formAdditionalFieldNames(formConfig, additionalFields);
        given().auth()
               .form(username, password, formConfig)
               .formParams(additionalFields)
               .when()
               .get(authFormUrl)
               .then()
               .statusCode(HttpStatus.SC_OK);
        loadGivenSpecWhen();
        return this;
    }

    /**
     * CSRF prepare call helper.
     *
     * @param authFormUrl
     *            formurl
     * @param username
     *            user
     * @param password
     *            secret
     * @param additionalFields
     *            additional fields
     *
     *
     * @return The same object for fluent api.
     */
    public RestAssuredWrapper withFormCSRF(final String authFormUrl,
                                           final String username,
                                           final String password,
                                           final Map<String, ?> additionalFields) {
        FormAuthConfig formConfig = formAuthConfig().withAutoDetectionOfCsrf();
        formAdditionalFieldNames(formConfig, additionalFields);
        given().auth()
               .form(username, password, formConfig)
               .formParams(additionalFields)
               .when()
               .get(authFormUrl)
               .then()
               .statusCode(HttpStatus.SC_OK);
        loadGivenSpecWhen();
        return this;
    }

    /**
     * CSRF prepare call helper with provided field name.
     *
     * @param authFormUrl
     *            formurl
     * @param username
     *            user
     * @param password
     *            secret
     * @param csrfFieldName
     *            csrf field name
     * @param csrfFieldAsHeader
     *            send as header if true
     * @param additionalFields
     *            additional fields
     *
     *
     * @return The same object for fluent api.
     */
    public RestAssuredWrapper withFormCSRFWithProvidedFieldName(final String authFormUrl,
                                                                final String username,
                                                                final String password,
                                                                final String csrfFieldName,
                                                                final boolean csrfFieldAsHeader,
                                                                final Map<String, ?> additionalFields) {
        FormAuthConfig formConfig = springSecurity().withCsrfFieldName(csrfFieldName);
        if (csrfFieldAsHeader) {
            formConfig = formConfig.sendCsrfTokenAsHeader();
        }
        given().auth()
               .form(username, password, formConfig)
               .formParams(additionalFields)
               .when()
               .get(authFormUrl)
               .then()
               .statusCode(HttpStatus.SC_OK);
        loadGivenSpecWhen();
        return this;
    }

    /**
     * OAUth1 prepare call helper.
     *
     * @param consumerKey
     *            consumer key
     * @param consumerSecret
     *            secret key
     *
     * @param accessToken
     *            acess token.
     * @param secretToken
     *            secret token
     *
     * @return The same object for fluent api.
     */
    public RestAssuredWrapper withOAuth(final String consumerKey,
                                        final String consumerSecret,
                                        final String accessToken,
                                        final String secretToken) {
        loadGivenSpecWhen().auth().oauth(consumerKey, consumerSecret, accessToken, secretToken);
        return this;
    }

    /**
     * OAUth2 prepare call helper.
     *
     * @param accessToken
     *            acess token.
     *
     * @return The same object for fluent api.
     */
    public RestAssuredWrapper withOAuth2(final String accessToken) {
        loadGivenSpecWhen().auth().oauth2(accessToken);
        return this;
    }

    /**
     * Utility method to create Rq spec.
     *
     * @return Rspec {@link RequestSpecification}
     */
    private RequestSpecification loadGivenSpecWhen() {
        if (null == requestSpec) {
            requestSpec = given().spec(requestSpecBuilder.build()).when();
        }
        return requestSpec;
    }

    /**
     * Utility to minimize code.
     *
     * @return Rq {@link RequestSpecification}
     * @throws RobotestException
     *             This method can only be invoked once per call, and be reset after call.
     */
    public RestAssuredWrapper buildRequest() throws RobotestException {
        if (null != requestSpec) {
            throw new RobotestException("BUILD WHEN CAN ONLY BE INVOKED ONCE PER CALL");
        }
        loadGivenSpecWhen();
        return this;
    }

    /**
     * do the Call with the spec request, url and method.
     *
     * @param url
     *            By default only relative path to http://localhost:80 you can specify you wan't.
     * @param httpMethod
     *            The RestAssured suported {@link Method}
     * @return The same object for fluent api.
     */
    public RestAssuredWrapper doCall(final String url, final Method httpMethod) {
        loadGivenSpecWhen();
        if (Method.GET.equals(httpMethod)) {
            response = requestSpec.get(url);
        } else if (Method.POST.equals(httpMethod)) {
            response = requestSpec.post(url);
        } else if (Method.PUT.equals(httpMethod)) {
            response = requestSpec.put(url);
        } else if (Method.DELETE.equals(httpMethod)) {
            response = requestSpec.delete(url);
        } else if (Method.PATCH.equals(httpMethod)) {
            response = requestSpec.patch(url);
        } else if (Method.OPTIONS.equals(httpMethod)) {
            response = requestSpec.options(url);
        } else {
            response = requestSpec.head(url);
        }
        requestSpec = null;
        return this;
    }

    /**
     * Utility method for aply response spec specified object.
     *
     * @return The same object for fluent api.
     * @throws RobotestException
     *             only call this method if doCall is already invoked
     */
    public RestAssuredWrapper thenResponseSpec() throws RobotestException {
        if (null == response) {
            throw new RobotestException("doCall METHOD NOT INVOKED");
        }
        response.then().spec(responseSpecBuilder.build());
        return this;
    }

}
