package com.example.test;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.List;
import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */

    private static final String SERVICE_BUS_FQDN = "sbpoc20122023.servicebus.windows.net";
    private static final String QUEUE_NAME = "einvoice";
    private static final String CONNECTION_STRING = "Endpoint=sb://sbpoc20122023.servicebus.windows.net/;SharedAccessKeyName=pru-policy;SharedAccessKey=PPVJpavg5NQnKMOtAjZMkWd1/X5HVzpWM+ASbK5jMkM=;EntityPath=einvoice";

    private ServiceBusSenderClient senderClient = null;
    private ObjectMapper objectMapper = new ObjectMapper();
    private  void sendMessage(EInvoice invoice){
        senderClient.sendMessage(new ServiceBusMessage(objectMapper.writeValueAsString(invoice));
    }

    public Function(){
                 senderClient = new ServiceBusClientBuilder()
                .connectionString(CONNECTION_STRING)
                .sender()
                .queueName(QUEUE_NAME)
                .buildClient();
                 System.out.println("----------------constructor:" + senderClient);
    }

    @FunctionName("TriggerStringPost")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        // Item list
        //context.getLogger().info("Request body is: " + request.getBody().orElse(""));

        // Check request body
        if (!request.getBody().isPresent()) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Document not found.")
                    .build();
        }
        else {

            try {
                String jsonDocument = request.getBody().get();
                List<EInvoice> einvoiceList = objectMapper.readValue(jsonDocument, new TypeReference<List<EInvoice>>() {
                });
                einvoiceList.forEach(this::sendMessage);
            }
            catch (Exception e){
                e.printStackTrace();
                return request.createResponseBuilder(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body("I got it...but got error when passing Json doc:")
                        .build();
            }

            return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body("I got it...validated the Json doc.")
                    .build();

            // return JSON from to the client
            // Generate document
//            final String body = request.getBody().get();
//            final String jsonDocument = "{\"id\":\"123456\", " +
//                    "\"description\": \"" + body + "\"}";
//            return request.createResponseBuilder(HttpStatus.OK)
//                    .header("Content-Type", "application/json")
//                    .body(jsonDocument)
//                    .build();
        }
    }
//    @FunctionName("HttpExample")
//    public HttpResponseMessage run(
//            @HttpTrigger(
//                name = "req",
//                methods = {HttpMethod.GET, HttpMethod.POST},
//                authLevel = AuthorizationLevel.ANONYMOUS)
//                HttpRequestMessage<Optional<String>> request,
//            final ExecutionContext context) {
//        context.getLogger().info("Java HTTP trigger processed a request.");
//
//        // Parse query parameter
//        final String query = request.getQueryParameters().get("name");
//        final String name = request.getBody().orElse(query);
//
//        if (name == null) {
//            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
//        } else {
//            return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name).build();
//        }
//    }
}
