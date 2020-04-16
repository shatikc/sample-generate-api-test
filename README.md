# OpenAPI Generate Provider Tests - Spring Cloud Contract OA3

1. [Goal](#Goal)
1. [Overview](#overview)
1. [Usage and Implementation](#Usage-and-Implementation)  
1. [How to run the Sample Project](#How-to-run-the-Sample-Project)
1. [References](#References)


<a name="Goal"></a>

## Goal
This document describes the usage of Spring Cloud Contract OA3 to generate tests for server side using Open APi specification.
<a name="overview"></a>

## Overview
Spring Cloud Contract OA3 allows us to use the Open APi specification yml as a contract, without to seperate it to many contracts.


<a name="Usage-and-Implementation"></a>

## Usage and Implementation

#### Maven 
Dependencies:
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-contract-verifier</artifactId>
    <version>2.2.1.RELEASE</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

Plugin:
```
 <plugin>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-contract-maven-plugin</artifactId>
        <version>2.1.1.RELEASE</version>
        <extensions>true</extensions>
        <configuration>
             <baseClassForTests>com.example.petstore.ContractBaseTest</baseClassForTests>
        </configuration>
        <dependencies>
        <!--needed to include oa3 converter-->
        <dependency>
            <groupId>guru.springframework</groupId>
            <artifactId>spring-cloud-contract-oa3</artifactId>
            <version>2.1.1.0</version>
            </dependency>
        </dependencies>
    </plugin>
```
* baseClassForTests - is the base test class that the plugin will inherit from.

#### How to config contract

Lets take for example get pet by id:
```
...
paths:
...
  '/pet/{petId}':
    get:
      tags:
        - pet
      summary: Find pet by ID
      description: Returns a single pet
      operationId: getPetById
      x-contracts:
        - contractId: 7
          priority: 7
          name: get id 1 test
          contractPath: /pet/1
        - contractId: 8
          priority: 8
          name: get invalid id
          contractPath: /pet/x
      parameters:
        - name: petId
          in: path
          description: ID of pet to return
          required: true
          schema:
            type: integer
            format: int64
      responses:
        '200':
          description: successful operation
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Pet'
          x-contracts:
            - contractId: 7
              body:
                id: 1
                name: test1
                category:
                  id: 10
                  name: doggie
                status: available
        '400':
          description: Invalid ID supplied
          x-contracts:
            - contractId: 8
              headers:
                Content-Type: application/json;charset=UTF-8
        '404':
          description: Pet not found
      security:
        - api_key: []
        - petstore_auth:
            - 'write:pets'
            - 'read:pets'
```
1. First declearing the test:
```
x-contracts:
  - contractId: 1 # id of the test
    priority: 1 # priority to run the test
    name: test #test function name
```
2. Let's look over our example, get contain two parts:
* Request - can contain query params ,path params and body.
    * Query param example:
      ```
      parameters:
        - name: status
          in: query
          description: Status values that need to be considered for filter
          required: false
          explode: true
          schema:
            type: string
            default: available
            enum:
              - available
              - pending
              - sold
          x-contracts:
            - contractId: 5
              value: available
            - contractId: 6
              value: test
      ```
      for each param we neen to add x-contract related to the contractId

      * Path param example:
        we need to add to the decleration:
        ```contractPath: /pet/x```

      * RequestBody json
      ```
      /pet:
        put:
          tags:
            - pet
          summary: Update an existing pet
          description: Update an existing pet by Id
          operationId: updatePet
          x-contracts:
            - contractId: 1
              name: positive update test
              priority: 1
              description: positive update
          requestBody:
            description: Update an existent pet in the store
            content:
              application/json:
                schema:
                  $ref: '#/components/schemas/Pet'
              application/x-www-form-urlencoded:
                schema:
                  $ref: '#/components/schemas/Pet'
            required: true
            x-contracts:
              - contractId: 1
                headers:
                  Content-Type: application/json;charset=UTF-8
                body:
                  id: 1
                  name: test1
                  category:
                    id: 10
                    name: doggie
                  status: available
          responses:
            '200':
              description: Successful operation
              content:
                application/json:
                  schema:
                    $ref: '#/components/schemas/Pet'
              x-contracts:
                - contractId: 1
                  body:
                    id: 1
                    name: test1
                    category:
                      id: 10
                      name: doggie
                    status: available
                  headers:
                    Content-Type: application/json;charset=UTF-8
            '400':
              description: Invalid ID supplied
            '404':
              description: Pet not found
            '405':
              description: Validation exception
          security:
            - petstore_auth:
                - 'write:pets'
                - 'read:pets'
        ```
* Response - contain return status, content
We have multiple responses for one service so for each code we can expect different result.
In this example:
```
 responses:
        '200':
          description: Successful operation
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Pet'
          x-contracts:
            - contractId: 1
              body:
                id: 1
                name: test1
                category:
                  id: 10
                  name: doggie
                status: available
              headers:
                Content-Type: application/json;charset=UTF-8
        '400':
          description: Invalid ID supplied
        '404':
          description: Pet not found
        '405':
          description: Validation exception
```
we expection the code to be 200 with json in the body.

3. Clean and install, it will generate tests in target like:
```

public class ContractVerifierTest extends ContractBaseTest {
    public ContractVerifierTest() {
    }

    @Test
    public void validate_positive_update_test() throws Exception {
        MockMvcRequestSpecification request = RestAssuredMockMvc.given().header("Content-Type", "application/json;charset=UTF-8", new Object[0]).body("{\"id\":1,\"name\":\"test1\",\"category\":{\"id\":10,\"name\":\"doggie\"},\"status\":\"available\"}");
        ResponseOptions response = RestAssuredMockMvc.given().spec(request).put("/pet", new Object[0]);
        SpringCloudContractAssertions.assertThat(response.statusCode()).isEqualTo(200);
        DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
        JsonAssertion.assertThatJson(parsedJson).field("['name']").isEqualTo("test1");
        JsonAssertion.assertThatJson(parsedJson).field("['category']").field("['id']").isEqualTo(10);
        JsonAssertion.assertThatJson(parsedJson).field("['status']").isEqualTo("available");
        JsonAssertion.assertThatJson(parsedJson).field("['id']").isEqualTo(1);
        JsonAssertion.assertThatJson(parsedJson).field("['category']").field("['name']").isEqualTo("doggie");
    }
 }
```
<a name="How-to-run-the-Sample-Project"></a>
## How to run the Sample Project
1. Clone the project
2. mvn clean install


