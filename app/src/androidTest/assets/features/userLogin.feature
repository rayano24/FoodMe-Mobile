Feature: User logs in to the system
  As a user, I would like to log in so I can receive personalized recommendations.

  Scenario Outline:
    Given I have an existing account
    When I enter my <username> and my <password>
    Then I should be able to successfully login

    Examples:
      | username  |  password    |
      | 'admin'   |  'password'  |

#  Scenario Outline:
#    Given I have an existing account
#    When I enter my <email> and my <password>
#    Then I should be able to successfully login
#
#    Examples:
#      | email     |   password
#      | 'admin'   |  'password'
#
#  Scenario Outline:
#    When I enter an invalid combination of <username> and <password>
#    Then I should not be able to login
#    And I should be prompted to re-enter my credentials
#
#    Examples:
#      | username     |   password   |
#      | ''           |     ''       |