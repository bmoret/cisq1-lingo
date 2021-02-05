Feature: Lingo Trainer
  As a Player,
  I want to guess 5, 6 and 7 letter words
  So that I can practise for the game Lingo

Scenario: Starting a new game
  When I "start" a new "game"
  Then I should see a the first letter of a 5 letter word
  And have a score of 0