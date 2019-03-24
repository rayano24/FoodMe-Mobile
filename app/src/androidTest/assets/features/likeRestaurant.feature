Feature: User likes a restaurant
  As a user, I would like mark a restaurant as 'liked' so that I am recommended it again

  Background:
    Given I am logged in

  Scenario: User likes a restaurant (Normal scenario)
    Given I am viewing a restaurant's information
    When I click on the 'like' button
    Then the restaurant should be marked as liked