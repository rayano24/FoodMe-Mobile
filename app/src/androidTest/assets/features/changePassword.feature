#Feature: User changes his password
#  As a user, I would like to be able to change my password.
#
#  Scenario Outline:  Successful password change (Normal scenario)
#    Given I have an existing account
#    And I am logged in
#    When I select the change password option
#    And I enter my <old password> and a <new password>
#    Then My password should be changed
#
#    Examples:
#      | old password  |  new password   |
#      | 'password'    |  '123'          |
#
#  Scenario Outline:  Unsuccessful password change (Error scenario)
#    Given I have an existing account
#    And I am logged in
#    When I select the change password option
#    And I enter my <wrong old password> and a <new password>
#    Then My password should not change
#
#    Examples:
#      | wrong old password  |  new password   |
#      | 'asdjjafds'         |  '123'          |