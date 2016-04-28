import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

import org.apache.spark.mllib.linalg.{ DenseVector, Vectors }
import org.apache.spark.mllib.optimization.tfocs.SolverSLP


object SimpleLPSolver {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Simple Application for LP solver example")
    val sc = new SparkContext(conf)
    
    // Constraint matrix
    val A = sc.parallelize(Array(
    Vectors.sparse(3, Seq((0, 0.88))),
    Vectors.sparse(3, Seq((1, 0.63))),
    Vectors.sparse(3, Seq((0, 0.29), (2, 0.18)))), 2)

    // Constraint vector
    var b = new DenseVector(Array(9.50, 6.84, 5.09))

    // Objective vector
    val c = sc.parallelize(Array(1.0, 2.0, 3.0), 2).glom.map(new DenseVector(_))

    // Smoothing parameter
    val mu = 1e-2
    
   // return A tuple containing two elements. The first element is a vector containing the optimized
   // 'x' value. The second element contains the objective function history.
   
    val (optimalX, _) = SolverSLP.run(c, A, b, mu)
    
    
    println("optimalX: ********************** " + optimalX.collect.mkString(", "))
    //Result : [1.4705776723011894], [10.857142857142861,28.294900005954958]
    
    sc.stop()
    
  }
}