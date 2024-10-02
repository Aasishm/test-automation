### README ###

This is a dockerized version of CodaPayments Assignment

First, load the image into docker using the below command:
	docker load -i codapayments-assignment.tar

Now run the following command to run all the tests in the suite:
	docker run -it -v $(pwd)/test-output:/app/test-output coda-payments-assignment:latest

After running the tests, an HTML report will be generated inside the folder "test-output" in the directory where you have kept the .tar file
The file name for the report is AutomationTestReport.html