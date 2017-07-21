package openie

import java.io.{BufferedWriter, FileOutputStream, OutputStreamWriter}

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Mayanka on 27-Jun-16.
  */
object SparkOpenIE {

  def main(args: Array[String]) {
    // Configuration
    val conf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")

    val sparkContext = new SparkContext(conf)

   // val fileName = "OpenIEResults"
    //val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)))


    // Turn off Info Logger for Console
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    val ipfile = sparkContext.textFile("data/sqad_data").map(s => {
      //Getting OpenIE Form of the word using lda.CoreNLP

      val output=MainNLP.returnTriplets(s)
      output
    })

    //println(CoreNLP.returnTriplets("The dog has a lifespan of upto 10-12 years."))
   println(ipfile.collect().mkString("\n"))
    //writing the OPenIE triplets into a file
   // writer.write(ipfile.collect().mkString("\n"))
    //writer.close()



  }

}
