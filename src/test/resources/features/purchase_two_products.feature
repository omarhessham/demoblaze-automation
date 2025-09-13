@all @two_products
Feature: Two laptops then checkout

  Scenario: Sign up → Log in → Add two laptops → Validate cart → Checkout
    Given I open the Demoblaze home page
    When I open the "Sign up" modal
    And I sign up with a fresh random username and password
    Then I should see an alert containing "Sign up successful."
    When I open the "Log in" modal
    And I log in with the last signed-up credentials
    Then I should be logged in as the last signed-up user

    When I navigate to "Laptops" category
    And I add product "Sony vaio i5" to the cart
    And I return to the product grid
    When I navigate to "Laptops" category
    And I add product "Dell i7 8gb" to the cart

    Then I open the cart
    And I should see items "Sony vaio i5, Dell i7 8gb" present in the cart
    And the cart total should equal the sum of the item prices

    When I place the order
    Then the modal total should equal the cart total
    When I fill checkout details "Omar Hesham", "EG", "Cairo", "4111111111111111", "12", "2027"
    And I purchase
    Then I should see a success message
