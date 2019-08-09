package rest.sample.personmanager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonManagerApplicationTests {

	@LocalServerPort
	private int port;

	private String baseUrl = "http://localhost";

	private static RestTemplate restTemplate = null;

	@BeforeAll
	static void init() {
		restTemplate = new RestTemplate();
	}

	@BeforeEach
	void setUp(){
		baseUrl = baseUrl.concat(":").concat(port +"").concat("/persons");
	}

	@Test
	void testGET() {
		Person person = restTemplate.getForObject(baseUrl.concat("/1"), Person.class);
		assertAll(
				() -> assertNotNull(person),
				() -> assertNotNull(person.getName()),
				() -> assertNull(person.getPassword())
		);
	}

	@Test
	void testPOST() {
		Person person = new Person();
		person.setName("Gigi Pedala");
		person.setPassword("gigipedala");

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		final HttpEntity<Person> postRequest = new HttpEntity<>(person, headers);
		Person newPerson = restTemplate.postForObject(baseUrl, postRequest, Person.class);

		assertAll(
				() -> assertNotNull(newPerson),
				() -> assertEquals(person.getName(), newPerson.getName()),
				() -> assertNotNull(newPerson.getId())
		);
	}
}
