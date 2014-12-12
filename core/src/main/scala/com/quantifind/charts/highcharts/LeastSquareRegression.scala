package com.qf.charts.highcharts

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression
import com.qf.charts.highcharts.Highchart._
import com.qf.charts.repl.Highcharts._

/**
 * User: jannis
 * Date: 12/12/14
 *
 * Plots both the scatter of the data points as well as the regression line for
 * the given data
 */

object LeastSquareRegression {

  def leastSquareRegression(xSeq: Seq[Double], ySeq: Seq[Double]) = {
    // regress the data
    val target: Array[Double]            = ySeq.toArray
    val predictor: Array[Array[Double]]  = xSeq.map(Array(_)).toArray
    val model = {
      val mod = new OLSMultipleLinearRegression()
      mod.newSampleData(target, predictor)
      mod
    }
    val params= model.estimateRegressionParameters
    val b  = params(0)
    val m = params(1)
    val residualSS = model.calculateResidualSumOfSquares()
    
    // make the plot 
    val xMin = xSeq.min
    val xMax = xSeq.max
    val data = Series(xSeq.zip(ySeq).map{case (x,y) => Data(x,y)}, name = "Datapoints", chart = "scatter")
    val line = Series(
      data = List(Data(xMin, b + xMin * m), Data(xMax, b + xMax * m)),
      color = data.color,
      name = "Regression line " + f"$b%1.5f" + " + " + f"$m%1.5f" + " * x"
    )
    plot(Highchart(List(data,line), Some(Title("Regression with residual sum of squares of " + f"$residualSS%1.5f"))))
  }
}
