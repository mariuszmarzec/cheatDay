# Testing Scenarios

I: Login to application
User launch application
And user sees login screen
Then user types login
And user types password
And user click login button
Then user see home screen

II: Login to application failed
User launch application
And user sees login screen
Then user types login
And user types password
And user click login button
Then user sees error message

III: Logout from application
Given user is logged in
Then user launch application
And user sees home screen
And user click logout button
And user sees login screen

IV: user increase diet counter
Given user is logged in
And user sees home screen
And user clicks increase button for diet
Then user sees counter value increased

V: user increase workout counter
Given user is logged in
And user sees home screen
And user clicks increase button for workout
Then user sees counter value increased

VI: user decrease cheat day counter
Given user is logged in
And user sees home screen
And user clicks decrease button for cheat day
Then user sees counter value decreased

VII: user increase cheat day counter by increasing diet
Given user is logged in
And user sees home screen
And user clicks increase button for diet 5 times
Then user sees cheat day counter value increased

VIII: user increase cheat day counter by increasing workout
Given user is logged in
And user sees home screen
And user clicks increase button for workout 3 times
Then user sees cheat day counter value increased

IX: user adds new weight result
Given user is logged in
And user sees home screen
And user clicks weights tab
Then user sees weights screen
And user clicks add new weight button
Then user sees add new weight screen
And user type weight value
And user clicks add button
Then user sees weights screen
And user sees new weight result added
And user clicks on home tab
Then user sees home screen
And user sees updated cheat day counter

X: user updates new weight result
Given user is logged in
And user has weights results
And user sees home screen
And user clicks weights tab
Then user sees weights screen
And user sees weight results
And user clicks on weight result
Then user sees update weight screen
And user type weight value
And user clicks update button
Then user sees weights screen
And user sees weight result updated

XI: user update max possible weight
Given user is logged in
And user has weights results
And user sees home screen
And user clicks weights tab
Then user sees weights screen
And user clicks max possible weight row
Then user sees max possible weight update dialog
And user type new max possible weight
And clicks ok button
Then user sees weights screen
And user sees updated max possible weight

XII: user updates target weight
Given user is logged in
And user has weights results
And user sees home screen
And user clicks weights tab
Then user sees weights screen
And user clicks target weight row
Then user sees target weight update dialog
And user type new target weight weight
And clicks ok button
Then user sees weights screen
And user sees updated target weight

XIII: user display weights chart
Given user is logged in
And user has weights results
And user sees home screen
And user clicks weights tab
Then user sees weights screen
And user sees weight results
And user clicks chart button
Then user sees weight chart screen