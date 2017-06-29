# T_modelling
To do our 2d representation of the documents, we first iterate through all the documents and store all the words in a hashmap. Then, we only start to count the words and TFIDF for each documents.

Then, we take TFIDF to calculate the Euclidean distance, Cosine similarity and Manhattan distance. We took the results and build the similarity matrixes of all documents.

Similarity matrixes is then took to do Hierarchical  Clustering.

#Library Used
We refer to another library called exude uttesh to do stemming filtering in the documents.

We used the library found on github created by lbehnke to do Hierarchical Clustering. Link at: https://github.com/lbehnke/hierarchical-clustering-java

#Explanation
Topic_modelling.java is the where the main class at.
java file start with Task (e.g. TaskGetCosine) is the tasks created and to be given to thread pool for process.
CountWord.java is the class that store all the method for topic_modelling.
CSVReader.java is the class to read the CSV file of similarity matrix for clustering.
Stemmer.java is the library we gotten from exude uttesh.
