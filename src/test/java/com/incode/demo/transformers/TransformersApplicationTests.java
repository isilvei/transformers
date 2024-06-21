package com.incode.demo.transformers;

import com.incode.demo.transformers.domain.StringRemoveTransformer;
import com.incode.demo.transformers.domain.StringReplaceTransformer;
import com.incode.demo.transformers.domain.StringTransliterationTransformer;
import com.incode.demo.transformers.domain.TransformRequest;
import com.incode.demo.transformers.service.TransformerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static com.incode.demo.transformers.TestDataMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
class TransformersApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@Autowired
	private StringRemoveTransformer stringRemoveTransformer;

	@Autowired
	private StringReplaceTransformer stringReplaceTransformer;

	@Autowired
	private StringTransliterationTransformer stringTransliterationTransformer;

	@SpyBean
	@Autowired
	private TransformerService transformerService;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}


	@Test
	void shouldNotCallServiceWithoutATransformerMatch() throws Exception {
		String reqBody = """
				{
					"elements": [
					{
					  "value": "Hello, World! Привет, мир!",
					  "transformers": [
						{
						  "groupId": "group1",
						  "transformerId": "transformer1",
						  "parameters": ["[aeiou]"]
						}
					  ]
					  }
					]
                  }""";

		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/api/transform")
								.contentType(MediaType.APPLICATION_JSON_VALUE)
								.accept(MediaType.APPLICATION_JSON_VALUE)
								.characterEncoding(StandardCharsets.UTF_8)
								.content(reqBody)
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("elements").isArray());

		Mockito.verify(this.transformerService, Mockito.times(0)).processTransformRequest(
				ArgumentMatchers.any(TransformRequest.class)
		);
	}


	@ParameterizedTest
	@ValueSource(strings = {
			STRING_REMOVE_TRANSFORMER_REQUEST,
			STRING_REPLACE_TRANSFORMER_REQUEST,
			STRING_TRANSLITERATION_TRANSFORMER_REQUEST
	})
	void shouldProcessIndividualTransformer(String reqBody) throws Exception {

		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/api/transform")
								.contentType(MediaType.APPLICATION_JSON_VALUE)
								.accept(MediaType.APPLICATION_JSON_VALUE)
								.characterEncoding(StandardCharsets.UTF_8)
								.content(reqBody)
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("elements").isArray());

		Mockito.verify(this.transformerService, Mockito.times(1)).processTransformRequest(
				ArgumentMatchers.any(TransformRequest.class)
		);
	}

	@Test
	void shouldProcessStringRemoveTransformer() throws Exception {
		String expectedValueTransformed = "Hll, Wrld! Привет, мир!";

		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/api/transform")
								.contentType(MediaType.APPLICATION_JSON_VALUE)
								.accept(MediaType.APPLICATION_JSON_VALUE)
								.characterEncoding(StandardCharsets.UTF_8)
								.content(STRING_REMOVE_TRANSFORMER_REQUEST)
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("elements").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].transformedValue")
						.value(expectedValueTransformed));

		Mockito.verify(this.transformerService, Mockito.times(1)).processTransformRequest(
				ArgumentMatchers.any(TransformRequest.class)
		);
	}

	@Test
	void shouldProcessStringReplaceTransformer() throws Exception {
		String expectedValueTransformed = "Hemmo, Wormd! Привет, мир!";

		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/api/transform")
								.contentType(MediaType.APPLICATION_JSON_VALUE)
								.accept(MediaType.APPLICATION_JSON_VALUE)
								.characterEncoding(StandardCharsets.UTF_8)
								.content(STRING_REPLACE_TRANSFORMER_REQUEST)
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("elements").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].transformedValue")
						.value(expectedValueTransformed));

		Mockito.verify(this.transformerService, Mockito.times(1)).processTransformRequest(
				ArgumentMatchers.any(TransformRequest.class)
		);
	}


	@Test
	void shouldProcessStringTransliterationTransformer() throws Exception {
		String expectedValueTransformed = "Hello, World! Privet, mir!";

		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/api/transform")
								.contentType(MediaType.APPLICATION_JSON_VALUE)
								.accept(MediaType.APPLICATION_JSON_VALUE)
								.characterEncoding(StandardCharsets.UTF_8)
								.content(STRING_TRANSLITERATION_TRANSFORMER_REQUEST)
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("elements").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].transformedValue")
						.value(expectedValueTransformed));

		Mockito.verify(this.transformerService, Mockito.times(1)).processTransformRequest(
				ArgumentMatchers.any(TransformRequest.class)
		);
	}

	@Test
	void shouldProcessAllStringTransformers() throws Exception {
		String expectedValueTransformed = "Hmm, Wrmd! Privet, mir!";

		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/api/transform")
								.contentType(MediaType.APPLICATION_JSON_VALUE)
								.accept(MediaType.APPLICATION_JSON_VALUE)
								.characterEncoding(StandardCharsets.UTF_8)
								.content(ALL_STRING_TRANSFORMERS_REQUEST)
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("elements").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].transformedValue")
						.value(expectedValueTransformed));

		Mockito.verify(this.transformerService, Mockito.times(1)).processTransformRequest(
				ArgumentMatchers.any(TransformRequest.class)
		);
	}

}
