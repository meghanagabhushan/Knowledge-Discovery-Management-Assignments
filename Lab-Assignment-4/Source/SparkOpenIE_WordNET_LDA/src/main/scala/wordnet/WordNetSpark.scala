package wordnet

import java.io.{BufferedWriter, FileOutputStream, OutputStreamWriter}

import org.apache.spark.{SparkConf, SparkContext}
import rita.RiWordNet

/**
  * Created by Mayanka on 26-06-2017.
  */
object WordNetSpark {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "C:\\Users\\Megha Nagabhushan\\Documents\\BDAA\\big_data")
    val sparkConf = new SparkConf().setAppName("WordNetSpark").setMaster("local[*]").set("spark.driver.memory", "4g").set("spark.executor.memory", "4g")
    val sparkContext = new SparkContext(sparkConf)



    val dataFile=sparkContext.textFile("data/michelle.obama")

    val dataParse=dataFile.map(s=>{
      val wordnet = new RiWordNet("C:\\Users\\Megha Nagabhushan\\Documents\\KDM\\WordNet-3.0.tar\\WordNet-3.0")
      val strings=s.split(" ")
      getSynoymns(wordnet,"republican")
    })
    dataParse.take(1).foreach(strings=>println(strings.mkString("\n")))



    val fileName = "obamaSynonyms"
    val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)))
    dataParse.take(1).foreach(f=>writer.write(f.mkString("\n")))
    writer.close()
  }
  def getSynoymns(wordnet:RiWordNet,word:String): Array[String] ={
    println(word)
    val pos=wordnet.getPos(word)
    println(pos.mkString(" "))
    val syn=wordnet.getAllSynonyms(word, pos(0), 10)
    syn
  }

}
