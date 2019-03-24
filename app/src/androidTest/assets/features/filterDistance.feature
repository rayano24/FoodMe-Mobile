Feature: User filters restaurants by distance
  As a user, I would like to filter restaurants by distance, so I can visit restaurants
  that are closest to me.

  Scenario Outline: Filter by distance when on restaurant page and logged in (Normal Scenario)
    Given I am logged in
    And I am on the restaurants page
    When I enter my preferred maximum distance <distance>
    Then I should see restaurants that are within this distance <distance>

    Examples:
      | distance |
      | 50       |

  Scenario Outline: Filter by distance when on restaurant page without logging in (Alternate Scenario)
    Given I choose not to log in
    And I am on the restaurants page
    When I enter my preferred maximum distance <distance>
    Then I should see restaurants that are within this distance <distance>

    Examples:
      | distance |
      | 50       |