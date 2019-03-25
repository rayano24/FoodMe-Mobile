#Feature: User logs in to the system
#  As a user, I would like to log in so I can receive personalized recommendations.
#
#  Background:
#    Given I am on the sign in form
#
#  Scenario Outline:  Successful login (Normal scenario)
#    Given I have an existing account
#    When I enter my <username> and my <password>
#    Then I should be able to successfully login
#
#    Examples:
#      | username        | password  |
#      | 'gherkin_test1' | 'test123' |
#      | 'gherkin_test2' | 'test123' |
#      | 'gherkin_test3' | 'test123' |
#
#  Scenario Outline:  Unsuccessful login (Error scenario)
#    When I enter an invalid combination of <username> and <password>
#    Then I should not be able to login
#    And I should be prompted to re-enter my credentials
#
#    Examples:
#      | username      | password     |
#      | 'user'        | ''           |
#      | ''            | 'pw'         |
#      | 'nonexistent' | 'nonexistent |
