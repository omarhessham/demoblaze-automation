@all @neg
Feature: Attempt purchase with invalid payment details

  Background:
    Given I NEG open the Demoblaze home page
    And I NEG add product "Samsung galaxy s6" to the cart
    And I NEG open the cart

  # Demoblaze only errors on empty name or card; this is the stable negative.
  Scenario: Attempt to purchase with missing credit card shows an error
    When I NEG click Place Order with name "Omar Hesham" and EMPTY card
    Then I NEG should see an alert containing "Please fill out Name and Creditcard."
