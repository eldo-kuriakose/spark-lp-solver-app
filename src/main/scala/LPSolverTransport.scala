import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

import org.apache.spark.mllib.linalg.{ DenseVector, Vectors }
import org.apache.spark.mllib.optimization.tfocs.SolverSLP


object LPSolverTransport {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("An LP transport example")
    val sc = new SparkContext(conf)
    
   val startTime = System.currentTimeMillis
    // A is mXn for m > n , and b is mX1 so that result vector y = Ax - b (y is nX1) 
    // Constraint matrix
   val A = sc.parallelize(Array(
        Vectors.sparse(10, Seq((0, 1.), (5, 1.))),
        Vectors.sparse(10, Seq((1, 1.), (5, 1.))),
        Vectors.sparse(10, Seq((2, 1.), (5, 1.))),
        Vectors.sparse(10, Seq((3, 1.), (5, 1.))),
        Vectors.sparse(10, Seq((4, 1.), (5, 1.))),

        Vectors.sparse(10, Seq((0, 1.), (6, 1.))),
        Vectors.sparse(10, Seq((1, 1.), (6, 1.))),
        Vectors.sparse(10, Seq((2, 1.), (6, 1.))),
        Vectors.sparse(10, Seq((3, 1.), (6, 1.))),
        Vectors.sparse(10, Seq((4, 1.), (6, 1.))),

        Vectors.sparse(10, Seq((0, 1.), (7, 1.))),
        Vectors.sparse(10, Seq((1, 1.), (7, 1.))),
        Vectors.sparse(10, Seq((2, 1.), (7, 1.))),
        Vectors.sparse(10, Seq((3, 1.), (7, 1.))),
        Vectors.sparse(10, Seq((4, 1.), (7, 1.))),

        Vectors.sparse(10, Seq((0, 1.), (8, 1.))),
        Vectors.sparse(10, Seq((1, 1.), (8, 1.))),
        Vectors.sparse(10, Seq((2, 1.), (8, 1.))),
        Vectors.sparse(10, Seq((3, 1.), (8, 1.))),
        Vectors.sparse(10, Seq((4, 1.), (8, 1.))),

        Vectors.sparse(10, Seq((0, 1.), (9, 1.))),
        Vectors.sparse(10, Seq((1, 1.), (9, 1.))),
        Vectors.sparse(10, Seq((2, 1.), (9, 1.))),
        Vectors.sparse(10, Seq((3, 1.), (9, 1.))),
        Vectors.sparse(10, Seq((4, 1.), (9, 1.))),

        //Vectors.sparse(10, Seq((0, -1.))),
        //Vectors.sparse(10, Seq((1, -1.))),
        //Vectors.sparse(10, Seq((2, -1.))),
        //Vectors.sparse(10, Seq((3, -1.))),
        //Vectors.sparse(10, Seq((4, -1.))),

        Vectors.sparse(10, Seq((5, 1.))),
        Vectors.sparse(10, Seq((6, 1.))),
        Vectors.sparse(10, Seq((7, 1.))),
        Vectors.sparse(10, Seq((8, 1.))),
        Vectors.sparse(10, Seq((9, 1.)))
    ), 2)

    // Constraint vector
    var b = new DenseVector(Array(15000, 7500,  22500, 7500,15000,16000,55000,40000,35000,25000))

    // Objective vector
    val c = sc.parallelize(Array(1.60,1.60,1.60,1.60,1.60, 1.55, 1.55, 1.55, 1.55, 1.55, 1.90, 1.90, 1.90, 1.90, 1.90, 1.80, 1.80, 1.80, 1.80, 1.80, 1.90, 1.90, 1.90, 1.90, 1.90, 0,0,0,0,0), 2).glom.map(new DenseVector(_))

    // Smoothing parameter
    val mu = 1e-3
    
   // return A tuple containing two elements. The first element is a vector containing the optimized
   // 'x' value. The second element contains the objective function history.
   
    val (optimalX, obectiveOptimum) = SolverSLP.run(c, A, b, mu)
    
    val endTime = System.currentTimeMillis
    
    println("optimalX: ********************** " + optimalX.collect.mkString(", "))
    println(" objective "+obectiveOptimum.mkString(","));
    //optimalX.collect.toArray.foreach(x => println(" -- " + x + " -- "));
    println(" Time taken  >>>>>>>>>>>>>>>>>>: " + (endTime - startTime) + " ms");
    
    sc.stop()
    
  }
}