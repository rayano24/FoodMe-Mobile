#Feature: User creates an account
#  As a user, I would like to create an account so that I can receive personalized restaurant recommendations.
#
#  Background:
#    Given I am on the register form
#
#  Scenario Outline:  Successful account creation (Normal scenario)
#    When I enter a <firstname>, a <lastname>, a <username>, an <email> and a <password>
#    Then I should have an account
#
#    Examples:
#      | firstname   |  lastname     |  username        |   email                |   password   |
#      | 'Test'      |  '1'          |  'test1'         |  'test1@gmail.com'     |   'test123'  |
#
#  Scenario Outline:  Unsuccessful account creation  (Error scenario)
#    Given I have an existing account
#    When I enter a <firstname>, a <lastname>, a <username>, an <email> and a <password>
#    Then I should not be able to create an account
#
#    Examples:
#      | firstname   |  lastname     |  username        |   email                |   password   |
#      | 'Test'      |  '1'          |  'test1'         |  'test1@gmail.com'     |   'test123'  |