Feature: User likes a restaurant
  As a user
  I would like to be able to like a restaurants
  So that I can get better recommendations based on my favorite restaurants

  Scenario Outline: Liking a restaurant (Normal scenario)
    Given I am logged in
    And I am on the restaurants page
    When I select a restaurant
    Then I should be able to like it

    Examples:
      | rating      |
      | 'None'       |
      | 'Chinese'    |
      | 'Italian'    |