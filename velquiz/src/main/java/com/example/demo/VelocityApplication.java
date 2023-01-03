package com.example.demo;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.config.appconfiguration;

@SpringBootApplication
public class VelocityApplication implements CommandLineRunner {
	@Autowired
	protected VelocityEngine velocity;
	public static void main(String[] args) {
		SpringApplication.run(VelocityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("bikes.json").getFile());
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(file);

		VelocityEngine velocityEngine = new appconfiguration().velocityEngine();
		velocityEngine.init();

		Template t = velocityEngine.getTemplate("templates/index.vm");
		VelocityContext context = new VelocityContext();
		context.put("payload", jsonNode);

		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		System.out.println("Velocity with JSON");
		System.out.println(writer.toString());
		FileWriter fw = new FileWriter("demoVM.html");
		fw.write(writer.toString());
		fw.close();
		
	}
}
