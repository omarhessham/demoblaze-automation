@all @neg
Feature: Login with incorrect credentials shows error

  Scenario: Attempt to log in with wrong username or password
    Given I NEG open the Demoblaze home page
    When I NEG log in with username "no_such_user_123" and password "wrongpass"
    Then I NEG should see an alert containing "User does not exist."
