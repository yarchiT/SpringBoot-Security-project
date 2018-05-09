package com.tykhonchuk.coursework;

import com.tykhonchuk.coursework.model.User;
import com.tykhonchuk.coursework.repository.RoleRepository;
import com.tykhonchuk.coursework.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = CourseworkApplication.class)
@WebAppConfiguration
public class CourseworkApplicationTests {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));

	private MockMvc mockMvc;
	private User user;
	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
				.findAny()
				.orElse(null);

		assertNotNull("the JSON message converter must not be null",
				this.mappingJackson2HttpMessageConverter);
	}

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();

		this.userRepository.deleteAllInBatch();
		this.roleRepository.deleteAllInBatch();

		this.user = new User("user@test.com", "Name", "Surname", "password");
		this.user = userRepository.save(user);
	}

	@Test
	public void createUser() throws Exception {
		String urlJson = json(new User("user2@test.com", "userName", "suerSurname", "123456"));

		this.mockMvc.perform(post("/registration")
				.contentType(contentType)
				.content(urlJson))
				.andExpect(status().isCreated());
	}

	@Test
	public void tryAccessAdminPage() throws Exception {

		this.mockMvc.perform(get("/admin/home")
				.contentType(contentType))
				.andExpect(status().isCreated());
	}

/*
	@Test
	public void passJson() throws Exception {
		this.mockMvc.perform(get("/urls"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(urlCheck.getId().intValue())))
				.andExpect(jsonPath("$[0].urlS", is(this.urlCheck.getUrlS())))
				.andExpect(jsonPath("$[0].responseCode", is(this.urlCheck.getResponseCode())));
	}*/



	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(
				o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
	@Test
	public void contextLoads() {
	}

}
