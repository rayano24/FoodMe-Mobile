Feature: User gets a random restaurant recommendation
  As a user
  I would like to be able to get a random restaurant recommendation
  So that I can try something new

  Scenario: Get random recommendation when logged in (Normal scenario)
    Given I am logged in
    And I am on the restaurants page
    When I select my rating <rating> preferences
    Then I should see restaurants that fit my rating <rating> selection

  Scenario: Get random recommendation without logging in (Alternate scenario)
    Given I choose not to log in
    And I am on the restaurants page
    When I select my rating <rating> preferences
    Then I should see restaurants that fit my rating <rating> selection