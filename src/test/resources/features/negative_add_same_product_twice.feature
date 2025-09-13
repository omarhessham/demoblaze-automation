@all @neg
Feature: Add same product twice

  Scenario: Adding the same product twice updates the cart (two rows and doubled total)
    Given I NEG open the Demoblaze home page
    When I NEG add product "Samsung galaxy s6" to the cart
    And I NEG add product "Samsung galaxy s6" to the cart
    And I NEG open the cart
    Then I NEG should see 2 rows for "Samsung galaxy s6"
    And I NEG verify the total equals price of "Samsung galaxy s6" multiplied by 2
