# Mapreduce-movie-average-rating


Goal:
Read in a large file containing movie
ratings and use MapReduce to calculate the average rating for each movie.


Dataset:
MovieLens dataset which is available at:
https://grouplens.org/datasets/movielens/latest/


Requirements:
1. The mapper code will read in the ratings.csv file and use the movieId as
the key and the rating field as the value.
2. The reducer code, will compute the average rating (as a double datatype)
for each movieId and store it in the output directory on HDFS.
