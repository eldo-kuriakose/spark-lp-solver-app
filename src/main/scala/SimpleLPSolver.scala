import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

import org.apache.spark.mllib.linalg.{ DenseVector, Vectors }
import org.apache.spark.mllib.optimization.tfocs.SolverSLP


object SimpleLPSolver {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Simple Application for LP solver example")
    val sc = new SparkContext(conf)
    
   val startTime = System.currentTimeMillis
    // A is mXn for m > n , and b is mX1 so that result vector y = Ax - b (y is nX1) 
    // Constraint matrix
   val A = sc.parallelize(Array(
        Vectors.sparse(4, Seq((0, 1.), (2, 1.))),
        Vectors.sparse(4, Seq((1, 1.), (2, 1.))),
        Vectors.sparse(4, Seq((0, 1.), (3, 1.))),
        Vectors.sparse(4, Seq((1, 1.), (3, 1.))),

        Vectors.sparse(4, Seq((0, -1.))),   // Slack 1: Demand constraint inequality @ TS1 (lower bound)
        Vectors.sparse(4, Seq((1, -1.))),   // Slack 2: Demand constraint inequality @ TS2 (lower bound)
        Vectors.sparse(4, Seq((2, 1.))),    // Slack 3: Supply constraint inequality @ TE1 (upper bound)
        Vectors.sparse(4, Seq((3, 1.)))     // Slack 4: Supply constraint inequality @ TE2 (upper bound)
    ), 2)

    // Constraint vector
    var b = new DenseVector(Array(7500, 15000,  30000, 15000))

    // Objective vector
    val c = sc.parallelize(Array(1., 1., 2., 2., 0,0,0,0), 2).glom.map(new DenseVector(_))

    // Smoothing parameter
    val mu = 1e-4
    
   // return A tuple containing two elements. The first element is a vector containing the optimized
   // 'x' value. The second element contains the objective function history.
   
    val (optimalX, _) = SolverSLP.run(c, A, b, mu)
    
    val endTime = System.currentTimeMillis
    
    println("optimalX: ********************** " + optimalX.collect.mkString(", "))
    println(" Time taken  ---------------: " + (endTime - startTime) + " ms");
    
    sc.stop()
    
  }
}