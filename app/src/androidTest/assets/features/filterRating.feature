Feature: User filters restaurants by rating
  As a user, I would like to filter restaurants by rating so that I can make a selection that fits my needs.

  Scenario Outline: Filter by rating when logged in (Normal scenario)
    Given I am logged in
    And I am on the restaurants page
    When I select my rating <rating> preferences
    Then I should see restaurants that fit my rating <rating> selection

    Examples:
      | rating    |
      | '$$$$'    |
      | '$$$'     |
      | '$$'      |
      | '$'       |

  Scenario Outline: Filter by rating without logging in (Alternate scenario)
    Given I choose not to log in
    And I am on the restaurants page
    When I select my rating <rating> preferences
    Then I should see restaurants that fit my rating <rating> selection

    Examples:
      | rating    |
      | '$$$$'    |
      | '$$$'     |
      | '$$'      |
      | '$'       |
