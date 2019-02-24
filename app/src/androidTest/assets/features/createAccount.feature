Feature: User creates an account
  As a user, I would like to create an account so that I can receive personalized restaurant recommendations.

  Background:
    Given I am on the register form

  Scenario Outline:  Successful account creation (Normal scenario)
    When I enter a <username>, an <email>, a <phone number> and a <password>
    Then I should have an account

    Examples:
      | username   |  email     |  phone number    |   password    |
      | 'a'        |  'email'   |  '450-555-5555'  |     'b'       |

  Scenario Outline:  Unsuccessful account creation  (Error scenario)
    Given I have an existing account
    When I enter a <username>, an <email>, a <phone number> and a <password>
    Then I should not be able to create an account

    Examples:
      | username   |  email     |  phone number    |   password    |
      | 'a'        |  'email'   |  '450-555-5555'  |     'b'       |