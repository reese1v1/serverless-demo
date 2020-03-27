/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package serverless.handler;

import java.util.Optional;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.CosmosDBOutput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import org.springframework.cloud.function.adapter.azure.AzureSpringBootRequestHandler;

public class FooHandler extends AzureSpringBootRequestHandler<String, String> {

	@FunctionName("cosmosDBCreateSCF")
	public String execute(
		@HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) 
			HttpRequestMessage<Optional<String>> request,
		@CosmosDBOutput(
			name = "database",
			databaseName = "demo",
			collectionName = "person",
			connectionStringSetting = "CosmosDbConnection")
		OutputBinding<String> outputItem,
		ExecutionContext context) {
		
		JsonObject responseJson = new JsonObject();
		// handleOutput(request.getBody().get(), outputItem, context);
		
		try {
			// Parse query parameter
			if (request.getBody().isPresent()) {
				final String result = handleRequest(request.getBody().get(), context);
				// database access
				outputItem.setValue(result);
				responseJson.addProperty("message", result);
			} else {
				responseJson.addProperty("message", "No data received");
			}

		} catch (JsonParseException ex) {
			responseJson.addProperty("statusCode", 400);
            responseJson.addProperty("exception", ex.getMessage());
		}

		return responseJson.toString();
	}

}