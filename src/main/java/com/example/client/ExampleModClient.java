package com.example.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleModClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("handflow");

	@Override
	public void onInitializeClient() {
		LOGGER.info("HandFlow client initialized");
	}
}
