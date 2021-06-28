Problem Statement
Time windowed key value store

Develop time window based key value store which evicts key based on key’s last update time and provides below listed query operation.

Operations:
Get: Returns latest value associated with key. It returns null if key has expired or does not exist.
Put:  Replaces if key already exist in store otherwise create new entry.
Average: Returns average values of all non-expired keys.

Features to implement are
1.	Provide in-memory store implementation
2.	Key expiration time should be configurable at store level and can be in seconds, minute or hour
3.	Implementation should be thread safe
4.	Expired Key-value should be written to local file but should be extensible to support other storage service like database or cloud storage.
5.	Handle Exceptions appropriately
6.	Code should be testable and write some unit test cases


Setup Locally
https://github.com/nive1980/KeyValueStore.git
mvn install
mvn test
Open the project in IntelliJ IDEA, compile and run the JUnit test cases.
A csv  file should get created with expired keys