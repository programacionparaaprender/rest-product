package org.programacionparaaprender.quarkus.starting;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.jackson.ObjectMapperCustomizer;
import jakarta.inject.Singleton;

@Singleton
public class MyObjectMapperCustomizer implements ObjectMapperCustomizer{

	@Override
	public void customize(ObjectMapper objectMapper) {
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		
	}

}
