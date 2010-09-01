/**
 * Copyright (C) 2010 Zhang, Guilin <guilin.zhang@hotmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.code.http.impl;

import java.net.MalformedURLException;
import java.net.URL;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.code.http.Headers;
import com.google.code.http.Request;
import com.google.code.http.RequestTestCase;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 *
 */
public final class PostTestCase extends RequestTestCase {
	
	@Test(expectedExceptions = MalformedURLException.class)
	public void construct_cause_exception() throws MalformedURLException {
		new Post("code.google.com");
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void toMessage_cause_exception() throws MalformedURLException {
		Request request = createRequest("http://www.google.com");
		request.toMessage();
	}
	
	@Test
	public void toMessage() throws MalformedURLException {
		assertion("http://www.google.com/search?q=http4j", "POST /search HTTP/1.1\r\nHost:www.google.com\r\n" + getDefaultHeaderString() + "Content-Length:8\r\n\r\nq=http4j");
		assertion("https://www.google.com:444/search?q=http4j&hl=en","POST /search HTTP/1.1\r\nHost:www.google.com:444\r\n" + getDefaultHeaderString() + "Content-Length:14\r\n\r\nq=http4j&hl=en");
		assertion("http://localhost:8080/index.jsp;jsessionid=ABCDE?u=colin&pwd=http4j","POST /index.jsp;jsessionid=ABCDE HTTP/1.1\r\nHost:localhost:8080\r\n" + getDefaultHeaderString() + "Content-Length:18\r\n\r\nu=colin&pwd=http4j");
	}
	
	@Test(dependsOnMethods = "toMessage")
	public void addParameter_string_strings() throws MalformedURLException {
		Post post = new Post("http://www.google.com/search");
		post.addParameter("q", "http4j");
		Assert.assertEquals( post.toMessage(), "POST /search HTTP/1.1\r\nHost:www.google.com\r\n" + getDefaultHeaderString() + "Content-Length:8\r\n\r\nq=http4j");
		post.addParameter("m", "GET", "POST");
		Assert.assertEquals( post.toMessage(), "POST /search HTTP/1.1\r\nHost:www.google.com\r\n" + getDefaultHeaderString() + "Content-Length:21\r\n\r\nq=http4j&m=GET&m=POST");
	}

	@Test(dependsOnMethods = "toMessage")
	public void setHeader() throws MalformedURLException {
		Post post = new Post("http://www.google.com/?u=http4j&p=http4j");
		post.setHeader(Headers.ACCEPT_ENCODING, "ISO-8859-1");
		Assert.assertEquals(post.toMessage(), "POST / HTTP/1.1\r\nHost:www.google.com\r\n" + getDefaultHeaderString() + "Accept-Encoding:ISO-8859-1\r\nContent-Length:17\r\n\r\nu=http4j&p=http4j");
		post.setHeader(Headers.ACCEPT_ENCODING, "UTF-8");
		Assert.assertEquals(post.toMessage(), "POST / HTTP/1.1\r\nHost:www.google.com\r\n" + getDefaultHeaderString() + "Accept-Encoding:UTF-8\r\nContent-Length:17\r\n\r\nu=http4j&p=http4j");
	}
	
	@Test(dependsOnMethods = "toMessage")
	public void construct_url() throws MalformedURLException {
		URL url = new URL("http://www.google.com/search?q=http4j");
		Post post = new Post(url);
		Assert.assertEquals(post.toMessage(), "POST /search HTTP/1.1\r\nHost:www.google.com\r\n" + getDefaultHeaderString() + "Content-Length:8\r\n\r\nq=http4j");
	}
	
	@Override
	protected Request createRequest(String url) throws MalformedURLException {
		return new Post(url);
	}
}