package com.aokolnychyi.spark.streaming.example

import org.apache.kafka.common.serialization.StringDeserializer

import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies._
import org.apache.spark.streaming.kafka010.LocationStrategies._
import org.apache.spark.streaming.kafka010.{CanCommitOffsets, HasOffsetRanges, KafkaUtils}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object ReduceByWindowExample {

  private val checkpointDir = "hdfs://localhost:9000/checkpoint-spark-streaming-reduce"

  def main(args: Array[String]): Unit = {

    val streamingContext = StreamingContext.getOrCreate(checkpointDir, createStreamingContext)

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "spark_test_group",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics = Array("spark-test-4-partitions")

    val stream = KafkaUtils.createDirectStream[String, String](
      streamingContext,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    val wordPairs = stream.map(record => (record.value(), 1))

    // Reduce each 30 seconds window to a single value each 15 seconds
    val totalWordCount = wordPairs.reduceByWindow(reduceFunction, Seconds(30), Seconds(15))
    // totalWordCount.print()

    // A more efficient implementation using inverse function
    val efficientTotalWordCount = wordPairs.reduceByWindow(reduceFunction, inverseReduceFunction, Seconds(30), Seconds(15))
    // efficientTotalWordCount.print()

    // Built-in functionality
    wordPairs.countByWindow(Seconds(30), Seconds(15)).print()

    // Commit offsets to a special Kafka topic to ensure recovery from a failure
    stream.foreachRDD { rdd =>
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
    }

    streamingContext.start()
    streamingContext.awaitTermination()
  }

  private def createStreamingContext(): StreamingContext = {
    val sparkConfig = new SparkConf().setMaster("local[4]").setAppName("Spark Kafka Test")
    val streamingContext = new StreamingContext(sparkConfig, Seconds(5))
    streamingContext.checkpoint(checkpointDir)
    streamingContext
  }

  private def reduceFunction(wordPair: (String, Int), anotherWordPair: (String, Int)): (String, Int) = {
    "total" -> (wordPair._2 + anotherWordPair._2)
  }

  private def inverseReduceFunction(wordPair: (String, Int), anotherWordPair: (String, Int)): (String, Int) = {
    "total" -> (wordPair._2 - anotherWordPair._2)
  }
}