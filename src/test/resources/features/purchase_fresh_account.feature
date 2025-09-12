Feature: Purchase two products with a fresh account

  Background:
    Given I open the Demoblaze home page

  @purchase @fresh
  Scenario: Sign up, then purchase two laptops successfully
    When I open the "Sign up" modal
    And I sign up with a fresh random username and password
    Then I should see an alert containing "Sign up successful."

    When I open the "Log in" modal
    And I log in with the last signed-up credentials

    When I navigate to "Laptops" category
    And I add product "Sony vaio i5" to the cart
    And I navigate to "Laptops" category
    And I add product "Dell i7 8gb" to the cart

    And I open the cart
    Then I should see items "Sony vaio i5, Dell i7 8gb" present in the cart
    And the cart total should equal the sum of the item prices

    When I place the order with details:
    | name    | Test User        |
    | country | EG               |
    | city    | Cairo            |
    | card    | 4242424242424242 |
    | month   | 12               |
    | year    | 2030             |
    Then I should see a success confirmation containing "Thank you for your purchase!"
