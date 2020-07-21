Meter Reading Parser - SimpleNem12ParserImpl.parseSimpleNem12
==========

Implementation
-----
* Made it a Maven project for ease of build and testing purposes.
* The method takes in a CSV and parses it with the format outlined in the requirements.
* Once parsed, it gets a collection of Meter Readings, each having a sorted Map (by date) of Meter volumes
* Parsing implemented using java.io.BufferedReader
* Added validations to prevent unsupported formats.

Assumptions/Improvements
-----
* The requirements had a set of assumptions, hence the checks in place cover the scenarios outside those assumed.
* Code could be enhanced to do custom exception handling if requirements arise.

Testing:
-----
* Converted the main method in TestHarness class to a Junit Test method.
* The TestHarness class tests total volume per customer across a couple of csv readings
* Test ensures that the data per customer is parsed in a sorted format

