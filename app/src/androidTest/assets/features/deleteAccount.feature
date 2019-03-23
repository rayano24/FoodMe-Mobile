#Feature: User deletes an account
#  As a user, I would like to be able to delete my account.
#
#  Scenario Outline:  Successful account deletion (Normal scenario)
#    Given I am logged in as <username> and <old password>
#    When I select the delete account option
#    Then I should not have an account
#    And I should be logged out
#
#    Examples:
#      | username | old password |
#      | 'test1'  | 'password'   |