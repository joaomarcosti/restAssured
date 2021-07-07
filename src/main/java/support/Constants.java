package support;

import io.restassured.http.ContentType;

public interface Constants {

	String ACCESS_TOKEN = "Bearer d433978bebd2b8e4739b550e484f76fc0dd30d34e80eebdd9949d8eafca235a4";
//	String URL_BASE = "http://seubarriga.wcaquino.me";
	String URL_BASE = "http://barrigarest.wcaquino.me";
	String BASE_PATH = "";
	Integer PORT = 80;
	ContentType CONTENT_TYPE = ContentType.JSON;
	Long TIMED_OUT = 8000l;
}