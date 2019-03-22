Feature: Getting information on a selected restaurant
  As a user
  I would like to get all information available on the restaurants that I select
  So that I can make an informed decision

  Scenario Outline: Filter by rating when logged in (Normal scenario)
    Given I am logged in
    And I am on the restaurants page
    When I select a restaurant
    Then I should see its information

    Examples:
      | rating      |
      | 'None'       |
      | 'Chinese'    |
      | 'Italian'    |

  Scenario Outline: Filter by rating without logging in (Alternate scenario)
    Given I choose not to log in
    And I am on the restaurants page
    When I select a restaurant
    Then I should see its information

    Examples:
      | rating      |
      | 'None'       |
      | 'Chinese'    |
      | 'Italian'    |