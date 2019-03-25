#Feature: User changes his password
#  As a user, I would like to be able to change my password.
##
#  Scenario Outline:  Successful password change (Normal scenario)
#
#    Given I am logged in as <username> and <old password>
#    When I select the change password option
#    And I enter my <old password> and a <new password>
#    Then My password should be changed
#
#    Examples:
#      | username        | old password  | new password  |
#      | 'admin'         | 'password'    | 'password123' |
#      | 'admin'         | 'password123' | 'password'    |
#      | 'gherkin_test1' | 'test123'     | 'alpha10'     |
#      | 'gherkin_test1' | 'alpha10'     | 'gherkin'     |
#
#  Scenario Outline:  Unsuccessful password change (Error scenario)
#
#    Given I am logged in as <username> and <old password>
#    When I select the change password option
#    And I enter my <wrong old password> and a <new password>
#    Then My password should not change
#
#    Examples:
#      | username | old password | wrong old password | new password   |
#      | 'admin'  | 'password'   | 'wrong_password'   | 'new_password' |
#      | 'admin'  | 'password'   | 'pass'             | 'word'         |