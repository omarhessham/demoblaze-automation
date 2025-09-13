@all @neg
Feature: Sign up then sign up again with same user

  Background:
    Given I NEG open the Demoblaze home page

  Scenario: Sign up successfully, then fail with the same username
    When I NEG sign up with a NEW username and password
    Then I NEG should see an alert containing "Sign up successful."
    When I NEG try to sign up again with the same credentials
    Then I NEG should see an alert containing "This user already exist."
