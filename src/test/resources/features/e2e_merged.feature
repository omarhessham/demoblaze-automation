Feature: E2E - Signup, Login, Add two products (merged)

  Background:
    Given I open the Demoblaze home page

  @e2e_merged @signup
  Scenario: Signup success -> Login -> Add Product 1 -> Add Product 2
    When I open the "Sign up" modal
    And I sign up with a fresh random username and password
    Then I should see an alert containing "Sign up successful."

    When I open the "Log in" modal
    And I log in with the last signed-up credentials
    Then I should be logged in as the last signed-up user

    When I navigate to "Laptops" category
    And I add product "Sony vaio i5" to the cart
    Then I should see an alert containing "added"

    When I navigate to "Laptops" category
    And I add product "Dell i7 8gb" to the cart
    Then I should see an alert containing "added"
