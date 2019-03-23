#Feature: User changes his password
#  As a user, I would like to be able to change my password.
#
#  Scenario Outline:  Successful password change (Normal scenario)
#
#    Given I am logged in as <username> and <old password>
#    When I select the change password option
#    And I enter my <old password> and a <new password>
#    Then My password should be changed
#
#    Examples:
#     | username        | old password  |  new password   |
#     |  'test1'        | 'password'    |  '123'          |
#
#  Scenario Outline:  Unsuccessful password change (Error scenario)
#
#    Given I am logged in as <username> and <old password>
#    When I select the change password option
#    And I enter my <wrong old password> and a <new password>
#    Then My password should not change
#
#    Examples:
#      | username        | wrong old password  |  new password   |
#      |  'test1'        | 'asdas'             |  '123'          |