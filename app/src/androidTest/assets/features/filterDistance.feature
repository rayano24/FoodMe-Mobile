Feature: User filters restaurants by distance
 As a user, I would like to filter restaurants by distance, so I can visit restaurants
 that are closest to me.

Background:
   Given I am on the restaurants page

Scenario Outline: Filter by distance when on restaurant page (Normal Scenario)
   Given I am on the restaurants page
   When I enter my preferred maximum distance <distance>
   Then I should see restaurants that are within this distance <distance>

     Examples:
       | distance     |
       | '50km'       |
