#Run tests
mvn clean test

#Generate html surefire report after test execution
mvn surefire-report:report-only
report will appear under target/site folder

#Some excuses
I didn't find a way how to reach in memory database of the service under test (not sure if it's possible) so used only given urls in test scenarios.