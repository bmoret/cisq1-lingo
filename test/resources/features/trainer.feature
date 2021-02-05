Feature: Lingo Trainer
  As a Player,
  I want to guess 5, 6 and 7 letter words
  So that I can practise for the game Lingo

Scenario: Starting a new game
  When I start a new game
  Then I should see a the "first letter" of a "5 letter word"
  And have a "score" of "0"

Scenario Outline: Starting a new round
  Given I am playing a game
  And the round was won
  And the last word had "<previous length>" letters
  When I start a new round
  Then the word to guess has "<next length>" letters

  Examples:
    | previous length | next length |
    | 5               | 6           |
    | 6               | 7           |
    | 7               | 5           |

  # Failure path
  Given I am playing a game
  And the round was lost
  Then I cannot start a new round

Scenario Outline: Guessing a word
  Given I am playing a game
  And the round hasn't finished
  When I make a "<guess>"
  Then my "<guess>" is compared to the "<word>"
  And "<feedback>" is shown based on that comparison

  Examples:
    | word    | guess   | feedback                                                  |
    | taken   | takes   | correct, correct, correct, correct, absent                |
    | forrest | failure | correct, absent, absent, absent, absent, present, present |
    | advert  | trial   | invalid, invalid, invalid, invalid, invalid, invalid      |
