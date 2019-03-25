#Feature: User sees a list of liked restaurants
#  As a user
#  I would like to be able to see a list of my favorite restaurants
#  So that I can see their information
#
#  Scenario Outline: Listing all liked restaurants (Normal scenario)
#    Given I am logged in
#    And I am on the restaurants page
#    When I select the list all liked restaurants option
#    Then I should be able to see a list of my favorite restaurants
#
#    Examples:
#      | rating      |
#      | 'None'       |
#      | 'Chinese'    |
#      | 'Italian'    |