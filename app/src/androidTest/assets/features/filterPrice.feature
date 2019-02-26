Feature: User filters restaurants by price
  As a user, I would like to filter restaurants by price so that I can make a selection that fits my needs.

  Scenario Outline: Filter by price when logged in (Normal scenario)
    Given I am logged in
    And I am on the restaurants page
    When I select my price <price> preferences
    Then I should see restaurants that fit my price <price> selection

    Examples:
      | price        |
      | 'None'       |
      | ''           |

  Scenario Outline: Filter by cuisine without loggin in (Alternate scenario)
    Given I choose not to log in
    And I am on the restaurants page
    When I select my price <price> preferences
    Then I should see restaurants that fit my price <price> selection

    Examples:
      | price        |
      | 'None'       |
      | ''           |