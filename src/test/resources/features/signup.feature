Feature: User signup


  Scenario: Verify that User Can Sign Up Successfully
    Given I open the Demoblaze home page
    When I open the "Sign up" modal
    And I sign up with a fresh random username and password
    Then I should see an alert containing "Sign up successful."
