Feature: Basic operations on rules

  Background:
    Given there is a Rules server

  Scenario: create a rule unauthenticated
    Given I have a rule payload
    When I POST the rule payload to the /rules endpoint
    Then I receive a 401 status code

  Scenario: create a rule
    Given I have successfully registered my app
    Given I have a rule payload
    When I POST the rule payload to the /rules endpoint
    Then I receive a 201 status code


  Scenario: create a rule then earn pointScales and badge
    Given I have successfully registered my app
    Given I have a rule payload if "eventType1" then award badge "badgeName1", pointsScale "psName" with 10 points out of 20
    Given I have an event payload for event "eventType1" user "userRuleTesting"
    When I have successfully registered a badge named "badgeName1"
    Then I receive a 201 status code

    When I have successfully registered a pointsScale "psName"
    Then I receive a 201 status code


    When I POST the rule payload to the /rules endpoint
    Then I receive a 201 status code
    Then I POST the event payload to the /events endpoint for rule
    Then I receive a 201 status code

    Then I send a GET to the badges endpoint for URL in the userLocation header
    Then I receive a 200 status code
    And I receive a list of 0 badges

    Then I send a GET to the /pointScales endpoint for user "userRuleTesting"
    Then I receive a 200 status code
    And I receive a pointScales named "psName" with 10 points out of 20

    Then I POST the event payload to the /events endpoint for rule
    Then I receive a 201 status code


    Then I send a GET to the /pointScales endpoint for URL in the userLocation header
    Then I receive a 200 status code
    And I receive a pointScales named "psName" with 20 points out of 20


    Then I send a GET to the badges endpoint for user "userRuleTesting"
    Then I receive a 200 status code
    And I receive a list of 1 badges

  Scenario: check leaderboards
    Given I have successfully registered my app
    Given I have a rule payload if "eventType2" then award badge "badgeName2", pointsScale "psName2" with 10 points out of 20
    Given I have an event payload for event "eventType2" user "userRuleTesting2"
    When I have successfully registered a badge named "badgeName2"
    Then I receive a 201 status code

    When I have successfully registered a pointsScale "psName2"
    Then I receive a 201 status code


    When I POST the rule payload to the /rules endpoint
    Then I receive a 201 status code
    Then I POST the event payload to the /events endpoint for rule
    Then I receive a 201 status code

    #Check user appears on leaderboard
    When I check top 5 users of leaderboard for "psname2"
    Then I receive a 200 status code
    And I received no more than 5 topUsers including "userRuleTesting2"
    And I didn't receive user "userRuleTesting"

     #Check leaderboard returns right number of users
    Given I have an event payload for event "eventType2" user "userRuleTesting3"
    Then I POST the event payload to the /events endpoint for rule
    Then I receive a 201 status code

    Given I have an event payload for event "eventType2" user "userRuleTesting4"
    Then I POST the event payload to the /events endpoint for rule
    Then I receive a 201 status code
    When I check top 2 users of leaderboard for "psname2"
    Then I receive a 200 status code
    And I received no more than 2 topUsers including "userRuleTesting2"