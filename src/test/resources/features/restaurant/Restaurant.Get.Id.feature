Feature: Restaurant Data Retrieval
  As a user
  I want to retrieve data of a restaurant by providing a valid UUID
  So that I can access detailed information about the restaurant

  Scenario: Retrieve Data for an Existing Restaurant
    Given there is a restaurant with the UUID "a3d7fe1f-5b0f-4bdb-8d5b-cc74c15fb98f"
    When I request the restaurant data with the UUID "a3d7fe1f-5b0f-4bdb-8d5b-cc74c15fb98f"
    Then I should receive the details of the restaurant

  Scenario: Retrieve Data for a Different Existing Restaurant
    Given there is another restaurant with a different UUID "4e72f122-0e88-4a7a-8b8a-8d56705ad1dc"
    When I request the restaurant data with the UUID "4e72f122-0e88-4a7a-8b8a-8d56705ad1dc"
    Then I should receive the details of the other restaurant

  Scenario: Attempt to Retrieve Data with an Invalid UUID
    Given there is no restaurant with the UUID "invalid-uuid-format"
    When I request the restaurant data with the UUID "invalid-uuid-format"
    Then the response status code should be 400 (Bad Request)

  Scenario: Attempt to Retrieve Data with an Empty UUID
    Given there is no restaurant with an empty UUID
    When I request the restaurant data with an empty UUID
    Then the response status code should be 400 (Bad Request)

  Scenario: Attempt to Retrieve Data with a Non-Existent UUID
    Given there is no restaurant with the UUID "non-existent-uuid"
    When I request the restaurant data with the UUID "non-existent-uuid"
    Then the response status code should be 400 (Bad Request)
