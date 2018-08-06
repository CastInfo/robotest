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

import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import com.castinfo.devops.robotest.config.RobotestHttpConnConfig;

import io.restassured.config.HttpClientConfig.HttpClientFactory;

public class RestAssuredHttpClientConfig implements HttpClientFactory {

    private RobotestHttpConnConfig httpConfig;

    public RestAssuredHttpClientConfig(final RobotestHttpConnConfig httpConfiguration) {
        httpConfig = httpConfiguration;
    }

    @Override
    public HttpClient createHttpClient() {
        DefaultHttpClient httpclient = configureTimeout(httpConfig.getGeneralTimeout());
        configureProxy(httpConfig,
                       httpclient);
        // return
        // httpclient.build();
        return httpclient;
    }

    /**
     * @param timeoutInMillis
     * @return
     */
    private DefaultHttpClient configureTimeout(final String timeoutInMillisecs) {
        /*
         * HttpClientBuilder dosn't return RA expected AbstractHttpClient!
         *
         * Expecting resolved issue we comment lines of builder code.
         *
         * RequestConfig timeoutParams = RequestConfig. custom() .setConnectTimeout (timeoutInMillis)
         * .setConnectionRequestTimeout (timeoutInMillis) .setSocketTimeout( timeoutInMillis) .build();
         *
         * HttpClientBuilder builder = HttpClientBuilder. create(). setDefaultRequestConfig (timeoutParams);
         */
        int timeoutInMillis = Integer.parseInt(timeoutInMillisecs);
        HttpParams timeoutParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(timeoutParams,
                                                  timeoutInMillis);
        HttpConnectionParams.setSoTimeout(timeoutParams,
                                          timeoutInMillis);
        return new DefaultHttpClient(timeoutParams);
    }

    private void configureProxy(final RobotestHttpConnConfig httpConfig,
                                final DefaultHttpClient httpclient) {
        if (httpConfig.haveProxyHost()) {
            HttpHost proxy = new HttpHost(httpConfig.getProxyHost(),
                                          httpConfig.obtainIntegerProxyPort(),
                                          HttpHost.DEFAULT_SCHEME_NAME);
            // httpclient.setProxy(proxy);
            httpclient.getParams()
                      .setParameter(ConnRoutePNames.DEFAULT_PROXY,
                                    proxy);
            configureNonProxy(httpConfig,
                              httpclient,
                              proxy);
            configureProxyAuth(httpConfig,
                               httpclient);
        }
    }

    private void configureNonProxy(final RobotestHttpConnConfig httpConfig,
                                   final DefaultHttpClient httpclient,
                                   final HttpHost proxy) {
        if (httpConfig.haveNoproxyfor()) {
            List<String> proxyExceptions = Arrays.asList(httpConfig.getNoproxyfor()
                                                                   .split(","));
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy) {
                @Override
                protected HttpHost determineProxy(final HttpHost target,
                                                  final HttpRequest request,
                                                  final HttpContext context) throws HttpException {
                    return proxyExceptions.contains(target.getHostName()) ? null : proxy;
                }
            };
            httpclient.setRoutePlanner(routePlanner);
        }
    }

    private void configureProxyAuth(final RobotestHttpConnConfig httpConfig,
                                    final DefaultHttpClient httpclient) {
        if (httpConfig.haveProxyUser()) {
            Credentials credentials = new UsernamePasswordCredentials(httpConfig.getProxyUser(),
                                                                      httpConfig.getProxySecret());
            AuthScope authScope = new AuthScope(httpConfig.getProxyHost(),
                                                httpConfig.obtainIntegerProxyPort());
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(authScope,
                                         credentials);
            // httpclient.setDefaultCredentialsProvider(credsProvider);
            httpclient.setCredentialsProvider(credsProvider);
        }
    }

}
