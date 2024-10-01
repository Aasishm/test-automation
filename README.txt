### README ###

This project is built by using Java, TestNG and Selenium dependencies.
This program uses Java SE 21 and Selenium 4.25.0
Dependencies are provided in the libs folder.

First, compile using the Java command:
	Linux - javac -cp "libs/*:src/main" -d bin @sources.txt
	Windows - javac -cp "libs/*;src/main" -d bin @sources.txt

Now run the following command to run all the tests in the suite:
	Linux - java -cp "libs/*:bin" test.AppTest     
	Windows - java -cp "libs/*;bin" test.AppTest


OR 

Alternatively, directly run the AppTest.java file as Java Application under src/test folder in Eclipse IDE or any other IDE

After running the tests, an HTML report will be generated inside the folder "test-output"
The file name for the report is AutomationTestReport.html

NOTE
=========
By default, the test runs on Chrome in headless mode. If you are using Chrome 129 version, there may be an issue with a white screen being open when running the test in Windows.

