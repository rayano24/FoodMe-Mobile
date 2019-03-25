#Feature: User filters restaurants by open
#  As a user, I would like to filter restaurants so that I know which are open.
#
#  Scenario Outline: Filter by open when logged in (Normal scenario)
#    Given I am logged in
#    And I am on the restaurants page
#    When I select my open <open> preferences
#    Then I should see restaurants that fit my open <open> selection
#
#    Examples:
#      | open      |
#      | 'false'   |
#      | 'true'    |
#
#  Scenario Outline: Filter by open without logging in (Alternate scenario)
#    Given I choose not to log in
#    And I am on the restaurants page
#    When I select my open <open> preferences
#    Then I should see restaurants that fit my open <open> selection
#
#    Examples:
#      | open      |
#      | 'false'   |
#      | 'true'    |