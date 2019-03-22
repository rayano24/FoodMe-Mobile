#Feature: User deletes an account
#  As a user, I would like to be able to delete my account.
#
#  Scenario:  Successful account deletion (Normal scenario)
#    Given I have an existing account
#    And I am logged in
#    When I select the delete account option
#    Then I should not have an account
#    And I should be logged out