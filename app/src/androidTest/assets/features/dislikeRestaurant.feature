Feature: User dislikes a restaurant
  As a user, I would like mark a restaurant as 'disliked' so that I am not recommended it again

  Scenario Outline:
    Given I am viewing a restaurant's information
    When I click on the 'dislike' button
    Then the restaurant should be marked as disliked
