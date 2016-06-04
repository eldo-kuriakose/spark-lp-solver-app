# spark-lp-solver-app

### SimpleLPSolver
                   | Destination  1 | Destination 2   | Capacity          | Price
    ------------- | -------------  | -------------    | ---------------   | ----------
    A             | X1             |    X2            |   30000           | 1.00
    B             | X3             |    X4            |   1500            | 2.00
    Demand        | 7500           | 15000            |                   |
    

Expression for the above code is 


* X1  +   0*X2 +     X3 + 0*X4 +(-1)S1 +  0*S2 +  0*S3 + 0*S4 = 7500 

* 0*X1  +      X2 + 0*X3 +     X4 + 0*S1 + (-1) S2 + 0*S3 + 0*S4 = 15000

* X1 +       X2 + 0*X3 + 0*X4 + 0*S1 +   0*S2 +     S3 + 0*S4 = 30000

* 0*X1 +    0*X2 +    X3 +     X4 + 0*S1 +   0*S2 +  0*S3 +    S4 = 15000


#### Run


spark-submit --class "SimpleLPSolver" --master local[4] --jars ./lib/spark-tfocs_2.10-1.0-SNAPSHOT.jar target/scala-2.10/example-project-to-run-lp-solver-in-spark_2.10-1.0.jar
