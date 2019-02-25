Feature: User filters restaurants by cuisine
  As a user, I would like to filter restaurants by cuisine so that I can make a selection that fits my craving.

  Scenario Outline: Filter by cuisine when logged in (Normal scenario)
    Given I am logged in
    When I select my cuisine <cuisine> preferences
    Then I should see restaurants that fit my cuisine <cuisine> selection

    Examples:
      | cuisine      |
      | 'None'       |
      | 'Chinese'    |
      | 'Italian'    |

  Scenario Outline: Filter by cuisine without logging in (Alternate scenario)
    Given I choose not to log in
    When I select my cuisine <cuisine> preferences
    Then I should see restaurants that fit my cuisine <cuisine> selection

    Examples:
      | cuisine      |
      | 'None'       |
      | 'Chinese'    |
      | 'Italian'    |