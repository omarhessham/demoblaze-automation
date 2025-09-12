Feature: Add products to cart

  Background:
    Given I open the Demoblaze home page

  @add_product1
  Scenario: Add one laptop to the cart
    When I navigate to "Laptops" category
    And I add product "Sony vaio i5" to the cart
    Then I should see an alert containing "Product added"
    And I open the cart
    Then I should see items "Sony vaio i5" present in the cart
